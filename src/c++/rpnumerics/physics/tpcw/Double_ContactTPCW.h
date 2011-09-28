#ifndef _DOUBLE_CONTACTTPCW_
#define _DOUBLE_CONTACTTPCW_

#include "math.h"
#include "Bifurcation_CurveTPCW.h"
#include "HyperCube.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

// Some matrices, etc., can be static. A static_init() method must take care of
// this. A static flag must be set. The ctor will query the flag's status and
// invoke static_init() if necessary. Garbage collector (when no instance of Double_Contact is using it, destroy the space.)
//
// cvert_, bsvert_, perm_, ncvert_, nsimp_, hn
// Outputs of mkface & mkcube are static.

class Double_ContactTPCW : public Bifurcation_CurveTPCW {
private:
    // ======================== Left  domain ======================== //
    RealVector leftpmin, leftpmax; // Input
    int *left_number_of_grid_pnts;
    const Flux2Comp2PhasesAdimensionalized *leftff;
    //                , *reducedleftff;
    const Accum2Comp2PhasesAdimensionalized *leftaa;
    //                , *reducedleftaa;

    Matrix<RealVector> leftgrid; // Left grid.
    Matrix<RealVector> leftffv, leftaav; // Values of the left flux function and
    // left accumulation function over the left grid.

    Matrix< std::vector<double> > lefte; // Eigenvalues in each point of the grid.
    Matrix< std::vector<bool> > left_eig_is_real; // If abovementioned eigenvalues are real.

    Matrix<bool> left_is_complex;

    //        Matrix<RealEigenvalueCell> left_cells;

    int nul;
    int nvl;
    double ul0;
    double ul1;
    double vl0;
    double vl1;
    double dul;
    double dvl;

    int left_family;
    bool left_cell_type; // True for squares, false for triangles.
    // TODO: Every cell must be classified as a square or a triangle (and perhaps as "outside of domain").
    // A matrix or matrices to that effect will be created at some point of the future.

    // TODO: Indices for the grid and the cells MAY start at a value different from zero.
    // ======================== Left  domain ======================== //

    // ======================== Right domain ======================== //
    RealVector rightpmin, rightpmax; // Input
    int *right_number_of_grid_pnts;

    Flux2Comp2PhasesAdimensionalized *rightff;
    //        , *reducedrightff;
    Accum2Comp2PhasesAdimensionalized *rightaa;
    //        , *reducedrightaa;

    Matrix<RealVector> rightgrid; // Right grid.
    Matrix<RealVector> rightffv, rightaav; // Values of the right flux function and
    // right accumulation function over the right grid.

    Matrix< std::vector<double> > righte; // Eigenvalues in each point of the grid.
    Matrix< std::vector<bool> > right_eig_is_real; // If abovementioned eigenvalues are real.

    Matrix<bool> right_is_complex;
    //        Matrix<RealEigenvalueCell> right_cells;

    int nur;
    int nvr;
    double ur0;
    double ur1;
    double vr0;
    double vr1;
    double dur;
    double dvr;

    int right_family;
    bool right_cell_type; // True for squares, false for triangles.
    // TODO: Every cell must be classified as a square or a triangle (and perhaps as "outside of domain").
    // A matrix or matrices to that effect will be created at some point of the future.

    // TODO: Indices for the grid and the cells MAY start at a value different from zero.
    // ======================== Right domain ======================== //

    double dumax, dvmax;

    void filedg4(Matrix<double> &sol, int dims, Matrix<int> &edges,
            int dime, int nedges_,
            int il, int jl, int ir, int jr,
            std::vector<RealVector> &left_vrs, std::vector<RealVector> &right_vrs);

    HyperCube hc;

    void func(double *val, int ir, int jr, int kl, int kr,
            double *lambda_left_input, Matrix<double> &flux_left_input, Matrix<double> &accum_left_input);

    int filhcub4(int ir, int jr, int *index, double *foncub, int hm, int ncvert_,
            double *lambda_left_input, Matrix<double> &flux_left_input, Matrix<double> &accum_left_input);

    inline bool left_right_ordering(int il, int jl, int ir, int jr);

    // Extra methods
    double det(int nn, double *A);

    template <typename T> void initialize_matrix(int n, int m, T *matrix, T value) {
        for (int i = 0; i < n * m; i++) matrix[i] = value;
        return;
    }
protected:
public:
    Double_ContactTPCW(const RealVector &lpmin, const RealVector &lpmax, const int *l_number_of_grid_pnts,
            const FluxFunction *lff, const AccumulationFunction *laa,
            //                       const FluxFunction *Redlff, const AccumulationFunction *Redlaa,
            int lf,
            const RealVector &rpmin, const RealVector &rpmax, const int *r_number_of_grid_pnts,
            const FluxFunction *rff, const AccumulationFunction *raa,
            //                       const FluxFunction *Redrff, const AccumulationFunction *Redraa,
            int rf);
    ~Double_ContactTPCW();

    void compute_double_contactTPCW(std::vector<RealVector> &left_vrs,
            std::vector<RealVector> &right_vrs);

    void set_left_family(int nlf); // This method sets the family and validates the cells for the left domain.
    void set_right_family(int nrf); // This method sets the family and validates the cells for the right domain.
};

#endif // _DOUBLE_CONTACTTPCW_

