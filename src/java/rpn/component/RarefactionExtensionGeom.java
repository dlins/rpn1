package rpn.component;

import java.awt.Color;
import wave.multid.*;
import wave.multid.view.*;

public class RarefactionExtensionGeom  extends SegmentedCurveGeom {
    

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);
    
    public RarefactionExtensionGeom(HugoniotSegGeom[] segArray, RarefactionExtensionGeomFactory factory) {

        super(segArray,factory);

    }
    
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionExtensionCurveView(this, transf, viewingAttr());
        
    }
    
    
    
}
