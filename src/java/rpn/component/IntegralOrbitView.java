package rpn.component;

import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;

public class IntegralOrbitView extends OrbitGeomView {

    
    public IntegralOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }
    
}
