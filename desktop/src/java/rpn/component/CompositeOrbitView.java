package rpn.component;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import rpnumerics.OrbitPoint;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import wave.multid.model.MultiGeometryImpl;
import wave.util.RealVector;

public class CompositeOrbitView extends WaveCurveOrbitGeomView {

    public CompositeOrbitView(MultiGeometryImpl abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);

    }


    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());

        Stroke actualStroke = g.getStroke();
        float[] dash = {10f};
        BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_MITER, dash, 0f);

        g.setStroke(stroke);

        super.draw(g);
        g.setStroke(actualStroke);


    }


    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        composite.append(normalCalculations(), false);
        //composite.append(dash(), false);
        composite.append(super.createShape(), false);
        
        return composite;

    }


    private Shape dash() {
        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        CompositeGeom absGeom = (CompositeGeom) getAbstractGeom();

        OrbitPoint[] points = absGeom.getPointsArray();

        int begin = 0;
        int end = points.length;

        int k = 2;

        if (end > 1) {
            for (int i = begin; i < end - k/2; i+=k) {
                Coords2D P1DC = new Coords2D();
                Coords2D P2DC = new Coords2D();

                RealVector P1WC = new RealVector(points[i]);
                RealVector P2WC = new RealVector(points[i + k/2]);

                getViewingTransform().viewPlaneTransform(new CoordsArray(P1WC), P1DC);
                getViewingTransform().viewPlaneTransform(new CoordsArray(P2WC), P2DC);

                Line2D line1 = new Line2D.Double(P1DC.getElement(0), P1DC.getElement(1), P2DC.getElement(0), P2DC.getElement(1));
                composite.append(line1, false);
            }
        }

        return composite;
    }


    private Shape normalCalculations() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        CompositeGeom absGeom = (CompositeGeom) getAbstractGeom();

        OrbitPoint[] points = absGeom.getPointsArray();

        int begin = 0;
        int end = points.length;

        double h = 0.0075;

        if (end > 1) {

            for (int i = begin; i < end - 1; i++) {

                 Coords2D P1DC = new Coords2D();
                 Coords2D P2DC = new Coords2D();

                 RealVector P1WC = new RealVector(points[i]);
                 RealVector P2WC = new RealVector(points[i + 1]);

                 double mperp = -(P2WC.getElement(0) - P1WC.getElement(0)) / (P2WC.getElement(1) - P1WC.getElement(1));
                 double h_ = h / Math.sqrt(mperp * mperp + 1);

                 double x1 = P1WC.getElement(0) + h_;
                 double y1 = P1WC.getElement(1) + mperp * h_;
                 double x2 = P1WC.getElement(0) - h_;
                 double y2 = P1WC.getElement(1) - mperp * h_;

                 RealVector lineP1 = new RealVector(P1WC.getSize());
                 RealVector lineP2 = new RealVector(P1WC.getSize());

                 lineP1.setElement(0, x1);
                 lineP1.setElement(1, y1);
                 lineP2.setElement(0, x2);
                 lineP2.setElement(1, y2);

                 getViewingTransform().viewPlaneTransform(new CoordsArray(lineP1), P1DC);
                 getViewingTransform().viewPlaneTransform(new CoordsArray(lineP2), P2DC);

                 Line2D line1 = new Line2D.Double(P1DC.getElement(0), P1DC.getElement(1), P2DC.getElement(0), P2DC.getElement(1));
                 composite.append(line1, false);

             }


             for (int i = end - 1; i > end - 2; i--) {
                 Coords2D P1DC = new Coords2D();
                 Coords2D P2DC = new Coords2D();
                 RealVector P1WC = new RealVector(points[i - 1]);
                 RealVector P2WC = new RealVector(points[i]);

                 double mperp = -(P2WC.getElement(0) - P1WC.getElement(0)) / (P2WC.getElement(1) - P1WC.getElement(1));
                 double h_ = h / Math.sqrt(mperp * mperp + 1);

                 double x1 = P2WC.getElement(0) + h_;
                 double y1 = P2WC.getElement(1) + mperp * h_;
                 double x2 = P2WC.getElement(0) - h_;
                 double y2 = P2WC.getElement(1) - mperp * h_;

                 RealVector lineP1 = new RealVector(P1WC.getSize());
                 RealVector lineP2 = new RealVector(P1WC.getSize());

                 lineP1.setElement(0, x1);
                 lineP1.setElement(1, y1);
                 lineP2.setElement(0, x2);
                 lineP2.setElement(1, y2);

                 getViewingTransform().viewPlaneTransform(new CoordsArray(lineP1), P1DC);
                 getViewingTransform().viewPlaneTransform(new CoordsArray(lineP2), P2DC);

                 Line2D line1 = new Line2D.Double(P1DC.getElement(0), P1DC.getElement(1), P2DC.getElement(0), P2DC.getElement(1));
                 composite.append(line1, false);
             }

         }

        return composite;

    }



}