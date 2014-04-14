#include "StoneExplicitHugoniot.h"

void StoneExplicitHugoniot::regularly_sampled_segment(const RealVector &p, const RealVector &q, int n, Curve &curve) {
    curve.curve.clear();

    double delta = 1.0 / (double) (n - 1);

    for (int i = 0; i < n; i++) curve.curve.push_back((p - q)*(delta * (double) (i)) + q);

    return;
}

StoneExplicitHugoniot::StoneExplicitHugoniot(StoneFluxFunction *ff, StoneAccumulation *aa, const Boundary *bb, Stone_Explicit_Bifurcation_Curves *s) : ExplicitHugoniot(bb), f(ff), sebf(s), a(aa) {
    side_opposite_vertex.push_back(THREE_PHASE_BOUNDARY_SW_ZERO);
    side_opposite_vertex.push_back(THREE_PHASE_BOUNDARY_SO_ZERO);
    side_opposite_vertex.push_back(THREE_PHASE_BOUNDARY_SG_ZERO);
}

StoneExplicitHugoniot::~StoneExplicitHugoniot() {
}

void StoneExplicitHugoniot::curve(GridValues & grid, ReferencePoint &ref, int type, std::vector<HugoniotPolyLine> & hugoniotCurve, std::vector<RealVector> & transitonList) {


    //GridValues & grid, ReferencePoint & refPoint,std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList)


    referencepoint = ref.point;

    vector<Curve> outputCurve;

    RealVector permeability_params = f->perm().params().params();

    expw = permeability_params(0);
    expg = permeability_params(1);
    expo = permeability_params(2);

    expow = permeability_params(3);
    expog = permeability_params(4);

    cnw = permeability_params(5);
    cng = permeability_params(6);
    cno = permeability_params(7);

    lw = permeability_params(8);
    lg = permeability_params(9);

    low = permeability_params(10);
    log = permeability_params(11);

    epsl = permeability_params(12);

    // Only compute the curves in this case.
    //
    if (expw == 2.0 && expg == 2.0 && expo == 2.0 &&
            expow == 2.0 && expog == 2.0 &&
            cnw == 0.0 && cng == 0.0 && cno == 0.0 &&
            low == 0.0 && log == 0.0 &&
            epsl == 0.0) {

        // Decide what the case is.
        //
        RealVector flux_params = f->fluxParams().params();

        RealVector mu(3);
        mu(0) = flux_params(3); // muw
        mu(2) = flux_params(4); // mug
        mu(1) = flux_params(5); // muo

        muw = flux_params(3);
        mug = flux_params(4);
        muo = flux_params(5);

        std::vector<double> distance;
        std::vector<RealVector> vertex(3), point_on_side(3);

        //        double phi_begin;
        //        double  phi_end;

        for (int i = 0; i < 3; i++) {
            sebf->vertex_and_side(side_opposite_vertex[i], mu, vertex[i], point_on_side[i]);

            distance.push_back(distance_point_line_2D(referencepoint, vertex[i], point_on_side[i]));
        }

        // Near the umbilic point.
        //
        double inv_sum_mu = 1.0 / (mu(0) + mu(1) + mu(2));

        double min_distance = 1e-2;

        // Secondary bifurcations should be temporarily stored here before being added to curve.
        std::vector<Curve> secondary_bifurcation;


        switch (type) {
            case 0:

                function_to_use = &generic;
                referencepoint = ref.point;
                phi_begin = 0.0;
                phi_end = M_PI;
                break;
                
                
            case 8:
                
                function_to_use = &O_bif;

                referencepoint = project_point_onto_line_2D(referencepoint, vertex[1], point_on_side[1]);

                phi_begin = 0.0;
                phi_end = M_PI;
                
                
                break;
                
            case 7:
                
                function_to_use = &G_bif;

                referencepoint = project_point_onto_line_2D(referencepoint, vertex[2], point_on_side[2]);

                phi_begin = 0.0;
                phi_end = M_PI;
                
                break;
                
            case 6:
                
                function_to_use = &W_bif;

                referencepoint = project_point_onto_line_2D(referencepoint, vertex[0], point_on_side[0]);

                phi_begin = 0.0;
                phi_end = M_PI;
                
                
                
                
                

        }



        ExplicitHugoniot::curve(referencepoint, outputCurve);

        // Add the secondary bifurcations.
        //

        for (int i = 0; i < secondary_bifurcation.size(); i++) outputCurve.push_back(secondary_bifurcation[i]);


        ColorCurve colorCurve(*f, *a);

        vector<RealVector> segments;

        for (int i = 0; i < outputCurve.size(); i++) {


            for (int j = 0; j < outputCurve[i].curve.size(); j++) {
                segments.push_back(outputCurve[i].curve[j]);
            }


        }



        colorCurve.classify_segmented_curve(segments, ref,
                hugoniotCurve, transitonList);



        /*

            if (std::abs(distance[0] - distance[1]) < min_distance && std::abs(distance[1] - distance[2]) < min_distance) {
                // Add the secondary bifurcations.
                //
                for (int i = 0; i < 3; i++) {
                    Curve temp;
                    int n = 50;

                    regularly_sampled_segment(vertex[i], point_on_side[i], n, temp);
                    secondary_bifurcation.push_back(temp);
                }

                // The rest.
                //
                function_to_use = &umbilic;

                referencepoint.resize(2);
                referencepoint(0) = mu(0) * inv_sum_mu;
                referencepoint(1) = mu(1) * inv_sum_mu;

                phi_begin = 0.0;
                phi_end = M_PI;

                // TODO: Add the secundary bifurcations here. It is easy: add them to curve. They must be divided in
                //       segments because they may be extended later.

                //            TestTools::pause("Umbilic");
            } else if (distance[0] < min_distance && distance[1] > min_distance && distance[2] > min_distance) {
                function_to_use = &W_bif;

                referencepoint = project_point_onto_line_2D(referencepoint, vertex[0], point_on_side[0]);

                phi_begin = 0.0;
                phi_end = M_PI;

                // TODO: Add the secundary bifurcations here. It is easy: add them to curve. They must be divided in
                //       segments because they may be extended later.

                //            TestTools::pause("W_bif");
            } else if (distance[1] < min_distance && distance[0] > min_distance && distance[2] > min_distance) {
                function_to_use = &O_bif;

                referencepoint = project_point_onto_line_2D(referencepoint, vertex[1], point_on_side[1]);

                phi_begin = 0.0;
                phi_end = M_PI;

                // TODO: Add the secundary bifurcations here. It is easy: add them to curve. They must be divided in
                //       segments because they may be extended later.

                //            TestTools::pause("O_bif");
            } else if (distance[2] < min_distance && distance[0] > min_distance && distance[1] > min_distance) {
                function_to_use = &G_bif;

                referencepoint = project_point_onto_line_2D(referencepoint, vertex[2], point_on_side[2]);

                phi_begin = 0.0;
                phi_end = M_PI;

                // TODO: Add the secundary bifurcations here. It is easy: add them to curve. They must be divided in
                //       segments because they may be extended later.

                //            TestTools::pause("G_bif");
            } else if (referencepoint(0) < min_distance && referencepoint(1) > min_distance) {
                // Add the secondary bifurcations.
                //
                RealVector A(2);
                A(0) = 0.0;
                A(1) = 0.0;

                RealVector B(2);
                B(0) = 0.0;
                B(1) = 1.0;

                int n = 50;

                Curve AB;
                regularly_sampled_segment(A, B, n, AB);
                secondary_bifurcation.push_back(AB);

                // The rest.
                //
                function_to_use = &GO_side;

                referencepoint.resize(2);
                referencepoint(0) = 0.0;
                referencepoint(1) = referencepoint(1);

                phi_begin = -M_PI / 2;
                phi_end = M_PI / 2;

                // TODO: Add the secundary bifurcations here. It is easy: add them to curve. They must be divided in
                //       segments because they may be extended later.

                //            TestTools::pause("GO_side");
            } else if (referencepoint(0) < min_distance && referencepoint(1) < min_distance) {
                outputCurve.clear();

                // Add both sides that converge on this vertex.
                //
                int n = 50;

                referencepoint.resize(2);
                referencepoint(0) = 0.0;
                referencepoint(1) = referencepoint(1);

                phi_begin = -M_PI / 2;
                phi_end = M_PI / 2;

                // TODO: Add the secundary bifurcations here. It is easy: add them to curve. They must be divided in
                //       segments because they may be extended later.

                //            TestTools::pause("G vertex");

                return;
            } else {
                function_to_use = &generic;

                referencepoint = ref.point;

                phi_begin = 0.0;
                phi_end = M_PI;

                //            TestTools::pause("Generic");
            }

            // Compute the curve.
            //


            ExplicitHugoniot::curve(referencepoint, outputCurve);

            // Add the secondary bifurcations.
            //

            for (int i = 0; i < secondary_bifurcation.size(); i++) outputCurve.push_back(secondary_bifurcation[i]);
        }





        ColorCurve colorCurve(*f, *a);

        vector<RealVector> segments;

        for (int i = 0; i < outputCurve.size(); i++) {


            for (int j = 0; j < outputCurve[i].curve.size(); j++) {
                segments.push_back(outputCurve[i].curve[j]);
            }



        }



        colorCurve.classify_segmented_curve(segments, ref,
                hugoniotCurve, transitonList);




         */
    }


    }

    //c     it returns -1 if the origin is  too close to the umbilic point.
    //c     it returns 1  if the origin is on the line through sw=0, so=0
    //c     it returns 2  if the origin is on the line through sw=0, sg=0
    //c     it returns 3  if the origin is on the line through so=0, sg=0
    //c     it returns 0  if the origin is in a generic position.
    //c     for cases 1, 2, 3 above, it finds its projection
    //c     bifpt on the appropriate bifurcation line

    RealVector StoneExplicitHugoniot::umbilic(StoneExplicitHugoniot *seh, double phi) {


        return RealVector(2);
    }

    RealVector StoneExplicitHugoniot::G_bif(StoneExplicitHugoniot *seh, double phi) {
        //    RealVector flux_params = seh->f->fluxParams().params();

        double muw = seh->muw;
        double mug = seh->mug;
        double muo = seh->muo;

        double alf = seh->referencepoint(0) / muw;
        double alf2 = alf*alf;

        double d = alf2 * (muw + muo) + (1. - alf * (muw + muo))*(1. - alf * (muw + muo)) / mug;

        double c = std::cos(phi);
        double s = std::sin(phi);

        double numer = -2. * alf2 * (c + s)*(alf * (muw + muo + mug) - 1.) / mug;
        double denom = d * c * s / (muw * muo) + (c * c / muw + s * s / muo + (c + s)*(c + s) / mug) * alf2;

        double r;
        if (std::abs(denom) <= 1.0e-3 * std::abs(numer)) {
            r = 1.0e3 * sign(denom) * sign(numer);
        } else {
            r = numer / denom;
        }

        RealVector hugxy(2);

        hugxy(0) = seh->referencepoint(0) + r*c;
        hugxy(1) = seh->referencepoint(1) + r*s;

        return hugxy;
    }

    RealVector StoneExplicitHugoniot::O_bif(StoneExplicitHugoniot *seh, double phi) {
        //    RealVector flux_params = seh->f->fluxParams().params();

        double muw = seh->muw;
        double mug = seh->mug;
        double muo = seh->muo;

        double alf = seh->referencepoint(0) / muw;
        double alf2 = alf*alf;

        double d = alf2 * (muw + mug) + (1. - alf * (muw + mug))*(1. - alf * (muw + mug)) / muo;

        double c = std::cos(phi);
        double s = std::sin(phi);

        double numer = -2. * alf2 * (c + s)*(alf * (muw + muo + mug) - 1.) / muo;
        double denom = d * c * s / (muw * mug) + (c * c / muw + s * s / mug + (c + s)*(c + s) / muo) * alf2;

        double r;
        if (std::abs(denom) <= 1.0e-3 * std::abs(numer)) {
            r = 1.0e3 * sign(denom) * sign(numer);
        } else {
            r = numer / denom;
        }

        RealVector hugxy(2);

        hugxy(0) = seh->referencepoint(0) + r*c;
        hugxy(1) = 1. - (seh->referencepoint(0) + seh->referencepoint(1)) + r*s;
        hugxy(1) = 1. - (hugxy(0) + hugxy(1));

        return hugxy;
    }

    RealVector StoneExplicitHugoniot::W_bif(StoneExplicitHugoniot *seh, double phi) {
        //    RealVector flux_params = seh->f->fluxParams().params();

        double muw = seh->muw;
        double mug = seh->mug;
        double muo = seh->muo;

        double alf = seh->referencepoint(1) / muo;
        double alf2 = alf*alf;

        double d = alf2 * (mug + muo) + (1. - alf * (mug + muo))*(1. - alf * (mug + muo)) / muw;

        double c = std::cos(phi);
        double s = std::sin(phi);

        double numer = -2. * alf2 * (c + s)*(alf * (muw + muo + mug) - 1.) / muw;
        double denom = d * c * s / (mug * muo) + (c * c / mug + s * s / muo + (c + s)*(c + s) / muw) * alf2;

        double r;
        if (std::abs(denom) <= 1.0e-3 * std::abs(numer)) {
            r = 1.0e3 * sign(denom) * sign(numer);
        } else {
            r = numer / denom;
        }

        RealVector hugxy(2);

        hugxy(0) = 1. - (seh->referencepoint(0) + seh->referencepoint(1)) + r*c;
        hugxy(1) = seh->referencepoint(1) + r*s;
        hugxy(0) = 1. - (hugxy(0) + hugxy(1));

        return hugxy;
    }

    RealVector StoneExplicitHugoniot::generic(StoneExplicitHugoniot *seh, double phi) {
        //    RealVector permeability_params = seh->f->perm().params().params();

        //    double expw = permeability_params(0);
        double expg = seh->expg;
        double expo = seh->expo;

        double expow = seh->expow;
        double expog = seh->expog;

        double cnw = seh->cnw;
        double cng = seh->cng;
        double cno = seh->cno;

        double lw = seh->lw;
        double lg = seh->lg;

        double low = seh->low;
        double log = seh->log;

        double epsl = seh->epsl;

        // wave-2011_01/rp/wavcrv/shock.F
        // bgnshk <== fills the reference point

        // See what hugjnk does. It seems to be secundary in importance.

        //    // muw, muo, mug
        //    RealVector flux_params = seh->f->fluxParams().params();

        double muw = seh->muw;
        double mug = seh->mug;
        double muo = seh->muo;

        double c = cos(phi);
        double s = sin(phi);

        double c2 = c*c;
        double s2 = s*s;

        double cps = c + s;
        double cps2 = cps*cps;

        double sw = seh->referencepoint(0);
        double so = seh->referencepoint(1);
        double sg = 1.0 - (sw + so);

        double kw = sw * sw / muw;
        double ko = so * so / muo;
        double kg = sg * sg / mug;

        double numer1 = (sw * c / muw)*(ko + kg);
        double numer2 = kw * (so * s / muo - sg * (s + c) / mug);
        double numer3 = (so * s / muo)*(kw + kg);
        double numer4 = ko * (sw * c / muw - sg * (s + c) / mug);
        double numer = s * (numer1 - numer2) - c * (numer3 - numer4);
        numer = -2. * numer;

        double denom1 = (c2 / muw)*(ko + kg);
        double denom2 = kw * (s2 / muo + cps2 / mug);
        double denom3 = (s2 / muo)*(kw + kg);
        double denom4 = ko * (c2 / muw + cps2 / mug);
        double denom = s * (denom1 - denom2) - c * (denom3 - denom4);

        double r;
        if (std::abs(denom) <= 1.0e-3 * std::abs(numer)) {
            r = 1.0e3 * sign(denom) * sign(numer);
        } else {
            r = numer / denom;
        }

        RealVector hugxy(2);

        hugxy(0) = seh->referencepoint(0) + r*c;
        hugxy(1) = seh->referencepoint(1) + r*s;

        return hugxy;
    }

    RealVector StoneExplicitHugoniot::fobj(double phi) {
        return (*function_to_use)(this, phi);
    }

    RealVector StoneExplicitHugoniot::GO_side(StoneExplicitHugoniot *seh, double phi) {
        //    // muw, muo, mug
        //    RealVector flux_params = seh->f->fluxParams().params();

        double muw = seh->muw;
        double mug = seh->mug;
        double muo = seh->muo;

        //    double sw_ref = 0.0;
        double so_ref = seh->referencepoint(1); // r in Cido's text.

        double so_ref2 = so_ref*so_ref;

        double D_so_ref = so_ref2 / muo + (1.0 - so_ref)*(1.0 - so_ref) / mug;
        double a = (mug + muw) * so_ref2;
        double b = muw * (mug + muo) * so_ref2 / muo - mug * muw*D_so_ref;

        double f = muw*so_ref2;
        double e = 2.0 * f;

        double c = e + mug * muo*D_so_ref;
        double d = e + mug * muo * so_ref*D_so_ref;

        double cos_phi = cos(phi);
        double sin_phi = sin(phi);

        double A = a * cos_phi * cos_phi + b * sin_phi * sin_phi + c * cos_phi*sin_phi;
        double B = (2.0 * b * so_ref - e) * sin_phi + (c * so_ref - d) * cos_phi;

        double rho = -B / A;

        RealVector hugxy(2);

        hugxy(0) = rho*cos_phi;
        hugxy(1) = so_ref + rho*sin_phi;

        //    std::cout << "GO_side, phi = " << phi << std::endl;
        //    std::cout << "    so_ref = " << so_ref << std::endl;
        //    std::cout << "    so_ref2 = " << so_ref2 << std::endl;
        //    std::cout << "    D_so_ref = " << D_so_ref << std::endl;
        //    std::cout << "    a = " << a << std::endl;
        //    std::cout << "    b = " << b << std::endl;
        //    std::cout << "    c = " << c << std::endl;
        //    std::cout << "    d = " << d << std::endl;
        //    std::cout << "    e = " << e << std::endl;
        //    std::cout << "    f = " << f << std::endl;
        //    std::cout << "    cos = " << cos_phi << std::endl;
        //    std::cout << "    sin = " << sin_phi << std::endl;
        //    std::cout << "    A = " << A << std::endl;
        //    std::cout << "    B = " << B << std::endl;
        //    std::cout << "    rho = " << rho << std::endl;
        //    std::cout << "    hugxy = " << hugxy << std::endl;

        //    TestTools::pause();

        return hugxy;
    }

    RealVector StoneExplicitHugoniot::GW_side(StoneExplicitHugoniot *seh, double phi) {
        double muw = seh->muw;
        double mug = seh->mug;
        double muo = seh->muo;

        double sw_ref = seh->referencepoint(0); // r in Cido's text.
        //    double so_ref = seh->referencepoint(1);

        double sw_ref2 = sw_ref*sw_ref;

        double D_sw_ref = sw_ref2 / mug + (1.0 - sw_ref)*(1.0 - sw_ref) / muw;
        double a = (muo + muw) * sw_ref2;
        double b = muw * (mug + muo) * sw_ref2 / mug - muo * muw*D_sw_ref;

        double f = muo*sw_ref2;
        double e = 2.0 * f;

        double c = e + mug * muw*D_sw_ref;
        double d = e + mug * muw * sw_ref*D_sw_ref;

        double cos_phi = cos(phi);
        double sin_phi = sin(phi);

        double A = a * cos_phi * cos_phi + b * sin_phi * sin_phi + c * cos_phi*sin_phi;
        double B = (2.0 * b * sw_ref - e) * sin_phi + (c * sw_ref - d) * cos_phi;

        double rho = -B / A;

        RealVector hugxy(2);

        hugxy(0) = sw_ref + rho*cos_phi;
        hugxy(1) = rho*sin_phi;

        return hugxy;
    }

    RealVector StoneExplicitHugoniot::WO_side(StoneExplicitHugoniot *seh, double phi) {
        double muw = seh->muw;
        double mug = seh->mug;
        double muo = seh->muo;

        double sw_ref = seh->referencepoint(0); // r in Cido's text.
        //    double so_ref = seh->referencepoint(1);

        double sw_ref2 = sw_ref*sw_ref;

        double D_sw_ref = sw_ref2 / muw + (1.0 - sw_ref)*(1.0 - sw_ref) / muo;
        double a = (muo + mug) * sw_ref2;
        double b = mug * (muw + muo) * sw_ref2 / muw - muo * mug*D_sw_ref;

        double f = mug*sw_ref2;
        double e = 2.0 * f;

        double c = e + muo * muw*D_sw_ref;
        double d = e + muo * muw * sw_ref*D_sw_ref;

        double cos_phi = cos(phi);
        double sin_phi = sin(phi);

        double A = a * cos_phi * cos_phi + b * sin_phi * sin_phi + c * cos_phi*sin_phi;
        double B = (2.0 * b * sw_ref - e) * sin_phi + (c * sw_ref - d) * cos_phi;

        double rho = -B / A;

        RealVector hugxy(2);

        hugxy(0) = sw_ref + rho*cos_phi;
        hugxy(1) = rho*sin_phi;

        return hugxy;
    }

