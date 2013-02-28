package rpn.component;

import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import java.util.ArrayList;
import java.util.Iterator;
import rpnumerics.OrbitPoint;
import wave.multid.view.GeomObjView;

public class RarefactionExtensionCurveView extends GeomObjView {

    private ArrayList arrowList_;
    private final static int ARROWS_STEP = 10;
    private final static int SCALE = 150;
    private OrbitPoint[] points_;

    public RarefactionExtensionCurveView(RarefactionExtensionGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);

    }

    //Original update method
    public void update() {
        viewList_.clear();

        RarefactionExtensionGeom rarefactionExtensionGeom = (RarefactionExtensionGeom) getAbstractGeom();

        Iterator geomListIterator = rarefactionExtensionGeom.getRealSegIterator();

        while (geomListIterator.hasNext()) {
            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }
}
