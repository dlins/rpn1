/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.PhasePoint;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.map.Map;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.AbstractSegment;
import wave.multid.model.AbstractSegmentAtt;
import wave.multid.model.BoundingBox;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.model.MultiPolyLine;
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
        
        factory_=factory;

        for (int i = 0; i < charPoints.size(); i++) {
            PhasePoint[] firstLine = charPoints.get(i);

            PhasePoint firstPoint = firstLine[0];
            PhasePoint secondPoint = firstLine[1];
            CoordsArray[] segmentCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];


            segmentCoords[0] = new CoordsArray(firstPoint.getCoords());
            segmentCoords[1] = new CoordsArray(secondPoint.getCoords());
            AbstractSegment segment = null;
            try {
                if (i == 0) {
                    segment = new AbstractSegment(segmentCoords, new AbstractSegmentAtt(AbstractSegment.SEG_LINETO));
                } else {
                    segment = new AbstractSegment(segmentCoords, new AbstractSegmentAtt(AbstractSegment.SEG_LINETO));
                }

                append(segment, false);

            } catch (WrongNumberOfDefPointsEx ex) {
                Logger.getLogger(CharacteristicCurveGeom.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DimMismatchEx ex) {
                Logger.getLogger(CharacteristicCurveGeom.class.getName()).log(Level.SEVERE, null, ex);
            }

        }







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
