package rpnumerics.methods;

import rpnumerics.*;

public abstract class BifurcationMethod implements RPMethod {

	public BifurcationMethod() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public abstract BifurcationCurve curve();

}
