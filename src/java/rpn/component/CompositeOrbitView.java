package rpn.component;

import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.PolyLine;

public class CompositeOrbitView extends PolyLine {

    public CompositeOrbitView(MultiGeometryImpl abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);
    }

}
