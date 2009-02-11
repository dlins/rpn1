package rpn.component;

import java.awt.Color;
import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class RarefactionGeom  extends MultiPolyLine implements RpGeometry {
    
    private RpGeomFactory factory_;
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);
    
    public RarefactionGeom(CoordsArray[] vertices, RarefactionOrbitGeomFactory factory) {
        super(vertices, VIEWING_ATTR);
        factory_=factory;
    }
    
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
//        return new OrbitGeomView(this,transf,viewingAttr());
        return new RarefactionOrbitView(this, transf, viewingAttr());
        
    }
    
    public RpGeomFactory geomFactory() {
        return factory_;
    }
    
    
}
