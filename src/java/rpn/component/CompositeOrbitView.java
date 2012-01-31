package rpn.component;

import java.awt.Graphics2D;
import rpnumerics.CompositeCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import wave.multid.model.MultiGeometryImpl;
import wave.util.RealVector;

public class CompositeOrbitView extends OrbitGeomView {

    public CompositeOrbitView(MultiGeometryImpl abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);
    }

//    @Override
//    public void draw(Graphics2D g) {
//
//        g.setColor(getViewingAttr().getColor());
//
//        CompositeCurve source = (CompositeCurve) (((RpGeometry) getAbstractGeom()).geomFactory().
//                geomSource());
//
//        for (int i = 0; i < source.getPoints().length; i++) {
//
//            Coords2D direction_dc = new Coords2D();
//            RealVector tempVector = new RealVector(source.getPoints()[i]);
//            getViewingTransform().viewPlaneTransform(new CoordsArray(
//                    tempVector),
//                    direction_dc);
//
//            g.drawString("+", (float) direction_dc.getX(), (float) direction_dc.getY());
//
//        }
//
//    }
}
