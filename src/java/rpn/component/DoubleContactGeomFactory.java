/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

public class DoubleContactGeomFactory extends BifurcationCurveGeomFactory {

    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.white);


    public DoubleContactGeomFactory(DoubleContactCurveCalc calc, DoubleContactCurve curve) {
        super(calc,curve);
    }

    public DoubleContactGeomFactory(DoubleContactCurveCalc calc) {
        super(calc);
        //System.out.println("Tamanho de calc em DoubleContactGeomFactory : " +((SegmentedCurve)calc.calc()).segments().size());
    }

    //
    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        DoubleContactCurve curve = (DoubleContactCurve) geomSource();

        BifurcationSegGeom[] leftBifurcationSegArray = null;

        int resultSize = curve.segments().size();
        System.out.println("Tamanho da curve em DoubleContactGeomFactory , metodo createGeomFromSource() : " +resultSize);        //*******************************8

        leftBifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            leftBifurcationSegArray[i] = new BifurcationSegGeom((RealSegment) curve.segments().get(i),viewAtt_);

        }
        
        return new DoubleContactCurveGeom(leftBifurcationSegArray, this);
        
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

    @Override
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        DoubleContactCurveCalc doubleContactCalc = (DoubleContactCurveCalc)rpCalc();

        buffer.append(" curvefamily=\""+doubleContactCalc.getCurveFamily()+"\""+" domainfamily=\""+doubleContactCalc.getDomainFamily()+"\""+">\n");

        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
