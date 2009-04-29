package rpnumerics.methods;

import java.util.ArrayList;
import java.util.List;

import rpnumerics.*;
import rpnumerics.methods.contour.*;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import wave.multid.CoordsArray;
import wave.util.*;

public class BifurcationContourMethod extends BifurcationMethod {

	private BifurcationParams params_;
	private ContourBifurcation contourMethod_;   
    private double[] boundaryArray_;
    private int[] resolution_;
    private int familyIndex;
	
	public BifurcationContourMethod(BifurcationParams params) {
		super();
		
        contourMethod_ = ContourFactory.createContourBifurcation(params);
        resolution_ = RPNUMERICS.getContourResolution();
        familyIndex = params.getFamilyIndex();

        Boundary boundary = RPNUMERICS.boundary();

        if (boundary instanceof RectBoundary) {
        	
        	int dim = RPNUMERICS.domainDim();
        	
            boundaryArray_ = new double[2*dim];

            RealVector minimums = boundary.getMinimums();
            RealVector maximums = boundary.getMaximums();

            double[] minimumsArray = minimums.toDouble();
            double[] maximumsArray = maximums.toDouble();

            for(int pont_dim = 0; pont_dim < dim; pont_dim++) {
            	boundaryArray_[2*pont_dim] = minimumsArray[pont_dim];
                boundaryArray_[2*pont_dim + 1] = maximumsArray[pont_dim];                	
            }            
            
        } else {

            System.out.println("Implementar para dominio triangular");

        }
		
		this.params_ = params;
	}

	public BifurcationCurve curve() {
			
		BifurcationCurve bifurcationCurve = null;
		
		try {
		
			RPnCurve curve = contourMethod_.curvND(boundaryArray_, resolution_);
	
	        PointNDimension[][] polyline = curve.getPolylines();
	
	        ArrayList realSegments = new ArrayList();
	        
	        RealVector p1 = null;
	        RealVector p2 = null;
	                    
	        for (int polyLineIndex = 0; polyLineIndex < polyline.length; polyLineIndex++) {
	        	            	
	            int size = polyline[polyLineIndex].length;
	            
	            CoordsArray[] coords = new CoordsArray[size];
	
	            for (int polyPoint = 0; polyPoint < size; polyPoint++) {                	
	            	coords[polyPoint] = polyline[polyLineIndex][polyPoint].toCoordsArray();                   
	                
	            }
	           
	            for (int i = 0; i < coords.length - 2; i++) {           	
	            	
	                p1 = new RealVector(coords[i].getCoords());
	                p2 = new RealVector(coords[i + 1].getCoords());
	
	                realSegments.add(new RealSegment(p1, p2));
	            }
	
	            p1 = new RealVector(coords[coords.length - 2].getCoords());
	            p2 = new RealVector(coords[coords.length - 1].getCoords());
	            realSegments.add(new RealSegment(p1, p2));
	        
	        }            
	        
	        bifurcationCurve = new BifurcationCurve(familyIndex, realSegments);
	
	    } catch (CanNotPerformCalculations ex) {
	        ex.printStackTrace();
	    }
	
	    return bifurcationCurve;
	}
	
	public BifurcationParams getParams() {
        return params_;
    }

}
