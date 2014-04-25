#ifndef _WAVECURVE_
#define _WAVECURVE_

#include "Curve.h"

class WaveCurve {
    private:
    protected:
    public:
        int family;

        int increase;

        ReferencePoint reference_point;

        std::vector<Curve> wavecurve;

        void init(const WaveCurve &orig);

        WaveCurve();
        WaveCurve(const WaveCurve &orig);
        WaveCurve(const WaveCurve *orig);
        ~WaveCurve();

        WaveCurve operator=(const WaveCurve &orig);

        void add(const Curve &c);

        // Insert a rarefaction-composite pair in the wavecurve BEFORE composite_insertion_point_index (mimicking std::vector::insert).
        //
        void insert_rarefaction_composite_pair(int composite_curve_index, int composite_insertion_point_index, const RealVector &rar_point, const RealVector &cmp_point);
        void insert_rarefaction_composite_pair(int composite_curve_index, int composite_insertion_point_index, const RealVector &rarcmp_point);
};

#endif // _WAVECURVE_

