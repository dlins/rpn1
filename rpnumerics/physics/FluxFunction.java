/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.HessianMatrix;

public  interface FluxFunction
{
	HessianMatrix D2F( RealVector U );
	
	RealMatrix2 DF( RealVector U );
	
	RealVector F( RealVector U );
	
	FluxParams fluxParams( );
	
	
}
