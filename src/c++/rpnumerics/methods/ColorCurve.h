#ifndef _COLORCURVE_
#define _COLORCURVE_

#include <vector>

//#include "../global_composite.h" // SUCCESSFUL OR ABORTED
#include "FluxFunction.h"
#include "AccumulationFunction.h"
//#include "RealVector.h"
//#include "fill_with_jet.h"
#include "eigen.h"
//#include "Shockcurve_Adaptive_Hypersurface_Newton.h"

#define INTERPOLATION_ERROR -1
#define INTERPOLATION_OK     0

#define _SHOCK_SIMPLE_ACCUMULATION_  10  // Traditional rarefaction, using dgeev.
#define _SHOCK_GENERAL_ACCUMULATION_ 11  // Rarefaction with generalized eigenpairs, using dggev.

struct HugoniotPolyLine {
public:
    std::vector<RealVector> vec; // Consolidated

    std::vector<RealVector> point;  // Each element has size = dimension.
    std::vector<double>     speed;      // Speed at each point.
    std::vector<RealVector> eigenvalue; // Each element has size = number of valid eigenvalues (noe).
    int type;
    
    int noe; // assert(eigenvalue.size() == noe);

    HugoniotPolyLine() {
        vec.clear();
        type = 0;
    };

    void dismember(int noe,int dim){
        for (int i = 0; i < vec.size(); i++){
            point[i].resize(dim); // Dim must come from somewhere
            for (int j = 0; j < dim; j++) point[i].component(j) = vec[i].component(j);

            speed[i] = vec[i].component(dim + noe);

            eigenvalue.resize(noe);
            for (int j = 0; j < noe; j++) eigenvalue[i].component(j) = vec[i].component(j + dim + noe + 1);
        }
        return;

    }
    
    
    
    
    ~HugoniotPolyLine() {
        vec.clear();
    };
};

// Color table. Could be anywhere.
//struct ColorTable{
//    public:
//        static int color[16][3];
//};

class ColorCurve {
private:
    static inline int sgn(double x);
    int interpolate(int ,const RealVector &p, const RealVector &q, std::vector<RealVector> &r);
    int classify_point(const RealVector &p, int);
    double shockspeed(int n, double Um[], double Up[], const FluxFunction &ff, const AccumulationFunction &aa, int type);
    void fill_with_jet(const RpFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H);
    FluxFunction * fluxFunction_;
    AccumulationFunction * accFunction_;

protected:
public:

    ColorCurve(const FluxFunction &, const AccumulationFunction &);
    virtual ~ColorCurve();

    void classify_segments(const std::vector<RealVector> &input, int, std::vector<HugoniotPolyLine> &output);
    int preprocess_data(const std::vector<RealVector> &curve, const RealVector &Uref, int noe,
            const FluxFunction & ff, const AccumulationFunction &aa, int type,
            std::vector<RealVector> &out);

    void classify_curve(vector<vector<RealVector> > &, const RealVector &, int, int, vector<HugoniotPolyLine> &output);

};

#endif // _COLORCURVE_

