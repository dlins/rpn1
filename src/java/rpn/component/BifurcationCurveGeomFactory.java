package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;
import wave.util.*;

public class BifurcationCurveGeomFactory extends RpCalcBasedGeomFactory {

    private RpGeometry leftGeom_;
    private RpGeometry rightGeom_;

    private HashMap<BifurcationCurveGeomSide, List<RealSegment>> segmentsMap_;

    public BifurcationCurveGeomFactory(ContourCurveCalc calc) {

        super(calc);

    }

    public BifurcationCurveGeomFactory(ContourCurveCalc calc, RpSolution curve) {

        super(calc, curve);

    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        // TODO confirm that all geomSource() calls will return a SegmentedCurve instance type
        BifurcationCurve geomSource = (BifurcationCurve) geomSource();

        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim()) + '\"';

        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<" + BifurcationCurve.XML_TAG + " curve_name=" + ' ' + curve_name + ' ' + "dimension=" + ' ' + dimension + ' ' + "format_desc=\"1 segment per row\">" + "\n");

        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        if (rpCalc().getConfiguration() != null) {
            buffer.append(rpCalc().getConfiguration().toXML());
        }

        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        buffer.append("<" + BifurcationCurve.LEFT_TAG + ">" + "\n");

        for (int i = 0; i < geomSource.leftSegments().size(); i++) {

            RealSegment realSegment = (RealSegment) geomSource.leftSegments().get(i);
            buffer.append(realSegment.toXML());
        }
        buffer.append("</" + BifurcationCurve.LEFT_TAG + ">" + "\n");

        buffer.append("<" + BifurcationCurve.RIGHT_TAG + ">" + "\n");

        for (int i = 0; i < geomSource.rightSegments().size(); i++) {

            RealSegment realSegment = (RealSegment) geomSource.rightSegments().get(i);
            buffer.append(realSegment.toXML());
        }
        buffer.append("</" + BifurcationCurve.RIGHT_TAG + ">" + "\n");
        buffer.append("</" + BifurcationCurve.XML_TAG + ">");

        return buffer.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    static ViewingAttr leftViewingAttr() {
//        return leftViewingAtt;
//    }
//
//    static ViewingAttr rightViewingAttr() {
//        return rightViewingAttr;
//    }

    void updateGeomSource(List<Area> areaListToRifine) {
    }

    @Override
    public void updateGeom(List<Area> areaListToRefine, List<Integer> segmentsToRemove) {

        System.out.println("updateGeom de Bifurcation...");

        List<RealSegment> segRem = new ArrayList<RealSegment>();
        List<RealSegment> segRemLeft = new ArrayList<RealSegment>();

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        isGeomOutOfDate_ = true;

        // --- 17JAN : permite remover corretamente, mesmo atuando sobre o painel principal
        for (Integer i : segmentsToRemove) {
            if (UIController.instance().isAuxPanelsEnabled()) {
                segRem.add(((BifurcationCurve) curve).rightSegments().get(i));
                segRemLeft.add(((BifurcationCurve) curve).leftSegments().get(i));
            } else {
                int j = 0;
                if (i >= curve.segments().size() / 2) {
                    j = i - curve.segments().size() / 2;
                    segRem.add(((BifurcationCurve) curve).rightSegments().get(j));
                    segRemLeft.add(((BifurcationCurve) curve).leftSegments().get(j));
                } else {
                    segRem.add(((BifurcationCurve) curve).rightSegments().get(i));
                    segRemLeft.add(((BifurcationCurve) curve).leftSegments().get(i));
                }
            }
        }
        // ---------------

        ((BifurcationCurve) curve).rightSegments().removeAll(segRem);
        ((BifurcationCurve) curve).leftSegments().removeAll(segRemLeft);
        segRem.addAll(segRemLeft);
        curve.segments().removeAll(segRem);

        System.out.println("Segmentos removidos: " + segRem.size());

        updateGeomSource(areaListToRefine);

        System.out.println("updateGeom : segmentos removidos ::: " + segRem.size());

        RPnDataModule.LEFTPHASESPACE.update();
        isGeomOutOfDate_ = true;
        RPnDataModule.RIGHTPHASESPACE.update();

        geom_ = createGeomFromSource();

        isGeomOutOfDate_ = true;
        RPnDataModule.PHASESPACE.update();

    }

    @Override
    public RpGeometry createGeomFromSource() {

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        segmentsMap_ = new HashMap<BifurcationCurveGeomSide, List<RealSegment>>();

        RealSegGeom[] bifurcationArrayRight = new RealSegGeom[curve.rightSegments().size()];
        RealSegGeom[] bifurcationArrayLeft = new RealSegGeom[curve.leftSegments().size()];

        System.out.println("curve.segments().size() ::::::: " + curve.segments().size());
        System.out.println("curve.rightSegments().size() :: " + curve.rightSegments().size());
        System.out.println("curve.leftSegments().size() ::: " + curve.leftSegments().size());

        ViewingAttr leftViewingAtt = new ViewingAttr(Color.yellow);
        ViewingAttr rightViewingAttr = new ViewingAttr(Color.magenta);

        int i = 0;
        for (Object realSegment : curve.rightSegments()) {
            bifurcationArrayRight[i] = new RealSegGeom((RealSegment) realSegment, rightViewingAttr);

            i++;

        }
        System.out.println("createGeomFromSource() : valor de i ::: " + i);

        int j = 0;
        for (Object realSegment : curve.leftSegments()) {
            bifurcationArrayLeft[j] = new RealSegGeom((RealSegment) realSegment, leftViewingAtt);

            j++;
        }
        System.out.println("createGeomFromSource() : valor de j ::: " + j);

        System.out.println("createGeomFromSource() : valor de i+j ::: " + (i + j));

        BifurcationCurveGeomSide leftBifurcationGeom = new BifurcationCurveGeomSide(bifurcationArrayLeft, this);
        BifurcationCurveGeomSide rightBifurcationGeom = new BifurcationCurveGeomSide(bifurcationArrayRight, this);

        segmentsMap_.put(leftBifurcationGeom, curve.leftSegments());
        segmentsMap_.put(rightBifurcationGeom, curve.rightSegments());
        leftBifurcationGeom.setOtherSide(rightBifurcationGeom);
        rightBifurcationGeom.setOtherSide(leftBifurcationGeom);

        List<BifurcationCurveBranchGeom> branches = new ArrayList<BifurcationCurveBranchGeom>();

        branches.add(leftBifurcationGeom);
        branches.add(rightBifurcationGeom);
        return new BifurcationCurveGeom(branches, this);

    }

    public List<RealSegment> segmentsList(BifurcationCurveGeomSide side) {
        return segmentsMap_.get(side);
    }

    public RpGeometry leftGeom() {
        return leftGeom_;
    }

    public RpGeometry rightGeom() {
        return rightGeom_;
    }
}
