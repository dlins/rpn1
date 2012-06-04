package rpn.component;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import wave.util.Arrow;
import wave.util.RealVector;

public class RarefactionOrbitView extends WaveCurveOrbitGeomView{

 

    public RarefactionOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }

    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        
        //***
        composite.append(shapeCalculations(), false);
        //***

        composite.append(super.createShape(), false);

        return composite;
    }


    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());
        
        super.draw(g);

    }



    private Shape shapeCalculations() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        Orbit source = (Orbit) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        int begin = ((RarefactionGeom) getAbstractGeom()).getBegin();
        int end   = ((RarefactionGeom) getAbstractGeom()).getEnd();

        OrbitPoint[] points = source.getPoints();

        //for (int i = 2; i < points.length - 2; i++) {       // à espera de confirmacao
        for (int i = begin + 2; i < end - 2; i++) {

            Coords2D direction_dc = new Coords2D();
            Coords2D start_dc = new Coords2D();

            ArrayList<RealVector> arrowPositions = levelsProcessing(points[i], points[i + 1], new Double(RPNUMERICS.getParamValue("orbit", "level")));

            for (RealVector arrowStartPoint : arrowPositions) {

                RealVector tempVector = new RealVector(points[i + 1].getCoords());
                getViewingTransform().viewPlaneTransform(new CoordsArray(
                        tempVector),
                        direction_dc);

                getViewingTransform().viewPlaneTransform(new CoordsArray(arrowStartPoint), start_dc);

                RealVector arrowStart = new RealVector(start_dc.getCoords());

                direction_dc.setElement(0, direction_dc.getX() - start_dc.getX());
                direction_dc.setElement(1, direction_dc.getY() - start_dc.getY());

                RealVector direction = new RealVector(direction_dc.getCoords());

                if (direction.norm() != 0.0) {

                    double lambda1 = points[i].getLambda();
                    double lambda2 = points[i + 1].getLambda();

                    if (lambda1 > lambda2) {
                        direction.negate();

                    }

                    Arrow arrow = new Arrow(arrowStart, direction, 5.0, 1.0);

                    RealVector p1 = (RealVector) arrow.getHeadDefPoints().get(0);
                    RealVector p2 = (RealVector) arrow.getHeadDefPoints().get(2);

                    Line2D line1 = new Line2D.Double(start_dc.getElement(0), start_dc.getElement(1), p1.getElement(0), p1.getElement(1));
                    Line2D line2 = new Line2D.Double(start_dc.getElement(0), start_dc.getElement(1), p2.getElement(0), p2.getElement(1));
                    composite.append(line1, false);
                    composite.append(line2, false);

                }


            }
        }

        return composite;
    }



}
