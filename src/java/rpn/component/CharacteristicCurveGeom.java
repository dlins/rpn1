/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static Color COLOR = new Color(20, 43, 140);
    RpGeomFactory factory_;

    public CharacteristicCurveGeom(List<PhasePoint[]> charPoints, CharacteristicsCurveGeomFactory factory) {
        super(new Space("characteristicsSpace", 2), new ViewingAttr(COLOR));

        factory_ = factory;

        for (int i = 0; i < charPoints.size(); i++) {


            PhasePoint[] line = charPoints.get(i);


            List<AbstractSegment> lineSegmentsList = createAbstractSegments(line);


            for (AbstractSegment abstractSegment : lineSegmentsList) {
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

        for (int i = 0; i < linePoints.length / 2; i++) {



            PhasePoint firstPoint = linePoints[2 * i];
            PhasePoint secondPoint = linePoints[2 * i + 1];
            CoordsArray[] segmentCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];

//            System.out.println(firstPoint.getCoords() + " " + secondPoint.getCoords());



            segmentCoords[0] = new CoordsArray(firstPoint.getCoords());
            segmentCoords[1] = new CoordsArray(secondPoint.getCoords());
            AbstractSegment segment;
            try {
                if (i == 0) {

                    segment = new AbstractSegment(segmentCoords, new AbstractSegmentAtt(AbstractSegment.SEG_MOVETO));
                } else {
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
}
