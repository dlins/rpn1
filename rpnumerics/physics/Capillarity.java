/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.physics;

import wave.util.RealMatrix2;
import wave.util.RealVector;

public class Capillarity {

    //
    // Members
    //
    private CapilParams params_;

    //
    // Constructors
    //
    public Capillarity(CapilParams params) {
        params_ = params;
    }

    //
    // Methods
    //
    public RealMatrix2 jacobian(RealVector U) {
        double sw = U.getElement(0);
        double so = U.getElement(1);
        double sg = 1. - sw - so;
        RealMatrix2 capillarity_jacobian = new RealMatrix2(2, 2);
        capillarity_jacobian.setElement(0, 0, -dpcowdsw(sw) + dpcogdsw(sg));
        capillarity_jacobian.setElement(0, 1, dpcogdso(sg));
        capillarity_jacobian.setElement(1, 0, dpcogdsw(sg));
        capillarity_jacobian.setElement(1, 1, dpcogdso(sg));
        return capillarity_jacobian;
    }

    protected double dpcowdsw(double sw) {
        return -params_.acw() * (params_.lcw() + (1. - params_.lcw()) * 2. * (1. - sw));
    }

    protected double dpcogdsw(double sg) {
        // finds DPcog(1-sw-so)/Dsw
        return params_.acg() * (params_.lcg() + (1. - params_.lcg()) * 2. * (1. - sg));
    }

    protected double dpcogdso(double sg) {
        // finds DPcog(1-sw-so)/Dso
        return params_.acg() * (params_.lcg() + (1. - params_.lcg()) * 2. * (1. - sg));
    }
}
