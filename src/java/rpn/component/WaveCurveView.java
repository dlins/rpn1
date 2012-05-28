package rpn.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.OrbitPoint;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveCalc;
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

//    @Override
//    public Shape createShape() {
//
//        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
//
//        List<WaveCurveOrbitGeom> orbitGeomList = ((WaveCurveGeom) getAbstractGeom()).getOrbitsGeomList();
//
//        for (WaveCurveOrbitGeom orbitGeom : orbitGeomList) {
//            try {
//                GeomObjView orbitView = orbitGeom.createView(getViewingTransform());
//
//                System.out.println("Visual: " + orbitView.getClass().getCanonicalName());
//
////                WaveCurveOrbitGeomView orbitGeomView = (WaveCurveOrbitGeomView) orbitView;
//
//
//                if (orbitView instanceof CompositeOrbitView) {
//                    CompositeOrbitView teste = (CompositeOrbitView) orbitView;
//                    composite.append(teste.createShape(), false);
//                }
//
//                if (orbitView instanceof ShockCurveGeomView) {
//                    ShockCurveGeomView teste = (ShockCurveGeomView) orbitView;
//                    composite.append(teste.createShape(), false);
//                }
//
//
//                if (orbitView instanceof RarefactionOrbitView) {
//                    RarefactionOrbitView teste = (RarefactionOrbitView) orbitView;
//
//                    composite.append(teste.createShape(), false);
//
//                }
//
//
//            } //        super.draw(g);
//            catch (DimMismatchEx ex) {
//                Logger.getLogger(WaveCurveView.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//
//
//        }
//
//
////        composite.append(super.createShape(), false);
//
//        return composite;
//    }
    @Override
    public void draw(Graphics2D g) {

        List<WaveCurveOrbitGeom> orbitGeomList = ((WaveCurveGeom) getAbstractGeom()).getOrbitsGeomList();

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





        for (WaveCurveOrbitGeom orbitGeom : orbitGeomList) {
            try {
                WaveCurveOrbitGeomView orbitView = (WaveCurveOrbitGeomView) orbitGeom.createView(getViewingTransform());

                if (orbitView instanceof CompositeOrbitView) {
                    CompositeOrbitView teste = (CompositeOrbitView) orbitView;

                    teste.setViewingAttr(new ViewingAttr(Color.green));

                    teste.draw(g);
                }

                if (orbitView instanceof ShockCurveGeomView) {
                    ShockCurveGeomView teste = (ShockCurveGeomView) orbitView;
                    teste.setViewingAttr(new ViewingAttr(Color.blue));

                    teste.draw(g);
                }


                if (orbitView instanceof RarefactionOrbitView) {
                    RarefactionOrbitView teste = (RarefactionOrbitView) orbitView;
                    teste.setViewingAttr(new ViewingAttr(Color.red));
                    teste.draw(g);
                }


            } catch (DimMismatchEx ex) {
                Logger.getLogger(WaveCurveView.class.getName()).log(Level.SEVERE, null, ex);
            }




                    pointMark.draw(g);

        }








//        super.draw(g);

    }
}
