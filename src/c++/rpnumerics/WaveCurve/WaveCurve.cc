#include "WaveCurve.h"

void WaveCurve::init(const WaveCurve &orig){
    family = orig.family;

    increase = orig.increase;

    reference_point = orig.reference_point;

    for (int i = 0; i < orig.wavecurve.size(); i++) wavecurve.push_back(orig.wavecurve[i]);

    return;
}

WaveCurve::WaveCurve(){
}

WaveCurve::WaveCurve(const WaveCurve &orig){
    init(orig);
}

WaveCurve::WaveCurve(const WaveCurve *orig){
    init(*orig);
}

WaveCurve::~WaveCurve(){
}

WaveCurve WaveCurve::operator=(const WaveCurve &orig){
    if (this != &orig) init(orig);

    return *this;
}

void WaveCurve::add(const Curve &c){
//    std:://cout << "WaveCurve, will try to push back." << std::endl;

    wavecurve.push_back(c);

//    std:://cout << "WaveCurve, successfully pushed back." << std::endl;

    return;
}

void WaveCurve::insert_rarefaction_composite_pair(int composite_curve_index, int composite_insertion_point_index, const RealVector &rar_point, const RealVector &cmp_point){
    if (wavecurve[composite_curve_index].type == COMPOSITE_CURVE){
        // Shift the indices on this composite and its associated rarefaction.

        if (composite_insertion_point_index >= 0 && composite_insertion_point_index < wavecurve[composite_curve_index].curve.size() - 1){
            // Index of the related rarefaction curve (to simplify the access).
            //
            int rarefaction_index = wavecurve[composite_curve_index].back_curve_index;
            int rarefaction_point_index = wavecurve[composite_curve_index].back_pointer[composite_insertion_point_index]; // See pic IMG_20140112_153146316.jpg. TODO: Explain.
            
            // Insert the point into the rarefaction curve.
            //
            std::vector<RealVector>::iterator rar_it = wavecurve[rarefaction_index].curve.begin();
            wavecurve[rarefaction_index].curve.insert(rar_it + rarefaction_point_index + 1, rar_point);

            // Update the back pointers for the rarefaction.
            //
            for (int i = rarefaction_point_index; i < wavecurve[rarefaction_index].curve.size(); i++) wavecurve[rarefaction_index].back_pointer[i] = i - 1; // TODO: Check if it's ok.

            // Insert the point into the composite curve.
            //
            std::vector<RealVector>::iterator cmp_it = wavecurve[composite_curve_index].curve.begin();
            wavecurve[composite_curve_index].curve.insert(cmp_it + composite_insertion_point_index, cmp_point);

            // Update the back pointers for the composite.
            //
            for (int i = composite_insertion_point_index; i >= 0; i--) wavecurve[composite_curve_index].back_pointer[i] = wavecurve[composite_curve_index].back_pointer[i - 1];

            // Find all the composite curves that also depend on this rarefaction and propagate the shifting of indices.
            // The search must be backwards (approaching the rarefaction), because the points in the rarefaction
            // that are shifted due to an insertion affect the composite curves that are closer to the rarefaction.
            //
            int pos = composite_curve_index - 1;
            while (pos > rarefaction_index){
                if (wavecurve[pos].type ==  COMPOSITE_CURVE){
                }

                pos--;
            }
        }
    }

    return;
}

void WaveCurve::insert_rarefaction_composite_pair(int composite_curve_index, int composite_insertion_point_index, const RealVector &rarcmp_point){
    int n = rarcmp_point.size()/2;

    insert_rarefaction_composite_pair(composite_curve_index, composite_insertion_point_index, RealVector(0, n, rarcmp_point), RealVector(n, n, rarcmp_point));

    return;
}

