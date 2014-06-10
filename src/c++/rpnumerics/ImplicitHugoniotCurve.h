#ifndef _IMPLICITHUGONIOTCURVE_
#define _IMPLICITHUGONIOTCURVE_

#include "HugoniotCurve.h"
#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "Newton_Improvement.h"

class ImplicitHugoniotCurve : public HugoniotCurve, public ImplicitFunction {
    private:
    protected:
        // TODO: Maybe these four members below can be eliminated.
        //
        RealVector Fref, Gref;
        DoubleMatrix JFref, JGref;
    public:
        ImplicitHugoniotCurve(const FluxFunction *ff, const AccumulationFunction *aa, const Boundary *bb);
        virtual ~ImplicitHugoniotCurve();

        void set_grid(GridValues *g){gv = g; return;}
    
        void curve(const ReferencePoint &ref, int type, std::vector<Curve> &c);

        int function_on_square(double *foncub, int i, int j);
        bool improvable(void);
        int complete(const RealVector &p0, const RealVector &p1, const RealVector &p_init, RealVector &p_completed);

        void types(std::vector<int> &type, std::vector<std::string> &name) const {
            type.clear();
            name.clear();

            return;
        }

        void curve(const ReferencePoint &ref, int type, std::vector<HugoniotPolyLine> &classified_curve){
            HugoniotCurve::curve(ref, type, classified_curve);

            return;
        }
};

#endif // _IMPLICITHUGONIOTCURVE_

