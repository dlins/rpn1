/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class SubInflectionExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public SubInflectionExtensionCurveGeomFactory(SubInflectionExtensionCurveCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

       SubInflectionExtensionCurve curve = (SubInflectionExtensionCurve) geomSource();

        HugoniotSegGeom[] bifurcationSegArray = null;

        int resultSize = curve.segments().size();

        bifurcationSegArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationSegArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));

        }

        return new SubInflectionExtensionCurveGeom(bifurcationSegArray, this);

    }

//    public List<RpGeometry> createGeometriesFromSource() {
//
//        DoubleContactCurve curve = (DoubleContactCurve) geomSource();
//        List<RpGeometry> result = new ArrayList<RpGeometry>();
//        // assuming a container with HugoniotSegment elements
//        int leftResultSize = curve.leftSegments().size();
//
//        int rightResultSize = curve.rightSegments().size();
//
//        HugoniotSegGeom[] leftHugoniotArray = new HugoniotSegGeom[leftResultSize];
//        for (int i = 0; i < leftResultSize; i++) {
//            leftHugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.leftSegments().get(i));
//        }
//
//        DoubleContactCurveGeom leftGeom = new DoubleContactCurveGeom(leftHugoniotArray, this);
//
//        result.add(leftGeom);
//
//        HugoniotSegGeom[] rightHugoniotArray = new HugoniotSegGeom[leftResultSize];
//        for (int i = 0; i < rightResultSize; i++) {
//            rightHugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.rightSegments().get(i));
//        }
//
//        DoubleContactCurveGeom rightGeom = new DoubleContactCurveGeom(rightHugoniotArray, this);
//
//        result.add(rightGeom);
//
//        return result;
//
//
//    }
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
