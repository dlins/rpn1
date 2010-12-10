/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.HugoniotSegment;
import wave.multid.model.*;
import wave.multid.view.*;
import wave.multid.*;
import java.awt.Color;

public class HugoniotSegGeom extends MultiPolyLine {
    public HugoniotSegGeom(HugoniotSegment segment) {
        super( new CoordsArray[] { new CoordsArray(segment.leftPoint()), new CoordsArray(segment.rightPoint())},viewAttrSelection(segment));
    }

    public static ViewingAttr viewAttrSelection(HugoniotSegment segment) {
        ViewingAttr attr = new ViewingAttr(Color.white);

//        if ((segment.type().negativeRealPartNoLeft() == 1) && (segment.type().negativeRealPartNoRight() == 0))
//            attr = new ViewingAttr(Color.cyan);
//        if ((segment.type().negativeRealPartNoLeft() == 0) && (segment.type().negativeRealPartNoRight() == 1))
//            attr = new ViewingAttr(Color.blue);
//        if ((segment.type().negativeRealPartNoLeft() == 1) && (segment.type().negativeRealPartNoRight() == 1))
//            attr = new ViewingAttr(Color.green);
//        if ((segment.type().negativeRealPartNoLeft() == 2) && (segment.type().negativeRealPartNoRight() == 1))
//            attr = new ViewingAttr(Color.pink);
//        if ((segment.type().negativeRealPartNoLeft() == 0) && (segment.type().negativeRealPartNoRight() == 2))
//            attr = new ViewingAttr(Color.yellow);
//        if ((segment.type().negativeRealPartNoLeft() == 1) && (segment.type().negativeRealPartNoRight() == 2))
//            attr = new ViewingAttr(Color.red);
        return attr;
    }
}
