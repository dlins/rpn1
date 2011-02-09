/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

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
//    public String toMatlab(int curveIndex) {
//
//
//        RealVector xMin = RPNUMERICS.boundary().getMinimums();
//        RealVector xMax = RPNUMERICS.boundary().getMaximums();
//
////
////        StringBuffer buffer = new StringBuffer();
////       SubInflectionCurve curve = (SubInflectionCurve) geomSource();
////        buffer.append("%%\nclose all;clear all;\n");
////        buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
////        buffer.append(curve.toMatlabData(0));
////
////        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
////        buffer.append("axis([" + xMin.getElement(1) + " " + xMax.getElement(1) + " " + xMin.getElement(0) + " " + xMax.getElement(0) + "]);\n");
////        buffer.append(curve.createMatlabPlotLoop(2, 1, 0));
//////        buffer.append(createAxisLabel2D(1, 0));
////
////
////
////        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
////        buffer.append("axis([" + xMin.getElement(2) + " " + xMax.getElement(2) + " " + xMin.getElement(0) + " " + xMax.getElement(0) + "]);\n");
////        buffer.append(curve.createMatlabPlotLoop(3, 1, 0));
//////        buffer.append(createAxisLabel2D(2, 0));
//
//
//        StringBuffer buffer = new StringBuffer();
//        System.out.println("Chamando geom factory de sub inflection");
//        buffer.append("%%\nclose all;clear all;\n");
//
//        SubInflectionCurve curve = (SubInflectionCurve) geomSource();
//
//        buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
//        buffer.append(curve.toMatlabData(0));
//
//
//        buffer.append(curve.createMatlabPlotLoop(0,1,0));
//
//
//        return buffer.toString();
//
//    }


//

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTCALC xzero=\"" + ((HugoniotCurve) geomSource()).getXZero() + "\"" + " methodname=\"" + ShockProfile.instance().getHugoniotMethodName() + "\"" + " flowname=\"" + RPNUMERICS.getShockProfile().getFlowName() + "\"" + ">\n");

        buffer.append(((HugoniotCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

        buffer.append("</HUGONIOTCALC>\n");

        return buffer.toString();

    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
