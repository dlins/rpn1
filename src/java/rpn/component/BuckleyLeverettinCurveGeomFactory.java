/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class BuckleyLeverettinCurveGeomFactory extends RpCalcBasedGeomFactory {

    public BuckleyLeverettinCurveGeomFactory(BuckleyLeverettinInflectionCurveCalc calc) {
        super(calc);

    }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        BuckleyLeverettInflectionCurve curve = (BuckleyLeverettInflectionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        return new BuckleyLeverettinInflectionGeom(hugoniotArray, this);

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
            {0, 204, 0},
            {135, 27, 224}
        };

        for (int i = 0; i < 19; i++) {

            for (int j = 0; j < 3; j++) {
                buffer.append("  " + toc[i][j] / 255.0 + " ");
            }
            buffer.append(";\n");
        }
        buffer.append("];\n\n");

        return buffer.toString();
    }

    public String toMatlab() {

        StringBuffer buffer = new StringBuffer();
        BuckleyLeverettInflectionCurve curve = (BuckleyLeverettInflectionCurve) geomSource();
        buffer.append("%%\nclose all;clear all;\n");
        buffer.append(createColorTable());
        buffer.append(curve.toMatlabData());

        buffer.append("%%\n% begin plot x y\n");
        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(curve.toMatlabPlot(1, 0));


//        buffer.append("\n%%\n% begin plot x z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append(curve.toMatlabPlot(2, 0));
//
//        buffer.append("\n%%\n% begin plot y z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append(curve.toMatlabPlot(2, 1));

        return buffer.toString();

    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTCALC xzero=\"" + ((HugoniotCurve) geomSource()).getXZero() + "\"" + " methodname=\"" + ShockProfile.instance().getHugoniotMethodName() + "\"" + " flowname=\"" + RPNUMERICS.getShockProfile().getFlowName() + "\"" + ">\n");

        buffer.append(((HugoniotCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

        buffer.append("</HUGONIOTCALC>\n");

        return buffer.toString();

    }
}
