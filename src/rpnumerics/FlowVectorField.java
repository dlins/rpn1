/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 */

package rpnumerics;

import wave.util.JetMatrix;
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

        WaveState input = new WaveState(new PhasePoint(y));
        JetMatrix output = new JetMatrix(y.getSize());
        wave_.jet(input, output,0);
        WavePoint point = new WavePoint(output.f(), 0);
        
        return point;
//        return wave_.flux(y);
    }
}
