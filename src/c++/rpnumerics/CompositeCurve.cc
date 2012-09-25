 #include "CompositeCurve.h"

int CompositeCurve::curve(const std::vector<RealVector> &rarcurve, int origin, int family, int increase, int number_ignore_doub_contact,
                           FluxFunction *ff, AccumulationFunction *aa, 
                           Boundary *boundary, std::vector<RealVector> &compcurve){
//                           Boundary *boundary, std::vector<RealVector> &compcurve, std::vector<RealVector> &shock_curve_temp){

    if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION) compcurve.clear();

    int start;

    // There is a problem with certain points when the last point of the rarefaction lays on the inflection curve,
    // for example (.422851, .246623) with family 1.
    //
    // TODO: Finish this comment.
    if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION) start = COMPOSITE_FROM_NORMAL_RAREFACTION_START;
    else start = 1;

    int dim = rarcurve[rarcurve.size() - 1].size() - 1;

    if (rarcurve.size() > 1){
        RealVector rar_point(dim); // The eigenvalue is not necessary here.

        // Only add the last point of the rarefaction (the inflection point) if the rarefaction does not come from a stack.
        // (This is related to the wave curve.)
        if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION){
            for (int j = 0; j < dim; j++) rar_point.component(j) = rarcurve[rarcurve.size() - 1].component(j);
            compcurve.push_back(rar_point);
        }

        int i;
        for (i = rarcurve.size() - start; i >= 0; i--){ // This changed from i = rarcurve.size() - 2 to i = rarcurve.size() - 3 to avoid problems with certain points (.422851, .246623)
//            printf("Rarcuve's index = %d\n", i);
            std::vector<RealVector> shockcurve, shockcurve_alt;

            for (int j = 0; j < dim; j++) rar_point.component(j) = rarcurve[i].component(j);

            // Follow the direction given by the vector formed by the difference between the rarefaction's last two points.
            RealVector orig_direction(dim);
            if (i == rarcurve.size() - start) for (int j = 0; j < dim; j++) orig_direction.component(j) = rarcurve[i].component(j) - rarcurve[i - 1].component(j);
            else                              for (int j = 0; j < dim; j++) orig_direction.component(j) = rarcurve[i + 1].component(j) - rarcurve[i].component(j);

            double norm_orig = 0.0;
            for (int j = 0; j < dim; j++) norm_orig += orig_direction.component(j)*orig_direction.component(j);
            norm_orig = 1.0/sqrt(norm_orig);
            for (int j = 0; j < dim; j++) orig_direction.component(j) *= norm_orig;

//            printf("Inside composite: TESTING 2012/04/17.");
//            printf("    rar.size() = %d\n", rarcurve.size());
//            printf("    orig = (%f, %f)\n", orig_direction.component(0), orig_direction.component(1));

//            printf("Before shock: i = %d\n", i);
            int info = SHOCK_OK, info_alt = SHOCK_OK;
//            int info = Shock::curve(rar_point, true, rar_point, increase, family, SHOCK_AS_ENGINE_FOR_COMPOSITE, &orig_direction, ff, aa, boundary, shockcurve, shockcurve_alt);
            Shock::curve(rar_point, true, rar_point, increase, family, SHOCK_AS_ENGINE_FOR_COMPOSITE, &orig_direction, number_ignore_doub_contact, ff, aa, boundary, 
                         shockcurve, info, 
                         shockcurve_alt, info_alt);

//            printf("Composite. Shock returned: %d and %d\n", info, info_alt);

            if (info == SHOCK_ERROR || info == SHOCK_REACHED_BOUNDARY){
//                printf("Composite curve. Shock problem, info = %d.\n", info);
//                // TEMPORAL BELOW
//                shock_curve_temp.clear();
//                for (int i = 0; i < shockcurve.size(); i++) shock_curve_temp.push_back(shockcurve[i]);
//                // TEMPORAL ABOVE

                return COMPOSITE_REACHED_BOUNDARY;
            }
            else if (info == SHOCK_OK || info == SHOCK_REACHED_DOUBLE_CONTACT){
//                printf("Composite curve. Shock ok or reached double contact. shockcurve.size() = %d.\n", shockcurve.size());
//                printf("    i = %d, compcurve.size() = %d\n", i, compcurve.size());
                RealVector p_boundary;
                int edge;
                int info_boundary = boundary->intersection(shockcurve[shockcurve.size() - 1], compcurve[compcurve.size() - 1], p_boundary, edge);
                if (info_boundary == 1){
                    // Both inside.
//                    printf("    Composite: Both inside.\n");
                    compcurve.push_back(shockcurve[shockcurve.size() - 1]);
                }
                else if (info_boundary == -1){
                    // Both outside
//                    printf("    Composite: Both outside.\n");
                    return COMPOSITE_ERROR;// SHOCK_ERROR;
                }
                else {
                    // One inside, one outside
//                    printf("    Composite: In and out.\n");
                    compcurve.push_back(p_boundary);
                    return COMPOSITE_REACHED_BOUNDARY;// SHOCK_REACHED_BOUNDARY;
                    //return SHOCK_ERROR;
                }

//                printf("    Composite: Finished checking boundary\n");

                if (info == SHOCK_REACHED_DOUBLE_CONTACT){
                    if (i == 0) return COMPOSITE_EXHAUSTED_RAREFACTION_AND_REACHED_DOUBLE_CONTACT;
                    else return COMPOSITE_REACHED_DOUBLE_CONTACT;
                }

            }
//                if (shockcurve.size() > 0) compcurve.push_back(shockcurve[shockcurve.size() - 1]);
        }

        if (i == -1) return COMPOSITE_EXHAUSTED_RAREFACTION;
        else return COMPOSITE_DID_NOT_EXHAUST_RAREFACTION;
    }
}

