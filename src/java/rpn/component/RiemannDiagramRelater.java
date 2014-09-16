/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

/**
 *
 * @author edsonlan
 */
public class RiemannDiagramRelater extends DiagramRelater {

    public RiemannDiagramRelater(ViewingTransform transform) {
      
    }
    
    @Override
    public double [] getPointToPhaseSpace(DiagramGeom diagram, double x,double [] yOnDiagram) {
        
       return yOnDiagram; 
       
    }

    @Override
    public RealVector getPointToDiagram() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
