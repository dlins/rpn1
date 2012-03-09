package rpn.component;

import java.awt.Graphics2D;
import wave.multid.model.AbstractGeomObj;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpnumerics.OrbitPoint;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.util.Arrow;
import wave.util.RealVector;

public class RarefactionExtensionCurveView implements GeomObjView {

    private ArrayList arrowList_;
    private final static int ARROWS_STEP = 10;
    private final static int SCALE = 150;
    private OrbitPoint[] points_;
    private List viewList_;
    private ViewingTransform viewingTransf_;
    private AbstractGeomObj abstractGeom_;
    private ViewingAttr viewAttr_;

    public RarefactionExtensionCurveView(RarefactionExtensionGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {


        setAbstractGeom(abstractGeom);
        setViewingTransform(transf);
        setViewingAttr(viewAttr);
        viewList_ = new ArrayList();
        update();


    }


    public void draw(Graphics2D g) {


           for (int i = 0; i < viewList_.size(); i++) {
            ((GeomObjView) viewList_.get(i)).draw(g);
        }


       
    }

    private void arrowsCalculations() {

        arrowList_ = new ArrayList();

        for (int i = 0; i < points_.length - 1; i += 2 * ARROWS_STEP) {
            Coords2D startPoint = new Coords2D();
            Coords2D endPoint = new Coords2D();
            getViewingTransform().viewPlaneTransform(new CoordsArray(points_[i]),
                    startPoint);
            getViewingTransform().viewPlaneTransform(new CoordsArray(points_[i
                    + 1]), endPoint);
            endPoint.sub(startPoint);
            if (endPoint.norm()
                    > (getViewingTransform().viewPlane().getViewport().getWidth()
                    / SCALE)) {

                Coords2D direction_dc = new Coords2D();
                Coords2D start_dc = new Coords2D();
                RealVector tempVector = new RealVector(points_[i + 1].getCoords());
                getViewingTransform().viewPlaneTransform(new CoordsArray(
                        tempVector),
                        direction_dc);
                getViewingTransform().viewPlaneTransform(new CoordsArray(
                        points_[i].getCoords()), start_dc);

                direction_dc.setElement(0, direction_dc.getX() - start_dc.getX());
                direction_dc.setElement(1, direction_dc.getY() - start_dc.getY());

                Arrow arrow = new Arrow(new RealVector(start_dc.getCoords()),
                        new RealVector(direction_dc.getCoords()),
                        getViewingTransform().viewPlane().
                        getViewport().getWidth() / SCALE,
                        getViewingTransform().viewPlane().
                        getViewport().getWidth() / SCALE);

                arrowList_.add(arrow);
            }
        }
    }

       public AbstractGeomObj getAbstractGeom() {
        return abstractGeom_;
    }

    public void setAbstractGeom(AbstractGeomObj abstractGeom) {
        abstractGeom_ = abstractGeom;
    }

    public ViewingTransform getViewingTransform() {
        return viewingTransf_;
    }

    public void setViewingTransform(ViewingTransform transf) {
        viewingTransf_ = transf;
    }

    public ViewingAttr getViewingAttr() {
        return viewAttr_;
    }

    public void setViewingAttr(ViewingAttr viewAttr) {
        viewAttr_ = viewAttr;
    }


      //Original update method
    public void update() {
        viewList_.clear();

        RarefactionExtensionGeom rarefactionExtensionGeom = (RarefactionExtensionGeom) abstractGeom_;
      
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
