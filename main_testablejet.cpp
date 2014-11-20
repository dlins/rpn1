#include "JetTester.h"

#include "ThreePhaseFlowSubPhysics.h"
#include "CoreyQuadSubPhysics.h"
#include "Brooks_CoreySubPhysics.h"
#include "SorbiePermeability.h"

#include "ICDOWSubPhysics.h"


//SubPhysics *subphysics;
//TestableJet *object;

//#define TEST_ACCUMULATION
#define TEST_FLUX
//#define TEST_HYDRODYNAMICS

int main(){
    // SubPhysics.
    //
//    subphysics   = new Brooks_CoreySubPhysics();
//    object = subphysics->permeability();
//    ThreePhaseFlowPermeability *permeability = subphysics->permeability();

    // Grid.
    //
//    RealVector pmin(2), pmax(2);
//    std::vector<unsigned long int> div(2);
//    for (int i = 0; i < 2; i++){
//        pmin(i) = 0.0;
//        pmax(i) = 1.0;
//        div[i]  = 100;
//    }

    RealVector objpmin, objpmax;

    #ifdef TEST_HYDRODYNAMICS
    ICDOWSubPhysics *subphysics = new ICDOWSubPhysics();
    ICDOWHydrodynamics *object = subphysics->hydrodynamics();

    RealVector pmin = subphysics->boundary()->minimums();
    RealVector pmax = subphysics->boundary()->maximums();

    objpmin.resize(1); objpmin(0) = pmin(0);
    objpmax.resize(1); objpmax(0) = pmax(0);

    std::vector<unsigned long int> div(1); div[0] = 80;
    #endif

    #ifdef TEST_ACCUMULATION
    ICDOWSubPhysics *subphysics = new ICDOWSubPhysics();
    ICDOWAccumulationFunction *object = (ICDOWAccumulationFunction*)subphysics->accumulation();

    RealVector pmin = subphysics->boundary()->minimums();
    RealVector pmax = subphysics->boundary()->maximums();

    objpmin = pmin;
    objpmax = pmax;

    std::vector<unsigned long int> div(pmin.size());
    for (int i = 0; i < div.size(); i++){
        div[i] = 400;
    }
    div[2] = 4;
    #endif

    #ifdef TEST_FLUX
    ICDOWSubPhysics *subphysics = new ICDOWSubPhysics();
    ICDOWFluxFunction *object = (ICDOWFluxFunction*)subphysics->flux();

    RealVector pmin = subphysics->boundary()->minimums();
    RealVector pmax = subphysics->boundary()->maximums();

    objpmin = pmin;
    objpmax = pmax;

    std::vector<unsigned long int> div(pmin.size());
    for (int i = 0; i < div.size(); i++){
        div[i] = 400;
    }
    div[2] = 4;
    #endif

    std::vector<int (*)(void*, const RealVector&, int degree, JetMatrix&)> list;
    std::vector<std::string> name;
    object->list_of_functions(list, name);

    for (int i = 0; i < name.size(); i++){
        std::cout << "*************************** " << name[i] << " (" << (void*)list[i] << ")" << " ***************************" << std::endl;

        JetTester jt;
        MultiArray<RealVector>    F;
        MultiArray<DoubleMatrix> JF;

        jt.populate_F(object, list[i],
                      objpmin, objpmax,
                      div,
                      F, JF);

//        std::cout << "JF.size() = " << JF.size() << std::endl;

        int rows, cols;
        rows = JF(JF.multiindex(0)).rows();// std::cout << "rows = " << rows << std::endl;
        cols = JF(JF.multiindex(0)).cols();// std::cout << "cols = " << cols << std::endl;

        int intdelta = 1;

        DoubleMatrix numerical_analytic_abs_deviation_sup;
        Matrix<std::vector<unsigned int> > sup_pos;
        double synthetic_deviation;
        double max_abs_F;

        jt.numerical_Jacobian(F, 
                              JF,
                              rows, cols,
                              intdelta,
                              objpmin, objpmax,
                              div,
                              numerical_analytic_abs_deviation_sup,
                              sup_pos,
                              synthetic_deviation,
                              max_abs_F); 

        std::cout << numerical_analytic_abs_deviation_sup << std::endl;
        std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;
        std::cout << "max_abs_F = " << max_abs_F << std::endl;
        std::cout << "synthetic_deviation/max_abs_F = " << numerical_analytic_abs_deviation_sup*(1.0/max_abs_F) << std::endl;

        std::cout << sup_pos.rows() << " x " << sup_pos.cols() << std::endl;

        for (int i = 0; i < rows; i++){
            std::cout << "Function component: " << i << std::endl;
            for (int j = 0; j < cols; j++){
                std::cout << "    Variable: " << j << std::endl;
                std::cout << "        Max. reached at: ";
                for (int p = 0; p < cols; p++) std::cout << sup_pos(i, j)[p] << ", ";
                std::cout << std::endl << std::endl;
            }
        }
    }

//    delete subphysics;

    return 0;
}

