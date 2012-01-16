package rpn.component;

import java.awt.Color;
import rpnumerics.CompositeCurve;
import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class CompositeGeom  extends MultiPolyLine implements RpGeometry {
    

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);

    private RpGeomFactory factory_;
    
    public CompositeGeom(CoordsArray[] coordsArray, CompositeGeomFactory factory) {


        super(coordsArray, new ViewingAttr(Color.yellow));
        factory_ = factory;


    }



    private static ViewingAttr selectViewingAttr(CompositeGeomFactory factory) {

        int family = (((CompositeCurve) factory.geomSource())).getFamilyIndex();
        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }


        return new ViewingAttr(Color.white);

    }

     public RpGeomFactory geomFactory() {
        return factory_;
    }
    
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CompositeOrbitView(this, transf, viewingAttr());
        
    }
    
    
    
}
