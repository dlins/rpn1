/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.methods;

import java.util.*;

import rpn.component.MultidAdapter;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotParams;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.methods.contour.ContourHugoniot;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import wave.multid.CoordsArray;
import wave.util.Boundary;
import wave.util.PointNDimension;
import wave.util.RealSegment;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class HugoniotContourMethod extends HugoniotMethod {

    private ContourHugoniot contourMethod_;
    private HugoniotParams hugoniotParams_;
    private double[] boundaryArray_;
    private int[] resolution_;

    public HugoniotContourMethod(HugoniotParams hugoniotParams) {

        contourMethod_ = ContourFactory.createContourHugoniot(hugoniotParams);
        resolution_ = new int[2];
        resolution_[0] = new Integer(RPNUMERICS.getConfiguration("Contour").getParam("x-resolution"));
        resolution_[1] = new Integer(RPNUMERICS.getConfiguration("Contour").getParam("y-resolution"));


        Boundary boundary = RPNUMERICS.boundary();

        if (boundary instanceof RectBoundary) {

            boundaryArray_ = new double[4];

            RealVector minimums = boundary.getMinimums();
            RealVector maximums = boundary.getMaximums();

            double[] minimumsArray = minimums.toDouble();
            double[] maximumsArray = maximums.toDouble();

            boundaryArray_[0] = minimumsArray[0];
            boundaryArray_[1] = maximumsArray[0];
            boundaryArray_[2] = minimumsArray[1];
            boundaryArray_[3] = maximumsArray[1];
        } else {

            System.out.println("Implementar para dominio triangular");

        }

        hugoniotParams_ = hugoniotParams;
    }

    public HugoniotCurve curve(RealVector initialPoint) {

        HugoniotCurve hugoniotCurve = null;
        try {
            contourMethod_.setInitialPoint(new PointNDimension(RPNUMERICS.getViscousProfileData().getXZero()));

            RPnCurve curve = contourMethod_.curvND(boundaryArray_, resolution_);

            PointNDimension[][] polyline = curve.getPolylines();

            List realSegments = new ArrayList();
            
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
            
            hugoniotCurve = new HugoniotCurve(hugoniotParams_.getXZero(), realSegments);

        } catch (CanNotPerformCalculations ex) {
            ex.printStackTrace();
        }

        return hugoniotCurve;

    }

    public HugoniotParams getParams() {
        return hugoniotParams_;

    }
}
