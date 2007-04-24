/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

public class CapilParams
{

	//
	// Constants
	//
	public static double DEFAULT_ACG = 1.0;
	public static double DEFAULT_ACW = 1.0;
	public static double DEFAULT_LCG = 0.5;
	public static double DEFAULT_LCW = 0.5;


	//
	// Members
	//
	private double acg_;
	private double acw_;
	private double lcg_;
	private double lcw_;
	

	//
	// Constructors
	//
	public CapilParams( double acw, double acg, double lcw, double lcg )
	{
		acw_ = acw;
		acg_ = acg;
		lcw_ = lcw;
		lcg_ = lcg;
	}

	//
	// Accessors/Mutators
	//
	public double acg( ) { return acg_; }
	public double acw( ) { return acw_; }
	public double lcg( ) { return lcg_; }
	public double lcw( ) { return lcw_; }

	public void reset( ) {
		acw_ = DEFAULT_ACW;
		acg_ = DEFAULT_ACG;
		lcw_ = DEFAULT_LCW;
		lcg_ = DEFAULT_LCG;
	}
}
