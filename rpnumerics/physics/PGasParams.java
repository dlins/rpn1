/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class PGasParams extends FluxParams {
    //
    // Constants
    //
    static public final double[] DEFAULT_FLUX_PARAMS = {0d,0d,-2d,0d,2d,0d};

    //
    // Constructors
    //
    public PGasParams(PGasParams copy) {
        super(copy);
    }

    public PGasParams() {
        this(new RealVector(DEFAULT_FLUX_PARAMS),0);
    }

    public PGasParams(RealVector params,int index) {
        super(PGas.FLUX_ID, params,index);
    }

    //
    // Accessors/Mutators
    //
    public FluxParams defaultParams() { return new PGasParams(); }

    public double alpha() { return getElement(0); }

    public double beta() { return getElement(1); }

    public double coefA3() { return getElement(2); }

    public double coefA2() { return getElement(3); }

    public double coefA1() { return getElement(4); }

    public double coefA0() { return getElement(5); }

    //
    // Methods
    //
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(alpha() + " ");
        buf.append(beta() + " ");
        buf.append(coefA3() + " ");
        buf.append(coefA2() + " ");
        buf.append(coefA1() + " ");
        buf.append(coefA0() + " ");
        return buf.toString();
    }
}
