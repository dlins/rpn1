/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class ShockCurveGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //
    public ShockCurveGeomFactory(ShockCurveCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {
        ShockCurve shockCurve = (ShockCurve) geomSource();

        return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(shockCurve.getPoints()), this);
    }

    public String toXML() {//TODO Implement
        StringBuffer str = new StringBuffer();
         String tdir = "pos";
        if (((ShockCurveCalc)rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR)
            tdir = "neg";
        str.append("<SHOCKCURVECALC tdirection=\"" + tdir + "\" calcready=\""+rpn.parser.RPnDataModule.RESULTS+"\">\n");
        if (!rpn.parser.RPnDataModule.RESULTS)
            str.append(((ShockCurve)geomSource()).getPoints() [0].toXML());
        str.append(((ShockCurve)geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
        str.append("</SHOCKCURVECALC>\n");
        return str.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
