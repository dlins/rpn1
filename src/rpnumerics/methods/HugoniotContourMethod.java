/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.methods;

import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.component.MultidAdapter;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotParams;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.methods.contour.ContourHugoniot;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import wave.util.Boundary;
import wave.util.PointNDimension;
import wave.util.RealVector;

public class HugoniotContourMethod extends HugoniotMethod {

    private ContourHugoniot contourMethod_;
    private HugoniotParams hugoniotParams_;
    private  double[] boundaryArray_;
    private int[] resolution_;
    
    
    public HugoniotContourMethod(ContourParams contourParams, HugoniotParams hugoniotParams) {
        
        contourMethod_=(ContourHugoniot) contourParams.getContour();
        resolution_ = contourParams.getResolution();
        boundaryArray_=contourParams.getBoundary();
        hugoniotParams_=hugoniotParams;
    }
    

    public HugoniotContourMethod(ContourHugoniot contourMethod, HugoniotParams hugoniotParams) {
        contourMethod_ = contourMethod;
        hugoniotParams_ = hugoniotParams;
    }

//    @Override
//    public HugoniotCurve curve(RealVector initialPoint) {
//
//        HugoniotCurve hugoniotCurve = null;
//        try {
//
//            Boundary boundary = RPNUMERICS.boundary();
//            double[] boundaryArray = new double[4];
//
//            RealVector minimums = boundary.getMinimums();
//            RealVector maximums = boundary.getMaximums();
//
//            double[] minimumsArray = minimums.toDouble();
//            double[] maximumsArray = maximums.toDouble();
//
//            boundaryArray[0] = minimumsArray[0];
//            boundaryArray[1] = maximumsArray[0];
//            boundaryArray[2] = minimumsArray[1];
//            boundaryArray[3] = maximumsArray[1];
//
//            int[] res = new int[2];
//
//            res[0] = 100;
//            res[1] = 100;
//            
//            contourMethod_.setInitialPoint(new PointNDimension(hugoniotParams_.getXZero()));
//            RPnCurve curve = contourMethod_.curvND(boundaryArray, res);
//            
//            hugoniotCurve = new HugoniotCurve(hugoniotParams_.getXZero(), MultidAdapter.converseRPnCurveToCoordsArray(curve));
//
//        } catch (CanNotPerformCalculations ex) {
//            ex.printStackTrace();
//        }
//
//
//        return hugoniotCurve;
//
//    }
    
     public HugoniotCurve curve(RealVector initialPoint) {

        HugoniotCurve hugoniotCurve = null;
        try {
            contourMethod_.setInitialPoint(new PointNDimension(hugoniotParams_.getXZero()));
            RPnCurve curve = contourMethod_.curvND(boundaryArray_, resolution_);
            
            hugoniotCurve = new HugoniotCurve(hugoniotParams_.getXZero(), MultidAdapter.converseRPnCurveToCoordsArray(curve));

        } catch (CanNotPerformCalculations ex) {
            ex.printStackTrace();
        }

        return hugoniotCurve;

    }
    
    
    

    public HugoniotParams getParams() {
        return hugoniotParams_;

    }
}
