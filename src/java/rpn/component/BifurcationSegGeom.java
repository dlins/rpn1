package rpn.component;

import java.awt.Color;
import javax.vecmath.Color3b;
import rpnumerics.HugoniotSegment;

import wave.util.*;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;

public class BifurcationSegGeom extends MultiPolyLine {


	public BifurcationSegGeom(RealSegment segment) {
		super(new CoordsArray[] { new CoordsArray(segment.p1()), new CoordsArray(segment.p2())}, new ViewingAttr(Color.white));


	}

        public BifurcationSegGeom (HugoniotSegment hsegment){

            super(new CoordsArray[]{new CoordsArray(hsegment.p1()), new CoordsArray(hsegment.p2())}, new ViewingAttr(selectColor(hsegment)));

        }

        private static Color selectColor (HugoniotSegment hsegment){
            if (hsegment.getType()==15)

                return Color.yellow;

            if (hsegment.getType()==16)
                return Color.green;

            return Color.white;
        }





}
