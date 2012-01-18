package rpn.component;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import rpnumerics.RarefactionOrbit;
import wave.multid.view.PolyLine;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import java.util.ArrayList;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import rpnumerics.OrbitPoint;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.util.Arrow;
import wave.util.RealVector;

public class RarefactionOrbitView extends PolyLine {

    private ArrayList arrowList_;
    private final static int ARROWS_STEP = 10;
    private final static int SCALE = 150;
    private OrbitPoint[] points_;

    public RarefactionOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }

    public Shape createShape() {


        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        RarefactionOrbit source = (RarefactionOrbit) (((RpGeometry) getAbstractGeom()).geomFactory().
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
}
