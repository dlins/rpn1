/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class ExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public ExtensionCurveGeomFactory(ExtensionCurveCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        ExtensionCurve curve = (ExtensionCurve) geomSource();

        BifurcationSegGeom[] leftBifurcationSegArray = null;

        int resultSize = curve.segments().size();

        leftBifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            leftBifurcationSegArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));

        }

        return new ExtensionCurveGeom(leftBifurcationSegArray, this);

    }

/
    public String toMatlab(int curveIndex) {
//        RealVector xMin = RPNUMERICS.boundary().getMinimums();
//        RealVector xMax = RPNUMERICS.boundary().getMaximums();
//
//
//        StringBuffer buffer = new StringBuffer();
//        HugoniotCurve curve = (HugoniotCurve) geomSource();
//        buffer.append("%%\nclose all;clear all;\n");
//
//        buffer.append(curve.toMatlabData(0));
//
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append("axis([" + xMin.getElement(1) + " " + xMax.getElement(1) + " " + xMin.getElement(0) + " " + xMax.getElement(0) + "]);\n");
//        buffer.append(curve.createMatlabPlotLoop(2, 1, 0));
//        buffer.append(SegmentedCurve.createAxisLabel2D(1, 0));
//
//
//
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append("axis([" + xMin.getElement(2) + " " + xMax.getElement(2) + " " + xMin.getElement(0) + " " + xMax.getElement(0) + "]);\n");
//        buffer.append(curve.createMatlabPlotLoop(3, 1, 0));
//        buffer.append(SegmentedCurve.createAxisLabel2D(2, 0));
//
//        return buffer.toString();
        return null;
//
    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        buffer.append("<DOUBLECONTACT>\n");

        buffer.append(curve.toXML());

        buffer.append("</DOUBLECONTACT>\n");

        return buffer.toString();

    }
}
