#include "SinglePhaseBoundary.h"

void SinglePhaseBoundary::init(VLE_Flash_TPCW *f, double Theta_min, double Theta_max, int domain_type_, double (*d)(double Theta)) {
    top_boundary_points = 1000;
    dimensionalize_variable = d;

    Flash = f;

    // Boundary extremes:
    //
    bmin.resize(2);
    bmax.resize(2);

    // Just in case...
    //
    bmin.component(1) = min(Theta_min, Theta_max);
    bmax.component(1) = max(Theta_min, Theta_max);

    int pos = ((domain_type == DOMAIN_IS_VAPOR) ? 1 : 0);

    double xc_yw[2] = {0.0, 0.0};
    double delta = (bmax.component(1) - bmin.component(1)) / 1000.0; //printf("SPB1!\n");

    Flash->flash((*dimensionalize_variable)(bmin.component(1)), xc_yw[0], xc_yw[1]); //printf("SPB2!\n");
    top_min_composition = xc_yw[pos];
    bmin.component(0) = 0.0;
    bmax.component(0) = xc_yw[pos];

    for (int i = 0; i < 1000; i++) {
        Flash->flash((*dimensionalize_variable)(bmin.component(1) + (i + 1) * delta), xc_yw[0], xc_yw[1]);
        top_min_composition = min(top_min_composition, xc_yw[pos]);
        bmax.component(0) = max(bmax.component(0), xc_yw[pos]);
    }

    return;
}

void SinglePhaseBoundary::copy_from(const SinglePhaseBoundary &orig) {
    Flash = orig.Flash;
    domain_type = orig.domain_type;

    top_boundary_points = orig.top_boundary_points;

    bmin = orig.bmin;
    bmax = orig.bmax;

    top_min_composition = orig.top_min_composition;

    dimensionalize_variable = orig.dimensionalize_variable;

    return;
}

SinglePhaseBoundary::SinglePhaseBoundary(VLE_Flash_TPCW *f,
        double Theta_min, double Theta_max,
        int domain_type_) : domain_type(domain_type_) {
    init(f, Theta_min, Theta_max, domain_type, &default_dimensionalize_variable);
}

SinglePhaseBoundary::SinglePhaseBoundary(VLE_Flash_TPCW *f,
        double Theta_min, double Theta_max,
        int domain_type_,
        double (*d)(double Theta)) : domain_type(domain_type_) {
    init(f, Theta_min, Theta_max, domain_type, d);
}

SinglePhaseBoundary::SinglePhaseBoundary(const SinglePhaseBoundary &orig) {
    copy_from(orig);
}

SinglePhaseBoundary::SinglePhaseBoundary(const SinglePhaseBoundary *orig) {
    copy_from(*orig);
}

SinglePhaseBoundary::~SinglePhaseBoundary() {
    // So far, nothing happens here.
}

bool SinglePhaseBoundary::inside(const RealVector &y) const {
    RealVector temp(y);

    return inside(temp.components());
}

bool SinglePhaseBoundary::inside(const double *y) const {
    double composition = y[0];
    double Theta = y[1];

    // A few quick cases here...
    //
    if (Theta < bmin.component(1) || Theta > bmax.component(1)) return false;

    if (composition < bmin.component(0) || composition > bmax.component(0)) return false;

    if (composition < top_min_composition) return true;

    // ...and the one that could not be quick here.
    //
    double xc, yw;
    Flash->flash((*dimensionalize_variable)(Theta), xc, yw);

    double t = ((domain_type == DOMAIN_IS_VAPOR) ? yw : xc);

    return composition <= t;
}

//! Virtual constructor

Boundary * SinglePhaseBoundary::clone()const {
    return new SinglePhaseBoundary(this);
}

//! Minimums boundary values accessor

const RealVector & SinglePhaseBoundary::minimums() const {
    return bmin;
}

//! Maximums boundary values accessor

const RealVector & SinglePhaseBoundary::maximums() const {
    return bmax;
}

RealVector SinglePhaseBoundary::intersect(RealVector &y1, RealVector &y2) const {
    return RealVector(2);
}

//! Returns the boundary type

const char * SinglePhaseBoundary::boundaryType() const {
    return "SinglePhaseBoundary";
}

int SinglePhaseBoundary::intersection(const RealVector &p, const RealVector &q, RealVector &r, int &w) const {
    int info = Boundary::intersection(p, q, r, w);

    // Temperature is the second coordinate of r.
    if (info == 0) { // There was an intersection!
        double c = r.component(0); // Composition
        double Theta = r.component(1); // Temperature

        if (c <= bmin.component(0) ||
                (Theta <= bmin.component(1) || Theta >= bmax.component(1))
                ) w = SINGLEPHASE_ANY_OTHER_SIDE;
        else w = SINGLEPHASE_TOTAL_COMPOSITION_SIDE;
    }

    return info;
}



RealVector SinglePhaseBoundary::side_transverse_interior(const RealVector &p, int s) const{
    
    cout<<"Chamando side_transverse_interior de single phase boundary"<<endl;
    
    return p;
    
}

void SinglePhaseBoundary::physical_boundary(std::vector<RealVector> &seg) {
    seg.clear();

    double xc, yw;

    RealVector temp(2);
    std::vector<RealVector> tempSeg;

    // Lower side
    edge_segments(DOMAIN_EDGE_LOWER_COMPOSITION, 3, tempSeg);

    for (int j = 0; j < tempSeg.size(); j++) {
        seg.push_back(tempSeg[j]);
    }

    // Right side
    temp.component(1) = bmax.component(1);

    temp.component(0) = 0.0;
    seg.push_back(temp);

    Flash->flash((*dimensionalize_variable)(bmax.component(1)), xc, yw);
    temp.component(0) = (domain_type == DOMAIN_IS_VAPOR) ? yw : xc;
    seg.push_back(temp);

    // Upper side
    edge_segments(DOMAIN_EDGE_UPPER_COMPOSITION, top_boundary_points, tempSeg);

    for (int j = tempSeg.size() - 1; j >= 0; j--) {
        seg.push_back(tempSeg[j]);
    }

    // Left side
    temp.component(1) = bmin.component(1);

    Flash->flash((*dimensionalize_variable)(bmin.component(1)), xc, yw);
    temp.component(0) = (domain_type == DOMAIN_IS_VAPOR) ? yw : xc;
    seg.push_back(temp);

    temp.component(0) = 0.0;
    seg.push_back(temp);

    return;
}

void SinglePhaseBoundary::extension_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &gv,
        int where_constant, int number_of_steps, bool singular,
        int fam, int characteristic,
        std::vector<RealVector> &c, std::vector<RealVector> &d) {

    // 
    c.clear();
    d.clear();

    std::vector<RealVector> seg;
    edge_segments(where_constant, number_of_steps, seg);

    Extension_Curve extension_curve;

    extension_curve.curve(f, a, gv, characteristic, singular, fam,
            seg,
            c, d);
    return;
}

void SinglePhaseBoundary::extension_curve(const FluxFunction *df, const AccumulationFunction *da, // Over the domain
        const FluxFunction *cf, const AccumulationFunction *ca, // Over the curve 
        GridValues &gv,
        int where_constant, int number_of_steps, bool singular,
        int fam, int characteristic,
        std::vector<RealVector> &c, std::vector<RealVector> &d) {
    // 
    c.clear();
    d.clear();

    std::vector<RealVector> seg;
    edge_segments(where_constant, number_of_steps, seg);
    std::cout << "SPB, seg.size() = " << seg.size() << std::endl;

    Extension_Curve extension_curve;

    extension_curve.curve(df, da, cf, ca, gv, characteristic, singular, fam,
            seg,
            c, d);
    return;
}

void SinglePhaseBoundary::envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &gv,
        int where_constant, int number_of_steps, bool singular,
        std::vector<RealVector> &c, std::vector<RealVector> &d) {
    return;
}




void SinglePhaseBoundary::envelope_curve(const FluxFunction *fl, const AccumulationFunction *al, const FluxFunction *fr, const AccumulationFunction *ar,
        GridValues &gr,
        int where_constant, int number_of_steps, bool singular,
        std::vector<RealVector> &c, std::vector<RealVector> &d){
    
}

int SinglePhaseBoundary::edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg) {
    seg.clear();

    if (number_of_steps < 3) number_of_steps = 3; // Extremes must be eliminated, thus the minimum number of desired segments is 3: of which only one segment will remain.

    //    double p[number_of_steps + 1][2];
    double p[number_of_steps + 1][3];
    double delta_Theta = (bmax.component(1) - bmin.component(1)) / (double) number_of_steps;
    for (int i = 0; i < number_of_steps + 1; i++) {
        p[i][1] = bmin.component(1) + (double) i*delta_Theta;
        p[i][2] = 1.0;
    }

    if (where_constant == DOMAIN_EDGE_LOWER_COMPOSITION) {
        for (int i = 0; i < number_of_steps + 1; i++) p[i][0] = 0.0;
    } else {
        int pos = ((domain_type == DOMAIN_IS_VAPOR) ? 1 : 0);

        double xc_yw[2];
        for (int i = 0; i < number_of_steps + 1; i++) {
            Flash->flash((*dimensionalize_variable)(p[i][1]), xc_yw[0], xc_yw[1]);
            p[i][0] = xc_yw[pos];
        }
    }

    // Boundary extension segments.
    seg.resize(2 * (number_of_steps));

    for (int i = 0; i < number_of_steps; i++) {
        seg[2 * i].resize(3);
        seg[2 * i + 1].resize(3);

        for (int j = 0; j < 3; j++) {
            seg[2 * i].component(j) = p[i][j];
            seg[2 * i + 1].component(j) = p[i + 1][j];
        }

    }
    
    for (int i = 0; i < seg.size(); i++) {

//    cout<<"Seg: "<<seg[i]<<endl;

    }


    return 1;
}

// Convert from Theta to T

double SinglePhaseBoundary::default_dimensionalize_variable(double Theta) {
    // This is a temporary stuff, until the "thermos" are unified!!
    return Theta * 304.63 + 274.3775;
    return Theta;
}

