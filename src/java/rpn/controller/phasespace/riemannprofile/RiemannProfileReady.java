/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.phasespace.riemannprofile;

import rpn.component.DiagramGeom;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;

public interface RiemannProfileReady extends RiemannProfileState{

    public WaveCurveGeom getFirstWaveCurve();

    public WaveCurveGeom getSecondWaveCurve();
    
    public GraphicsUtil getSelection();
    
    
    public DiagramGeom calcProfile();
    
    
    public void updateRiemannProfile();
    

}
