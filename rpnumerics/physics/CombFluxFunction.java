/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class CombFluxFunction implements FluxFunction
{
    private CombFluxParams fluxParams_;
    
    public CombFluxFunction( CombFluxParams params )
    {
	fluxParams_ = params;
    }
	

    public HessianMatrix D2F( RealVector U )
    {
	return null;
    }
	
    public RealMatrix2 DF( RealVector U )
    {
	return null;
    }
    
    public RealVector F( RealVector U )
    {
	return null;
    }


    public FluxParams fluxParams(){return fluxParams_;}




}
