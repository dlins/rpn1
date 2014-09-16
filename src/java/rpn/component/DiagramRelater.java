

package rpn.component;

import wave.util.RealVector;


public abstract class  DiagramRelater {
    
    public abstract double [] getPointToPhaseSpace (DiagramGeom diagram, double x,double [] yOnDiagram);
    
    public abstract RealVector getPointToDiagram ();
    
}
