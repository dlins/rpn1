#include "JetTester.h"

#include "ThreePhaseFlowSubPhysics.h"

#include "CoreyQuadSubPhysics.h"
#include "Brooks_CoreySubPhysics.h"
#include "SorbiePermeability.h"

ThreePhaseFlowSubPhysics *subphysics;
ThreePhaseFlowPermeability *permeability;

int main(){
    // SubPhysics.
    //
//    subphysics   = new Brooks_CoreySubPhysics();
//    permeability = subphysics->permeability();
    
    permeability = new SorbiePermeability(0);

    // Grid.
    //
    RealVector pmin(2), pmax(2);
    std::vector<unsigned long int> div(2);
    for (int i = 0; i < 2; i++){
        pmin(i) = 0.0;
        pmax(i) = 1.0;
        div[i]  = 100;
    }

    std::vector<int (*)(void*, const RealVector&, int degree, JetMatrix&)> list;
    std::vector<std::string> name;

    permeability->list_of_functions(list, name);
    for (int i = 0; i < name.size(); i++){
        std::cout << "*************************** " << name[i] << " ***************************" << std::endl;

        JetTester jt;
        MultiArray<RealVector>    F;
        MultiArray<DoubleMatrix> JF;

        jt.populate_F(permeability, list[i],
                      pmin, pmax,
                      div,
                      F, JF);

        //std::cout << "JF.size() = " << JF.size() << std::endl;

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
                              pmin, pmax,
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

