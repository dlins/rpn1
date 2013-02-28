package rpnumerics.methods.contour.markedhypercubes;

import java.util.List;
import rpnumerics.RPnCurve;
import wave.multid.model.*;
import wave.util.*;

public class BoundedRPNCurve extends RPnCurve {

	public BoundedRPNCurve(RPnCurve curve, double error) {
		super(fromRPNCurveToBounded(curve, error), curve.getViewAttr());
	}	
	
	private static PointNDimension[][] fromRPNCurveToBounded(RPnCurve curve, double tolerance) {
		
		PointNDimension[][] polyline = curve.getPolylines();
		
		for(int pont_polyline = 0; pont_polyline < polyline.length; pont_polyline++) {
			for(int pont_point = 0; pont_point < polyline[pont_polyline].length; pont_point++) {
				polyline[pont_polyline][pont_point] = new BoundedPointNDimension(polyline[pont_polyline][pont_point], tolerance);
			}
		}
		
		return polyline;
	}

    @Override
    public List segments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
