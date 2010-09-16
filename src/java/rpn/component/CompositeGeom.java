package rpn.component;

import java.awt.Color;
import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class CompositeGeom  extends MultiPolyLine implements RpGeometry {
    
    private RpGeomFactory factory_;
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);
    
    public CompositeGeom(CoordsArray[] vertices, CompositeGeomFactory factory) {
        super(vertices, VIEWING_ATTR);
        factory_=factory;
    }
    
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf, viewingAttr());
        
    }
    
    public RpGeomFactory geomFactory() {
        return factory_;
    }
    
    
}
