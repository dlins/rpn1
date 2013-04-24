#ifndef _EXTENSION_CURVETPCW_
#define _EXTENSION_CURVETPCW_

#include "math.h"
#include "Bifurcation_CurveTPCW.h"
#include "HyperCube.h"

// Some matrices, etc., can be static. A static_init() method must take care of
// this. A static flag must be set. The ctor will query the flag's status and
// invoke static_init() if necessary. Garbage collector (when no instance of Double_Contact is using it, destroy the space.)
//
// cvert_, bsvert_, perm_, ncvert_, nsimp_, hn
// Outputs of mkface & mkcube are static.

class Extension_CurveTPCW : public Bifurcation_CurveTPCW {
private:
    // ========================= Left  curve ======================== //
    std::vector<RealVector> curve_segments;
    int curve_number_of_segments;

    std::vector<RealVector> curve_reduced_ffv, curve_reduced_aav; // Values of the curve's flux function and
    // curve's accumulation function over the curve.

    std::vector< std::vector<double> > curve_reduced_e; // Eigenvalues in each point of the curve.
    std::vector< std::vector<bool> > curve_reduced_eig_is_real; // If abovementioned eigenvalues are real.

    int curve_family;
    // ========================= Left  curve ======================== //

    // ======================== Right domain ======================== //
    RealVector domain_pmin, domain_pmax; // Input
    int *domain_number_of_grid_pnts;

    const Flux2Comp2PhasesAdimensionalized *domain_ff;
    const Accum2Comp2PhasesAdimensionalized *domain_aa;
    const Flux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized *domain_reduced_ff;
    const Accum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized *domain_reduced_aa;

    Matrix<RealVector> domain_grid; // Right grid.
    Matrix<RealVector> domain_reduced_ffv, domain_reduced_aav; // Values of the right flux function and
    // right accumulation function over the right grid.

    Matrix< std::vector<double> > domain_reduced_e; // Eigenvalues in each point of the grid.
    Matrix< std::vector<bool> > domain_reduced_eig_is_real; // If abovementioned eigenvalues are real.

    Matrix<bool> domain_is_complex;

    int nur;
    int nvr;
    double ur0;
    double ur1;
    double vr0;
    double vr1;
    double dur;
    double dvr;

    int domain_family;
    bool domain_cell_type; // True for squares, false for triangles.
    // TODO: Every cell must be classified as a square or a triangle (and perhaps as "outside of domain").
    // A matrix or matrices to that effect will be created at some point of the future.

    // TODO: Indices for the grid and the cells MAY start at a value different from zero.

    //        void set_domain_family(int family);
    // ======================== Right domain ======================== //

    // =======================  Combinatorial ======================= //
    int hn; //N
    int hm; //M
    //int DNCV = 16;
    //int DNSIMP = 24;
    //int DNSF = 5;
    //int DNFACE = 125;
    int nsface_, nface_, nsoln_, nedges_;
    int dims_;
    int dime_;

    int dncv;
    int dimf;

    int ncvert_; // N^2
    int nsimp_; // N!

    int numberOfCombinations; // = hc.combination(hn + 1, hm + 1) = hc.combination(3 + 1, 2 + 1);


    void filedg3(Matrix<double> &sol, int dims, Matrix<int> &edges,
            int dime, int nedges_,
            const std::vector<RealVector> &segment,
            RealVector &uv,
            std::vector<RealVector> &left_vrs, std::vector<RealVector> &right_vrs);

    //template <typename T> void initialize_matrix(int n, int m, T *matrix, T value);

    int filhcub3(int ir, int jr, int *index, double *foncub, int hm, int ncvert_, int characteristic_where,
            double *lambda_left_input, Matrix<double> &flux_left_input, Matrix<double> &accum_left_input);

    // =======================  Combinatorial ======================= //
    HyperCube hc;
    Matrix<double> cvert_, vert, foncub, cpp_sol;
    Matrix<int> bsvert_, perm_, comb_, fnbr_, solptr_, cpp_edges_, smpedg_, facptr_, face_;

    int *storn_, *storm_, *index, *exstfc;


    void extension_curve_func(double *val, int ir, int jr, int kl, int kr, int characteristic_where, // Equivalent to bctype's type in sqfun.F
            double *segment_lambda, Matrix<double> &segment_flux, Matrix<double> &segment_accum);

    inline bool left_right_adjacency(int il, int jl, int ir, int jr);

    void curve2p5(int characteristic_where,
            std::vector<RealVector> current_segment,
            double *segment_lambda, Matrix<double> &segment_flux, Matrix<double> &segment_accum,
            int singular,
            std::vector<RealVector> &curve_segments, std::vector<RealVector> &domain_segments);

    double det(int nn, double *A);
protected:
public:
    // TODO: Maybe this class could be formed by purely static. In that case the ctor() may be useless.
    // The convenience of this approach is to be discussed sometime.
    Extension_CurveTPCW(const RealVector &dpmin, const RealVector &dpmax, const int *domain_number_of_grid_pnts_input,
            const Flux2Comp2PhasesAdimensionalized *dff, const Accum2Comp2PhasesAdimensionalized *daa);
    //                            const FluxFunction *drff, const AccumulationFunction *draa);
   virtual ~Extension_CurveTPCW();

    void compute_extension_curve(int characteristic_where, int singular,
            const std::vector<RealVector> &original_segments, int curve_family,
           const Flux2Comp2PhasesAdimensionalized *curve_ff, const Accum2Comp2PhasesAdimensionalized *curve_aa, // For the curve.
//            FluxFunction *curve_reduced_ff, AccumulationFunction *curve_reduced_aa, // For the curve.
            int domain_family,
            std::vector<RealVector> &curve_segments,
            std::vector<RealVector> &domain_segments);

    void set_domain_family(int nrf); // This method sets the family and validates the cells for the right domain.
};

#endif // _EXTENSION_CURVETPCW_


