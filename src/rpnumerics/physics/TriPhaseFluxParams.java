/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class TriPhaseFluxParams extends FluxParams {

    //
    // Constants
    //
    static public final double[] DEFAULT_FLUX_PARAMS = {1d,1d/3d,1d/3d,1d/3d,0d,0d,0d};
    //
    // Members
    //
    private double vel_;
    private double muw_, muo_, mug_;
    private double grw_, gro_, grg_;

    //
    // Constructors
    //
    public TriPhaseFluxParams() {
        this(new RealVector(DEFAULT_FLUX_PARAMS),0);
    }

    public TriPhaseFluxParams(TriPhaseFluxParams copy) {
        super(copy);
    }

    public TriPhaseFluxParams(RealVector params,int index) {
        super(TriPhase.FLUX_ID, params,index);
    }

    //
    // Accessors/Mutators
    //
    public double vel() { return getParams().getElement(0); }

    public double muw() { return getParams().getElement(1); }

    public double muo() { return getParams().getElement(2); }

    public double mug() { return getParams().getElement(3); }

    public double grw() { return getParams().getElement(4); }

    public double gro() { return getParams().getElement(5); }

    public double grg() { return getParams().getElement(6); }

    //
    // Methods
    //
    public FluxParams defaultParams() { return new TriPhaseFluxParams(); }
}
