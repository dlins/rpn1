/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class FluxFunction extends NativeRpFunction {
//	HessianMatrix D2F( RealVector U );
//	
//	RealMatrix2 DF( RealVector U );
//	
//	RealVector F( RealVector U );
    public FluxParams fluxParams() {
        return new FluxParams("Quad2", new RealVector(2), 1);
    }
}  
