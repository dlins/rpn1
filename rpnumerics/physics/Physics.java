/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.multid.Space;
import rpnumerics.physics.FluxFunction;
import wave.util.Boundary;

public  interface Physics
{
	Boundary boundary( );

        void setBoundary (Boundary boundary);

	Space domain( );

	FluxFunction fluxFunction( );

        FluxFunction [] fluxFunctionArray();

        FluxFunction fluxFunction(int index);

        AccumulationFunction accumulationFunction();

	String ID( );


}
