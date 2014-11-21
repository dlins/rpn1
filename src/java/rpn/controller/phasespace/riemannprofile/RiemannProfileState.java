package rpn.controller.phasespace.riemannprofile;


import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;



public interface  RiemannProfileState  {
    

    public void add(WaveCurveGeom geom);

    
    public void remove(RpGeometry geom);


    public void select(GraphicsUtil area) ;
}
