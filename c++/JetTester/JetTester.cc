#include "JetTester.h"

JetTester::JetTester(){
}

JetTester::~JetTester(){
}

// Grids are expected to be diadic: each element of subdivision is of the form (2^n + 1).
// The same is to be expected in the rest of the methods in this class.
// The user could use a more or less refined grid, always based on 2, and would
// obtain ever improving results.
//
void JetTester::populate_F(void *obj, int (*f)(void *obj, const RealVector &state, int degree, JetMatrix &jm),
                           const RealVector &pmin, const RealVector &pmax,
                           const std::vector<unsigned long int> &fine_subdivision,
                           MultiArray<RealVector> &fine_F, MultiArray<DoubleMatrix> &fine_JF){

    fine_F.resize(fine_subdivision);
    fine_JF.resize(fine_subdivision);

    //
    std::vector<std::vector<double> > coord(fine_subdivision.size());
    for (int i = 0; i < coord.size(); i++){
        double delta = (pmax[i] - pmin[i])/(double)(fine_subdivision[i] - 1);

        coord[i].resize(fine_subdivision[i]);
        for (int j = 0; j < fine_subdivision[i]; j++) coord[i][j] = pmin[i] + delta*((double)j);
    }

    // Use many times.
    //
    JetMatrix jm;
    RealVector point(fine_subdivision.size());

//    for (int i0 = 0; i0 < fine_subdivision[0]; i0++){
//        point(0) = coord[0][i0];

//        for (int i1 = 0; i1 < fine_subdivision[1]; i1++){
//            point(1) = coord[1][i1];

//            for (int i2 = 0; i2 < fine_subdivision[2]; i2++){
//                point(2) = coord[2][i2];

//                (*f)(obj, point, 1, jm);
//                fine_F(i0, i1, i2)  = jm.function();
//                fine_JF(i0, i1, i2) = jm.Jacobian();
//            }
//        }
//    }

    unsigned long int total_number_elements = 1;
    for (unsigned long int i = 0; i < fine_subdivision.size(); i++) total_number_elements *= fine_subdivision[i];

    for (unsigned long int location = 0; location < total_number_elements; location++){
        std::vector<unsigned long int> mi = fine_F.multiindex(location);

        for (int p = 0; p < fine_subdivision.size(); p++) point(p) = coord[p][mi[p]];

        (*f)(obj, point, 1, jm);
        fine_F(mi)  = jm.function();
        fine_JF(mi) = jm.Jacobian();
    }

    return;
}

//void JetTester::coarse_F(const MultiArray<RealVector> &fine_F, const MultiArray<DoubleMatrix> &fine_JF,
//                         int step,
//                         MultiArray<RealVector> &coarse_F, MultiArray<DoubleMatrix> &coarse_JF){

//    

//    return;
//}

void JetTester::populate_JF(void *obj, int (*f)(void *obj, const RealVector &state, int degree, JetMatrix &jm),
                            const RealVector &pmin, const RealVector &pmax,
                            const std::vector<unsigned long int> &subdivision,
                            MultiArray<DoubleMatrix> &JF, MultiArray<std::vector<DoubleMatrix> > &HF){

    JF.resize(subdivision);
    HF.resize(subdivision);

    //
    std::vector<std::vector<double> > coord(subdivision.size());
    for (int i = 0; i < coord.size(); i++){
        double delta = (pmax[i] - pmin[i])/(double)(subdivision[i] - 1);

        coord[i].resize(subdivision[i]);
        for (int j = 0; j < subdivision[i]; j++) coord[i][j] = pmin[i] + delta*((double)j);
    }

    // Use many times.
    //
    JetMatrix jm;
    RealVector point(subdivision.size());

    for (int i0 = 0; i0 < subdivision[0]; i0++){
        point(0) = coord[0][i0];

        for (int i1 = 0; i1 < subdivision[1]; i1++){
            point(1) = coord[1][i1];

            for (int i2 = 0; i2 < subdivision[2]; i2++){
                point(2) = coord[2][i2];

                (*f)(obj, point, 2, jm);
                JF(i0, i1, i2) = jm.Jacobian();
                HF(i0, i1, i2) = jm.Hessian();
            }
        }
    }

    return;
}

//void JetTester::numerical_Jacobian(const MultiArray<RealVector>    &F, 
//                                   const MultiArray<DoubleMatrix> &JF,
//                                   int rows, int cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
//                                   unsigned long int intdelta,
//                                   const RealVector &pmin, const RealVector &pmax,
//                                   const std::vector<unsigned long int> &subdivision,
//                                   /*MultiArray<DoubleMatrix> &numerical_analytic_deviation*/
//                                   DoubleMatrix &numerical_analytic_abs_deviation_sup,
//                                   double &synthetic_deviation,
//                                   double &max_abs_F){

//    // Delta.
//    //
//    std::vector<double > inv_2_delta(subdivision.size());
//    for (int i = 0; i < subdivision.size(); i++) inv_2_delta[i] = 1.0/((double)(2*intdelta)*(pmax[i] - pmin[i])/(double)(subdivision[i] - 1));

//    // Resize numerical_analytic_deviation.
//    //
//    std::vector<long unsigned int> range = F.range();
////    numerical_analytic_deviation.resize(range);

//    //  
//    //
//    numerical_analytic_abs_deviation_sup = DoubleMatrix::zero(rows, cols);

//    max_abs_F = 0.0;

//    // Proceed.
//    //
//    std::vector<double> dev(cols); // ASAP, rows -> number_of_equations, cols -> number_of_variables.
//    std::vector<unsigned long int> pos(cols);

//    for (int i0 = intdelta; i0 < range[0] - intdelta; i0++){ // The cycle step here and below should be powers of 2. The cycle's beginnig and end should take this into account.
//        for (int i1 = intdelta; i1 < range[1] - intdelta; i1++){
////            for (int i2 = intdelta; i2 < range[2] - intdelta; i2++){

//                for (int m = 0; m < rows; m++){
//                    // What is here a shift by one should be a shift by 2^n. 
//                    //
////                    double dev0 = std::abs((F(i0 + 1, i1/*, i2*/)(m) - F(i0 - 1, i1/*, i2*/)(m))*inv_2_delta[0] - JF(i0, i1/*, i2*/)(m, 0));
////                    double dev1 = std::abs((F(i0, i1 + 1/*, i2*/)(m) - F(i0, i1 - 1/*, i2*/)(m))*inv_2_delta[1] - JF(i0, i1/*, i2*/)(m, 1));
////                    double dev2 = std::abs((F(i0, i1, i2 + 1)(m) - F(i0, i1, i2 - 1)(m))*inv_2_delta[2] - JF(i0, i1, i2)(m, 2));

//                    std::vector<unsigned long int> index(cols);
//                    index[0] = i0;
//                    index[1] = i1;
////                    index[2] = i2;

//                    for (int p = 0; p < cols; p++){
//                        std::vector<unsigned long int> p_shifted(index);
//                        p_shifted[p] += intdelta;

//                        std::vector<unsigned long int> m_shifted(index);
//                        m_shifted[p] -= intdelta;

//                        dev[p] = std::abs((F(p_shifted)(m) - F(m_shifted)(m))*inv_2_delta[p] - JF(index)(m, p));

//                        numerical_analytic_abs_deviation_sup(m, p) = std::max(numerical_analytic_abs_deviation_sup(m, p), dev[p]);
//                    }
// 
////                    dev[0] = std::abs((F(i0 + intdelta, i1/*, i2*/)(m) - F(i0 - intdelta, i1/*, i2*/)(m))*inv_2_delta[0] - JF(i0, i1/*, i2*/)(m, 0));
////                    dev[1] = std::abs((F(i0, i1 + intdelta/*, i2*/)(m) - F(i0, i1 - intdelta/*, i2*/)(m))*inv_2_delta[1] - JF(i0, i1/*, i2*/)(m, 1));
////                    dev[2] = std::abs((F(i0, i1, i2 + 1)(m) - F(i0, i1, i2 - 1)(m))*inv_2_delta[2] - JF(i0, i1, i2)(m, 2));

////                    if (dev0 > numerical_analytic_abs_deviation_sup(m, 0)) std::cout << "i1 = " << i1 << std::endl;

////                    numerical_analytic_abs_deviation_sup(m, 0) = std::max(numerical_analytic_abs_deviation_sup(m, 0), dev[0]);
////                    numerical_analytic_abs_deviation_sup(m, 1) = std::max(numerical_analytic_abs_deviation_sup(m, 1), dev[1]);
////                    numerical_analytic_abs_deviation_sup(m, 2) = std::max(numerical_analytic_abs_deviation_sup(m, 2), dev2);

//                    max_abs_F = std::max(max_abs_F, F(i0, i1/*, i2*/)(m));
//                }


////            } // i2
//        } // i1
//    } // i0

//    synthetic_deviation = numerical_analytic_abs_deviation_sup.max();
//    
//    return;
//}

// Working method!
//
//void JetTester::numerical_Jacobian(const MultiArray<RealVector>    &F, 
//                                   const MultiArray<DoubleMatrix> &JF,
//                                   int rows, int cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
//                                   unsigned long int intdelta,
//                                   const RealVector &pmin, const RealVector &pmax,
//                                   const std::vector<unsigned long int> &subdivision,
//                                   /*MultiArray<DoubleMatrix> &numerical_analytic_deviation*/
//                                   DoubleMatrix &numerical_analytic_abs_deviation_sup,
//                                   Matrix<std::vector<unsigned int> > &sup_pos,
//                                   double &synthetic_deviation,
//                                   double &max_abs_F){

//    // Delta.
//    //
//    std::vector<double > inv_2_delta(subdivision.size());
//    for (int i = 0; i < subdivision.size(); i++) inv_2_delta[i] = 1.0/((double)(2*intdelta)*(pmax[i] - pmin[i])/(double)(subdivision[i] - 1));

//    // Resize numerical_analytic_deviation.
//    //
//    std::vector<long unsigned int> range = F.range();

//    numerical_analytic_abs_deviation_sup = DoubleMatrix::zero(rows, cols);

//    sup_pos.resize(rows, cols);
//    for (int i = 0; i < rows*cols; i++) sup_pos(i).resize(cols);

//    max_abs_F = 0.0;

//    // Proceed.
//    //
//    std::vector<double> dev(cols); // ASAP, rows -> number_of_equations, cols -> number_of_variables.
//    std::vector<unsigned long int> pos(cols);

//    for (int i0 = intdelta; i0 < range[0] - intdelta; i0++){
//        for (int i1 = intdelta; i1 < range[1] - intdelta; i1++){
//            for (int i2 = intdelta; i2 < range[2] - intdelta; i2++){
//                for (int m = 0; m < rows; m++){
//                    std::vector<unsigned long int> index(cols);
//                    index[0] = i0;
//                    index[1] = i1;
//                    index[2] = i2;

//                    for (int p = 0; p < cols; p++){
//                        std::vector<unsigned long int> p_shifted(index);
//                        p_shifted[p] += intdelta;

//                        std::vector<unsigned long int> m_shifted(index);
//                        m_shifted[p] -= intdelta;

//                        dev[p] = std::abs((F(p_shifted)(m) - F(m_shifted)(m))*inv_2_delta[p] - JF(index)(m, p));

////                        numerical_analytic_abs_deviation_sup(m, p) = std::max(numerical_analytic_abs_deviation_sup(m, p), dev[p]);
//                        if (dev[p] > numerical_analytic_abs_deviation_sup(m, p)){
//                            // Store the indices of the variables where the supremum was reached for 
//                            // the given component of the function.
//                            //
//                            for (int v = 0; v < cols; v++) sup_pos(m, p)[v] = index[v];
//                            numerical_analytic_abs_deviation_sup(m, p) = dev[p];
//                        }
//                    }

//                    max_abs_F = std::max(max_abs_F, F(index)(m));
//                }
//            } // i2
//        } // i1
//    } // i0

//    synthetic_deviation = numerical_analytic_abs_deviation_sup.max();
//    
//    return;
//}

void JetTester::numerical_Jacobian(const MultiArray<RealVector>    &F, 
                                   const MultiArray<DoubleMatrix> &JF,
                                   int rows, int cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                                   unsigned long int intdelta,
                                   const RealVector &pmin, const RealVector &pmax,
                                   const std::vector<unsigned long int> &subdivision,
                                   /*MultiArray<DoubleMatrix> &numerical_analytic_deviation*/
                                   DoubleMatrix &numerical_analytic_abs_deviation_sup,
                                   Matrix<std::vector<unsigned int> > &sup_pos,
                                   double &synthetic_deviation,
                                   double &max_abs_F){

    // Delta.
    //
    std::vector<double > inv_2_delta(subdivision.size());
    for (int i = 0; i < subdivision.size(); i++) inv_2_delta[i] = 1.0/((double)(2*intdelta)*(pmax[i] - pmin[i])/(double)(subdivision[i] - 1));

    // Resize numerical_analytic_deviation.
    //
    std::vector<long unsigned int> range = F.range();

    numerical_analytic_abs_deviation_sup = DoubleMatrix::zero(rows, cols);

    sup_pos.resize(rows, cols);
    for (int i = 0; i < rows*cols; i++) sup_pos(i).resize(cols);

    max_abs_F = 0.0;

    // Proceed.
    //
    std::vector<double> dev(cols); // ASAP, rows -> number_of_equations, cols -> number_of_variables.
    std::vector<unsigned long int> pos(cols);

    unsigned long int total_number_elements = 1;
    for (unsigned long int i = 0; i < subdivision.size(); i++) total_number_elements *= subdivision[i];

    for (int i = 0; i < total_number_elements; i++){
        std::vector<unsigned long int> index = F.multiindex(i);
        bool valid = true;

        for (int j = 0; j < index.size(); j++){
            if (index[j] < intdelta || index[j] >= range[j] - intdelta){
                valid = false;
                break;
            }
        }

        if (!valid) continue;

        for (int m = 0; m < rows; m++){
            for (int p = 0; p < cols; p++){
                std::vector<unsigned long int> p_shifted(index);
                p_shifted[p] += intdelta;

                std::vector<unsigned long int> m_shifted(index);
                m_shifted[p] -= intdelta;

                dev[p] = std::abs((F(p_shifted)(m) - F(m_shifted)(m))*inv_2_delta[p] - JF(index)(m, p));

                if (dev[p] > numerical_analytic_abs_deviation_sup(m, p)){
                    // Store the indices of the variables where the supremum was reached for 
                    // the given component of the function.
                    //
                    for (int v = 0; v < cols; v++) sup_pos(m, p)[v] = index[v];
                        numerical_analytic_abs_deviation_sup(m, p) = dev[p];
                    }
            }

            max_abs_F = std::max(max_abs_F, F(index)(m));
        }
    }

//    for (int i0 = intdelta; i0 < range[0] - intdelta; i0++){
//        for (int i1 = intdelta; i1 < range[1] - intdelta; i1++){
//            for (int i2 = intdelta; i2 < range[2] - intdelta; i2++){
//                for (int m = 0; m < rows; m++){
//                    std::vector<unsigned long int> index(cols);
//                    index[0] = i0;
//                    index[1] = i1;
//                    index[2] = i2;

//                    for (int p = 0; p < cols; p++){
//                        std::vector<unsigned long int> p_shifted(index);
//                        p_shifted[p] += intdelta;

//                        std::vector<unsigned long int> m_shifted(index);
//                        m_shifted[p] -= intdelta;

//                        dev[p] = std::abs((F(p_shifted)(m) - F(m_shifted)(m))*inv_2_delta[p] - JF(index)(m, p));

////                        numerical_analytic_abs_deviation_sup(m, p) = std::max(numerical_analytic_abs_deviation_sup(m, p), dev[p]);
//                        if (dev[p] > numerical_analytic_abs_deviation_sup(m, p)){
//                            // Store the indices of the variables where the supremum was reached for 
//                            // the given component of the function.
//                            //
//                            for (int v = 0; v < cols; v++) sup_pos(m, p)[v] = index[v];
//                            numerical_analytic_abs_deviation_sup(m, p) = dev[p];
//                        }
//                    }

//                    max_abs_F = std::max(max_abs_F, F(index)(m));
//                }
//            } // i2
//        } // i1
//    } // i0

    synthetic_deviation = numerical_analytic_abs_deviation_sup.max();
    
    return;
}

void JetTester::numerical_Hessian(const MultiArray<DoubleMatrix>               &JF, 
                                  const MultiArray<std::vector<DoubleMatrix> > &HF,
                                  int rows, int cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                                  const RealVector &pmin, const RealVector &pmax,
                                  const std::vector<unsigned long int> &subdivision,
                                  /*MultiArray<DoubleMatrix> &numerical_analytic_deviation*/
                                  std::vector<DoubleMatrix> &numerical_analytic_abs_deviation_sup,
                                  RealVector &max_vec,
                                  double &synthetic_deviation){

    // Delta.
    //
    std::vector<double > inv_2_delta(subdivision.size());
    for (int i = 0; i < subdivision.size(); i++) inv_2_delta[i] = 1.0/(2.0*(pmax[i] - pmin[i])/(double)(subdivision[i] - 1));

    // Resize numerical_analytic_deviation.
    //
    std::vector<long unsigned int> range = JF.range();

    //  
    //
    numerical_analytic_abs_deviation_sup.resize(HF.size());
    for (int i = 0; i < HF.size(); i++) numerical_analytic_abs_deviation_sup[i] = DoubleMatrix::zero(cols, cols);

    // Proceed.
    //
    for (int component = 0; component < rows; component++){
        for (int i0 = 1; i0 < range[0] - 1; i0++){ // The cycle step here and below should be powers of 2. The cycle's beginnig and end should take this into account.
            for (int i1 = 1; i1 < range[1] - 1; i1++){
                for (int i2 = 1; i2 < range[2] - 1; i2++){
                    DoubleMatrix dev(cols, cols);

                    dev(0, 0) = std::abs((JF(i0 + 1, i1, i2)(component, 0) - JF(i0 - 1, i1, i2)(component, 0))*inv_2_delta[0] - HF(i0, i1, i2)[component](0, 0));
                    dev(0, 1) = std::abs((JF(i0 + 1, i1, i2)(component, 1) - JF(i0 - 1, i1, i2)(component, 1))*inv_2_delta[0] - HF(i0, i1, i2)[component](0, 1));
                    dev(0, 2) = std::abs((JF(i0 + 1, i1, i2)(component, 2) - JF(i0 - 1, i1, i2)(component, 2))*inv_2_delta[0] - HF(i0, i1, i2)[component](0, 2));

                    dev(1, 0) = std::abs((JF(i0, i1 + 1, i2)(component, 0) - JF(i0, i1 - 1, i2)(component, 0))*inv_2_delta[1] - HF(i0, i1, i2)[component](1, 0));
                    dev(1, 1) = std::abs((JF(i0, i1 + 1, i2)(component, 1) - JF(i0, i1 - 1, i2)(component, 1))*inv_2_delta[1] - HF(i0, i1, i2)[component](1, 1));
                    dev(1, 2) = std::abs((JF(i0, i1 + 1, i2)(component, 2) - JF(i0, i1 - 1, i2)(component, 2))*inv_2_delta[1] - HF(i0, i1, i2)[component](1, 2));

                    dev(2, 0) = std::abs((JF(i0, i1, i2 + 1)(component, 0) - JF(i0, i1, i2 - 1)(component, 0))*inv_2_delta[2] - HF(i0, i1, i2)[component](2, 0));
                    dev(2, 1) = std::abs((JF(i0, i1, i2 + 1)(component, 1) - JF(i0, i1, i2 - 1)(component, 1))*inv_2_delta[2] - HF(i0, i1, i2)[component](2, 1));
                    dev(2, 2) = std::abs((JF(i0, i1, i2 + 1)(component, 2) - JF(i0, i1, i2 - 1)(component, 2))*inv_2_delta[2] - HF(i0, i1, i2)[component](2, 2));

                    numerical_analytic_abs_deviation_sup[component](0, 0) = std::max(numerical_analytic_abs_deviation_sup[component](0, 0), dev(0, 0));
                    numerical_analytic_abs_deviation_sup[component](0, 1) = std::max(numerical_analytic_abs_deviation_sup[component](0, 1), dev(0, 1));
                    numerical_analytic_abs_deviation_sup[component](0, 2) = std::max(numerical_analytic_abs_deviation_sup[component](0, 2), dev(0, 2));

                    numerical_analytic_abs_deviation_sup[component](1, 0) = std::max(numerical_analytic_abs_deviation_sup[component](1, 0), dev(1, 0));
                    numerical_analytic_abs_deviation_sup[component](1, 1) = std::max(numerical_analytic_abs_deviation_sup[component](1, 1), dev(1, 1));
                    numerical_analytic_abs_deviation_sup[component](1, 2) = std::max(numerical_analytic_abs_deviation_sup[component](1, 2), dev(1, 2));

                    numerical_analytic_abs_deviation_sup[component](2, 0) = std::max(numerical_analytic_abs_deviation_sup[component](2, 0), dev(2, 0));
                    numerical_analytic_abs_deviation_sup[component](2, 1) = std::max(numerical_analytic_abs_deviation_sup[component](2, 1), dev(2, 1));
                    numerical_analytic_abs_deviation_sup[component](2, 2) = std::max(numerical_analytic_abs_deviation_sup[component](2, 2), dev(2, 2));

                } // i2
            } // i1
        } // i0
    } // component

    std::cout << "Here" << std::endl;

    {
        synthetic_deviation = 0.0;
        max_vec.resize(rows);
        for (int component = 0; component < rows; component++){
            max_vec(component) = numerical_analytic_abs_deviation_sup[component].max();
            synthetic_deviation = std::max(synthetic_deviation, max_vec(component));
        }
    }
    
    return;
}

void JetTester::test(void *obj, int (*f)(void *obj, const RealVector &state, int degree, JetMatrix &jm), 
                     const std::vector<int> &variables,
                     const RealVector &pmin, const RealVector &pmax,
                     const std::vector<unsigned long int> &subdivision,
                     std::vector<double> &min_error, 
                     std::vector<double> &max_error,
                     std::vector<double> &mean_value){

    min_error.clear();
    max_error.clear();
    mean_value.clear();

    MultiArray<RealVector>    F(subdivision);
    MultiArray<DoubleMatrix> JF(subdivision);

    populate_F(obj, f,
               pmin, pmax,
               subdivision,
               F, JF);

    for (int i = 0; i < variables.size(); i++){
        double delta = (pmax(i) - pmin(i))/(double)(subdivision[i] - 1);
        double min =  std::numeric_limits<double>::infinity();
        double max = -std::numeric_limits<double>::infinity();
        double mean = 0.0;

        for (int j = 0; j < subdivision[i]; j++){
            
        }

        min_error.push_back(min);
        max_error.push_back(max);
        mean_value.push_back(mean);
    }

    return;
}

