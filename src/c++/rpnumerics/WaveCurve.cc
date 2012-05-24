#include <vector>

#include "WaveCurve.h"

double WaveCurve::ddot(int n, double *x, double *y) {
    double p = 0.0;

    for (int i = 0; i < n; i++) p += x[i] * y[i];

    return p;
}

WaveCurve::WaveCurve(const FluxFunction *f, const AccumulationFunction *a, const Boundary *b) {
    ff = f;
    aa = a;
    boundary = b;
}

WaveCurve::~WaveCurve() {
}

// TODO: It MAY be important to pass the final vector of direction of one curve of the wavecurve to the next one.
//

int WaveCurve::half_wavecurve(int initial_curve, const RealVector &init, int family, int increase, std::vector<Curve> &c) {
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

    // To prevent the SHOCK_AS_ENGINE_FOR_COMPOSITE from finishing too early, it must be informed
    // of the number of double contacts it has to ignore, which is 
    // the maximum number of rarefactions in the stack for a given time.

    while (true) {

        int out;

        if (curve_to_be_computed == RAREFACTION_CURVE) {
            printf("\n\n\n WAVECURVE: RAREFACTION \n\n\n");

            std::vector<RealVector> inflection_points;

            double deltaxi = 1e-3;
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

            first_curve = false;

            // The index of the rarefaction's related curve is the position of the rarefaction in the wavecurve.
            index_current_rarefaction = c.size();
            c.push_back(Curve(temp_curve, RAREFACTION_CURVE, index_current_rarefaction));

            if (out == RAREFACTION_NOT_MONOTONOUS) {
                previous_curve = curve_to_be_computed;
                curve_to_be_computed = COMPOSITE_CURVE;
                origin = COMPOSITE_FROM_NORMAL_RAREFACTION;

                rar_before_composite.clear();
                rar_before_composite.resize(temp_curve.size());
                for (int i = 0; i < temp_curve.size(); i++) {
                    rar_before_composite[i].resize(temp_curve[i].size());
                    for (int j = 0; j < temp_curve[i].size(); j++) rar_before_composite[i].component(j) = temp_curve[i].component(j);
                }

                // temp_init is not necessary here.
            } else if (out == RAREFACTION_REACHED_BOUNDARY) return WAVE_CURVE_REACHED_BOUNDARY;
        } else if (curve_to_be_computed == SHOCK_CURVE) {
            printf("\n\n\n WAVECURVE: SHOCK \n\n\n");
            //          out = shock(temp_init, temp_curve);
            //           std::vector<RealVector> shockcurve_alt;

            //          out = Shock::curve(rar_point, true, rar_point, increase, family, SHOCK_AS_ENGINE_FOR_COMPOSITE, &orig_direction, ff, aa, boundary, shockcurve, shockcurve_alt);
            std::vector<RealVector> temp_curve, temp_curve_alt;
            RealVector *orig_direction = (RealVector*) 0; // Only used when SHOCK_AS_ENGINE_FOR_COMPOSITE; when SHOCK_FOR_ITSELF it has no utility.

            int info, info_alt;
            bool valid_curve_found = false;
            bool valid_curve_alt_found = false;

            if (first_curve) {
                printf("WaveCurve. SHOCK. First curve.\n");
                //                out = Shock::curve(init, true, init, increase, family, SHOCK_FOR_ITSELF, orig_direction, (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, temp_curve, temp_curve_alt);
                Shock::curve(init, true, init, increase, family, SHOCK_FOR_ITSELF, orig_direction, 0, (FluxFunction*) ff, (AccumulationFunction*) aa, (Boundary*) boundary,
                        temp_curve, info,
                        temp_curve_alt, info_alt);

                // Only temp_curve is filled in this case.
                //
                c.push_back(Curve(temp_curve, SHOCK_CURVE, c.size()));

                first_curve = false;
                valid_curve_found = true;
            } else {
                valid_curve_found = false;
                valid_curve_alt_found = false;

                printf("WaveCurve. SHOCK. Not first curve.\n");
                printf("    temp_init = (%f, %f)\n", temp_init.component(0), temp_init.component(1));
                //                out = Shock::curve(init, false, temp_init, increase, family, SHOCK_FOR_ITSELF, orig_direction, (FluxFunction*)ff, (AccumulationFunction*)aa, (Boundary*)boundary, temp_curve, temp_curve_alt);
                Shock::curve(init, false, temp_init, increase, family, SHOCK_FOR_ITSELF, orig_direction, 0, (FluxFunction*) ff, (AccumulationFunction*) aa, (Boundary*) boundary,
                        temp_curve, info,
                        temp_curve_alt, info_alt);

                printf("WaveCurve: info = %d, info_alt = %d\n", info, info_alt);

                bool check_temp_alt = false;

                // Decide which curve is the good one.
                if (temp_curve.size() > 1) {
                    double temp_direction[dim];
                    for (int i = 0; i < dim; i++) temp_direction[i] = temp_curve[1].component(i) - temp_curve[0].component(i);

                    if (ddot(dim, previous_vector, temp_direction) > 0) {
                        c.push_back(Curve(temp_curve, SHOCK_CURVE, c.size()));
                        valid_curve_found = true;
                    } else check_temp_alt = true;
                } else check_temp_alt = true;

                if (temp_curve_alt.size() > 1 && check_temp_alt) {
                    double temp_direction_alt[dim];
                    for (int i = 0; i < dim; i++) temp_direction_alt[i] = temp_curve_alt[1].component(i) - temp_curve_alt[0].component(i);

                    if (ddot(dim, previous_vector.components(), temp_direction_alt) > 0) {
                        c.push_back(Curve(temp_curve_alt, SHOCK_CURVE, c.size()));
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

            //            if (out == SHOCK_REACHED_BOUNDARY) return WAVE_CURVE_REACHED_BOUNDARY;
            // SUPER WARNING!!!
            // This is going to change when the recursive or tree-like algorithm for the wavecurve is mature.
            //
            if ((valid_curve_found && info == SHOCK_REACHED_BOUNDARY) || (valid_curve_alt_found && info_alt == SHOCK_REACHED_BOUNDARY)) {
                printf("info == SHOCK_REACHED_BOUNDARY || info_alt == SHOCK_REACHED_BOUNDARY\n");
                return WAVE_CURVE_REACHED_BOUNDARY;
            }                //            else if (out == NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY){
            else if ((valid_curve_found && info == NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY) ||
                    (valid_curve_alt_found && info_alt == NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY)) {
                printf("WaveCurve: NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY\n");
                previous_curve = curve_to_be_computed;
                curve_to_be_computed = RAREFACTION_CURVE;

                // Dan suggests that the initial point of a rarefaction after a shock can be taken as the first non-Lax point of
                // the previous shock, rather than the last Lax point of the shock curve. So far no-one can tell for sure
                // what is the best approach. For the moment being, the first non-Lax point is discarded, so here
                // the first point of the rarefaction is the last Lax point of the shock curve.
                temp_init = temp_curve[temp_curve.size() - 1];
            }                //            else if (out == NON_LAX_FOR_OTHER_REASON){
            else if ((valid_curve_found && info == NON_LAX_FOR_OTHER_REASON) ||
                    (valid_curve_alt_found && info_alt == NON_LAX_FOR_OTHER_REASON)) {
                return WAVE_CURVE_SHOCK_RIGHT_CHARACTERISTIC_WITH_OTHER_FAMILY;
            }
        } else if (curve_to_be_computed == COMPOSITE_CURVE) {
            printf("\n\n\n WAVECURVE: COMPOSITE \n\n\n");
            int number_ignore_doub_contact;
            if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION) number_ignore_doub_contact = 0;
            else {
                printf("    Size of indices in stack: %d\n", rarefaction_stack_indices.size());
                for (int i = 0; i < rarefaction_stack_indices.size(); i++) printf("rarefaction_stack_indices[%d] = %d\n", i, rarefaction_stack_indices[i]);

                printf("    Rar stack:\n");
                for (int i = 0; i < rarefaction_stack.size(); i++) printf("rarefaction_stack[%d].index_related_curve = %d\n", i, rarefaction_stack[i].index_related_curve);

                int pos = 0;
                bool found = false;
                while (pos < rarefaction_stack_indices.size() && !found) {
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
                int m = c[n - 1].curve.size();
                p = c[n - 1].curve[m - 1];

                temp_curve.push_back(p);
            }

            printf("    WAVECURVE. Composite will be computed with number_ignore_doub_contact = %d\n", number_ignore_doub_contact);
            out = CompositeCurve::curve(rar_before_composite, origin, family, increase, number_ignore_doub_contact,
                    (FluxFunction*) ff, (AccumulationFunction*) aa, (Boundary*) boundary, temp_curve);

            // Build the vector of indices
            std::vector<int> related_indices_for_composite(temp_curve.size());
            if (origin == COMPOSITE_FROM_NORMAL_RAREFACTION) {
                related_indices_for_composite[0] = rar_before_composite.size() - 1;
                for (int i = 1; i < temp_curve.size(); i++) {
                    related_indices_for_composite[i] = rar_before_composite.size() - (i + COMPOSITE_FROM_NORMAL_RAREFACTION_START - 1);
                }
            } else {
                for (int i = 0; i < temp_curve.size(); i++) {
                    related_indices_for_composite[i] = rar_before_composite.size() - 1 - i;
                }
            }

            c.push_back(Curve(temp_curve, related_indices_for_composite, COMPOSITE_CURVE, index_current_rarefaction));

            if (out == COMPOSITE_REACHED_BOUNDARY) {
                return WAVE_CURVE_REACHED_BOUNDARY;
            } else if (out == COMPOSITE_EXHAUSTED_RAREFACTION) {

                printf("WaveCurve. COMPOSITE_EXHAUSTED_RAREFACTION, rarefaction_stack.size() = %d\n", rarefaction_stack.size());
                if (rarefaction_stack.size() > 0) {
                    printf("    rarefaction_stack.size() > 0\n");
                    // Copy the elements in the stack to the rarefaction curve that will be fed to the next composite.
                    rar_before_composite.clear();
                    rar_before_composite.resize(rarefaction_stack[rarefaction_stack.size() - 1].curve.size());
                    for (int i = 0; i < rarefaction_stack[rarefaction_stack.size() - 1].curve.size(); i++) {
                        rar_before_composite[i].resize(rarefaction_stack[rarefaction_stack.size() - 1].curve[i].size());
                        for (int j = 0; j < rarefaction_stack[rarefaction_stack.size() - 1].curve[i].size(); j++) {
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
                } else {
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
            } else if (out == COMPOSITE_REACHED_DOUBLE_CONTACT || out == COMPOSITE_EXHAUSTED_RAREFACTION_AND_REACHED_DOUBLE_CONTACT) {
                previous_curve = curve_to_be_computed;
                curve_to_be_computed = RAREFACTION_CURVE;

                temp_init.resize(temp_curve[temp_curve.size() - 1].size());
                for (int i = 0; i < temp_curve[temp_curve.size() - 1].size(); i++) temp_init.component(i) = temp_curve[temp_curve.size() - 1].component(i);

                // Fill the stack with the points in the rarefaction from the beginning of it and
                // until the point just before reaching the double contact.
                //
                std::vector<RealVector> rar_for_stack;
                std::vector<int> rar_for_stack_index;

                for (int i = 0; i < related_indices_for_composite[related_indices_for_composite.size() - 1] - 1; i++) {
                    RealVector r(rar_before_composite[i].size());
                    for (int j = 0; j < r.size(); j++) r.component(j) = rar_before_composite[i].component(j);

                    rar_for_stack.push_back(r);
                    rar_for_stack_index.push_back(i);
                }

                printf("    Creating the stack: rar_for_stack.size() = %d\n", rar_for_stack.size());

                // Notice that the index in this curve is the one of the rarefaction that was not exhausted.
                // It will be retrieved after a composite exhausts a rarefaction and a new composite is computed
                // from the part of the rarefaction here stored.
                //
                if (rar_for_stack.size() > 0) {
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

    //return WAVE_CURVE_OK;
}

int WaveCurve::wavecurve(const RealVector &init, int family, int increase, std::vector<Curve> &c) {
    c.clear();

    half_wavecurve(RAREFACTION_CURVE, init, family, increase, c);

    for (int i = 0; i < c.size(); i++) {

        std::vector<RealVector> tempVector;
//        for (int k = 0; k < c[i].curve.size(); k++) {
//
//            cout << "Coordenada original: " << c[i].curve[k] << endl;
//
//        }



        vector<RealVector>::reverse_iterator rit;
        for (rit = c[i].curve.rbegin(); rit < c[i].curve.rend(); ++rit) {
            tempVector.push_back(*rit);
        }




        for (int j = 0; j < c[i].curve.size(); j++) {
            c[i].curve[j] = tempVector[j];

//            cout << "Coordenada invertida: " << c[i].curve[j] << endl;

        }





    }
    std::vector<Curve> tempCurveVector;
    vector<Curve>::reverse_iterator rit;
    for (rit = c.rbegin(); rit < c.rend(); ++rit) {

        tempCurveVector.push_back(*rit);

    }


     for (int j = 0; j < c.size(); j++) {
            c[j] = tempCurveVector[j];

    }




    half_wavecurve(SHOCK_CURVE, init, family, increase, c);

    return WAVE_CURVE_OK;
}

