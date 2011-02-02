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

    private String createColorTable() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("toc=[");


        double toc[][] = {{255, 255, 255},
            {255, 255, 255},
            {255, 0, 0},
            {247, 151, 55},
            {255, 255, 255},
            {255, 255, 255},
            {255, 255, 255},
            {255, 255, 255},
            {255, 0, 255},
            {255, 255, 255},
            {18, 153, 1},
            {0, 0, 255},
            {255, 255, 255},
            {255, 255, 255},
            {0, 255, 255},
            {255, 255, 255}
        };

        for (int i = 0; i < 16; i++) {

            for (int j = 0; j < 3; j++) {
                buffer.append("  " + toc[i][j] / 255.0 + " ");
            }
            buffer.append(";\n");
        }
        buffer.append("];\n\n");

        return buffer.toString();
    }

    private String createAxisLabel2D(int x, int y) {

        String axisName[] = new String[3];

        axisName[0] = "s";
        axisName[2] = "T";
        axisName[1] = "u";


        StringBuffer buffer = new StringBuffer();
        buffer.append("xlabel('");
        buffer.append(axisName[x] + "')\n");
        buffer.append("ylabel('" + axisName[y] + "')\n");

        return buffer.toString();



    }

    private String createMatlabFor(int x, int y, int identifier) {
        int dimension = RPNUMERICS.domainDim();
        StringBuffer buffer = new StringBuffer();

        buffer.append("for i=1: length(data" + identifier + ")\n");
        buffer.append("plot([ data" + identifier);

        buffer.append("(i" + "," + x + ") ");
        buffer.append("data" + identifier + "(i," + (x + dimension) + ")],");

        buffer.append("[ data" + identifier);

        buffer.append("(i" + "," + y + ") ");
        buffer.append("data" + identifier + "(i," + (y + dimension) + ")],");
        buffer.append("'Color',");
        buffer.append("[toc(type" + identifier + "(i), 1) toc(type" + identifier + "(i), 2) toc(type" + identifier + "(i), 3)])\n");

        buffer.append("hold on\n");

        buffer.append("end\n");

        return buffer.toString();

//        figure(1) % Assume figure(1) corresponds to the x-y projection
//xlabel('x')
//ylabel('y')
//for i = 1: length(data0)
//    plot([data0(i, 1) data0(i, 4)], [data0(i, 2) data0(i, 5)])
//    hold on
//end
    }

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

    public String toMatlab() {
        RealVector xMin = RPNUMERICS.boundary().getMinimums();
        RealVector xMax = RPNUMERICS.boundary().getMaximums();


        StringBuffer buffer = new StringBuffer();
        HugoniotCurve curve = (HugoniotCurve) geomSource();
        buffer.append("%%\nclose all;clear all;\n");
        buffer.append(createColorTable());
        buffer.append(curve.toMatlabData(0));

        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append("axis([" + xMin.getElement(1) + " " + xMax.getElement(1) + " " + xMin.getElement(0) + " " + xMax.getElement(0) + "]);\n");
        buffer.append(createMatlabFor(2, 1, 0));
        buffer.append(createAxisLabel2D(1, 0));

        
        
        
        
        
        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append("axis([" + xMin.getElement(2) + " " + xMax.getElement(2) + " " + xMin.getElement(0) + " " + xMax.getElement(0) + "]);\n");
        buffer.append(createMatlabFor(3, 1, 0));
        buffer.append(createAxisLabel2D(2, 0));

//        buffer.append("%%\n% begin plot x y\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
////         buffer.append("set(figure,'Name','x y')");
////        buffer.append("xlabel('s')\n");
////        buffer.append("ylabel('T')");
//
//        buffer.append(createAxisLabel2D(1, 0));
//        buffer.append(curve.toMatlabPlot(1, 0));
//
//        buffer.append("\n%%\n% begin plot x z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
////         buffer.append("set(figure,'Name','x y')");
//        buffer.append(createAxisLabel2D(0, 2));
//        buffer.append(curve.toMatlabPlot(0, 2));
//
//        buffer.append("\n%%\n% begin plot y z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
////         buffer.append("set(figure,'Name','x y')");
//        buffer.append(createAxisLabel2D(1, 2));
//        buffer.append(curve.toMatlabPlot(1, 2));

//        // Modified from here...
//        buffer.append("\n%%\n% begin plot3d\n");
//        buffer.append("figure; ");
//        //set(gca, 'Color',[0 0 0]); hold on\n");
//
//        buffer.append(curve.toMatlabData(0));
//        buffer.append(createAxisLabel3D(0, 1, 2));

        // ...to here.

        return buffer.toString();

    }

//    public String toMatlab() {
//
//        StringBuffer buffer = new StringBuffer();
//        HugoniotCurve curve = (HugoniotCurve) geomSource();
//        buffer.append("%%\nclose all;clear all;\n");
//        buffer.append(createColorTable());
//        buffer.append(curve.toMatlabData());
//
//        buffer.append("%%\n% begin plot x y\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append(curve.toMatlabPlot(0, 1));
//
//
//        buffer.append("\n%%\n% begin plot x z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append(curve.toMatlabPlot(0, 2));
//
//        buffer.append("\n%%\n% begin plot y z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append(curve.toMatlabPlot(1, 2));
//
//        return buffer.toString();
//
//    }
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTCALC xzero=\"" + ((HugoniotCurve) geomSource()).getXZero() + "\"" + " methodname=\"" + ShockProfile.instance().getHugoniotMethodName() + "\"" + " flowname=\"" + RPNUMERICS.getShockProfile().getFlowName() + "\"" + ">\n");

        buffer.append(((HugoniotCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

        buffer.append("</HUGONIOTCALC>\n");

        return buffer.toString();

    }
}
