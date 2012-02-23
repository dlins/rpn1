package rpn.component;

import java.awt.Color;
import wave.multid.*;
import wave.multid.view.*;

public class RarefactionExtensionGeom  extends BifurcationCurveGeom {
    

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);
    
    public RarefactionExtensionGeom(RealSegGeom[] segArray, RarefactionExtensionGeomFactory factory) {

        super(segArray,factory);

    }
    
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionExtensionCurveView(this, transf, viewingAttr());
        
    }
    
    
    
}
