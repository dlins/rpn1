#include "CompositeCurve.h"

void CompositeCurve::curve(const std::vector<RealVector> &rarcurve, int origin, int family, int increase, 
                           FluxFunction *ff, AccumulationFunction *aa, 
                           Boundary *boundary, std::vector<RealVector> &compcurve){
    compcurve.clear();

    int start;

    // There is a problem with certain points when the last point of the rarefaction lays on the inflection curve,
    // for example (.422851, .246623) with family 1.
    //
    // TODO: Finish this comment.
    if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION) start = COMPOSITE_FROM_NORMAL_RAREFACTION_START;
    else start = 1;

    if (rarcurve.size() > 1){
        RealVector rar_point(rarcurve[rarcurve.size() - 1].size() - 1); // The eigenvalue is not necessary here.

        // Only add the last point of the rarefaction (the inflection point) if the rarefaction does not come from a stack.
        // (This is related to the wave curve.)
        if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION){
            for (int j = 0; j < rarcurve[rarcurve.size() - 1].size() - 1; j++) rar_point.component(j) = rarcurve[rarcurve.size() - 1].component(j);
            compcurve.push_back(rar_point);
        }

        for (int i = rarcurve.size() - start; i >= 0; i--){ // This changed from i = rarcurve.size() - 2 to i = rarcurve.size() - 3 to avoid problems with certain points (.422851, .246623)
            printf("Rarcuve's index = %d\n", i);
            std::vector<RealVector> shockcurve, shockcurve_alt;

            for (int j = 0; j < rarcurve[i].size() - 1; j++) rar_point.component(j) = rarcurve[i].component(j);

            // Follow the direction given by the vector formed by the difference between the rarefaction's last two points.
            RealVector orig_direction(rarcurve[i].size());            
            for (int j = 0; j < rarcurve[i].size(); j++) orig_direction.component(j) = rarcurve[i + 1].component(j) - rarcurve[i].component(j);

            printf("Before shock: i = %d\n", i);
            int info = Shock::curve(rar_point, true, rar_point, increase, family, SHOCK_AS_ENGINE_FOR_COMPOSITE, &orig_direction, ff, aa, boundary, shockcurve, shockcurve_alt);

            if (info == SHOCK_ERROR || info == SHOCK_REACHED_BOUNDARY){
                printf("Composite curve. Shock problem.\n");
                return;
            }
            else if (info == SHOCK_OK || info == SHOCK_REACHED_DOUBLE_CONTACT){
                RealVector p_boundary;
                int edge;
                int info_boundary = boundary->intersection(shockcurve[shockcurve.size() - 1], compcurve[compcurve.size() - 1], p_boundary, edge);
                if (info_boundary == 1){
                    // Both inside.
                    compcurve.push_back(shockcurve[shockcurve.size() - 1]);
                }
                else if (info_boundary == -1){
                    // Both outside
                    return;// SHOCK_ERROR;
                }
                else {
                    // One inside, one outside
                    compcurve.push_back(p_boundary);
                    return;// SHOCK_REACHED_BOUNDARY;
                    //return SHOCK_ERROR;
                }

                if (info == SHOCK_REACHED_DOUBLE_CONTACT) return;
            }
//                if (shockcurve.size() > 0) compcurve.push_back(shockcurve[shockcurve.size() - 1]);
        }
    }

    return;
}

