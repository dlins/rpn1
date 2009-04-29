package rpn.component;

import rpnumerics.*;
import wave.util.*;


public class BifurcationCurveGeomFactory extends RpCalcBasedGeomFactory {
    public BifurcationCurveGeomFactory(BifurcationCurveCalc calc) {
        super(calc);
    }

   
    protected RpGeometry createGeomFromSource() {

        BifurcationCurve curve = (BifurcationCurve)geomSource();

        int resultSize = curve.segments().size();

        BifurcationSegGeom[] bifurcationArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationArray[i] = new BifurcationSegGeom((RealSegment) curve.segments().get(i));
        }
        
        return new BifurcationCurveGeom(bifurcationArray,this);
    }
    
    public String toXML() {
    	 StringBuffer buffer = new StringBuffer();

         buffer.append("<BIFURCATIONCALC xzero=\"" + ((HugoniotCurve) geomSource()).getXZero() + "\"" + " methodname=\"" + ShockProfile.instance().getHugoniotMethodName() + "\"" + " flowname=\"" + RPNUMERICS.getShockProfile().getFlowName() + "\"" + ">\n");

         buffer.append(((BifurcationCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
         
         buffer.append("</BIFURCATIONCALC>\n");

         return buffer.toString();
    }
}
