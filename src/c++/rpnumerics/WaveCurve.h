#ifndef _WAVECURVE_
#define _WAVECURVE_

#include <vector>

#include "eigen.h"

#include "Rarefaction.h"
#include "Shock.h"
#include "CompositeCurve.h"

#include "Boundary.h"
#include "FluxFunction.h"
#include "AccumulationFunction.h"

#define RAREFACTION_CURVE 1
#define SHOCK_CURVE       2
#define COMPOSITE_CURVE   3

#define WAVE_CURVE_REACHED_BOUNDARY                             10
#define WAVE_CURVE_SHOCK_RIGHT_CHARACTERISTIC_WITH_OTHER_FAMILY 11

#define WAVE_CURVE_OK                                           100
#define WAVE_CURVE_ERROR                                        200

// Struct 
//
//
struct Curve {
    public:
        std::vector<RealVector> curve;
        std::vector<int> corresponding_point_in_related_curve;

        int type;
        int index_related_curve;

        Curve(const std::vector<RealVector> &orig, const std::vector<int> &orig_corresponding, int t, int index){
            curve.resize(orig.size());
            for (int i = 0; i < orig.size(); i++){
                curve[i].resize(orig[i].size());
                for (int j = 0; j < orig[i].size(); j++) curve[i].component(j) = orig[i].component(j);
            }

            corresponding_point_in_related_curve.resize(orig_corresponding.size());
            for (int i = 0; i < orig_corresponding.size(); i++) corresponding_point_in_related_curve[i] = orig_corresponding[i];

            type = t;

            index_related_curve = index;
        }

        Curve(const std::vector<RealVector> &orig, int t, int index){
            curve.resize(orig.size());
            corresponding_point_in_related_curve.resize(orig.size());
            for (int i = 0; i < orig.size(); i++){
                corresponding_point_in_related_curve[i] = i;

                curve[i].resize(orig[i].size());
                for (int j = 0; j < orig[i].size(); j++) curve[i].component(j) = orig[i].component(j);
            }

            type = t;

            index_related_curve = index;
        }

        ~Curve(){
            curve.clear();
            corresponding_point_in_related_curve.clear();
        }

        
};

class WaveCurve {
    private:
    protected:
        const FluxFunction *ff;
        const AccumulationFunction *aa;
        const Boundary *boundary;


        double ddot(int n, double *x, double *y);
    public:
        WaveCurve(const FluxFunction *f, const AccumulationFunction *a, const Boundary *b);
        ~WaveCurve();
        int half_wavecurve(int initial_curve, const RealVector &init, int family, int increase, std::vector<Curve> &c);
        int wavecurve(const RealVector &init, int family, int increase, std::vector<Curve> &c);
};

#endif // _WAVECURVE_

