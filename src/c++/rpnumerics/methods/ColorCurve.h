#ifndef _COLORCURVE_
#define _COLORCURVE_

#include <vector>
#include <string>

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "eigen.h"

#define INTERPOLATION_ERROR -1
#define INTERPOLATION_OK     0

//#define _SHOCK_SIMPLE_ACCUMULATION_  10  // Traditional rarefaction, using dgeev.
//#define _SHOCK_GENERAL_ACCUMULATION_ 11  // Rarefaction with generalized eigenpairs, using dggev.

#ifndef UNCLASSIFIABLE_POINT
#define UNCLASSIFIABLE_POINT (-1)
#endif

extern "C" void dgesv_(int*,int*,double *,int*,int *,double *,int* ,int*);


struct HugoniotPolyLine {
public:
    std::vector<RealVector>  point;  	  // Each element has size = dimension (dim).
    std::vector<double>      speed;       // Speed at each point.
    std::vector<RealVector>  eigenvalue;  // Each element has size = number of valid eigenvalues (fam).
    std::vector<std::string> signature;   // This returns the Hugoniot signature, i.e., "--++", etc
                                          // zero means characteristic, "-." or "+." means complex
                                          // conjugate, with the real part sign.


//    std::vector<int>        complextype; // If eigenvalue is real, complextype = 0.
    /*  [COMPLEX_TYPE explanation:]
     *
     *  The complextype (ct) helps in the signature classification, thus ct = 0 means all eigenvalues
     *  are real. For fam = 2, ct = -1 means the left side has complex eigenvalues, ct = 1 means the
     *  right side has complex eigenvalues. (It must be written with an asterix after the real part
     *  sign, for example, +*-- for ct = -1, or -+-* for ct = 1.)
     *     The ordering of eigenvalues helps for more than 2 valid eigenvalues. With negative ct,
     *  the number is related with the pair, as well as a positive ct. For example with three valid
     *  eigenvalues, we can have for ct = -2 the sigature -+*--+, and for ct = 1, we can have the
     *  signature ----*+. (Notice that   |ct| < fam   always holds.)
     */
    int type;
    
    HugoniotPolyLine() {
      
        type = -1;
    };

    ~HugoniotPolyLine() {
      
    };
};

// Color table. Could be anywhere.
//struct ColorTable{
//    public:
//        static int color[16][3];
//};

class ColorCurve {
    private:
        std::string sp, sm, sc, sz;
    protected:
        int solve(const double *A,  double *b, int dim, double *x);

        void Left_Newton_improvement(const RealVector &input, const int type, RealVector &out);
        void Right_Newton_improvement(const RealVector &input, const int type, RealVector &out);

        int interpolate(const RealVector &p, double &s_p,
                        const std::vector<double> &eigenvalue_p, const int type_p,
                        const RealVector &q, double &s_q,
                        const std::vector<double> &eigenvalue_q, const int type_q,
                        vector<RealVector> &r, vector<int> &rtype);

        int complete_point(RealVector &p, double &s, std::vector<double> &eigenvalue, int *complex);

        int classify_point(RealVector &p, double &s, std::vector<double> &eigenvalue, std::string signature);

        void classify_segment(RealVector &p,  RealVector &q, 
                              std::vector<HugoniotPolyLine> &classified_curve,
                              std::vector<RealVector> &transition_list);

        FluxFunction * fluxFunction_;
        AccumulationFunction * accFunction_;

        RealVector ref_point;
        std::vector<double> ref_eigenvalue;
        RealVector F_ref, G_ref;
    public:

    ColorCurve(const FluxFunction &, const AccumulationFunction &);
    virtual ~ColorCurve();

// So por chamada deixo este metodo...
    void classify_curve(vector<vector<RealVector> > &, const RealVector &, int, int, vector<HugoniotPolyLine> &output);
// ... de ser tudo certo, isto va embora.

    void classify_segmented_curve(std::vector<RealVector>  &original, const RealVector &ref,
                                  std::vector<HugoniotPolyLine> &classified_curve,
                                  std::vector<RealVector> &transition_list);

    void classify_continuous_curve(std::vector<RealVector>  &original, const RealVector &ref,
                                   std::vector<HugoniotPolyLine> &classified_curve,
                                   std::vector<RealVector> &transition_list);
};

#endif // _COLORCURVE_
