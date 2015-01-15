  /*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.io.FileWriter;
import java.io.IOException;
import rpn.configuration.Configuration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wave.util.RealSegment;
import wave.util.RealVector;

public class RiemannProfileCalc implements RpCalculation, RpDiagramCalc {
    //
    // Constants
    //
    //
    // Members
    //

    private Area area_;
    private Configuration configuration_;
    private int[] waveCurvesID_;
    private RealVector pointOnSecondWaveCurve_;
    private RealVector referencePointOfFirstWaveCurve_;

    int firstWaveCurveID_;
    int secondWaveCurveID_;

    private final boolean isAllProfile_;

    //
    // Constructors/Initializers
    //
    public RiemannProfileCalc(WaveCurve firstWaveCurve, WaveCurve secondWaveCurve, RealVector pointOnSecondWaveCurve) {

        firstWaveCurveID_ = firstWaveCurve.getId();
        secondWaveCurveID_ = secondWaveCurve.getId();

        pointOnSecondWaveCurve_ = pointOnSecondWaveCurve;
        referencePointOfFirstWaveCurve_ = firstWaveCurve.getReferencePoint().getCoords();

        isAllProfile_ = true;

    }

    public RiemannProfileCalc(Area selectedArea, int[] waveCurvesID) {
        area_ = selectedArea;
        waveCurvesID_ = waveCurvesID;

        isAllProfile_ = false;
    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public Configuration getConfiguration() {
        return configuration_;
    }

    public RpSolution calc() throws RpException {

        
       
        RpSolution result = null;

        if (isAllProfile_) {

            result = nativeAllProfileCalc(firstWaveCurveID_, secondWaveCurveID_, referencePointOfFirstWaveCurve_, pointOnSecondWaveCurve_);

        } else {
            RealVector pmin = area_.getDownLeft();
            RealVector pmax = area_.getTopRight();

            result = nativeCalc(pmin, pmax, waveCurvesID_);

        }

        if (result == null) {
            throw new RpException("Intersection not found");
        }

        Diagram riemannProfile = (Diagram) result;
        
        System.err.println("Ponto R: "+riemannProfile.getInfo());

        if (!isRiemannSpeedLimitsGood(riemannProfile)) {
            throw new RpException("Wrong speed limits");
        }

        addSteadyState(riemannProfile);
        
        

        System.out.println("Saida na forma de texto linha:  ");
        
//        System.out.println(riemannProfile.toMatlab());
            
        try {
            FileWriter fWriter = new FileWriter("ArquivoRTeste");
            
            fWriter.write(riemannProfile.toMatlab());
            
            fWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(RiemannProfileCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        

        return riemannProfile;
    }

    private native RpSolution nativeCalc(RealVector pmin, RealVector pmax, int[] waveCurveIdArray) throws RpException;

    private native RpSolution nativeAllProfileCalc(int firstWaveCurveID, int secondWaveCurveID, RealVector firstWaveCurveRefPoint, RealVector pointOnSecondWaveCurve) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RpSolution createDiagramSource() throws RpException {

        return calc();

    }

    @Override
    public RpSolution updateDiagramSource() throws RpException {

        return calc();

    }

//    private void addSteadyState(Diagram riemannProfile) {
//
//        String Xlimits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");
//
//        double xMin = Double.parseDouble(Xlimits[0]);
//
//        double xMax = Double.parseDouble(Xlimits[1]);
//
//        List<DiagramLine> lines = riemannProfile.getLines();
//
//        for (int i=0 ; i < lines.size();i++) {
//
//            RealSegment firstSegment = lines.get(i).getSegments().get(0);
//
//            RealVector leftPoint = new RealVector(2);
//
//            leftPoint.setElement(0, xMin);
//            
//            leftPoint.setElement(1, firstSegment.p1().getElement(1));
//            
//            lines.get(i).addCoord(0, 0, leftPoint);
//            
//            List<RealVector> lineList = lines.get(i).getCoords().get(0);
//            RealVector lastPoint = lineList.get(lineList.size()-1);
//            RealVector rightPoint = new RealVector(2);
//
//            rightPoint.setElement(0, xMax);
//
//            rightPoint.setElement(1, lastPoint.getElement(1));
//            
//            lines.get(i).addCoord(0, lineList.size()-1, lastPoint);
//              
//            lines.get(i).addCoord(0, lineList.size()-1, rightPoint);
//        }
//
//    }
//    
    
    
    private void addSteadyState(Diagram riemannProfile) {

        String Xlimits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");

        double xMin = Double.parseDouble(Xlimits[0]);

        double xMax = Double.parseDouble(Xlimits[1]);

        List<DiagramLine> lines = riemannProfile.getLines();

        for (int i=0 ; i < lines.size();i++) {

            
           
            
            RealSegment firstSegment = lines.get(i).getSegments().get(0);

            RealVector leftPoint = new RealVector(2);

            leftPoint.setElement(0, xMin);
            
            leftPoint.setElement(1, firstSegment.p1().getElement(1));
            
            
            lines.get(i).setFirstCoord(leftPoint);
            
            
            RealSegment lastSegment = lines.get(i).getSegments().get(lines.get(i).getSegments().size()-1);
            
            
//            lines.get(i).addCoord(0, 0, leftPoint);
//            
//            List<RealVector> lineList = lines.get(i).getCoords().get(0);
//            RealVector lastPoint = lineList.get(lineList.size()-1);
            RealVector rightPoint = new RealVector(2);

            rightPoint.setElement(0, xMax);

            rightPoint.setElement(1, lastSegment.p2().getElement(1));
            
            
            lines.get(i).setLastCoord(rightPoint);
            
//            lines.get(i).addCoord(0, lineList.size()-1, lastPoint);
//              
//            lines.get(i).addCoord(0, lineList.size()-1, rightPoint);
        }

    }
    
    
    

    private boolean isRiemannSpeedLimitsGood(Diagram riemannProfile) {

        String Xlimits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");

        double xMin = Double.parseDouble(Xlimits[0]);

        double xMax = Double.parseDouble(Xlimits[1]);

        List<DiagramLine> lines = riemannProfile.getLines();

        for (DiagramLine diagramLine : lines) {

            RealVector lineMin = diagramLine.getMin();
            RealVector lineMax = diagramLine.getMax();

            if (lineMin.getElement(0) < xMin) {
                return false;
            }

            if (lineMax.getElement(0) > xMax) {
                return false;

            }

        }

        return true;
    }

    @Override
    public void setReferencePoint(OrbitPoint referencePoint) {
       
    }

}
