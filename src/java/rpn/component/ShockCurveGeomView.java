/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
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
        Color color = getViewingAttr().getColor();
        g.setColor(color);
        super.draw(g);

        

    }

    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        composite.append(dash(), false);

        return composite;

    }

    private Shape dash() {
        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        ShockCurveGeom absGeom = (ShockCurveGeom) getAbstractGeom();

        OrbitPoint[] points = absGeom.getPointsArray();

        int begin = 0;
        int end = points.length;

        int k = Math.max(10, (int) Math.round(0.15 * end));   // *******

        if (end > 1) {
            for (int i = begin; i < end - k / 2; i += k) {
                Coords2D P1DC = new Coords2D();
                Coords2D P2DC = new Coords2D();

                RealVector P1WC = new RealVector(points[i]);
                RealVector P2WC = new RealVector(points[i + k / 2]);

                getViewingTransform().viewPlaneTransform(new CoordsArray(P1WC), P1DC);
                getViewingTransform().viewPlaneTransform(new CoordsArray(P2WC), P2DC);

                Line2D line1 = new Line2D.Double(P1DC.getElement(0), P1DC.getElement(1), P2DC.getElement(0), P2DC.getElement(1));
                composite.append(line1, false);
            }
        }
        return composite;
    }

}
