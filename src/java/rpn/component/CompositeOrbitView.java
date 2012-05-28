package rpn.component;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import rpnumerics.CompositeCurve;
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

        super.draw(g);

    }


    @Override
    public Shape createShape() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        //***
        composite.append(shapeCalculations(), false);
        composite.append(normalCalculations(), false);
        setShape(composite);
        //***

        //composite.append(super.createShape(), false);     //Deve mesmo chamar super ? Pq ?

        return composite;

    }


    private Shape shapeCalculations() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        CompositeCurve source = (CompositeCurve) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

        OrbitPoint[] points = source.getPoints();


        for (int i = 1; i < points.length; i++) {

            Coords2D direction_dc = new Coords2D();
            Coords2D start_dc = new Coords2D();

            RealVector tempVector2 = new RealVector(points[i].getCoords());
            getViewingTransform().viewPlaneTransform(new CoordsArray(tempVector2), direction_dc);

            RealVector tempVector1 = new RealVector(points[i-1].getCoords());
            getViewingTransform().viewPlaneTransform(new CoordsArray(tempVector1), start_dc);

            Line2D line1 = new Line2D.Double(start_dc.getElement(0), start_dc.getElement(1), direction_dc.getElement(0), direction_dc.getElement(1));
            composite.append(line1, false);

        }

        return composite;
    }


    private Shape normalCalculations() {

        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        CompositeCurve source = (CompositeCurve) (((RpGeometry) getAbstractGeom()).geomFactory().
                geomSource());

         double h = 0.01;

         if (source.getPoints().length > 1) {

             for (int i = 0; i < source.getPoints().length - 1; i++) {

                 Coords2D P1DC = new Coords2D();
                 Coords2D P2DC = new Coords2D();
                 RealVector P1WC = new RealVector(source.getPoints()[i]);
                 RealVector P2WC = new RealVector(source.getPoints()[i + 1]);

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


             for (int i = source.getPoints().length - 1; i > source.getPoints().length - 2; i--) {
                 Coords2D P1DC = new Coords2D();
                 Coords2D P2DC = new Coords2D();
                 RealVector P1WC = new RealVector(source.getPoints()[i - 1]);
                 RealVector P2WC = new RealVector(source.getPoints()[i]);

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