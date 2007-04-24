/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class CombFluxParams extends FluxParams {
    //
    // Constants
    //
    static public final double[] DEFAULT_FLUX_PARAMS = {6.13E-4,0.3,0.214,1.0121,0.,205.8,68.19,0.027,23.69};
    //
    // Members
    //
    private double a_, phi_, Le_, q_, h_, mu_, mug_, alpha_, gamma_;

    //
    // Constructors
    //
    public CombFluxParams() {
        this(new RealVector(DEFAULT_FLUX_PARAMS),0);
    }

    public CombFluxParams(CombFluxParams copy) {
        super(copy);
    }

    public CombFluxParams(RealVector params,int index) {
        super(Comb.FLUX_ID, params,index);
    }

    //
    // Accessors/Mutators
    //

    public double a() { return getParams().getElement(0); }

    public double phi() { return getParams().getElement(1); }

    public double Le() { return getParams().getElement(2); }

    public double q() { return getParams().getElement(3); }

    public double h() { return getParams().getElement(4); }

    public double mu() { return getParams().getElement(5); }

    public double mug() { return getParams().getElement(6); }

    public double alpha() { return getParams().getElement(7); }

    public double gamma() { return getParams().getElement(8); }

    //
    // Methods
    //
    public FluxParams defaultParams() { return new CombFluxParams(); }
}
