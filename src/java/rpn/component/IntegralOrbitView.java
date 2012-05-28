package rpn.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.IntegralCurve;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPoint;
import wave.multid.view.PointMark;
import wave.multid.view.ViewingAttr;
import wave.util.Arrow;
import wave.util.RealVector;

public class IntegralOrbitView extends WaveCurveOrbitGeomView {

    private List<PointMark> inflectionPointsMark_;
 

    public IntegralOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }

    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        createInflectionMarks();
        
        //***
        composite.append(shapeCalculations(), false);
        setShape(composite);
        //***               

        composite.append(super.createShape(), false);

        return composite;

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());

        super.draw(g);

        for (PointMark pointMark : inflectionPointsMark_) {

            pointMark.draw(g);

        }

    }

    //*** metodo original, usado para preencher a lista de pontos de inflexao. Originalmente, draw desenha esta lista
    private void createInflectionMarks() {

        inflectionPointsMark_ = new ArrayList<PointMark>();

        IntegralCurve source = (IntegralCurve) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        List<RealVector> inflectionPoints = source.getInflectionPoints();
        for (RealVector inflectionPoint : inflectionPoints) {

            CoordsArray pointCoords = new CoordsArray(inflectionPoint);
            ViewingAttr viewAtt = new ViewingAttr(Color.white);

            MultiPoint point = new MultiPoint(pointCoords, viewAtt);
            PointMark pointMark = null;
            try {
                pointMark = new PointMark(point, getViewingTransform(), viewAtt);
            } catch (DimMismatchEx ex) {
                ex.printStackTrace();
            }
            inflectionPointsMark_.add(pointMark);
        }


    }


    private Shape shapeCalculations() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        IntegralCurve source = (IntegralCurve) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        OrbitPoint[] points = source.getPoints();

        for (int i = 1; i < points.length - 1; i++) {

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

                    Arrow arrow = new Arrow(arrowStart,
                            direction,
                            5.0, 1.0);      //tamanho da cabeça, comprimento da flecha
                    
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
