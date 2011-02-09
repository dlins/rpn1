/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import rpn.controller.HugoniotController;
import rpn.controller.RpController;
import wave.util.RealVector;

public class HugoniotCurveGeomFactory extends RpCalcBasedGeomFactory {

    public HugoniotCurveGeomFactory(HugoniotCurveCalc calc) {
        super(calc);

    }

    public HugoniotCurveGeomFactory(ShockCurveCalc calc) {
        super(calc);

    }

    @Override
    protected RpController createUI() {
        return new HugoniotController();
    }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        HugoniotCurve curve = (HugoniotCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        return new HugoniotCurveGeom(hugoniotArray, this);

    }

   

//    private String createAxisLabel2D(int x, int y) {
//
//        String axisName[] = new String[3];
//
//        axisName[0] = "s";
//        axisName[1] = "T";
//        axisName[2] = "u";
//
//
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("xlabel('");
//        buffer.append(axisName[x] + "')\n");
//        buffer.append("ylabel('" + axisName[y] + "')\n");
//
//        return buffer.toString();
//    }

  
    private String createAxisLabel3D(int x, int y, int z) {

        String axisName[] = new String[3];

        axisName[0] = "s";
        axisName[1] = "T";
        axisName[2] = "u";


        StringBuffer buffer = new StringBuffer();
        buffer.append("xlabel('");
        buffer.append(axisName[x] + "')\n");
        buffer.append("ylabel('" + axisName[y] + "')\n");

        buffer.append("zlabel('" + axisName[z] + "')\n");

        return buffer.toString();



    }

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
//
//
//        return buffer.toString();
        return null;
//
    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTCALC xzero=\"" + ((HugoniotCurve) geomSource()).getXZero() + "\"" + " methodname=\"" + ShockProfile.instance().getHugoniotMethodName() + "\"" + " flowname=\"" + RPNUMERICS.getShockProfile().getFlowName() + "\"" + ">\n");

        buffer.append(((HugoniotCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

        buffer.append("</HUGONIOTCALC>\n");

        return buffer.toString();

    }
}
