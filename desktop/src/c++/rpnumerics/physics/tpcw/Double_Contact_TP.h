#ifndef _DOUBLE_CONTACT_TP_
#define _DOUBLE_CONTACT_TP_


#include "Contour2x2_Method.h"
#include "Double_Contact_Function.h"

class Double_Contact_TP : public Double_Contact_Function {
    private:
    protected:
        RealVector lambda_left;
        Matrix<double> flux_left, accum_left;

        int left_family, right_family;

        const FluxFunction *lff;
    	const AccumulationFunction *laa;

        const FluxFunction *rff;
        const AccumulationFunction *raa;

    public:
        Double_Contact_TP(){
            gv_left = gv_right = 0;

            lambda_left.resize(4);        
            flux_left.resize(3, 4);
            accum_left.resize(3, 4);

            left_family = right_family = 0;

            singular = false;
        }

        ~Double_Contact_TP(){}

        bool prepare_cell(int i, int j);

        bool function_on_cell(double *val, int ir, int jr, int kl, int kr);

        void curve(const FluxFunction *lf, const AccumulationFunction *la, GridValues *lg, int lfam,
                   const FluxFunction *rf, const AccumulationFunction *ra, GridValues *rg, int rfam,
                   std::vector<RealVector> &left_curve, std::vector<RealVector> &right_curve);
        
        
        
        
       
};

#endif // _DOUBLE_CONTACT_TP_

