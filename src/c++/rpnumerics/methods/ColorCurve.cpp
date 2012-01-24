#include "ColorCurve.h"

// Initialize the color table:
//int ColorTable::color[16][3] = { 255, 255, 255,   //  0 = Left transport    COLOR=white     ---- ATTENTION IT IS ALSO USEFUL TO PUT THIS +'s AND -'s.
//                                 255, 255, 255,   //  1
//                                 255,   0,   0,   //  2 = Choque 2 LAX.     COLOR=red       -+--
//                                 247, 151,  55,   //  3 = SUPERCOMPRESSIVE  COLOR=orange    ++--
//                                 255, 255, 255,   //  4
//                                 255, 255, 255,   //  5
//                                 255, 255, 255,   //  6
//                                 255, 255, 255,   //  7
//                                 255,   0, 255,   //  8 = EXPANSIVE 2       COLOR=pink      ---+
//                                 255, 255, 255,   //  9
//                                  18, 153,   1,   // 10 = TRANSITIONAL      COLOR=green     -+-+
//                                   0,   0, 255,   // 11 = Choque 1 LAX.     COLOR=dark blue ++-+
//                                 255, 255, 255,   // 12 = Central transport COLOR=white     --++
//                                 255, 255, 255,   // 13
//                                   0, 255, 255,   // 14 = EXPANSIVE 1       COLOR=cyan      -+++
//                                 255, 255, 255    // 15 = Right transport   COLOR=white     ++++
//                                };

// Sign function. Inlined, should be fast.

ColorCurve::ColorCurve(const FluxFunction & fluxFunction, const AccumulationFunction & accumulationFunction):fluxFunction_((FluxFunction *)fluxFunction.clone()),accFunction_((AccumulationFunction *)accumulationFunction.clone()) {

}
ColorCurve::~ColorCurve(){
    delete fluxFunction_;
    delete accFunction_;
}

int ColorCurve::sgn(double x) {
    if (x < 0.) return -1;
    else if (x > 0.) return 1;
    else return 0;
}

//
//    r = alpha*p + (1 - alpha)*q
//

void ColorCurve::fill_with_jet(const RpFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);

    //    cout << "Tamanho em fill: " << n << endl;
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];
    //    cout << "Entrada em fill: " << r << endl;
    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);

    JetMatrix c_jet(n);
    //    cout << "Depois da linha 296 " << c_jet.size() << endl;
    flux_object.jet(state_c, c_jet, degree);
    //    cout << "Depois de flux object: " << n << endl;

    // Fill F
    if (F != 0) for (int i = 0; i < n; i++) F[i] = c_jet(i);

    // Fill J
    if (J != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                J[i * n + j] = c_jet(i, j);
            }
        }
    }

    // Fill H
    if (H != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    H[(i * n + j) * n + k] = c_jet(i, j, k); // Check this!!!!!!!!
                }
            }
        }
    }
    //    cout << "Dentro de fill with jet shock" << endl;
    return;
}

int ColorCurve::interpolate(int noe, const RealVector &p, const RealVector &q, std::vector<RealVector> &r) {
    r.clear();
    int dim = p.size() - 2 * noe - 1;

    double fq[4], fp[4]; // f(p), f(q), the function whose zero is to be found

    double l0_ref_p = p.component(dim); // lambda_0(Uref)
    double l1_ref_p = p.component(dim+1); // lambda_1(Uref)
    double s_p = p.component(dim+2); // shock speed s(Uref,p)
    double l0_p = p.component(dim+3); // lambda_0(p)
    double l1_p = p.component(dim+4); // lambda_1(p)

    double l0_ref_q = q.component(dim); // lambda_0(Uref)
    double l1_ref_q = q.component(dim+1); // lambda_1(Uref)
    double s_q = q.component(dim+2); // shock speed s(Uref,q)
    double l0_q = q.component(dim+3); // lambda_0(q)
    double l1_q = q.component(dim+4); // lambda_1(q)

    // To detect if more than two inequality hold (an error situation)
    int noi = 0; // Number of inequalities

    // Find where the sign changes.
    // Notice that there is a bias here. Ideas are welcome.
    if (sgn(l0_ref_p - s_p) != sgn(l0_ref_q - s_q)) {
        fp[noi] = l0_ref_p - s_p;
        fq[noi] = l0_ref_q - s_q;
        noi++;
    }

    if (sgn(l1_ref_p - s_p) != sgn(l1_ref_q - s_q)) {
        fp[noi] = l1_ref_p - s_p;
        fq[noi] = l1_ref_q - s_q;
        noi++;
    }

    if (sgn(l0_p - s_p) != sgn(l0_q - s_q)) {
        fp[noi] = l0_p - s_p;
        fq[noi] = l0_q - s_q;
        noi++;
    }

    if (sgn(l1_p - s_p) != sgn(l1_q - s_q)) {
        fp[noi] = l1_p - s_p;
        fq[noi] = l1_q - s_q;
        noi++;
    }

//    printf("    During interpolation: number of inequalities (noi) = %d\n", noi);

    if (noi <= 0 || noi > 2) return INTERPOLATION_ERROR; // If the sign changes more than twice, return an error.
        // Is it better to fill the vector of r's at all in this case?
    else {
        double alpha[noi];
        for (int i = 0; i < noi; i++) {
//        cout<<"Diferenca: " <<fq[i] - fp[i]<<endl;
            alpha[i] = fq[i] / (fq[i] - fp[i]);

        }

        // Order the alphas in descending order 
        // (thus r[0] will be closer to p than to q, and r[1] closer to q than to p).
        if (noi == 2) {
            if (alpha[0] < alpha[1]) {
                double temp = alpha[0];
                alpha[0] = alpha[1];
                alpha[1] = temp;
            }
        }

        r.resize(noi);
        for (int i = 0; i < noi; i++) {
            r[i].resize(p.size());
            double beta = 1.0 - alpha[i];
            for (int j = 0; j < p.size(); j++) r[i].component(j) = alpha[i] * p.component(j) + beta * q.component(j);
        }
//        cout <<"Depois resize"<<endl;
        return INTERPOLATION_OK;
    }
}

int ColorCurve::classify_point(const RealVector &p,int noe) {


    int dim = p.size()-2*noe-1;

    double l0_ref = p.component(dim); // lambda_0(Uref)
    double l1_ref = p.component(dim+1); // lambda_1(Uref)
    double s = p.component(dim+2); // shock speed s(Uref,p)
    double l0_p = p.component(dim+3); // lambda_0(p)
    double l1_p = p.component(dim+4); // lambda_1(p)

//    double l0_ref =0;
//    double l1_ref =0;
//    double s = 0;
//    double l0_p =0;
//    double l1_p = 0;

    int type = 0;

    if (l0_ref - s >= 0) type += 1;
    if (l1_ref - s >= 0) type += 2;
    if (l0_p - s >= 0) type += 4;
    if (l1_p - s >= 0) type += 8;

    return type;
}

void ColorCurve::classify_segments(const std::vector<RealVector> &input, int noe, std::vector<HugoniotPolyLine> &output) {

//    cout << "Tamanho do vetor para classificar: " << input.size() << endl;
    if (input.size() < 2) return;

    // Create a vector of the positions where a change of type occurs.
    // These will be used later, when filling the output. 
    // The first and last points will be treated differently, and extra care should be taken then.
    std::vector<int> pos;

    int input_size = input.size();

    int t_current, t_next; // Two consecutive types

    // Initialize t_current
    t_current = classify_point(input[1],noe);


    // The first point will not be classified
    int p = 1;
    while (p < input_size - 1) {


        t_next = classify_point(input[p + 1],noe);

//        cout << "Depois de t_next" << endl;

        if (t_current != t_next) {
            pos.push_back(p + 1);
//            printf("    Change of type: t_current(%d) = %d, t_next(%d) = %d\n", p, t_current, p + 1, t_next);
        }

        p++;
        t_current = t_next;
    }

    // The number of segments exceeds by one the number of points where a change of type was detected.
//    output.clear();
//    printf("pos.size() = %d\n", pos.size());
//    for (unsigned int i = 0; i < pos.size(); i++) printf("pos[%d] = %d\n", i, pos[i]);
    output.resize(pos.size() + 1);

    // Add the points to the segment they belong, according to their type.
    for (unsigned int seg_num = 0; seg_num < output.size(); seg_num++) {
        int init, end;
        if (seg_num == 0) {
            init = 1;
            if (pos.size() != 0) end = pos[seg_num]; // pos can have size == 0, this is a must.
            else end = input.size() ;
        } else if (seg_num == output.size() - 1) {
            init = pos[seg_num - 1] ;
            end = input.size() ;
        } else {
            init = pos[seg_num - 1] ;
            end = pos[seg_num];
        }

//        printf("seg_num = %d, init = %d, end = %d\n", seg_num, init, end);

        // Set the type of this segment
        output[seg_num].type = classify_point(input[init],noe);

//        output[seg_num].type = 0; //TODO Test  Remove

        // Add the points
        for (int i = init; i < end; i++) {
            output[seg_num].vec.push_back(input[i]);
        }

        //printf("Segment %d added, size = %d, type = %d\n", seg_num, output[seg_num].vec.size(), output[seg_num].type);

        // Interpolate and add the interpolated points.
        // This only happens if the current segment is not the last one.
        if (seg_num < output.size() - 1) {
            std::vector<RealVector> r;
            int info = interpolate(noe,input[end - 1], input[end], r);

//            printf("type(%d) = %d, type(%d) = %d\n", end - 1, classify_point(input[end - 1],noe), end, classify_point(input[end],noe));

            if (info == INTERPOLATION_OK) {
                //printf("r.size() = %d\n", r.size());

                // If only one point is returned, add it to this segment and to the next one.
                // No gap is present between the segments.
                if (r.size() == 1) {
                    output[seg_num].vec.push_back(r[0]);
//                    printf("    Interpolation, added 1 point (a).\n");
                    output[seg_num + 1].vec.push_back(r[0]);
//                    printf("    Interpolation, added 1 point (b).\n");
                }// If two points are returned, the first one is added to the current segment,
                    // while the second one is added to the next segment. There is a gap between the two segments.
                else {
                    output[seg_num].vec.push_back(r[0]);
                    //                    printf("    Interpolation, added 2 points (a).\n");
                    output[seg_num + 1].vec.push_back(r[1]);
                    //                    printf("    Interpolation, added 2 points (b).\n");
                }
            }
        }
    }

    return;
}

double ColorCurve::shockspeed(int n, double Um[], double Up[], const FluxFunction &ff, const AccumulationFunction &aa, int type) {
    // Compute F(Up), F(Um):
    double Fp[n], Fm[n];
    fill_with_jet(ff, n, Up, 0, Fp, 0, 0);
    fill_with_jet(ff, n, Um, 0, Fm, 0, 0);

    double Gp[n], Gm[n];
    if (type == _SHOCK_SIMPLE_ACCUMULATION_) {
        for (int i = 0; i < n; i++) {
            Gp[i] = Up[i];
            Gm[i] = Um[i];
        }
    } else {
        fill_with_jet(aa, n, Up, 0, Gp, 0, 0);
        fill_with_jet(aa, n, Um, 0, Gm, 0, 0);
    }

    // The speed
    double s = 0, den = 0;
    int i;

    for (i = 0; i < n; i++) {
        s += (Fp[i] - Fm[i])*(Gp[i] - Gm[i]);
        den += (Gp[i] - Gm[i])*(Gp[i] - Gm[i]);
    }

    return s / den;
}

void ColorCurve::classify_curve(vector<vector<RealVector> > & input, const RealVector & Uref, int noe, int accumulationType, vector<HugoniotPolyLine> & output) {
    //    output.clear();
    //    int N = 3 + 2 * noe + 1;
    vector<vector<RealVector> > complete_segments;
    complete_segments.resize(input.size());
    for (int i = 0; i < input.size(); i++) {
        complete_segments[i].resize(input[i].size());
        //                cout << "tamanho de input " << input[i].size() << endl;
        //                cout << "tamanho de input " << Uref.size() << endl;
        //                cout<< "Noe: "<<noe<<endl;

        preprocess_data(input[i], Uref, noe, *fluxFunction_, *accFunction_, accumulationType, complete_segments[i]);

        //cout << "After preprocessing: size of elements in complete_segments = " <<  complete_segments[i][0].size() << endl;
    }

    for (unsigned int i = 0; i < complete_segments.size(); i++) {

        vector<HugoniotPolyLine> hugoniotPolyLineVector;
        classify_segments(complete_segments[i], noe, hugoniotPolyLineVector);
        //        cout << "hugoniot polyline: " << hugoniotPolyLineVector.size() << endl;
        //    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {
        //
        //        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].vec.size() - 1; j++) {
        //
        //
        //
        //
        //            cout << "type of " << j << " = " << hugoniotPolyLineVector[i].type << endl;
        //            cout << "coord 1 " << j << " = " << hugoniotPolyLineVector[i].vec[j] << endl;
        //            cout << "coord 2 " << j + 1 << " = " << hugoniotPolyLineVector[i].vec[j + 1] << endl;
        //
        //
        //        }
        //    }

        for (int j = 0; j < hugoniotPolyLineVector.size(); j++) output.push_back(hugoniotPolyLineVector[j]);

    }

    //        cout << "Tamanho de output" << output.size() << endl;
    return;

}



// Given an array of points that form a shockcurve it will return the same curve augmented with the valid eigenvalues at the
// reference point, and the speed and valid eigenvalues at each point.
//
//     INPUT:
//
//     curve: Array of points that form a shockcurve.
//      Uref: Reference point of the curve.
//       noe: Number of valid eigenvalues.
//        ff: Pointer to the FluxFunction being used.
//        aa: Pointer to the AccumulationFunction being used.
//      type: _SHOCK_SIMPLE_ACCUMULATION_ or _SHOCK_GENERAL_ACCUMULATION_.
//
//    OUTPUT:
//       out: Array of points augmented with the information necessary to perform the classification. 
//            Let n be the dimension of the space. Then each RealVector is of the form:
//
//                Elements 0,..., (n - 1):                 The point.
//                Elements n,..., (n + neo):               The eigenvalues at Uref.
//                Element (n + neo + 1):                   The speed at the point.
//                Elements (n + neo + 2),..., (n + 2*neo): The eigenvalues at the point.
//

int ColorCurve::preprocess_data(const std::vector<RealVector> &curve, const RealVector &Uref, int noe,
        const FluxFunction &ff, const AccumulationFunction &aa, int type,
        std::vector<RealVector> &out) {
    //    out.clear();

    //        cout <<"Dentro de preprocess data"<<endl;

    if (curve.size() != 0) {
        int m = Uref.size();
        double U[m];
        for (int i = 0; i < m; i++) U[i] = Uref.component(i);
        double lr[noe]; // Eigenvalues at Uref.
        std::vector<eigenpair> e;
        int info_eigen;

        // Find the eigenvalues at Uref
        if (type == _SHOCK_SIMPLE_ACCUMULATION_) {
            double A[m][m];
            fill_with_jet(ff, m, U, 1, 0, &A[0][0], 0);

            info_eigen = Eigen::eig(m, &A[0][0], e);
        } else {
            double A[m][m];
            fill_with_jet(ff, m, U, 1, 0, &A[0][0], 0);

            double B[m][m];
            fill_with_jet(aa, m, U, 1, 0, &B[0][0], 0);

            info_eigen = Eigen::eig(m, &A[0][0], &B[0][0], e);
        }

        if (info_eigen != 0) return ABORTED_PROCEDURE;

        for (int i = 0; i < noe; i++) lr[i] = e[i].r;

        out.resize(curve.size());

        // Onto the curve
        RealVector temp(m + 2 * noe + 1);
        for (unsigned int i = 0; i < curve.size(); i++) {
            for (int j = 0; j < m; j++) temp.component(j) = curve[i].component(j); // Point
            for (int j = 0; j < noe; j++) temp.component(j + m) = lr[j]; // Uref's eigenvalues

            double p[m];
            for (int j = 0; j < m; j++) p[j] = curve[i].component(j);
            temp.component(noe + m) = shockspeed(m, U, p, ff, aa, type); // Speed

            // Find the eigenvalues at the ith-point
            if (type == _SHOCK_SIMPLE_ACCUMULATION_) {
                double A[m][m];
                fill_with_jet(ff, m, p, 1, 0, &A[0][0], 0);

                info_eigen = Eigen::eig(m, &A[0][0], e);
            } else {
                double A[m][m];
                fill_with_jet(ff, m, p, 1, 0, &A[0][0], 0);

                double B[m][m];
                fill_with_jet(aa, m, p, 1, 0, &B[0][0], 0);

                info_eigen = Eigen::eig(m, &A[0][0], &B[0][0], e);
            }

            if (info_eigen != 0) return ABORTED_PROCEDURE;

            for (int j = 0; j < noe; j++) temp.component(noe + m + 1 + j) = e[j].r;

            //            out[i] = temp;

            out[i].resize(m + 2 * noe + 1);
            for (int j = 0; j < m + 2 * noe + 1; j++) out[i].component(j) = temp.component(j);


        }

    }
    return SUCCESSFUL_PROCEDURE;
}

