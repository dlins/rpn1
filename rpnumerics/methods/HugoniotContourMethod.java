/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.methods;


import rpnumerics.methods.contour.*;
import rpnumerics.PhasePoint;
import wave.util.RealVector;
import rpnumerics.HugoniotCurve;
import rpnumerics.GenericHugoniotFunction;
import rpnumerics.HugoniotParams;
import java.util.Vector;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolyLine;
import wave.util.RealSegment;
import wave.multid.view.ViewingAttr;
import java.awt.Color;
import wave.util.PointNDimension;
import rpnumerics.RPnCurve;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.*;

public class HugoniotContourMethod  extends HugoniotMethod {

  private ContourND contourND_;

  private HugoniotContourParams contourParams_;
  
  private HyperCubeErrorTreatmentBehavior treatment = new TestHyperCubeErrorTreatment(); 

  public HugoniotContourMethod(HugoniotContourParams params) {

    contourParams_ =  params;
    
    CubeFunction[] function = contourParams_.getFunctions();
	
    contourND_ = this.setContour(function[0], treatment);
    
  }
  
  public HugoniotCurve curve(RealVector initialPoint) {

	  Vector segend = new Vector();
	  
	  RPnCurve rpncurve = null;

	  try {
		  ((ContourHugoniot) contourND_).setInitialPoint(new PointNDimension(initialPoint));
		  rpncurve = contourND_.curvND(contourParams_.getBoundary(), contourParams_.getResolution());
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

  protected ContourND getContour() {
	  return this.contourND_;
  }
  
  protected HyperCubeErrorTreatmentBehavior getTreatment() {
	  return this.treatment;
  }
  
  protected ContourND setContour(CubeFunction function, HyperCubeErrorTreatmentBehavior treatment) {
	  return new ContourHugoniot((HugoniotFunction) function, treatment);    
  }
  
  private MultiPolyLine[] vectorNDToMultiPolyLine(Vector segend_) {
    MultiPolyLine[] polies = null;
    ViewingAttr viewAttr = new ViewingAttr(Color.yellow, true);
    int size = segend_.size();
    if (size != 0) {
      // por enquanto
      if (size >= 5000) {
        size = 5000;
      }
      int dimension = ( (RealSegment) segend_.firstElement()).p1().getSize();
      polies = new MultiPolyLine[size];
      for (int pont_segend_ = 0; pont_segend_ < size; pont_segend_++) {
        RealSegment v = (RealSegment) segend_.elementAt(pont_segend_);

        double[] vertice1 = new double[dimension];
        double[] vertice2 = new double[dimension];

        for (int pont_coord = 1; pont_coord <= dimension; pont_coord++) {
          vertice1[pont_coord - 1] = v.p1().getElement(pont_coord - 1);
          vertice2[pont_coord - 1] = v.p2().getElement(pont_coord - 1);
        }

        CoordsArray[] vertice = new CoordsArray[2];
        vertice[0] = new CoordsArray(vertice1);
        vertice[1] = new CoordsArray(vertice2);
        CoordsArray[] segment = {
            vertice[0], vertice[1]};
        polies[pont_segend_] = new MultiPolyLine(segment, viewAttr);
      }
    }
    return polies;
  }

public HugoniotContourParams getParams(){return contourParams_;}


}
