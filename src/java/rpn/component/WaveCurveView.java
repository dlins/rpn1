package rpn.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.component.util.GraphicsUtil;
import rpnumerics.OrbitPoint;
import rpnumerics.WaveCurveOrbitCalc;
import wave.multid.CoordsArray;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPoint;
import wave.multid.view.PointMark;
import wave.multid.view.ViewingAttr;

public class WaveCurveView extends WaveCurveOrbitGeomView {

    public WaveCurveView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);

    }

    @Override
    public Shape createShape() {

      
        List<WaveCurveBranchGeom> branchGeomList = ((WaveCurveGeom) getAbstractGeom()).getOrbitGeom();

        GeneralPath gPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        for (WaveCurveBranchGeom branchGeom : branchGeomList) {
            try {
                OrbitGeom orbitGeom = (OrbitGeom) branchGeom;
                WaveCurveOrbitGeomView orbitView = (WaveCurveOrbitGeomView) orbitGeom.createView(getViewingTransform());
                gPath.append(orbitView.createShape(), false);

            } catch (DimMismatchEx ex) {
                Logger.getLogger(WaveCurveView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return gPath;

    }

    @Override
    public void draw(Graphics2D g) {

        //List<WaveCurveOrbitGeom> orbitGeomList = ((WaveCurveGeom) getAbstractGeom()).getOrbitsGeomList();
        WaveCurveGeom waveCurveGeom = ((WaveCurveGeom) getAbstractGeom());

        RpCalcBasedGeomFactory wCurveFactory = (WaveCurveGeomFactory) waveCurveGeom.geomFactory();

        WaveCurveOrbitCalc calc = (WaveCurveOrbitCalc) wCurveFactory.rpCalc();

        OrbitPoint initialPoint = calc.getStart();

        CoordsArray coordsArray = new CoordsArray(initialPoint);

        ViewingAttr viewAtt = new ViewingAttr(Color.yellow);

        MultiPoint mPoint = new MultiPoint(coordsArray, viewAtt);

        PointMark pointMark = null;
        try {
            pointMark = new PointMark(mPoint, getViewingTransform(), viewAtt);
        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }

        pointMark.draw(g);

        super.draw(g);

        Iterator<GraphicsUtil> annotationIterator = waveCurveGeom.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            graphicsUtil.update(getViewingTransform());
            graphicsUtil.getViewingAttr().setVisible(waveCurveGeom.viewingAttr().isVisible());
            g.setColor(graphicsUtil.getViewingAttr().getColor());
            graphicsUtil.draw(g);

        }

    }

}
