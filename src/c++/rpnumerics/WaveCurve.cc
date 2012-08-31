#include "WaveCurve.h"

// Find the intesection between two segments: p1-p2 and q1-q2, store the answer in r.
// If there is no intersection, return false (and r is useless), otherwise return true.
//
bool WaveCurve::segment_intersection(double *p1, double *p2, double *q1, double *q2, double *r){
    double alpha, beta;

    double A[2][2], b[2];
    for (int i = 0; i < 2; i++){
        A[i][0] = p1[i] - p2[i];
        A[i][1] = q2[i] - q1[i];

        b[i]    = q2[i] - p2[i];
    }

    double delta = A[0][0]*A[1][1] - A[0][1]*A[1][0];
    if (fabs(delta) < 1e-10) {
        return false;
    }

    alpha = (b[0]*A[1][1] - b[1]*A[0][1])/delta;
    beta  = (b[1]*A[0][0] - b[0]*A[1][0])/delta;

    for (int i = 0; i < 2; i++) r[i] = .5*(alpha*p1[i] + (1.0 - alpha)*p2[i] + beta*q1[i] + (1.0 - beta)*q2[i]);

    return (alpha >= 0.0 && alpha <= 1.0) && (beta >= 0.0 && beta <= 1.0);
}

double WaveCurve::ddot(int n, double *x, double *y){
    double p = 0.0;

    for (int i = 0; i < n; i++) p += x[i]*y[i];

    return p;
}

WaveCurve::WaveCurve(const FluxFunction *f, const AccumulationFunction *a, const Boundary *b){
    ff = f;
    aa = a;
    boundary = b;
}

WaveCurve::~WaveCurve(){}

// TODO: It MAY be important to pass the final vector of direction of one curve of the wavecurve to the next one.
//
int WaveCurve::half_wavecurve(int initial_curve, const RealVector &init, int family, int increase, std::vector<Curve> &c){
    // TODO: Unify the Rarefaction's increase parameter with WAVE_FORWARD etc.
    int rarefaction_increase = (increase == WAVE_FORWARD) ? RAREFACTION_SPEED_INCREASE : RAREFACTION_SPEED_DECREASE;
    // END TODO:


    int dim = init.size();

    int curve_to_be_computed = initial_curve, previous_curve = initial_curve;

    std::vector<RealVector> temp_curve;
    std::vector<RealVector> rar_before_composite;
    std::vector<Curve> rarefaction_stack;

    std::vector<int> rarefaction_stack_indices;

    int index_current_rarefaction;

    int origin;

    RealVector temp_init(init);

    RealVector previous_curve_last_vector;
    RealVector previous_vector;

    bool first_curve = true;

    int shock_curve_reference = c.size();

    // To prevent the SHOCK_AS_ENGINE_FOR_COMPOSITE from finishing too early, it must be informed
    // of the number of double contacts it has to ignore, which is 
    // the maximum number of rarefactions in the stack for a given time.

    while (true){
        
        int out;

        if (curve_to_be_computed == RAREFACTION_CURVE){
            printf("\n\n\n WAVECURVE: RAREFACTION \n\n\n");

            std::vector<RealVector> inflection_points;

            double deltaxi = 1e-3; // Was 1e-3.
            out = Rarefaction::curve(temp_init, 
                         RAREFACTION_INITIALIZE_YES,
                         0,
                         family, 
                         rarefaction_increase, // RAREFACTION_SPEED_INCREASE, TODO: Solve this like estated before.
                         RAREFACTION_FOR_ITSELF, 
                         deltaxi,
                         ff, aa,
                         RAREFACTION_GENERAL_ACCUMULATION,
                         boundary,
                         temp_curve, 
                         inflection_points);

            // The index of the rarefaction's related curve is the position of the rarefaction in the wavecurve.
            index_current_rarefaction = c.size();

            std::vector<int> related_curve(temp_curve.size());
            std::vector<int> corresponding_indices(temp_curve.size());

            for (int i = 0; i < temp_curve.size(); i++){
                related_curve[i] = c.size();
                corresponding_indices[i] = i - 1;
            }

            if (first_curve) related_curve[0] = -1;           // Sentinel.
            else {
                related_curve[0] = c.size() - 1;
                corresponding_indices[0] = c[c.size() - 1].curve.size() - 1;
            }

            c.push_back(Curve(temp_curve, related_curve, corresponding_indices, RAREFACTION_CURVE, index_current_rarefaction, first_curve));

            first_curve = false;

            if (out == RAREFACTION_NOT_MONOTONOUS){
                previous_curve = curve_to_be_computed;
                curve_to_be_computed = COMPOSITE_CURVE;
                origin = COMPOSITE_FROM_NORMAL_RAREFACTION;

                rar_before_composite.clear();
                rar_before_composite.resize(temp_curve.size());
                for (int i = 0; i < temp_curve.size(); i++){
                    rar_before_composite[i].resize(temp_curve[i].size());
                    for (int j = 0; j < temp_curve[i].size(); j++) rar_before_composite[i].component(j) = temp_curve[i].component(j);
                }

                // temp_init is not necessary here.
            }
            else if (out == RAREFACTION_REACHED_BOUNDARY) return WAVE_CURVE_REACHED_BOUNDARY;
        }
        else if (curve_to_be_computed == SHOCK_CURVE){
            printf("\n\n\n WAVECURVE: SHOCK \n\n\n");
 //          out = shock(temp_init, temp_curve);
 //           std::vector<RealVector> shockcurve_alt;

  //          out = Shock::curve(rar_point, true, rar_point, increase, family, SHOCK_AS_ENGINE_FOR_COMPOSITE, &orig_direction, ff, aa, boundary, shockcurve, shockcurve_alt);
            std::vector<RealVector> temp_curve, temp_curve_alt;
            RealVector *orig_direction = (RealVector*)0; // Only used when SHOCK_AS_ENGINE_FOR_COMPOSITE; when SHOCK_FOR_ITSELF it has no utility.

            int info, info_alt;
            bool valid_curve_found = false;
            bool valid_curve_alt_found = false;

            if (first_curve){
                printf("WaveCurve. SHOCK. Firstc.clear(); curve.\n");
//                out = Shock::curve(init, true, init, increase, family, SHOCK_FOR_ITSELF, orig_direction, (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, temp_curve, temp_curve_alt);
                Shock::curve(init, true, init, increase, family, SHOCK_FOR_ITSELF, orig_direction, 0, (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, 
                             temp_curve, info, 
                             temp_curve_alt, info_alt);

                // Only temp_curve is filled in this case.
                //
//                std::cout << "Before add_sigma" << std::endl;
//                std::vector<RealVector> temp2(temp_curve.size());
//                for (int i = 0; i < temp2.size(); i++){
//                    temp2[i].resize(temp_curve[i].size());
//                    for (int j = 0; j < temp_curve[i].size(); j++) temp2[i].component(j) = temp_curve[i].component(j);
//                }
//                Shock::add_sigma(init, (FluxFunction*)ff, (AccumulationFunction*)aa, temp2);
//                std::cout << "After add_sigma" << std::endl;
//                c.push_back(Curve(temp2, SHOCK_CURVE, c.size()));

                Shock::add_sigma(init, (FluxFunction*)ff, (AccumulationFunction*)aa, temp_curve);

                std::vector<int> related_curve(temp_curve.size());
                std::vector<int> corresponding_indices(temp_curve.size());

                for (int i = 0; i < temp_curve.size(); i++){
                    related_curve[i] = 0;
                    corresponding_indices[i] = 0;
                }
                related_curve[0] = -1; // TODO: Verificar esto, que puede estar mal. (Panters, Rodrigo, 9-agosto-2012) 

                c.push_back(Curve(temp_curve, related_curve, corresponding_indices, SHOCK_CURVE, shock_curve_reference, first_curve));
//                c[c.size() - 1].reference_point = c[0].curve[0]; // The reference point for the shock curves within the half-wavecurve 
//                                                                 // is the initial point of the half-wavecurve. This approach could be verified
//                                                                 // for complex cases. In such a case, a reference point (if different from the
//                                                                 // initial point) could be added to this method's parameter list.
                first_curve = false;
                valid_curve_found = true;
            }
            else {
                valid_curve_found = false;
                valid_curve_alt_found = false;

                printf("WaveCurve. SHOCK. Not first curve.\n");
                printf("    temp_init = (%f, %f)\n", temp_init.component(0), temp_init.component(1));
//                out = Shock::curve(init, false, temp_init, increase, family, SHOCK_FOR_ITSELF, orig_direction, (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, temp_curve, temp_curve_alt);
                Shock::curve(init, false, temp_init, increase, family, SHOCK_FOR_ITSELF, orig_direction, 0, (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, 
                             temp_curve, info, 
                             temp_curve_alt, info_alt);

                printf("WaveCurve: info = %d, info_alt = %d\n", info, info_alt);
                Shock::add_sigma(init, (FluxFunction*)ff, (AccumulationFunction*)aa, temp_curve);
                Shock::add_sigma(init, (FluxFunction*)ff, (AccumulationFunction*)aa, temp_curve_alt);

                bool check_temp_alt = false;

                // Decide which curve is the good one.
                if (temp_curve.size() > 1){
                    double temp_direction[dim];
                    for (int i = 0; i < dim; i++) temp_direction[i] = temp_curve[1].component(i) - temp_curve[0].component(i);

//                    if (ddot(dim, previous_vector, temp_direction) > 0){
//                        c.push_back(Curve(temp_curve, SHOCK_CURVE, c.size()));
//                        //c.pus        int index_related_curve;h_back(Curve(temp_curve_alt, SHOCK_CURVE, c.size())); // Eliminate afterwards
//                        valid_curve_found = true;
//                    }
//                    else check_temp_alt = true;
                    if ( ((temp_curve[1].component(2) < temp_curve[0].component(2)) && increase == WAVE_FORWARD) ||
                         ((temp_curve[1].component(2) > temp_curve[0].component(2)) && increase == WAVE_BACKWARD)){

                        std::vector<int> related_curve(temp_curve.size());
                        std::vector<int> corresponding_indices(temp_curve.size());

                        for (int i = 0; i < temp_curve.size(); i++){
                            related_curve[i] = 0;
                            corresponding_indices[i] = 0;
                        }
                        related_curve[0] = -1;

                        c.push_back(Curve(temp_curve, related_curve, corresponding_indices, SHOCK_CURVE, shock_curve_reference, first_curve));

//                        std::vector<int> corresponding_indices(temp_curve.size());
//                        for (int i = 0; i < temp_curve.size(); i++) corresponding_indices[i] = 0;
//                        c.push_back(Curve(temp_curve, SHOCK_CURVE, c.size()));

                        valid_curve_found = true;
                    }
                    else check_temp_alt = true;
                }
                else check_temp_alt = true;
                
                if (temp_curve_alt.size() > 1 && check_temp_alt){
                    double temp_direction_alt[dim];
                    for (int i = 0; i < dim; i++) temp_direction_alt[i] = temp_curve_alt[1].component(i) - temp_curve_alt[0].component(i);

//                    if (ddot(dim, previous_vector.components(), temp_direction_alt) > 0){
//                        c.push_back(Curve(temp_curve_alt, SHOCK_CURVE, c.size()));
//                        //c.push_back(Curve(temp_curve, SHOCK_CURVE, c.size())); // Eliminate afterwards
//                        valid_curve_alt_found = true;
//                    }
                    if ( ((temp_curve_alt[1].component(2) < temp_curve_alt[0].component(2)) && increase == WAVE_FORWARD) ||
                         ((temp_curve_alt[1].component(2) > temp_curve_alt[0].component(2)) && increase == WAVE_BACKWARD)){

                        std::vector<int> related_curve(temp_curve_alt.size());
                        std::vector<int> corresponding_indices(temp_curve_alt.size());

                        for (int i = 0; i < temp_curve_alt.size(); i++){
                            related_curve[i] = 0;
                            corresponding_indices[i] = 0;
                        }
                        related_curve[0] = -1;

                        c.push_back(Curve(temp_curve_alt, related_curve, corresponding_indices, SHOCK_CURVE, shock_curve_reference, first_curve));

//                        c.push_back(Curve(temp_curve_alt, SHOCK_CURVE, c.size()));
                        valid_curve_alt_found = true;
                    }
                }

                printf("valid_curve_found = %d, valid_curve_alt_found = %d\n", valid_curve_found, valid_curve_alt_found);

                if (!valid_curve_found && !valid_curve_alt_found) {
                    printf("    temp_curve_alt.size() = %d, check_temp_alt = %d\n", temp_curve_alt.size(), check_temp_alt);
                    printf("    WAVE_CURVE_ERROR\n");
                    return WAVE_CURVE_ERROR;
                }
            }
            printf("WaveCurve. SHOCK. out = %d\n", out);

//            c.push_back(Curve(temp_curve, SHOCK_CURVE));//modificar segundo a observacao embaixo
// TODO: IMPORTANTE: Quando o choque for precedido por alguma curva (acho que só poderia ser uma composta) acredito que temos que ver qual das duas curvas: temp_curve ou temp_curve_alt sai na mesma direcao (desde temp_init) que a diferenca entre o ultimo e o penultimo pontos da curva composta anterior, só fariamos o push_back de uma das duas curvas temp_curve o temp_curve_alt. Tal vez precisemos separar os casos de choques precedidos por composta dos casos de choques sem nada antes. (Panters)

//            if (out == SHOCK_RE        int index_related_curve;ACHED_BOUNDARY) return WAVE_CURVE_REACHED_BOUNDARY;
            // SUPER WARNING!!!
            // This is going to change when the recursive or tree-like algorithm for the wavecurve is mature.
            //
            if ((valid_curve_found && info == SHOCK_REACHED_BOUNDARY) || (valid_curve_alt_found && info_alt == SHOCK_REACHED_BOUNDARY)){
                printf("info == SHOCK_REACHED_BOUNDARY || info_alt == SHOCK_REACHED_BOUNDARY\n");
                return WAVE_CURVE_REACHED_BOUNDARY;
            }
//            else if (out == NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY){
            else if ((valid_curve_found && info == NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY) ||
                     (valid_curve_alt_found && info_alt == NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY)){
                printf("WaveCurve: NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY\n");
                previous_curve = curve_to_be_computed;
                curve_to_be_computed = RAREFACTION_CURVE;

                // Dan suggests that the initial point of a rarefaction after a shock can be taken as the first non-Lax point of
                // the previous shock, rather than the last Lax point of the shock curve. So far no-one can tell for sure
                // what is the best approach. For the moment being, the first non-Lax point is discarded, so here
                // the first point of the rarefaction is the last Lax point of the shock curve.
                //temp_init = temp_curve[temp_curve.size() - 1];
//                temp_init = c[c.size() - 1].curve[c[c.size() - 1].curve.size() - 1];

                temp_init.resize(init.size());
                for (int i = 0; i < init.size(); i++) temp_init.component(i) = c[c.size() - 1].curve[c[c.size() - 1].curve.size() - 1].component(i);
            }
//            else if (out == NON_LAX_FOR_OTHER_REASON){
            else if ((valid_curve_found && info == NON_LAX_FOR_OTHER_REASON) || 
                     (valid_curve_alt_found && info_alt == NON_LAX_FOR_OTHER_REASON)){
                return WAVE_CURVE_SHOCK_RIGHT_CHARACTERISTIC_WITH_OTHER_FAMILY;
            }
        }
        else if (curve_to_be_computed == COMPOSITE_CURVE){
            printf("\n\n\n WAVECURVE: COMPOSITE \n\n\n");
            int number_ignore_doub_contact;
            if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION) number_ignore_doub_contact = 0;
            else {
                printf("    Size of indices in stack: %d\n", rarefaction_stack_indices.size());
                for (int i = 0; i < rarefaction_stack_indices.size(); i++) printf("rarefaction_stack_indices[%d] = %d\n", i, rarefaction_stack_indices[i]);

                printf("    Rar stack:\n");
                for (int i = 0; i < rarefaction_stack.size(); i++) printf("rarefaction_stack[%d].indecx_related_curve = %d\n", i, rarefaction_stack[i].index_related_curve);
          
                int pos = 0; bool found = false;
                while (pos < rarefaction_stack_indices.size() && !found){
                    if (rarefaction_stack_indices[pos] == index_current_rarefaction) found = true;
                    else pos++;
                }

                if (found) number_ignore_doub_contact = rarefaction_stack_indices.size() - pos;
                else number_ignore_doub_contact = 0;

                if (found) printf("    Found: number_ignore_doub_contact = %d\n", number_ignore_doub_contact);
                else printf("    Not found: number_ignore_doub_contact = %d\n", number_ignore_doub_contact);

                temp_curve.clear();

                RealVector p(2);

                int n = c.size();
                int m  = c[n - 1].curve.size();
                p = c[n - 1].curve[m - 1];

                temp_curve.push_back(p);
            } 

            printf("    WAVECURVE. Composite will be computed with number_ignore_doub_contact = %d\n", number_ignore_doub_contact);
            // TEMPORAL BELOW
            std::vector<RealVector> shock_curve_temp;
            // TEMPORAL ABOVE

            out = CompositeCurve::curve(rar_before_composite, origin, family, increase, number_ignore_doub_contact,
                                        (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, temp_curve);
//                                        (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, temp_curve, shock_curve_temp);
//c.clear();
            // Build the vector of indices
            std::vector<int> related_indices_for_composite(temp_curve.size());
            if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION){
                related_indices_for_composite[0] = rar_before_composite.size() - 1;
                for (int i = 1; i < temp_curve.size(); i++){
                    related_indices_for_composite[i] = rar_before_composite.size() - (i + COMPOSITE_FROM_NORMAL_RAREFACTION_START - 1);
                }
            }
            else {
                for (int i = 0; i < temp_curve.size(); i++){
                    related_indices_for_composite[i] = rar_before_composite.size() - 1 - i;
                }
            }

            // Add lambda as the third component of each point of the composite curve
            std::vector<int> related_curve(temp_curve.size());
            std::vector<RealVector> temp_composite_for_storage(temp_curve.size());

            for (int i = 0; i < temp_curve.size(); i++){
                related_curve[i] = index_current_rarefaction;

                temp_composite_for_storage[i].resize(temp_curve[i].size() + 1);
                for (int j = 0; j < temp_curve[i].size(); j++) temp_composite_for_storage[i].component(j) = temp_curve[i].component(j);
                temp_composite_for_storage[i].component(temp_curve[i].size()) = c[index_current_rarefaction].curve[related_indices_for_composite[i]].component(temp_curve[i].size());
            }

            //c.push_back(Curve(temp_curve, related_indices_for_composite, COMPOSITE_CURVE, index_current_rarefaction, first_curve));
            c.push_back(Curve(temp_composite_for_storage, related_curve, related_indices_for_composite, COMPOSITE_CURVE, index_current_rarefaction, first_curve));

            if (out == COMPOSITE_REACHED_BOUNDARY) {
//                c.push_back(Curve(shock_curve_temp, SHOCK_CURVE, index_current_rarefaction, first_curve));
                return WAVE_CURVE_REACHED_BOUNDARY;
            }
            else if (out == COMPOSITE_EXHAUSTED_RAREFACTION ){

                printf("WaveCurve. COMPOSITE_EXHAUSTED_RAREFACTION, rarefaction_stack.size() = %d\n", rarefaction_stack.size());
                if (rarefaction_stack.size() > 0){
                    printf("            int index_related_curve;rarefaction_stack.size() > 0\n");
                    // Copy the elements in the stack to the rarefaction curve that will be fed to the next composite.
                    rar_before_composite.clear();
                    rar_before_composite.resize(rarefaction_stack[rarefaction_stack.size() - 1].curve.size());
                    for (int i = 0; i < rarefaction_stack[rarefaction_stack.size() - 1].curve.size(); i++){
                        rar_before_composite[i].resize(rarefaction_stack[rarefaction_stack.size() - 1].curve[i].size());
                        for (int j = 0; j < rarefaction_stack[rarefaction_stack.size() - 1].curve[i].size(); j++){
                            rar_before_composite[i].component(j) = rarefaction_stack[rarefaction_stack.size() - 1].curve[i].component(j);
                        }
                    }

                    // To establish that the next composite curve is based on a rarefaction from the stack, the following
                    // parameters are set. Notice that it is crucial to update the index of the rarefaction in which the composite will be based and
                    // to pop the rarefaction from the stack.
                    //
                    origin = COMPOSITE_FROM_STACK_RAREFACTION;
                    previous_curve = curve_to_be_computed;
                    curve_to_be_computed = COMPOSITE_CURVE;
                    index_current_rarefaction = rarefaction_stack[rarefaction_stack.size() - 1].index_related_curve;
                    rarefaction_stack.pop_back();

                    //if (rarefaction_stack.size() == 0) rarefaction_stack_indices.clear();
                }
                else {
                    printf("    rarefaction_stack.size() = 0\n");
                    previous_curve = curve_to_be_computed;
                    curve_to_be_computed = SHOCK_CURVE;

                    temp_init.resize(temp_curve[temp_curve.size() - 1].size());
                    for (int i = 0; i < temp_curve[temp_curve.size() - 1].size(); i++) temp_init.component(i) = temp_curve[temp_curve.size() - 1].component(i);
                    printf("    COMPOSITE. temp_init = (%f, %f)\n", temp_init.component(0), temp_init.component(1));

                    // Last composite vector (previous vector) to establish the direction of the next curve (a shock).
                    previous_vector.resize(temp_curve[temp_curve.size() - 1].size());
                    for (int i = 0; i < temp_curve[temp_curve.size() - 1].size(); i++) previous_vector.component(i) = temp_curve[temp_curve.size() - 1].component(i) - temp_curve[temp_curve.size() - 2].component(i);
                 }
            }
            else if (out == COMPOSITE_REACHED_DOUBLE_CONTACT || out == COMPOSITE_EXHAUSTED_RAREFACTION_AND_REACHED_DOUBLE_CONTACT){
                previous_curve = curve_to_be_computed;
                curve_to_be_computed = RAREFACTION_CURVE;

                temp_init.resize(temp_curve[temp_curve.size() - 1].size());
                for (int i = 0; i < temp_curve[temp_curve.size() - 1].size(); i++) temp_init.component(i) = temp_curve[temp_curve.size() - 1].component(i);

                // Fill the stack with the points inc.clear(); the rarefaction from the beginning of it andc
                // until the point just before reaching the double contact.
                //
                std::vector<RealVector> rar_for_stack;
                std::vector<int> rar_for_stack_index;

                for (int i = 0; i < related_indices_for_composite[related_indices_for_composite.size() - 1] - 1; i++){
                    RealVector r(rar_before_composite[i].size());
                    for (int j = 0; j < r.size(); j++) r.component(j) = rar_before_composite[i].component(j);

                    rar_for_stack.push_back(r);
                    rar_for_stack_index.push_back(i);
                }

                printf("    Creating the stack: rar_for_stack.size() = %d\n", rar_for_stack.size());

                // Notice that the index in this curve is the one of the rarefaction that was not exhacusted.
                // It will be retrieved after a composite exhausts a rarefaction and a new composite is computed
                // from the part of the rarefaction here stored.
                //
                if (rar_for_stack.size() > 0){
                    rarefaction_stack.push_back(Curve(rar_for_stack, rar_for_stack_index, RAREFACTION_CURVE, index_current_rarefaction));

                    // This will help the SHOCK_AS_ENGINE_FOR_COMPOSITE ignore some double contacts.
                    rarefaction_stack_indices.push_back(index_current_rarefaction); // Only push.

                    printf("    Now in the stack there are: %d curves.\n", rarefaction_stack.size());
                    for (int i = 0; i < rarefaction_stack.size(); i++) printf("    Curve in stack #%d has %d points\n", i, rarefaction_stack[i].curve.size());
                    for (int i = 0; i < rarefaction_stack_indices.size(); i++) printf("    Index of curve in stack #%d is %d\n", i, rarefaction_stack_indices[i]);

                    // Total number of double contacts along the current wave curve to be ignored by the SHOCK_AS_ENGINE_FOR_COMPOSITE
                    //  total_number_of_double_contacts = rarefaction_stack_indices.size();
                }
            }
        }
    }

    //return WAVE_CURVE_OK;        int index_related_curve;
}

int WaveCurve::wavecurve(const RealVector &init, int family, int increase, std::vector<Curve> &c){
    c.clear();

//    half_wavecurve(RAREFACTION_CURVE, init, init, true, family, increase, c);
//    half_wavecurve(SHOCK_CURVE,       init, init, true, family, increase, c);

    half_wavecurve(RAREFACTION_CURVE, init, family, increase, c);
    half_wavecurve(SHOCK_CURVE,       init, family, increase, c);

    return WAVE_CURVE_OK;
}

int WaveCurve::intersection(const std::vector<Curve> &c1, const std::vector<Curve> &c2, const RealVector &pmin, const RealVector &pmax, 
                             RealVector &p, int &subc1, int &subc1_point, int &subc2, int &subc2_point){
    PointND hpmin(2), hpmax(2);
    hpmin(0) = hpmin(1) = 0.0;
    hpmax(0) = hpmax(1) = 1.0;
    BoxND b(hpmin, hpmax);

    HyperOctree<WaveCurveSegment> h1(b), h2(b);

    // Fill quadtrees
    for (int i = 0; i < c1.size(); i++){
        if (c1[i].curve.size() < 2) continue;
        for (int j = 0; j < c1[i].curve.size() - 1; j++){
            //std::cout << "1, (" <<  c1[i].curve[j] << ")-(" << c1[i].curve[j + 1] << ")" << std::endl;
            WaveCurveSegment *wcs = new WaveCurveSegment(c1[i].curve[j], c1[i].curve[j + 1], i, j);
            h1.add(wcs);
        }
    }

    for (int i = 0; i < c2.size(); i++){
        if (c2[i].curve.size() < 2) continue;
        for (int j = 0; j < c2[i].curve.size() - 1; j++){
            //std::cout << "2, (" <<  c2[i].curve[j] << ")-(" << c2[i].curve[j + 1] << ")" << std::endl;
            WaveCurveSegment *wcs = new WaveCurveSegment(c2[i].curve[j], c2[i].curve[j + 1], i, j);
            h2.add(wcs);
        }
    }

    // Query the quadtrees to find the segments contained in the given box.
    PointND ppmin(2), ppmax(2);
    for (int i = 0; i < 2; i++){
        ppmin(i) = pmin.component(i);
        ppmax(i) = pmax.component(i);
    }

    BoxND box(ppmin, ppmax);

    std::vector<WaveCurveSegment*> wcs1, wcs2;
    h1.within_box(box, wcs1);
    h2.within_box(box, wcs2);

    // Find the first intersection point:
    //
    bool found = false;

    for (int i = 0; i < wcs1.size(); i++){
        for (int j = 0; j < wcs2.size(); j++){
            RealVector r(2);

            if (segment_intersection(wcs1[i]->rv[0].components(), wcs1[i]->rv[1].components(), 
                                     wcs2[j]->rv[0].components(), wcs2[j]->rv[1].components(), 
                                     r.components())
               ) {
                p = r;
                subc1 = wcs1[i]->curve_position; subc1_point = wcs1[i]->segment_position;
                subc2 = wcs2[j]->curve_position; subc2_point = wcs2[j]->segment_position;

                found = true;
                continue;
            }
        }
    }

    for (int i = 0; i < wcs1.size(); i++) delete wcs1[i];
    for (int i = 0; i < wcs2.size(); i++) delete wcs2[i];

    if (found) return WAVE_CURVE_INTERSECTION_FOUND;
    else       return WAVE_CURVE_INTERSECTION_NOT_FOUND;
}

