/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import wave.util.RealVector;

/**
 *
 * @author edsonlan
 */
class DefaultDiagramRelater extends DiagramRelater {

    public DefaultDiagramRelater() {

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
