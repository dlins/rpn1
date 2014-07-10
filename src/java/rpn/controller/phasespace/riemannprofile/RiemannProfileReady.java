/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.phasespace.riemannprofile;

import rpn.component.DiagramGeom;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;

/**
 *
 * @author edsonlan
 */
public interface RiemannProfileReady extends RiemannProfileState{

    public WaveCurveGeom getFirstWaveCurve();

    public WaveCurveGeom getSecondWaveCurve();
    
    public GraphicsUtil getSelection();
    
    
    public DiagramGeom calcProfile();
    

}
