package rpn.component;

import java.awt.Graphics2D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
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
//        List<OrbitGeom> orbitGeomList = ((WaveCurveGeom) getAbstractGeom()).getOrbitsGeomList();
//
//        for (OrbitGeom orbitGeom : orbitGeomList) {
//            try {
//                GeomObjView orbitView = orbitGeom.createView(getViewingTransform());
//                OrbitGeomView orbitGeomView = (OrbitGeomView) orbitView;
//
//                composite.append(orbitGeomView.createShape(), false);
//
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
//
//
//
//
//
//
//        composite.append(super.createShape(), false);
//
//        return composite;
//    }

    @Override
    public void draw(Graphics2D g) {

          List<WaveCurveOrbitGeom> orbitGeomList = ((WaveCurveGeom) getAbstractGeom()).getOrbitsGeomList();



        System.out.println("tamanho da lista: "+orbitGeomList.size());

        for (WaveCurveOrbitGeom orbitGeom : orbitGeomList) {
            try {
                System.out.println(orbitGeom.getClass().getCanonicalName());
                WaveCurveOrbitGeomView orbitView = (WaveCurveOrbitGeomView) orbitGeom.createView(getViewingTransform());


                g.setColor(orbitView.getViewingAttr().getColor());

                orbitView.draw(g);



            } //        super.draw(g);
            catch (DimMismatchEx ex) {
                Logger.getLogger(WaveCurveView.class.getName()).log(Level.SEVERE, null, ex);
            }



        }








//        super.draw(g);

    }
}
