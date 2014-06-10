#ifndef _SECONDARY_BIFURCATION_
#define _SECONDARY_BIFURCATION_

#include "ThreeImplicitFunctions.h"
#include "Contour2x2_Method.h"
#include "Secondary_Bifurcation_Interface.h"

class Secondary_Bifurcation : public ThreeImplicitFunctions, public Secondary_Bifurcation_Interface {
    private:
    protected:
        Matrix<double> flux_left, accum_left;

        const FluxFunction *lff;
    	const AccumulationFunction *laa;

        const FluxFunction *rff;
        const AccumulationFunction *raa;

    public:
        
        
        Secondary_Bifurcation(){
            gv_left = gv_right = 0;
            
            flux_left.resize(2, 4);
            accum_left.resize(2, 4);

            singular = true;
        }
        
        
        Secondary_Bifurcation(const FluxFunction  *leftFlux, const AccumulationFunction  * leftAccum, const FluxFunction  * rightFlux, const AccumulationFunction * rightAccum){
//            gv_left = gv_right = 0;

            lff=leftFlux;
            laa=leftAccum;
            rff=rightFlux;
            raa= rightAccum;
            
            
            
            
            flux_left.resize(2, 4);
            accum_left.resize(2, 4);

            singular = true;
        }

        ~Secondary_Bifurcation(){}

        bool prepare_cell(int i, int j);

        bool function_on_cell(double *val, int ir, int jr, int kl, int kr);

        void curve(const FluxFunction *lf, const AccumulationFunction *la, GridValues &lg,
                   const FluxFunction *rf, const AccumulationFunction *ra, GridValues &rg,
                   std::vector<RealVector> &left_curve, std::vector<RealVector> &right_curve);
        
        
        
         int bifurcationCurve(std::vector<RealVector> &,std::vector<RealVector> &) ;
};

#endif // _SECONDARY_BIFURCATION_
