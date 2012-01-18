/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.*;

import wave.multid.*;
import wave.multid.model.*;
import wave.multid.view.*;

public class ShockCurveGeomView extends PolyLine {

    public ShockCurveGeomView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {

        super(geom, transf, attr);

    }

//    @Override
//    public Shape createShape() {
//
//        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
//
//        ShockCurve source = (ShockCurve) (((RpGeometry) getAbstractGeom()).geomFactory().
//                geomSource());
//
//        points_ = source.getPoints();
//
//        arrowsCalculations();
//
//        try {
//            composite.append(super.createShape(), false);
//        } catch (DimMismatchEx ex) {
//
//            ex.printStackTrace();
//
//        }
//
//        return composite;
//
//    }
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());
        Stroke actualStroke = g.getStroke();
        float[] dash = {10f};
        BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_MITER, dash, 0f);

        g.setStroke(stroke);

        super.draw(g);
        g.setStroke(actualStroke);


    }
}
