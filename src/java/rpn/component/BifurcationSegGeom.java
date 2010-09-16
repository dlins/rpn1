package rpn.component;

import java.awt.Color;

import wave.util.*;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;

public class BifurcationSegGeom extends MultiPolyLine {

	public BifurcationSegGeom(RealSegment segment) {
		super(new CoordsArray[] { new CoordsArray(segment.p1()), new CoordsArray(segment.p2())}, new ViewingAttr(Color.white));
	}
}
