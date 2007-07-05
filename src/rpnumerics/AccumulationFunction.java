/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.HessianMatrix;

public  interface AccumulationFunction
{
	HessianMatrix D2H( RealVector U );

	RealMatrix2 DH( RealVector U );

	RealVector H( RealVector U );

//	AccumulationParams accumulationParams( );


}
