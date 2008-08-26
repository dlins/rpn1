package rpn.component;

import rpnumerics.*;


public class BifurcationCurveGeomFactory extends RpCalcBasedGeomFactory {
    public BifurcationCurveGeomFactory(BifurcationCurveCalc calc) {
        super(calc);
    }

   
    protected RpGeometry createGeomFromSource() {

        BifurcationCurve curve = (BifurcationCurve)geomSource();

        return new BifurcationCurveGeom(curve,this);
    }
    
    public String toXML() {
        return "";
    }
}
