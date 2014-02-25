#ifndef _CURVE_
#define _CURVE_

#include <vector>
#include "RealVector.h"
#include "ReferencePoint.h"

#define RAREFACTION_CURVE 1
#define COMPOSITE_CURVE   2
#define SHOCK_CURVE       3

#define SPEED_INCREASE   10
#define SPEED_DECREASE   11

class Curve {
    private:
    protected:
    public:
        // RAREFACTION_CURVE or COMPOSITE_CURVE or SHOCK_CURVE.
        //
        int type;

        // From whence this curve came.
        //
        int back_curve_index;

        // These lines below should be moved to WaveCurve (this impacts RarefactionCurve, CompositeCurve and maybe ShockCurve).
        int family;

        int increase;

        ReferencePoint reference_point;

        //
        //     this->curve[i] is related to wavecurve[back_curve_index][back_pointer[i]] 
        //
        std::vector<RealVector> curve;
        std::vector<int>        back_pointer;

        RealVector last_point;
        RealVector final_direction;

        // Why this curve stopped.
        //
        int reason_to_stop;

        // The following members are not to be filled when computing an integral curve:
        //
        std::vector<double>     speed;
        
        std::vector<RealVector> eigenvalues;

        void init(const Curve &orig);

        Curve();
        Curve(const Curve &orig);
        Curve(const Curve *orig);

        virtual ~Curve();

        Curve operator=(const Curve &orig);

        void clear();
};

#endif // _CURVE_

