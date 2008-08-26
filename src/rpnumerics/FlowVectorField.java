/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 */

package rpnumerics;

import wave.util.VectorField;
import wave.util.RealVector;

/*
 * adapter class to have a Flux VectorField
 *
 */

public class FlowVectorField implements VectorField {
    //
    // Members
    //
    private WaveFlow wave_;

    //
    // Constructors
    //
    public FlowVectorField(WaveFlow wave) {
        wave_ = wave;
    }

    //
    // Methods
    //
    public WavePoint f(RealVector y) {
        return wave_.flux(y);
    }
}
