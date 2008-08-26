/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

public class ViscosityParams
{

	//
	// Constants
	//
	public static final double DEFAULT_EPSL = 0.1;
	

	//
	// Members
	//
	private double epsl_;
	
	
	//
	// Constructors
	//
	public ViscosityParams( double eps )
	{
		epsl_ = eps;
	}

	//
	// Accessors/Mutators
	//
	public double epsl( ) { return epsl_; }
}
