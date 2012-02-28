package rpn.component;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import java.util.ArrayList;
import java.util.List;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RarefactionOrbit;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.util.Arrow;
import wave.util.RealVector;

//public class RarefactionOrbitView extends OrbitGeomView{
//
//    private ArrayList arrowList_;
//    private final static int ARROWS_STEP = 10;
//    private final static int SCALE = 150;
//    private OrbitPoint[] points_;
//
//    public RarefactionOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
//            ViewingAttr attr) throws DimMismatchEx {
//        super(geom, transf, attr);
//    }
//
////    public Shape createShape() {
////
////        System.out.println("Usando método createShape da subclasse");
////        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
////        RarefactionOrbit source = (RarefactionOrbit) (((RpGeometry) getAbstractGeom()).geomFactory().
////                geomSource());
////        points_ = source.getPoints();
////        //arrowsCalculations();
////
////        //try {
////        //    composite.append(super.createShape(), false);
////
////        //} catch (DimMismatchEx ex) {
////
////        //    ex.printStackTrace();
////
////        //}
////
////        return composite;
////    }
////
////
////
////
////
////
////
////
////    private void arrowsCalculations() {
////
////        arrowList_ = new ArrayList();
////
////        for (int i = 0; i < points_.length - 1; i += 2 * ARROWS_STEP) {
////            Coords2D startPoint = new Coords2D();
////            Coords2D endPoint = new Coords2D();
////            getViewingTransform().viewPlaneTransform(new CoordsArray(points_[i]),
////                    startPoint);
////            getViewingTransform().viewPlaneTransform(new CoordsArray(points_[i
////                    + 1]), endPoint);
////            endPoint.sub(startPoint);
////            if (endPoint.norm()
////                    > (getViewingTransform().viewPlane().getViewport().getWidth()
////                    / SCALE)) {
////
////                Coords2D direction_dc = new Coords2D();
////                Coords2D start_dc = new Coords2D();
////                RealVector tempVector = new RealVector(points_[i + 1].getCoords());
////                getViewingTransform().viewPlaneTransform(new CoordsArray(
////                        tempVector),
////                        direction_dc);
////                getViewingTransform().viewPlaneTransform(new CoordsArray(
////                        points_[i].getCoords()), start_dc);
////
////                direction_dc.setElement(0, direction_dc.getX() - start_dc.getX());
////
////
////
////                direction_dc.setElement(1, direction_dc.getY() - start_dc.getY());
////
////                Arrow arrow = new Arrow(new RealVector(start_dc.getCoords()),
////                        new RealVector(direction_dc.getCoords()),
////                        getViewingTransform().viewPlane().
////                        getViewport().getWidth() / SCALE,
////                        getViewingTransform().viewPlane().
////                        getViewport().getWidth() / SCALE);
////
////                arrowList_.add(arrow);
////            }
////        }
////    }
//}

//******************************************************************************

//*** Leandro em 13/02/2012: puxei de IntegralOrbit View. Submeter a testes e conferencias com Edson, Panters e Pablo.

public class RarefactionOrbitView extends OrbitGeomView{

    private List<Arrow> arrowList_;

    public RarefactionOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }

    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        //createInflectionMarks();
        arrowsCalculations();

        composite.append(super.createShape(), false);

        return composite;
    }


    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());

        super.draw(g);

        for (int i = 0; i < arrowList_.size(); i++) {

            ((Arrow) (arrowList_.get(i))).paintComponent(g);
        }


    }


    private ArrayList<RealVector> levelsProcessing(OrbitPoint firstPoint, OrbitPoint secondPoint, double level) {


        ArrayList<RealVector> arrowPostions = new ArrayList<RealVector>();

        int lp = firstPoint.getSize();

        // Find the integer levels of the ends of each segment
        double lambda_first = firstPoint.getLambda();
        double lambda_last = secondPoint.getLambda();

        int lambda_first_level;
        int lambda_last_level;
        int lambda_j_first, lambda_j_last;

        if (lambda_last > lambda_first) {
            lambda_first_level = (int) Math.floor(lambda_first / level);
            lambda_last_level = (int) Math.floor(lambda_last / level);

            lambda_j_first = lambda_first_level;
            lambda_j_last = lambda_last_level;
        } else {
            lambda_first_level = (int) Math.ceil(lambda_first / level);
            lambda_last_level = (int) Math.ceil(lambda_last / level);

            lambda_j_first = lambda_last_level;
            lambda_j_last = lambda_first_level;
        }

        if (lambda_first_level != lambda_last_level) {

            for (int j = lambda_j_first; j <= lambda_j_last; j++) {
                double lambda = j * level;
                double alpha = (lambda - lambda_last) / (lambda_first - lambda_last);
                if (alpha < 0.0 || alpha > 1.0) {
                    continue;
                }
                double beta = 1.0 - alpha;
                RealVector pos = new RealVector(lp);
                for (int k = 0; k < lp; k++) {
                    pos.setElement(k, alpha * firstPoint.getElement(k) + beta * secondPoint.getElement(k));
                }
                arrowPostions.add(pos);

            }

        }

        return arrowPostions;

    }



    private void arrowsCalculations() {

        arrowList_ = new ArrayList();

        RarefactionOrbit source = (RarefactionOrbit) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        OrbitPoint[] points = source.getPoints();

        for (int i = 0; i < points.length - 2; i++) {

            Coords2D direction_dc = new Coords2D();
            Coords2D start_dc = new Coords2D();

            ArrayList<RealVector> arrowPositions = levelsProcessing(points[i], points[i + 1],  new Double (RPNUMERICS.getParamValue("orbit", "level")));

            for (RealVector arrowStartPoint : arrowPositions) {

                RealVector tempVector = new RealVector(points[i + 1].getCoords());
                getViewingTransform().viewPlaneTransform(  new CoordsArray(
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
                            5.0, 5.0);
                    arrowList_.add(arrow);

                }

            }
        }
    }
}
