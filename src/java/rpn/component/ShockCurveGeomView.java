/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;

import wave.multid.*;
import wave.multid.model.*;
import wave.multid.view.*;
import wave.util.RealVector;

public class ShockCurveGeomView extends WaveCurveOrbitGeomView {

    public ShockCurveGeomView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {

        super(geom, transf, attr);

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());

        //---------------------- código original que fazia a curva ser tracejada
        //Stroke actualStroke = g.getStroke();
        //float[] dash = {10f};
        //BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_MITER, dash, 0f);

        //g.setStroke(stroke);

        //super.draw(g);
        //g.setStroke(actualStroke);
        //----------------------------------------------------------------------

        setShape(shapeCalculations());
        super.draw(g);

    }


    //*** tentando substituir o tracejado (antes no draw) por shapes
    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        //***
        //composite.append(shapeCalculations(), false);
        //setShape(composite);
        //***

        composite.append(super.createShape(), false);

        return composite;

    }


    private Shape shapeCalculations() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        Orbit source = (Orbit) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        OrbitPoint[] points = source.getPoints();

        for (int i = 0; i < points.length - 1; i+=3) {

            Coords2D direction_dc = new Coords2D();
            Coords2D start_dc = new Coords2D();

            RealVector tempVector2 = new RealVector(points[i+1].getCoords());
            getViewingTransform().viewPlaneTransform(new CoordsArray(tempVector2), direction_dc);

            RealVector tempVector1 = new RealVector(points[i].getCoords());
            getViewingTransform().viewPlaneTransform(new CoordsArray(tempVector1), start_dc);

            Line2D line1 = new Line2D.Double(start_dc.getElement(0), start_dc.getElement(1), direction_dc.getElement(0), direction_dc.getElement(1));
            composite.append(line1, false);

        }

        return composite;
    }


}
