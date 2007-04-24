package rpnumerics.methods;

import rpnumerics.methods.contour.*;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.HugoniotFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;


public class HugoniotContourTriangleDomainMethod extends HugoniotContourMethod {
	
	public HugoniotContourTriangleDomainMethod(HugoniotContourParams params) {
		super(params);		
	}

	protected ContourND setContour(HugoniotFunction function, HyperCubeErrorTreatmentBehavior treatment) {
		  return new ContourHugoniotTriangleDomain2D(function, treatment);    
	}
}
