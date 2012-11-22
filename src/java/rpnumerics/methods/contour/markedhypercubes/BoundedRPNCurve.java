package rpnumerics.methods.contour.markedhypercubes;

import java.util.List;
import rpnumerics.RpCurve;
import wave.multid.model.*;
import wave.util.*;

public class BoundedRPNCurve extends RpCurve {

	public BoundedRPNCurve(RpCurve curve, double error) {
		super(fromRPNCurveToBounded(curve, error), curve.getViewAttr());
	}	
	
	private static PointNDimension[][] fromRPNCurveToBounded(RpCurve curve, double tolerance) {
		
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
