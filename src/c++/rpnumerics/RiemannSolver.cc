#include "RiemannSolver.h"

double RiemannSolver::min(double x, double y){
    return (x < y) ? x : y;
}

double RiemannSolver::max(double x, double y){
    return (x > y) ? x : y;
}

void RiemannSolver::minmax(const std::vector<Curve> &wave_curve, double &min, double &max){
    if (wave_curve.size() > 0){
        if (wave_curve[0].curve.size() == 0) return;
    }

    int n = wave_curve[0].curve[0].size();
    min = max = wave_curve[0].curve[0].component(n - 1);

    for (int i = 0; i < wave_curve.size(); i++){
        for (int j = 0; j < wave_curve[i].curve.size(); j++){
            double temp = wave_curve[i].curve[j].component(wave_curve[i].curve[j].size() - 1);

            if (temp < min) min = temp;
            if (temp > max) max = temp;
        }
    }

    return;
}

void RiemannSolver::half_profile(const std::vector<Curve> &c, int subc, int subc_point,
                                 std::vector<RealVector> &profile){
    //
    
    int curvepos = subc;
    int pos = subc_point;

    do {
        //printf("Inside RiemannSolver. curvepos = %d, pos = %d\n", curvepos, pos);

//        profile.push_back(c[curvepos].curve[pos]);
        if (c[curvepos].type != RAREFACTION_CURVE ||
            (c[curvepos].type == RAREFACTION_CURVE && pos != 0)
           ) profile.push_back(c[curvepos].curve[pos]);

        int temp_pos = c[curvepos].corresponding_point_in_related_curve[pos];
        int old_curvepos = curvepos;
        int old_pos = pos;

        curvepos     = c[curvepos].related_curve[pos];
        pos          = temp_pos;

        if (c[old_curvepos].type == SHOCK_CURVE){
            profile.push_back(c[curvepos].curve[pos]);
            profile[profile.size() - 1].component(profile[profile.size() - 1].size() - 1) = c[old_curvepos].curve[old_pos].component(c[old_curvepos].curve[old_pos].size() - 1);
            break;
        }

    } while (curvepos != -1 && pos != -1);

    return;
}

double RiemannSolver::alpha(const RealVector &p0, const RealVector &p1, const RealVector &p){
    double a = 0;

    for (int i = 0; i < p.size(); i++) a += (p.component(i) - p1.component(i))/(p0.component(i) - p1.component(i));

    return a/(double)p.size();
}

int RiemannSolver::saturation_profiles(const std::vector<Curve> &one_wave_curve, // Family 0, forward
                                       const std::vector<Curve> &two_wave_curve, // Family 1, backward
                                       const RealVector &pmin, const RealVector &pmax, 
                                       double time,
                                       std::vector<RealVector> &profile){

    profile.clear();

    // Compute the intersection between the wavecurves
    RealVector p;
    int subc1, subc1_point, subc2, subc2_point;

    int found = WaveCurve::intersection(one_wave_curve, two_wave_curve, pmin, pmax, 
                                        p, subc1, subc1_point, subc2, subc2_point);

    if (found == WAVE_CURVE_INTERSECTION_NOT_FOUND) return RIEMANNSOLVER_ERROR;

    // Process the first half of the profile
    std::vector<RealVector> temp;
    half_profile(one_wave_curve, subc1, subc1_point, temp);

    // Reverse it:
    //
    profile.resize(temp.size());
    for (int i = 0; i < temp.size(); i++) profile[i] = temp[temp.size() - i - 1];

    // Add the middle point, twice, with the speed being interpolated.
    // The last point of the profile so far will be replaced by M, with the speed interpolated.
    //
    int point_size = one_wave_curve[subc1].curve[subc1_point].size();
    double alpha1 = alpha(one_wave_curve[subc1].curve[subc1_point], one_wave_curve[subc1].curve[subc1_point + 1], p);
    double one_interpolated_speed = alpha1*one_wave_curve[subc1].curve[subc1_point].component(point_size - 1) + 
                                    (1.0 - alpha1)*one_wave_curve[subc1].curve[subc1_point + 1].component(point_size - 1);

    for (int i = 0; i < point_size - 1; i++) profile[profile.size() - 1].component(i) = p.component(i);
    //profile[profile.size() - 1].component(point_size - 1) = one_interpolated_speed;
    
    // Remember this position in the profile. It will be used later.
    int interpolated_point_position = profile.size();

    // Process the second half of the profile
    half_profile(two_wave_curve, subc2, subc2_point, profile);

    // Add the middle point again, again the first point of this half-profile will be replaced
    // by M, its speed interpolated.
    //
    double alpha2 = alpha(two_wave_curve[subc2].curve[subc2_point], two_wave_curve[subc2].curve[subc2_point + 1], p);
    double two_interpolated_speed = alpha2*two_wave_curve[subc2].curve[subc2_point].component(point_size - 1) + 
                                    (1.0 - alpha2)*two_wave_curve[subc2].curve[subc2_point + 1].component(point_size - 1);
    
    for (int i = 0; i < point_size - 1; i++) profile[interpolated_point_position].component(i) = p.component(i);
    //profile[interpolated_point_position].component(point_size - 1) = two_interpolated_speed;

    // Verify that the speed at the first point is smaller than at the last point
    if (profile[0].component(point_size - 1) > profile[profile.size() - 1].component(point_size - 1)) return RIEMANNSOLVER_ERROR;

    // Add the constant regions to the beginning and at the end of the profile
    if (profile.size() > 2){
        // Speed component
        int sc = profile[0].size();
        double delta = .25*fabs(profile[profile.size() - 1].component(sc - 1) - profile[0].component(sc - 1));

        // First and last points:
        //
        RealVector fp(profile[0]), lp(profile[profile.size() - 1]);

        fp.component(sc - 1) -= delta; 
        lp.component(sc - 1) += delta;

        profile.insert(profile.begin(), fp);
        profile.push_back(lp);
    }

    return RIEMANNSOLVER_OK;
}

