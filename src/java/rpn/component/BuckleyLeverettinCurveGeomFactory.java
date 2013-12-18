/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import wave.util.RealSegment;

public class BuckleyLeverettinCurveGeomFactory extends BifurcationCurveGeomFactory {

    public BuckleyLeverettinCurveGeomFactory(BuckleyLeverettinInflectionCurveCalc calc) {
        super(calc);

    }
    
     public BuckleyLeverettinCurveGeomFactory(BuckleyLeverettinInflectionCurveCalc calc, BuckleyLeverettInflectionCurve curve) {
        super(calc,curve);

    }

    //
    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        BuckleyLeverettInflectionCurve curve = (BuckleyLeverettInflectionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));
        }
        return new BuckleyLeverettinInflectionGeom(hugoniotArray, this);

    }

    @Override
    public String toMatlab(int curveIndex) {

        StringBuilder buffer = new StringBuilder();
        BuckleyLeverettInflectionCurve curve = (BuckleyLeverettInflectionCurve) geomSource();
        buffer.append("%%\nclose all;clear all;\n");
        //buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
        buffer.append((curve.toMatlabData(0)));

        buffer.append(curve.createMatlabPlotLoop(0, 1, 0));

        return buffer.toString();

    }
}
