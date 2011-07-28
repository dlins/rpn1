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
import rpn.RPnPhaseSpacePanel;

public class HugoniotSegGeom extends MultiPolyLine {

    static public String[] s = {"----", "", "-+--", "++--", "", "", "", "", "---+", "", "-+-+", "++-+", "--++", "", "-+++", "++++"};   //** declarei isso (Leandro)


    public HugoniotSegGeom(HugoniotSegment segment) {
        super(new CoordsArray[]{new CoordsArray(segment.leftPoint()), new CoordsArray(segment.rightPoint())}, viewAttrSelection(segment));

    }

    public static ViewingAttr viewAttrSelection(HugoniotSegment segment) {
        ViewingAttr attr = new ViewingAttr(Color.white);

        if (segment.getType() == 0) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 1) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 2) {
            attr = new ViewingAttr(new Color(255, 0, 0));
        }

        if (segment.getType() == 3) {
            attr = new ViewingAttr(new Color(247, 151, 55));
        }

        if (segment.getType() == 4) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 5) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 6) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 7) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 8) {
            attr = new ViewingAttr(new Color(255, 0, 255));
        }

        if (segment.getType() == 9) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 10) {
            attr = new ViewingAttr(new Color(18, 153, 1));
        }

        if (segment.getType() == 11) {
            attr = new ViewingAttr(new Color(0, 0, 255));
        }

        if (segment.getType() == 12) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 13) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }

        if (segment.getType() == 14) {
            attr = new ViewingAttr(new Color(0, 255, 255));
        }

        if (segment.getType() == 15) {
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 255));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(128, 128, 128));
            }
        }


        //TODO SubInflection Curve segment type .
        if (segment.getType() == 16) {

            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(255, 255, 0));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(243, 123, 46));
            }
            
        }


        //TODO Coincidence Curve segment type
        if (segment.getType() == 17) {

            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(0, 204, 0));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(20, 43, 140));
            }

        }


        //TODO  BuckleyLeverettinflection Curve segment type
        if (segment.getType() == 18) {

            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
                attr = new ViewingAttr(new Color(135, 27, 224));
            }
            if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
                attr = new ViewingAttr(new Color(0, 255, 0));
            }
        }
        

        return attr;
    }

   
}
