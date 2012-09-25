package rpn.component;

import java.util.Iterator;

import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

public class BifurcationCurveView extends GeomObjView {

    public BifurcationCurveView(BifurcationCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);

    }
   

    //Original update method
    public void update() {
        viewList_.clear();
        Iterator geomListIterator = ((BifurcationCurveGeom) getAbstractGeom()).getBifurcationSegmentsIterator();
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
