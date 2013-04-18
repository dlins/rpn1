#ifndef _CONTOUR2X2_METHOD_
#define _CONTOUR2X2_METHOD_

#include "HyperCube.h"
#include "RealSegment.h"
#include "ThreeImplicitFunctions.h"

#include <iostream>
#include <vector>

#include "Boundary.h"
#include "GridValues.h"

//#ifndef INVALID_FUNCTION_ON_VERTICES
//#define INVALID_FUNCTION_ON_VERTICES 0
//#endif

//#ifndef VALID_FUNCTION_ON_VERTICES
//#define VALID_FUNCTION_ON_VERTICES 1
//#endif

//#ifndef NO_ZERO_ON_CUBE
//#define NO_ZERO_ON_CUBE 2
//#endif

struct context;

class Contour2x2_Method {
    private:
        int dimension;

        static bool is_first;

        static HyperCube hc;
        static const int hn;
        static const int hm;
        static int nsface_, nface_;
        //, nsoln_, nedges_;
        static const int dims_;
        static const int dime_;
        static const int dimf_;
        static const int ncvert_;
        static const int dncv;  // TODO, ver si no es ncvert.
        static const int nsimp_;

        static const int numberOfCombinations;

        //global
        static Matrix<int>    fnbr_;
        static Matrix<double> cvert_;
        static Matrix<int>    facptr_;
        static Matrix<int>    face_;
        static int *index;
        static int *usevrt; // TODO, ver si es necesario, sino, matar [dncv]

        static int tsimp;
        static int tface;


        static void allocate_arrays(void);
        // 
        static void process_cell( context *cnt, ThreeImplicitFunctions *timpf, int il, int jl, int ir, int jr);
        static void process_cell( ThreeImplicitFunctions *timpf, int il, int jl, int ir, int jr,
                                 std::vector<RealVector> &left_vrs,   // on_domain
                                 std::vector<RealVector> &right_vrs); // on_curve
        //
    protected:
        static double ul0, vl0, ur0, vr0;
        static double dul, dvl, dur, dvr;
        static double dumax, dvmax;

        static bool filhcub4(ThreeImplicitFunctions *timpf,
                             int ir, int jr, int *index, double *foncub);

        static void filedg4(Matrix<double> &sol_, Matrix<int> &edges_, int nedges_, 
                            int il, int jl, int ir, int jr,
                            std::vector<RealVector> &left_vrs, std::vector<RealVector> &right_vrs);

		static bool left_right_adjacency(int il, int jl, int ir, int jr);
    public:
        static void deallocate_arrays(void);

        static void curve2x2(ThreeImplicitFunctions *timpf,
                             std::vector<RealVector> &left_vrs, 
                             std::vector<RealVector> &right_vrs);
};

#endif // _CONTOUR2X2_METHOD_

