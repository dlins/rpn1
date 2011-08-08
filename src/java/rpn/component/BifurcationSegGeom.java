package rpn.component;

import java.awt.Color;
import rpnumerics.HugoniotSegment;

import wave.util.*;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;

public class BifurcationSegGeom extends MultiPolyLine {

    public BifurcationSegGeom(RealSegment segment) {
        super(new CoordsArray[]{new CoordsArray(segment.p1()), new CoordsArray(segment.p2())}, new ViewingAttr(Color.white));
    }

    public BifurcationSegGeom(HugoniotSegment hsegment) {

        super(new CoordsArray[]{new CoordsArray(hsegment.p1()), new CoordsArray(hsegment.p2())}, new ViewingAttr(Color.white));

    }

    private static Color selectColor(HugoniotSegment hsegment) {
        if (hsegment.getType() == 18) {
            return Color.GREEN;
        }

        if (hsegment.getType() == 19) {
            return new Color(248, 17, 47);
        }

        if (hsegment.getType() == 20) {
            return new Color(0, 153, 153);
        }

        return  Color.white;
    }
}
