package rpn.component;

import java.awt.Color;
import rpnumerics.HugoniotSegment;

import wave.util.*;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;

public class RealSegGeom extends MultiPolyLine {

    public RealSegGeom(RealSegment segment) {
        super(new CoordsArray[]{new CoordsArray(segment.p1()), new CoordsArray(segment.p2())}, new ViewingAttr(Color.white));
    }

    public RealSegGeom(RealSegment segment,ViewingAttr viewingAtt) {
        super(new CoordsArray[]{new CoordsArray(segment.p1()), new CoordsArray(segment.p2())}, viewingAtt);
    }



    public RealSegGeom(HugoniotSegment hsegment) {

        super(new CoordsArray[]{new CoordsArray(hsegment.p1()), new CoordsArray(hsegment.p2())}, new ViewingAttr(Color.white));

    }



}
