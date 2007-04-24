package rpnumerics.methods;

import java.util.Vector;

import rpnumerics.HugoniotCurve;
import rpnumerics.RPnCurve;
import rpnumerics.methods.contour.*;
import rpnumerics.methods.contour.ContourND;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.markedhypercubes.TestHyperCubeErrorTreatment;
import wave.util.RealSegment;
import wave.util.RealVector;

public class BifurcationContourMethod extends HugoniotContourMethod {

	private HyperCubeErrorTreatmentBehavior treatment = new TestHyperCubeErrorTreatment();
		
	public BifurcationContourMethod(HugoniotContourParams params) {
		super(params);
	}

	protected ContourND setContour(CubeFunction function, HyperCubeErrorTreatmentBehavior treatment) {
		this.treatment = treatment;
		return new ContourBifurcation((MultiBifurcationFunction) function, treatment);   
		
	}
	
	public HugoniotCurve curve(RealVector initialPoint) {

		  Vector segend = new Vector();
		  
		  RPnCurve rpncurve = null;

		  try {
			  rpncurve = this.getContour().curvND(this.getParams().getBoundary(), this.getParams().getResolution());
		  } catch (CanNotPerformCalculations e) {
			  e.printStackTrace();
		  }

		 if (treatment.isThereMarkedHyperCubes()) {
			 treatment.action();
		 } else {
			 System.out.println("nao achou nada...");
		 }
		 	 
		 //((rpnumerics.methods.contour.markedhypercubes.ContourNDSegmentedHypercubes) contourND_).actionOnMarkedHyperCubes();

		  wave.util.PointNDimension[][] polyline = rpncurve.getPolylines();

			for (int pont_polyline = 0; pont_polyline < polyline.length; pont_polyline++) {
				wave.util.PointNDimension previous =  polyline[pont_polyline][0];
				for(int pont_point = 1; pont_point < polyline[pont_polyline].length; pont_point++) {
					wave.util.PointNDimension next = polyline[pont_polyline][pont_point];

					RealVector p1 = previous.toRealVector();
					RealVector p2 = next.toRealVector();

					segend.add(new RealSegment(p1, p2));

					wave.util.PointNDimension tmp = previous;
					previous = next;
					next = tmp;
				}
			}

	    return new HugoniotCurve (rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus(),segend);

	  }
}
