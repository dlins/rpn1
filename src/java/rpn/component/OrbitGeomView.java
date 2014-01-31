/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.*;
import java.awt.geom.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import rpnumerics.*;
import wave.multid.*;
import wave.multid.model.*;
import wave.multid.view.*;
import wave.util.Arrow;
import wave.util.RealVector;

public class OrbitGeomView extends PolyLine {

    private final static int ARROWS_STEP = 30;      //era 10
    private final static int SCALE = 800;           //era 150

    public OrbitGeomView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {

        super(geom, transf, attr);

    }

    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        //***
        if (getClass().getSimpleName().equals("OrbitGeomView"))
            composite.append(shapeCalculations(), false);
        //***

        try {
            composite.append(super.createShape(), false);
        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }

        return composite;

    }

    private Shape shapeCalculations() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        Orbit source = (Orbit) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        OrbitPoint[] points = source.getPoints();

        int dir = 1;
        if(source.getDirection() == -1) dir = -1;

        for (int i = 0; i < points.length - 1; i += ARROWS_STEP) {
            Coords2D startPoint = new Coords2D();
            Coords2D endPoint = new Coords2D();
            getViewingTransform().viewPlaneTransform(new CoordsArray(points[i]),
                    startPoint);
            getViewingTransform().viewPlaneTransform(new CoordsArray(points[i
                    + 1]), endPoint);
            endPoint.sub(startPoint);

            if (endPoint.norm()
                    > (getViewingTransform().viewPlane().getViewport().getWidth()
                    / SCALE)) {

                Coords2D direction_dc = new Coords2D();
                Coords2D start_dc = new Coords2D();
                RealVector tempVector = new RealVector(points[i + 1].getCoords());
                getViewingTransform().viewPlaneTransform(new CoordsArray(
                        tempVector),
                        direction_dc);
                getViewingTransform().viewPlaneTransform(new CoordsArray(
                        points[i].getCoords()), start_dc);

                direction_dc.setElement(0, direction_dc.getX() - start_dc.getX());
                direction_dc.setElement(1, direction_dc.getY() - start_dc.getY());

                Arrow arrow = new Arrow(new RealVector(start_dc.getCoords()),
                        new RealVector(direction_dc.getCoords()),
                        5.0, dir * 1.0);

                RealVector p1 = (RealVector) arrow.getHeadDefPoints().get(0);
                RealVector p2 = (RealVector) arrow.getHeadDefPoints().get(2);

                Line2D line1 = new Line2D.Double(start_dc.getElement(0), start_dc.getElement(1), p1.getElement(0), p1.getElement(1));
                Line2D line2 = new Line2D.Double(start_dc.getElement(0), start_dc.getElement(1), p2.getElement(0), p2.getElement(1));
                composite.append(line1, false);
                composite.append(line2, false);

            }
        }

        return composite;
    }

    //-----------------------------------------------
    @Override
    public void draw(Graphics2D g) {
        Color previous = g.getColor();
        g.setColor(getViewingAttr().getColor());
        g.draw(getShape());
        
         
          OrbitGeom orbitGeom = (OrbitGeom)getAbstractGeom();
          
          MultiPoint referencePoint = orbitGeom.getStarPoint();
//        try {
//            ViewingAttr viewAtrAttr = referencePoint.viewingAttr();
//            viewAtrAttr.setVisible(getViewingAttr().isVisible());
//            PointMark startPoint = new PointMark(referencePoint, getViewingTransform(), viewAtrAttr);
//            g.setColor(viewAtrAttr.getColor());
//            g.draw(startPoint.getShape());
//
//        } catch (DimMismatchEx ex) {
//            Logger.getLogger(OrbitGeomView.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
        
        g.setColor(previous);


    }
}
