/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import rpn.controller.HugoniotController;
import rpn.controller.RpController;

public class SubInflectionCurveGeomFactory extends RpCalcBasedGeomFactory {

    public SubInflectionCurveGeomFactory(SubInflectionCurveCalc calc) {
        super(calc);

    }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        SubInflectionCurve curve = (SubInflectionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        return new SubInflectionCurveGeom(hugoniotArray, this);

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
            {255, 255, 255},
            {255, 255, 0},
             {0, 204, 0}
        };

        for (int i = 0; i < 18; i++) {

            for (int j = 0; j < 3; j++) {
                buffer.append("  " + toc[i][j] / 255.0 + " ");
            }
            buffer.append(";\n");
        }
        buffer.append("];\n\n");

        return buffer.toString();
    }



//    private String createMatlabFor(int x, int y,int identifier){
//        int dimension = RPNUMERICS.domainDim();
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("xlabel('"+x+"')");
//        buffer.append("ylabel('" + x + "')");
//        buffer.append("for i=1: length(data" + identifier + ")");
//        buffer.append("plot([ data"+identifier);
//
//        buffer.append("(i"+","+x+") ");
//        buffer.append("data" + identifier + "(i," + (x+dimension) + ")],");
//
//        buffer.append("[ data"+identifier);
//
//        buffer.append("(i"+","+y+") ");
//        buffer.append("data" + identifier + "(i," + (y+dimension) + ")])");
//
//        buffer.append("hold on");
//
////        figure(1) % Assume figure(1) corresponds to the x-y projection
////xlabel('x')
////ylabel('y')
////for i = 1: length(data0)
////    plot([data0(i, 1) data0(i, 4)], [data0(i, 2) data0(i, 5)])
////    hold on
////end
//    }
    public String toMatlab() {

        StringBuffer buffer = new StringBuffer();
        SubInflectionCurve curve = (SubInflectionCurve) geomSource();
        buffer.append("%%\nclose all;clear all;\n");
        buffer.append(createColorTable());
        buffer.append(curve.toMatlabData(0));

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

        return buffer.toString();

    }


//

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTCALC xzero=\"" + ((HugoniotCurve) geomSource()).getXZero() + "\"" + " methodname=\"" + ShockProfile.instance().getHugoniotMethodName() + "\"" + " flowname=\"" + RPNUMERICS.getShockProfile().getFlowName() + "\"" + ">\n");

        buffer.append(((HugoniotCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

        buffer.append("</HUGONIOTCALC>\n");

        return buffer.toString();

    }
}
