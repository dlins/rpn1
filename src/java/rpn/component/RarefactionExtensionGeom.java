package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class RarefactionExtensionGeom   extends SegmentedCurveGeom {
    

    
    public RarefactionExtensionGeom(RealSegGeom[] segArray, RarefactionExtensionGeomFactory factory) {

        super(segArray,factory);

    }
    
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionExtensionCurveView(this, transf, viewingAttr());
        
    }

    
    
}
