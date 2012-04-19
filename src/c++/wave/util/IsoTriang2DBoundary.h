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
    //
    double end_edge;

    virtual void edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg);
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

    void envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &gv,
            int where_constant, int number_of_steps, bool singular,
            std::vector<RealVector> &c, std::vector<RealVector> &d);

    void extension_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &gv,
            int where_constant, int number_of_steps, bool singular,
            int fam,
            std::vector<RealVector> &c, std::vector<RealVector> &d);




    const char* boundaryType() const;

    const RealVector & getA()const;
    const RealVector & getB()const;
    const RealVector & getC()const;

};








//
//
//class IsoTriang2DBoundary : public Boundary {
//private:
//
//    RealVector * minimums_;
//    RealVector * maximums_;
//
//    const char * type_;
//
//    // This number is given as (pmax(0) + pmax(1))/2.0 + 0.000001, where 0.000001 is a trick for
//    // the usage in HyperCube sons.
//    //
//    double end_edge;
//
//
//protected:
//
//
//    void edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg);
//
//public:
//
//    virtual ~IsoTriang2DBoundary();
//
//    IsoTriang2DBoundary(const RealVector & A, const RealVector & B, const RealVector & C);
//
//    IsoTriang2DBoundary(const IsoTriang2DBoundary &);
//
//    bool inside(const RealVector &y) const;
//
//    bool inside(const double*)const;
//
//    //! Virtual constructor
//    Boundary * clone()const;
//
//    const RealVector & minimums() const;
//
//    const RealVector & maximums() const;
//
//    RealVector intersect(RealVector &y1, RealVector &y2) const;
//
//
//
//    int intersection(const RealVector &p, const RealVector &q, RealVector &r, int &w) const;
//
//
//    virtual void envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
//            GridValues &gv,
//            int where_constant, int number_of_steps, bool singular,
//            std::vector<RealVector> &c, std::vector<RealVector> &d);
//
//    virtual void extension_curve(const FluxFunction *f, const AccumulationFunction *a,
//            GridValues &gv,
//            int where_constant, int number_of_steps, bool singular,
//            int fam,
//            std::vector<RealVector> &c, std::vector<RealVector> &d);
//
//
//
//
//

//
//};
//

inline const RealVector & Three_Phase_Boundary::getA()const {
    return *A_;
}

inline const RealVector & Three_Phase_Boundary::getB()const {
    return *B_;
}

inline const RealVector & Three_Phase_Boundary::getC()const {
    return *C_;
}

//inline const RealVector & IsoTriang2DBoundary::minimums() const {
//    return *minimums_;
//}
//
//inline const RealVector & IsoTriang2DBoundary::maximums() const {
//    return *maximums_;
//}


#endif //! _IsoTriang2DBoundary_H
