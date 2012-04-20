#include "Extension_Curve.h"

int Extension_Curve::function_on_vertices(double *foncub, int domain_i, int domain_j, int kl) {
    if (gv == 0 || oc == 0) return INVALID_FUNCTION_ON_VERTICES;
      
    if (!gv->eig_is_real(domain_i, domain_j)[family]) return INVALID_FUNCTION_ON_VERTICES;

    double lambda;
    if (characteristic_where == CHARACTERISTIC_ON_CURVE) {
        lambda = segment_lambda[kl];
    } else {
        lambda = gv->e(domain_i, domain_j)[family].r;
    }

    foncub[0] = lambda * (gv->G_on_grid(domain_i, domain_j).component(0) - segment_accum(0, kl))
                       - (gv->F_on_grid(domain_i, domain_j).component(0) - segment_flux(0, kl));
    foncub[1] = lambda * (gv->G_on_grid(domain_i, domain_j).component(1) - segment_accum(1, kl))
                       - (gv->F_on_grid(domain_i, domain_j).component(1) - segment_flux(1, kl));

    return VALID_FUNCTION_ON_VERTICES;
}


bool Extension_Curve::valid_segment(int i){
    if (oc == 0) return false;

    double epsilon = 1e-7;
    
    double F[2], G[2], JF[2][2], JG[2][2];

    std::vector<eigenpair> e;
    
    for (int j = 0; j < 2; j++){
        ff->fill_with_jet(2, oc->at(i + j).components(), 1, F, &JF[0][0], 0);
        aa->fill_with_jet(2, oc->at(i + j).components(), 1, G, &JG[0][0], 0);
        
        e.clear();
        Eigen::eig(2, &JF[0][0], &JG[0][0], e);  
        
        if (characteristic_where == CHARACTERISTIC_ON_CURVE){
            if (fabs(e[family].i) > epsilon) return false;
            segment_lambda.component(j) = e[family].r;
        }
//        else {
//
//        }
            for (int k = 0; k < 2; k++){
                segment_flux(k, j)  = F[k];
                segment_accum(k, j) = G[k];
            }
        
    }
    
    return true;
}

void Extension_Curve::curve(const FluxFunction *f, const AccumulationFunction *a, 
                            GridValues &g, int where_is_characteristic,
                            bool is_singular, int fam,
                            std::vector<RealVector> &original_curve,
                            std::vector<RealVector> &extension_on_curve,
                            std::vector<RealVector> &extension_on_domain){
                            
    ff = f;
    aa = a;
    
    family = fam;


    characteristic_where = where_is_characteristic;
    singular = is_singular;

    gv = &g;
    oc = &original_curve;
    
    gv->fill_eigenpairs_on_grid(ff, aa);
    
    extension_on_curve.clear(); 
    extension_on_domain.clear(); 

    Contour2p5_Method::contour2p5(this, extension_on_curve, extension_on_domain);
           
    return;
}
