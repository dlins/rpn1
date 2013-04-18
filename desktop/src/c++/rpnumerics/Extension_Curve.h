#ifndef _EXTENSION_CURVE_
#define _EXTENSION_CURVE_

#ifndef CHARACTERISTIC_ON_CURVE
#define CHARACTERISTIC_ON_CURVE  0
#endif

#ifndef CHARACTERISTIC_ON_DOMAIN
#define CHARACTERISTIC_ON_DOMAIN 1
#endif

#include "TwoImplicitFunctions.h"
#include "Contour2p5_Method.h"

class Extension_Curve : public TwoImplicitFunctions {
private:
protected:
    int characteristic_where, family;
    
    Matrix<double> segment_flux, segment_accum;
    RealVector segment_lambda;
    
    const FluxFunction *ff;
    const AccumulationFunction *aa;

    static int species_physic(Extension_Curve*, double*, int, int, int);
    static int compositional_physic(Extension_Curve*, double*, int, int, int);

    int (*type_of_physic)(Extension_Curve*, double*, int, int, int);
public:
    // TODO: Maybe this class could be formed by purely static. In that case the ctor() may be useless.
    // The convenience of this approach is to be discussed sometime.
    Extension_Curve(){
        gv = 0;
        oc = 0;
        singular = true;
        
        segment_flux.resize(3, 2);
        segment_accum.resize(3, 2);
        
        segment_lambda.resize(3);
        family = 0;
    }
    
    ~Extension_Curve(){}

    bool valid_segment(int i);

    int function_on_vertices(double *foncub, int domain_i, int domain_j, int kl);
        
    void curve(const FluxFunction *f, const AccumulationFunction *a, 
               GridValues &g, int where_is_characteristic,
               bool is_singular, int fam,  
               std::vector<RealVector> &original_curve,
               std::vector<RealVector> &extension_on_curve,
               std::vector<RealVector> &extension_on_domain);
};

#endif // _EXTENSION_CURVE_
