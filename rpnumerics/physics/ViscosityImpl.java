/**
 * @(#) ViscosityImpl.java
 */

package rpnumerics.physics;

import wave.util.RealVector;
import wave.util.RealMatrix2;

public abstract interface ViscosityImpl
{
	RealVector conservedQuantity( RealVector U );
	
	RealMatrix2 viscosity( RealVector U );
	
	
}
