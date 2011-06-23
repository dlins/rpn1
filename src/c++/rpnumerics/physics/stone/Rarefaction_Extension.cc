#include "Rarefaction_Extension.h"

void Rarefaction_Extension::extension_curve(const FluxFunction *curve_ff, const AccumulationFunction *curve_aa,
        const RealVector &initial_point,
        double deltaxi,
        int curve_family,
        int increase,
        const Boundary *boundary,
        const RealVector &pmin, const RealVector &pmax, int *number_of_grid_points, // For the domain.
        int domain_family,
        const FluxFunction *domain_ff, const AccumulationFunction *domain_aa,
        int characteristic_where, int singular,
        std::vector<RealVector> &rarefaction_segments,
        std::vector<RealVector> &curve_segments,
        std::vector<RealVector> &domain_segments) {
    curve_segments.clear();
    domain_segments.clear();

    std::vector<RealVector> rarefaction_curve;

    //    int info = Rarefaction::curve(initial_point,
    //                                  curve_family,
    //                                  increase,
    //                                  deltaxi,
    //                                  curve_ff, curve_aa,
    //                                  RAREFACTION_GENERAL_ACCUMULATION,
    //                                  (Boundary*)boundary,
    //                                  rarefaction_curve);
    //

    int info = Rarefaction::curve(initial_point,
            RAREFACTION_INITIALIZE_YES,
            (const RealVector *) 0,
            curve_family,
            increase,
            deltaxi,
            curve_ff, curve_aa,
            RAREFACTION_GENERAL_ACCUMULATION,
            (Boundary*) boundary,
            rarefaction_curve);

    cout << "Tamanho da rarefacao: " << rarefaction_curve.size() << endl;
    if (rarefaction_curve.size() < 2) return;

    int n = initial_point.size();

    cout <<"valor de n: "<<n<<endl;

    // Turn the curve of points into a curve of segments.
    //vector <RealVector> rarefaction_segments;
    rarefaction_segments.clear();
    rarefaction_segments.resize(2 * rarefaction_curve.size() - 2);
    for (int i = 0; i < rarefaction_curve.size() - 1; i++) {
        for (int j = 0; j < n; j++) {
            rarefaction_segments[2 * i].component(j) = rarefaction_curve[i].component(j);
            rarefaction_segments[2 * i + 1].component(j) = rarefaction_curve[i + 1].component(j);
        }
    }


    // Compute the extension curve for the rarefaction
    Extension_Curve extension_curve(pmin, pmax, number_of_grid_points,
            domain_ff, domain_aa);

    extension_curve.compute_extension_curve(characteristic_where, singular,
            rarefaction_segments, curve_family,
            (FluxFunction*) curve_ff, (AccumulationFunction*) curve_aa,
            domain_family,
            curve_segments,
            domain_segments);


    cout<<"Tamanho de rarefaction segments: "<<rarefaction_segments.size()<<endl;
    cout << "Tamanho de curve segments: " << curve_segments.size()<<endl;








    return;
}

