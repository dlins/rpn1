/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.component.util.GraphicsUtil;
import rpnumerics.PhasePoint;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.model.AbstractSegment;
import wave.multid.model.AbstractSegmentAtt;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.model.WrongNumberOfDefPointsEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

public class CharacteristicCurveGeom extends MultiGeometryImpl implements RpGeometry {

    //
    // Constructors
    private RpGeomFactory factory_;

    public CharacteristicCurveGeom(List<PhasePoint[]> charPoints, CharacteristicsCurveGeomFactory factory, ViewingAttr viewAttr) {
        super(new Space("CharacteristicsSpace", 2), viewAttr);

        factory_ = factory;

        for (int i = 0; i < charPoints.size(); i++) {
            PhasePoint[] line = charPoints.get(i);

            List<AbstractSegment> lineSegmentsList = createAbstractSegments(line);
            for (int j = 0; j < lineSegmentsList.size(); j++) {
                AbstractSegment abstractSegment = lineSegmentsList.get(j);
                try {
                    append(abstractSegment, false);
                } catch (DimMismatchEx ex) {
                    Logger.getLogger(CharacteristicCurveGeom.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }

    private List<AbstractSegment> createAbstractSegments(PhasePoint[] linePoints) {

        ArrayList<AbstractSegment> abstractSegmentsList = new ArrayList<AbstractSegment>();

        for (int i = 0; i < linePoints.length; i++) {
            CoordsArray[] segmentCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            AbstractSegment segment;
            try {

                if (i == 0) {
                    segmentCoords[0] = new CoordsArray(linePoints[i].getCoords());
                    segmentCoords[1] = new CoordsArray(new Space("", 2));
                    segment = new AbstractSegment(segmentCoords, new AbstractSegmentAtt(AbstractSegment.SEG_MOVETO));
                } else {

                    segmentCoords[0] = new CoordsArray(linePoints[i - 1].getCoords());
                    segmentCoords[1] = new CoordsArray(linePoints[i].getCoords());
                    segment = new AbstractSegment(segmentCoords, new AbstractSegmentAtt(AbstractSegment.SEG_LINETO));
                }
                abstractSegmentsList.add(segment);
            } catch (WrongNumberOfDefPointsEx ex) {
                Logger.getLogger(CharacteristicCurveGeom.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return abstractSegmentsList;
    }

//
// Methods
//
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CharacteristicCurveGeomView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    @Override
    public boolean isVisible() {
        return viewingAttr().isVisible();
    }

    @Override
    public boolean isSelected() {
        return viewingAttr().isSelected();
    }

    @Override
    public void addAnnotation(GraphicsUtil annotation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<GraphicsUtil> getAnnotationIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearAnnotations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeLastAnnotation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAnnotation(GraphicsUtil selectedAnnotation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
