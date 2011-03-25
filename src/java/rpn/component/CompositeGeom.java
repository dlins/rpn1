package rpn.component;

import java.awt.Color;
import wave.multid.*;
import wave.multid.view.*;

public class CompositeGeom  extends SegmentedCurveGeom {
    

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);
    
    public CompositeGeom(HugoniotSegGeom[] segArray, CompositeGeomFactory factory) {

        super(segArray,factory);

    }
    
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CompositeOrbitView(this, transf, viewingAttr());
        
    }
    
    
    
}
