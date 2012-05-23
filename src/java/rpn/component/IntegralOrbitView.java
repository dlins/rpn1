package rpn.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import rpnumerics.IntegralCurve;
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

        for (PointMark pointMark : inflectionPointsMark_) {

            pointMark.draw(g);

        }

    }

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

   
   
}
