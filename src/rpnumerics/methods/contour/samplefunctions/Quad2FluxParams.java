/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.physics;

import rpnumerics.FluxParams;
import wave.util.RealVector;


public class Quad2FluxParams extends FluxParams {

    public Quad2FluxParams(FluxParams params) {
        super(params);
    }

    public Quad2FluxParams(){
        super(new RealVector(14));
    }

    
}
