/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) IsoTriang2DBoundary.h
 */

#ifndef _IsoTriang2DBoundary_H
#define _IsoTriang2DBoundary_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Boundary.h"
#include "RealVector.h"

#include "Envelope_Curve.h"
#include "Extension_Curve.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


#define THREE_PHASE_BOUNDARY_SW_ZERO 0
#define THREE_PHASE_BOUNDARY_SO_ZERO 1
#define THREE_PHASE_BOUNDARY_SG_ZERO 2

#define THREE_PHASE_EXTENDED_BOUNDARY_SW 0
#define THREE_PHASE_EXTENDED_BOUNDARY_SO 1
#define THREE_PHASE_EXTENDED_BOUNDARY_SG 2

class Three_Phase_Boundary : public Boundary {
protected:
    RealVector *pmin, *pmax;

    RealVector * A_;
    RealVector * B_;
    RealVector * C_;

    // This number is given as (pmax(0) + pmax(1))/2.0 + 0.000001, where 0.000001 is a trick for
    // the usage in HyperCube sons.
    // TODO: If the kluge "+ 0.000001" changes in end_edge, it must change also in edge_segments.
    //
    double end_edge;

    virtual int edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg);

public:
    Three_Phase_Boundary();
    Three_Phase_Boundary(const RealVector &ppmin, const RealVector &ppmax);
    Three_Phase_Boundary(const Three_Phase_Boundary &original);
    ~Three_Phase_Boundary();

    bool inside(const RealVector &p) const;
    bool inside(const double *p) const;

    Boundary* clone() const;

    const RealVector& minimums(void) const;
    const RealVector& maximums(void) const;

    RealVector intersect(RealVector &p1, RealVector &p2) const;
    int intersection(const RealVector &p, const RealVector &q, RealVector &r, int &w) const;





    void extension_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &gv,
            int where_constant, int number_of_steps, bool singular,
            int fam, int characteristic,
            std::vector<RealVector> &c, std::vector<RealVector> &d);


    void envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &gv,
            int where_constant, int number_of_steps, bool singular,
            std::vector<RealVector> &c, std::vector<RealVector> &d);





    void physical_boundary(std::vector<RealVector> &);


    const char* boundaryType() const;

    const RealVector & getA()const;
    const RealVector & getB()const;
    const RealVector & getC()const;

};






inline const RealVector & Three_Phase_Boundary::getA()const {
    return *A_;
}

inline const RealVector & Three_Phase_Boundary::getB()const {
    return *B_;
}

inline const RealVector & Three_Phase_Boundary::getC()const {
    return *C_;
}



#endif //! _IsoTriang2DBoundary_H
