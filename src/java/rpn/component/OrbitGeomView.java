/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.*;
import java.awt.geom.*;

import rpnumerics.*;
import wave.multid.*;
import wave.multid.model.*;
import wave.multid.view.*;
import java.util.ArrayList;
import wave.util.Arrow;
import wave.util.RealVector;

public class OrbitGeomView extends PolyLine {

    private ArrayList arrowList_;
    private OrbitPoint[] points_;
    private final static int ARROWS_STEP = 10;
    private final static int SCALE = 150;

    public OrbitGeomView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {

        super(geom, transf, attr);

    }

    @Override
    public Shape createShape() {

        arrowList_= new ArrayList();

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        Orbit source = (Orbit) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        points_ = source.getPoints();

        arrowsCalculations();

        try {
            composite.append(super.createShape(), false);
        } catch (DimMismatchEx ex) {

            ex.printStackTrace();

        }

        return composite;

    }

    public void draw(Graphics2D g) {



        g.setColor(getViewingAttr().getColor());

        super.draw(g);

        for (int i = 0; i < arrowList_.size(); i++) {

            ((Arrow) (arrowList_.get(i))).paintComponent(g);
        }
    }

    protected void arrowsCalculations() {

        arrowList_ = new ArrayList();
        System.out.println("Qtd de pontos: " + points_.length);

        for (int i = 0; i < points_.length - 1; i += ARROWS_STEP) {
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
}
