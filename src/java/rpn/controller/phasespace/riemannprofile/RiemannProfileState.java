package rpn.controller.phasespace.riemannprofile;


import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;



public interface  RiemannProfileState  {
    

    public void add(RpGeometry geom);
    
//    public void add(RpGeometry geom) {
//        
//        if (geom instanceof WaveCurveGeom) {
//
//            firstWaveCurve_ = (WaveCurveGeom) geom;
//            
//            WaveCurve firstWaveCurveSource = (WaveCurve) firstWaveCurve_.geomFactory().geomSource();
//            
//            if(firstWaveCurveSource.getDirection()==Orbit.FORWARD_DIR && firstWaveCurveSource.getFamily()==0){
//
//                FirstWaveCurveReady firstWaveCurveState = new FirstWaveCurveReady(firstWaveCurve_);
////                RPnDataModule.RIEMANNPHASESPACE.changeState(firstWaveCurveState);
//                
//            }
//        }
//
//    }


    
    public void remove(RpGeometry geom);


    public void select(GraphicsUtil area) ;
}
