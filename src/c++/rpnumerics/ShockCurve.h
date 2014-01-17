#ifndef _SHOCKCURVE_
#define _SHOCKCURVE_

#include "HugoniotContinuation.h"
#include "Curve.h"
#include <vector>
#include <algorithm>

#ifndef SHOCKCURVE_OK
#define SHOCKCURVE_OK 0
#endif

#ifndef SHOCKCURVE_NEWTON_CONVERGED
#define SHOCKCURVE_NEWTON_CONVERGED 1
#endif

#ifndef SHOCKCURVE_NEWTON_DID_NOT_CONVERGE
#define SHOCKCURVE_NEWTON_DID_NOT_CONVERGE 2
#endif

#ifndef SHOCKCURVE_NEWTON_OUTSIDE_DOMAIN
#define SHOCKCURVE_NEWTON_OUTSIDE_DOMAIN 3
#endif

// Types of curves.
//
#ifndef SHOCKCURVE_TOTAL_CLASSIFICATION
#define SHOCKCURVE_TOTAL_CLASSIFICATION 10
#endif

#ifndef SHOCKCURVE_SHOCK_CURVE
#define SHOCKCURVE_SHOCK_CURVE 11
#endif

// Subtypes of curves.
//
#define DONT_CHECK_EQUALITY_AT_LEFT                                      100
#define SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_LEFT        101 // lambda(Uref)[family] == sigma.
#define SHOCK_SIGMA_EQUALS_LAMBDA_OF_SOME_FAMILY_AT_LEFT   102 // lambda(Uref)[:]      == sigma.

#define DONT_CHECK_EQUALITY_AT_RIGHT                                     200
#define SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_RIGHT       201 // lambda(Uright)[family] == sigma.
#define SHOCK_SIGMA_EQUALS_LAMBDA_OF_SOME_FAMILY_AT_RIGHT  202 // lambda(Uright)[:]      == sigma, or, sigma_dot == 0.

// For shock_stopped_because.
//
#define SHOCK_LEFT_CHARACTERISTIC_AT_FAMILY                              300
#define SHOCK_LEFT_CHARACTERISTIC_AT_CONTIGUOUS_FAMILY                   301

#define SHOCK_RIGHT_CHARACTERISTIC_AT_FAMILY                             302
#define SHOCK_RIGHT_CHARACTERISTIC_AT_CONTIGUOUS_FAMILY                  303

#define SHOCK_COMPLEX_EIGENVALUE_AT_FAMILY                               305
#define SHOCK_REACHED_BOUNDARY                                           306

#define DONT_USE_INTERRUPTION_FUNCTIONS            1
#define USE_INTERRUPTION_FUNCTIONS_SPECIFIC_FAMILY 2
#define USE_INTERRUPTION_FUNCTIONS_ALL_FAMILIES    3

#define SHOCKSPEED_INCREASES                       4
#define SHOCKSPEED_DECREASES                       5

#define CONTINUE_AFTER_TRANSITION                  6
#define STOP_AFTER_TRANSITION                      7

// For what_family_to_use:
//
#define USE_ALL_FAMILIES                           8
#define USE_CURRENT_FAMILY                         9

#define TRANSITION_FOUND                          20
#define TRANSITION_CURRENT_FOUND                  21
#define TRANSITION_REFERENCE_FOUND                22
#define TRANSITION_NOT_FOUND                      23

// No-one should derive from this class, which is only auxiliary.
//
class TransitionPointStructure {
    private:
    protected:
    public:
        double alpha;
        RealVector point;
        int family;
        bool local; // True if local, false if reference.

        TransitionPointStructure(double a, const RealVector &p, int f, bool l){
            alpha  = a;
            point  = p;
            family = f;
            local  = l;
        }

        ~TransitionPointStructure(){}

        friend bool operator<(const TransitionPointStructure &a, const TransitionPointStructure &b){
            return a.alpha < b.alpha; // Dan hated this one here. alpha's meaning should be redefined to (1.0 - alpha), so it is bound to the last point and not to the previous point.
                                      // Corrected this one. Morante 2013/10/20.
        }

        friend bool operator>(const TransitionPointStructure &a, const TransitionPointStructure &b){
            return b < a;
        }
};

class ShockCurvePoints {
    private:
    protected:
    public:
        ShockCurvePoints(){}
        ShockCurvePoints(const std::vector<RealVector> &p, 
                         const std::vector<int> tci, const std::vector<int> tcf,
                         const std::vector<int> tri, const std::vector<int> trf,
                         int f, const ReferencePoint &r){

            curve = p;
            transition_current_index    = tci;
            transition_current_family   = tcf;
            transition_reference_index  = tri;
            transition_reference_family = trf;
            family = f;
            ref = r;
        }

        ~ShockCurvePoints(){}

        std::vector<RealVector> curve;
        std::vector<int>        transition_current_index;
        std::vector<int>        transition_current_family;
        std::vector<int>        transition_reference_index;
        std::vector<int>        transition_reference_family;

        int family;
        ReferencePoint ref;
};

class ShockCurve {
    private:
    protected:
        HugoniotContinuation *hc;

        const FluxFunction *f;
        const AccumulationFunction *g;
        const Boundary *boundary;
        const Boundary *computational_domain;

        ReferencePoint ref;

        // TODO: Make these pure virtual, to be defined in derived classes.
        //
        virtual int find_point_for_sigma_equal_current_lambda(const RealVector &in, RealVector &out);

        virtual void find_system_for_sigma_equal_current_lambda(const RealVector &in, DoubleMatrix &nablaH, RealVector &H);

        virtual void find_system(const RealVector &in, const RealVector &rarefaction_point, double lambda_ref, DoubleMatrix &nablaH, RealVector &H);
        virtual void find_system(const RealVector &in, const ReferencePoint &reference_point, double lambda_ref, DoubleMatrix &nablaH, RealVector &H);
        //virtual void find_system(const RealVector &in, double lambda_ref, DoubleMatrix &nablaH, RealVector &H);


        // Use (or don't) interruption functions.
        // If no one is used, the shock is allowed to reach the boundary.
        int use_interruption_functions;

        // These below can exist in one of two states, either on or off.
        int shock_increases;
        int continue_after_transition;

        int current_family;

        // For the stop criteria
        void find_alphas_for_characteristic_shocks(const RealVector &previous_lambda_minus_sigma, 
                                                       const RealVector &lambda_minus_sigma, 
                                                       int what_family_to_use, int continue_after_transition, 
                                                       std::vector<double> &alpha,
                                                       std::vector<int> &corresponding_family);

        // First stop test
        int local_speed_equality(const RealVector &previous_lambda_minus_sigma, const RealVector &previous_point, 
                                     const RealVector &lambda_minus_sigma,          const RealVector &candidate_point,
                                     int what_family_to_use,
                                     std::vector<TransitionPointStructure> &transition_points);

        // Second test
        int reference_speed_equality(const RealVector &previous_lambdaref_minus_sigma, const RealVector &previous_point, 
                                         const RealVector &lambdaref_minus_sigma,          const RealVector &candidate_point,
                                         int what_family_to_use,
                                         std::vector<TransitionPointStructure> &transition_points);

        // Call all interruption functions (stop criteria)
        int call_interruption_functions(const RealVector &previous_lambdaref_minus_sigma, const RealVector &previous_lambda_minus_sigma, const RealVector &previous_point, 
                                        const RealVector &lambdaref_minus_sigma,          const RealVector &lambda_minus_sigma,          const RealVector &candidate_point,
                                        int what_family_to_use,
                                        int after_transition,
                                        int left_subtype, int right_subtype,
                                        Curve &curve,          // To be updated by this method, adding the transition points.
                                        std::vector<int> &transition_current_index,
                                        std::vector<int> &transition_current_family,
                                        int &transition_current_found,
                                        std::vector<int> &transition_reference_index,
                                        std::vector<int> &transition_reference_family,
                                        int &transition_reference_found);

        void add_point(Curve &c, const RealVector &p);
    public:
        ShockCurve(HugoniotContinuation *h);
        virtual ~ShockCurve();

//        virtual int curve_engine(const ReferencePoint &r, const RealVector &in, const RealVector &initial_direction, int family, int type, int subtype, 
//                                 std::vector<RealVector> &curve, 
//                                 std::vector<RealVector> &transition_current,
//                                 std::vector<RealVector> &transition_reference,
//                                 int &edge);

        // TODO: Create the method below. Increase/decrease will be given, and from it initial_direction will be determined.
        //       Then the other curve_engine will be called. The name shall be selected in other moment.

//        virtual int curve_superengine(const ReferencePoint &r, const RealVector &in, int increase, int family, int type, int subtype, 
//                             int what_family_to_use,
//                             int after_transition,
//                             std::vector<RealVector> &shockcurve, 
//                             std::vector<int> &transition_current_index,
//                             std::vector<int> &transition_current_family,
//                             std::vector<int> &transition_reference_index,
//                             std::vector<int> &transition_reference_family,  
//                             int &edge);

        virtual int curve_engine(const ReferencePoint &r, const RealVector &in, const RealVector &initial_direction, int family, 
                                 int type, int left_subtype, int right_subtype,
                                 int what_family_to_use,
                                 int after_transition,
                                 Curve &shockcurve, 
                                 std::vector<int> &transition_current_index,
                                 std::vector<int> &transition_current_family,
                                 std::vector<int> &transition_reference_index,
                                 std::vector<int> &transition_reference_family, 
                                 int &shock_stopped_because,
                                 int &edge);

//        virtual int curve(const ReferencePoint &ref, int type, int subtype, std::vector< std::vector<RealVector> > &curve, 
//                          std::vector< std::vector<RealVector> > &transition_current, 
//                          std::vector< std::vector<RealVector> > &transition_reference);
        
        virtual int curve(const ReferencePoint &ref, int type, int left_subtype, int right_subtype,
                          int what_family_to_use, int after_transition,
                          std::vector<ShockCurvePoints> &shockcurve_with_transitions);

        virtual void set_reference_point(const ReferencePoint &r){ref = r; return;}

        virtual int find_point_for_sigma_equal_reference_lambda(const RealVector &in, double lambda_ref, RealVector &out);

        HugoniotContinuation * get_HugoniotContinuation(){return hc;}
};

#endif // _SHOCKCURVE_

