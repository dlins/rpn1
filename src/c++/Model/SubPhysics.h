#ifndef _SUBPHYSICS_
#define _SUBPHYSICS_

#include <limits>

#include "Parameter.h"
#include "AuxiliaryFunction.h"
#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Boundary.h"
#include "Viscosity_Matrix.h"
#include "GridValues.h"

#include "HugoniotCurve.h"
#include "Extension.h"

#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "ShockCurve.h"
#include "HugoniotContinuation.h"
#include "ODE_Solver.h"
#include "WaveCurveFactory.h"

#include "Explicit_Bifurcation_Curves.h"

#include "Coincidence.h"
#include "Coincidence_Contour.h"

#include "Inflection_Curve.h"

#include "BifurcationCurve.h"

#include "Double_Contact.h"
#include "ContactRegionBoundary.h"

//#include "Extension_Curve.h"
//#include "Double_Contact.h"
//#include "Hysteresis.h"

// Class SubPhysics. This class is abstract. The users will derive from it and provide what is needed.
//
// In the classes derived from SubPhysics the user will have to define:
//
//     Default constructor (perhaps).
//     Virtual destructor (because classes will be derived from this one).
//
// All methods within this class should be virtual, so the derived classes can
// modify them as need be.

// TODO: Is it necessary for a SubPhysics to know which Physics is its parent?
//
//       Morante.

class SubPhysics {
private:
    // Users should not add any private members.
protected:
    std::string info_subphysics_;

    unsigned int number_of_families_;

    const FluxFunction *flux_;
    const AccumulationFunction *accumulation_;
    const Boundary *boundary_;
    const Viscosity_Matrix *viscosity_matrix_;

    std::vector<AuxiliaryFunction*> auxiliaryfunctions_;

    RarefactionCurve *rarefactioncurve_;
    CompositeCurve *compositecurve_;
    HugoniotContinuation *hugoniotcontinuation_;
    ShockCurve *shockcurve_;
    WaveCurveFactory *wavecurvefactory_;
    ODE_Solver *odesolver_;

    GridValues *gridvalues_;

    std::vector<HugoniotCurve*> hugoniot_curve;
    std::vector<Extension*> extension_curve;

    Explicit_Bifurcation_Curves *explicitbifurcationcurves_;

    // I do not know if the RPn can use it right now.
    //
    DoubleMatrix transformation_matrix_;

    std::string xlabel_, ylabel_;

    std::vector<Parameter*> equation_parameter_;

    const Coincidence *coincidence_;
    Coincidence_Contour *coincidence_contour_;

    Inflection_Curve *inflection_curve_;

    BifurcationCurve *bifurcationcurve_;

    Double_Contact *doublecontact_;

    ContactRegionBoundary *contactregionboundary_;

public:
    SubPhysics();
    virtual ~SubPhysics();

    // Information about the subphysic, which can be displayed somewhere.
    //

    virtual const std::string info_subphysics() {
        return info_subphysics_;
    }

    // Access to the data.

    virtual const FluxFunction * flux() {
        return flux_;
    }

    virtual const AccumulationFunction * accumulation() {
        return accumulation_;
    }

    virtual const Boundary * boundary() {
        return boundary_;
    }

    virtual GridValues * gridvalues() {
        return gridvalues_;
    }

    //        virtual const Viscosity_Matrix *     viscosity_matrix(){return viscosity_matrix_;}

    virtual void list_of_Hugoniot_methods(std::vector<HugoniotCurve*> &h) const {
        h = hugoniot_curve;
        return;
    }

    virtual void list_of_extension_methods(std::vector<Extension*> &e) const {
        e = extension_curve;
        return;
    }

    virtual Explicit_Bifurcation_Curves* explicit_bifurcation_curves() {
        return explicitbifurcationcurves_;
    }

    virtual DoubleMatrix transformation_matrix() {
        return transformation_matrix_;
    }

    virtual std::string xlabel() {
        return xlabel_;
    }

    virtual std::string ylabel() {
        return ylabel_;
    }

    virtual void equation_parameter(std::vector<Parameter*> &ep) {
        ep = equation_parameter_;

        return;
    }

    virtual Coincidence_Contour* coincidence_contour() {
        return coincidence_contour_;
    }

    virtual void auxiliary_functions(std::vector<AuxiliaryFunction*> &vaf) {
        vaf = auxiliaryfunctions_;
        return;
    }

    virtual RarefactionCurve * rarefaction_curve() {
        return rarefactioncurve_;
    }

    virtual ShockCurve *shock() {
        return shockcurve_;
    }

    virtual CompositeCurve *composite() {
        return compositecurve_;
    }

    virtual ODE_Solver* ode_solver(){
        return odesolver_;
    }

    virtual HugoniotContinuation * Hugoniot_continuation() {
        return hugoniotcontinuation_;
    }

    virtual WaveCurveFactory * wavecurvefactory() {
        return wavecurvefactory_;
    }

    virtual Inflection_Curve * inflection_curve() {
        return inflection_curve_;
    }

    virtual const Coincidence *coincidence() {
        return coincidence_;
    }

    virtual BifurcationCurve* bifurcation_curve() {
        return bifurcationcurve_;
    }

    virtual Double_Contact* double_contact() {
        return doublecontact_;
    }

    // Check if the values of the parameters are
    // consistent. 
    //
    virtual bool parameter_consistency(){
        return true;
    }

    // Contact region. This method is not enough when there are contacts
    // for more than one family. TODO: Create a new method that characterizes
    // this situation, to be used by the rarefaction, wavecurve, etc.
    //
    // Right now the question to be asked is if the point lies in
    // a contact region for all families.
    //
    virtual bool inside_contact_region(const RealVector &p, int family){
        return false;
    }

//    virtual double distance_to_contact_region(const RealVector &p, int family) = 0;
    virtual double distance_to_contact_region(const RealVector &p){
        return std::numeric_limits<double>::infinity();
    }

    virtual unsigned int number_of_families(){
        return number_of_families_;
    }

    virtual ContactRegionBoundary* contact_region_boundary(){
        return contactregionboundary_;
    }


};

#endif // _SUBPHYSICS_

