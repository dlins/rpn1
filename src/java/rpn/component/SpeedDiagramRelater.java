/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.RpException;
import rpnumerics.WaveCurveBranch;
import wave.util.RealVector;

public class SpeedDiagramRelater extends DiagramRelater {

   
    @Override
    public double[] getPointToPhaseSpace(DiagramGeom diagram, double x, double[] yOnDiagram) {

        RpDiagramFactory factory = (RpDiagramFactory) diagram.geomFactory();

        WaveCurveBranch waveCurve = (WaveCurveBranch) factory.rpDiagramCalc();
        
        double [] result=null;
        try {
            result = waveCurve.getCoordByArcLength(x);
        } catch (RpException ex) {
            Logger.getLogger(SpeedDiagramRelater.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;

    }

    @Override
    public RealVector getPointToDiagram() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
