package rpn.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.OrbitPoint;
import rpnumerics.WaveCurveCalc;
import wave.multid.CoordsArray;
import wave.multid.model.AbstractGeomObj;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPoint;
import wave.multid.view.GeomObjView;
import wave.multid.view.PointMark;
import wave.multid.view.ShapedGeometry;
import wave.multid.view.ViewingAttr;

public class WaveCurveView extends WaveCurveOrbitGeomView {

    
    public WaveCurveView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
        
    }


    @Override
    public Shape createShape() {

        List<GeomObjView> objView = new ArrayList<GeomObjView>();

        List<WaveCurveBranchGeom> branchGeomList = ((WaveCurveGeom) getAbstractGeom()).getOrbitGeom();

        GeneralPath gPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        for (WaveCurveBranchGeom branchGeom : branchGeomList) {
            try {
                OrbitGeom orbitGeom = (OrbitGeom)branchGeom;
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

        WaveCurveCalc calc = (WaveCurveCalc) wCurveFactory.rpCalc();

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

//        for (WaveCurveOrbitGeom orbitGeom : orbitGeomList) {
//            try {
//                WaveCurveOrbitGeomView orbitView = (WaveCurveOrbitGeomView) orbitGeom.createView(getViewingTransform());
//
//                if (orbitView instanceof CompositeOrbitView) {
//                    CompositeOrbitView teste = (CompositeOrbitView) orbitView;
//                    g.setColor(Color.green);
//                    g.setStroke(new BasicStroke(1.0f));
//                    g.draw(teste.createShape());
//                }
//
//                if (orbitView instanceof ShockCurveGeomView) {
//                    ShockCurveGeomView teste = (ShockCurveGeomView) orbitView;
//
//                    g.setColor(Color.blue);
//                    g.setStroke(new BasicStroke(1.0f));
//                    g.draw(teste.createShape());
//
//                }
//
//
//                if (orbitView instanceof RarefactionOrbitView) {
//
//
//                    RarefactionOrbitView teste = (RarefactionOrbitView) orbitView;
//
//                    g.setColor(Color.red);
//                    g.setStroke(new BasicStroke(1.0f));
//                    g.draw(teste.createShape());
//
//                }
//
//
//            } catch (DimMismatchEx ex) {
//                Logger.getLogger(WaveCurveView.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            pointMark.draw(g);
//
//        }


    }

    
}
