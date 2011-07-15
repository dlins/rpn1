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



    public HugoniotSegGeom(HugoniotSegment segment,ViewingAttr viewingAtt) {
        super(new CoordsArray[]{new CoordsArray(segment.leftPoint()), new CoordsArray(segment.rightPoint())}, viewingAtt);

    }



    public HugoniotSegGeom(HugoniotSegment segment) {
        super(new CoordsArray[]{new CoordsArray(segment.leftPoint()), new CoordsArray(segment.rightPoint())}, viewAttrSelection(segment));

    }

    public static ViewingAttr viewAttrSelection(HugoniotSegment segment) {
        ViewingAttr attr = new ViewingAttr(Color.white);

        if (segment.getType() == 0) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }

        if (segment.getType() == 1) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }
        if (segment.getType() == 2) {
            attr = new ViewingAttr(new Color(255, 0, 0));
        }
        if (segment.getType() == 3) {
            attr = new ViewingAttr(new Color(247, 151, 55));
        }
        if (segment.getType() == 4) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }
        if (segment.getType() == 5) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }
        if (segment.getType() == 6) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }
        if (segment.getType() == 7) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }
        if (segment.getType() == 8) {
            attr = new ViewingAttr(new Color(255, 0, 255));
        }
        if (segment.getType() == 9) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }
        if (segment.getType() == 10) {
            attr = new ViewingAttr(new Color(18, 153, 1));
        }
        if (segment.getType() == 11) {
            attr = new ViewingAttr(new Color(0, 0, 255));

        }
        if (segment.getType() == 12) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }
        if (segment.getType() == 13) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }

        if (segment.getType() == 14) {
            attr = new ViewingAttr(new Color(0, 255, 255));
        }
        if (segment.getType() == 15) {
            attr = new ViewingAttr(new Color(255, 255, 255));
        }


        //TODO SubInflection Curve segment type .

        if (segment.getType() == 16) {
            attr = new ViewingAttr(new Color(255, 255, 0));
        }
        //TODO Coincidence Curve segment type
        if (segment.getType() == 17) {
            attr = new ViewingAttr(new Color(0, 204, 0));
        }

        //TODO  BuckleyLeverettinflection Curve segment type
        if (segment.getType() == 18) {
            attr = new ViewingAttr(new Color(135, 27, 224));
        }
        return attr;
    }

   
}
