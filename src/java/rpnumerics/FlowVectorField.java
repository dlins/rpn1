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

    public void setWaveFlow(WaveFlow flow){
        wave_=flow;
    }
    
    //
    // Methods
    //
    public WavePoint f(RealVector y) {

        WavePoint point = new WavePoint( wave_.flux(y), 0);
        return point;

    }
}
