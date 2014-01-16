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
import rpnumerics.Orbit;

public class HugoniotSegGeom extends MultiPolyLine {

    static public String[] s = {"----", "", "-+--", "++--", "", "", "", "", "---+", "", "-+-+", "++-+", "--++", "", "-+++", "++++"};   //** declarei isso (Leandro)
    static public int DIRECTION = Orbit.FORWARD_DIR;

    public HugoniotSegGeom(HugoniotSegment segment, ViewingAttr viewingAtt) {
        super(new CoordsArray[]{new CoordsArray(segment.leftPoint()), new CoordsArray(segment.rightPoint())}, viewingAtt);

    }

    public HugoniotSegGeom(HugoniotSegment segment) {
        super(new CoordsArray[]{new CoordsArray(segment.leftPoint()), new CoordsArray(segment.rightPoint())}, viewAttrSelection(segment));

    }

    public static ViewingAttr viewAttrSelection(HugoniotSegment segment) {

        ViewingAttr attr = new ViewingAttr(Color.white);
        HugoniotSegColor [] segColorArray = HugoniotSegColor.values();
        HugoniotSegColor segColor = segColorArray[segment.getType()];
        attr= new ViewingAttr(segColor.getColor());

        if (DIRECTION == Orbit.BACKWARD_DIR) {

//            if (segment.getType() == 0) {
//                attr = new ViewingAttr(new Color(255, 255, 255));
//            }
//
//            if (segment.getType() == 1) {
//                attr = new ViewingAttr(new Color(128, 128, 128));
//            }

            if (segment.getType() == 2) {
                attr = new ViewingAttr(HugoniotSegColor.EXPANSIVE_2.getColor());
            }

            if (segment.getType() == 3) {
                attr = new ViewingAttr(HugoniotSegColor.SUPER_EXPANSIVE.getColor());
            }

//            if (segment.getType() == 4) {
//                attr = new ViewingAttr(new Color(128, 128, 128));
//            }
//
//            if (segment.getType() == 5) {
//                attr = new ViewingAttr(new Color(128, 128, 128));
//            }
//
//            if (segment.getType() == 6) {
//                attr = new ViewingAttr(new Color(128, 128, 128));
//            }
//
//            if (segment.getType() == 7) {
//                attr = new ViewingAttr(new Color(128, 128, 128));
//            }

            if (segment.getType() == 8) {
                attr = new ViewingAttr(HugoniotSegColor.LAX_SHOCK_2.getColor());
            }

//            if (segment.getType() == 9) {
//                attr = new ViewingAttr(new Color(128, 128, 128));
//            }
//
//            if (segment.getType() == 10) {
//                attr = new ViewingAttr(new Color(18, 153, 1));
//            }

            if (segment.getType() == 11) {
                attr = new ViewingAttr(HugoniotSegColor.EXPANSIVE_1.getColor());
            }

            if (segment.getType() == 12) {
                attr = new ViewingAttr(HugoniotSegColor.SUPER_COMPRESSIVE.getColor());
            }

//            if (segment.getType() == 13) {
//                attr = new ViewingAttr(new Color(128, 128, 128));
//            }

            if (segment.getType() == 14) {
                attr = new ViewingAttr(HugoniotSegColor.LAX_SHOCK_1.getColor());
            }

//            if (segment.getType() == 15) {
//                attr = new ViewingAttr(new Color(127, 127, 127));
//            }


        }


        //TODO SubInflection Curve segment type .

        if (segment.getType() == 16) {
            attr = new ViewingAttr(new Color(243, 123, 46));
        }


        //TODO Coincidence Curve segment type
        if (segment.getType() == 17) {
            attr = new ViewingAttr(new Color(20, 43, 140));
        }


        //TODO  BuckleyLeverettinflection Curve segment type
        if (segment.getType() == 18) {
            attr = new ViewingAttr(new Color(0, 255, 0));
        }
        return attr;
    }

    public enum HugoniotSegColor {
        //      255, 255, 255, //  0 = Left transport     COLOR = white      ----
//                                 255, 255, 255, //  1                                         +---
//                                 255,   0,   0, //  2 = Choque 2 LAX.      COLOR = red        -+--
//                                 247, 151,  55, //  3 = SUPER_COMPRESSIVE  COLOR = orange     ++--
//                                 255, 255, 255, //  4                                         --+-
//                                 255, 255, 255, //  5                                         +-+-
//                                 255, 255, 255, //  6                                         -++-
//                                 255, 255, 255, //  7                                         +++-
//                                 255,   0, 255, //  8 = EXPANSIVE 2        COLOR = pink       ---+
//                                 255, 255, 255, //  9                                         +--+
//                                  18, 153,   1, // 10 = TRANSITIONAL       COLOR = green      -+-+
//                                   0,   0, 255, // 11 = Choque 1 LAX.      COLOR = dark blue  ++-+
//                                 255, 255, 255, // 12 = SUPER_EXPANSIVE    COLOR = white      --++
//                                 255, 255, 255, // 13                                         +-++
//                                   0, 255, 255, // 14 = EXPANSIVE 1        COLOR = cyan       -+++
//                                 255, 255, 255, // 15 = Right transport    COLOR = white  
        
        
        LEFT_TRANSPORT(255,255,255), //0
        NONE1(0,0,0),          //1 
        LAX_SHOCK_2(255,0,0),    //2
        SUPER_COMPRESSIVE(247,151,55), //3
        NONE4(0,0,0), //4
        NONE5(0,0,0),//5
        NONE6(0,0,0), //6
        NONE7(0,0,0), //7
        EXPANSIVE_2(255,0,255), //8
        NONE9(0,0,0), //9
        TRANSITIONAL(18,153,1), //10
        LAX_SHOCK_1(0,0,255), //11
        SUPER_EXPANSIVE(255,255,0), //12
        NONE13(0,0,0), //13
        EXPANSIVE_1(0,255,255), //14
        RIGHT_TRANSPORT(127,127,127); //15
        
        
        private Color color_;
        
        HugoniotSegColor (int r, int g,int b){
            
            color_=new Color(r, g, b);
        }
        
        public Color getColor(){return color_;}
        
        
    }
}
