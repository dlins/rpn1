package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnDesktopPlotter;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;
import wave.util.*;

public class BifurcationCurveGeomFactory extends RpCalcBasedGeomFactory {

    private RpGeometry leftGeom_;
    private RpGeometry rightGeom_;

    public BifurcationCurveGeomFactory(ContourCurveCalc calc) {
        super(calc);
        try {
            leftGeom_ = createLeftGeom();
            rightGeom_ = createRightGeom();
            ((BifurcationCurveGeom) leftGeom_).setOtherSide(rightGeom_);
            ((BifurcationCurveGeom) rightGeom_).setOtherSide(leftGeom_);
        } catch (RpException ex) {
            RPnDesktopPlotter.showCalcExceptionDialog(ex);

        }


    }

    public BifurcationCurveGeomFactory(ContourCurveCalc calc, RpSolution curve) {
        super(calc, curve);
        try {
            leftGeom_ = createLeftGeom();
            rightGeom_ = createRightGeom();
            ((BifurcationCurveGeom) leftGeom_).setOtherSide(rightGeom_);
            ((BifurcationCurveGeom) rightGeom_).setOtherSide(leftGeom_);
        } catch (RpException ex) {
            RPnDesktopPlotter.showCalcExceptionDialog(ex);
        }

    }

    public String toXML() {
        StringBuffer buffer = new StringBuffer();

        String commandName = geomSource().getClass().getName();
        commandName = commandName.toLowerCase();
        commandName = commandName.replaceAll(".+\\.", "");

        ContourCurveCalc calc = ((ContourCurveCalc) rpCalc());

        ContourParams params = calc.getParams();

        buffer.append("<COMMAND name=\"" + commandName + "\" ");

        buffer.append(params.toString());


        return buffer.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected ViewingAttr leftViewingAttr() {
        return new ViewingAttr(Color.yellow);
    }

    protected ViewingAttr rightViewingAttr() {
        return new ViewingAttr(Color.magenta);
    }

    protected RpGeometry createLeftGeom() throws RpException {

        BifurcationCurve curve = (BifurcationCurve) geomSource();
        if (curve == null) {
            throw new RpException("Bifurcation curve empty");
        }
        RealSegGeom[] bifurcationArray = new RealSegGeom[curve.leftSegments().size()];

        for (int i = 0; i < curve.leftSegments().size(); i++) {
            bifurcationArray[i] = new RealSegGeom((RealSegment) curve.leftSegments().get(i), leftViewingAttr());

        }
        return new BifurcationCurveGeom(bifurcationArray, this);

    }

    protected RpGeometry createRightGeom() throws RpException {

        BifurcationCurve curve = (BifurcationCurve) geomSource();
        RealSegGeom[] bifurcationArray = new RealSegGeom[curve.rightSegments().size()];
        if (curve == null) {
            throw new RpException("Bifurcation curve empty");
        }
        for (int i = 0; i < curve.rightSegments().size(); i++) {
            bifurcationArray[i] = new RealSegGeom((RealSegment) curve.rightSegments().get(i), rightViewingAttr());
        }
        return new BifurcationCurveGeom(bifurcationArray, this);
    }

    @Override
    public void updateGeom() {
        try {
            super.updateGeom();
            leftGeom_ = createLeftGeom();
            rightGeom_ = createRightGeom();
            ((BifurcationCurveGeom) leftGeom_).setOtherSide(rightGeom_);
            ((BifurcationCurveGeom) rightGeom_).setOtherSide(leftGeom_);
        } catch (RpException ex) {
            RPnDesktopPlotter.showCalcExceptionDialog(ex);
        }

    }

    // ----------------------------------- NAO HAVIA ATÉ 31/10
    @Override
    public void updateGeom(List<Area> areaToRefine, List<Integer> segmentsToRemove) {

        System.out.println("updateGeom de Bifurcation...");

        List<RealSegment> segRem = new ArrayList<RealSegment>();
        List<RealSegment> segRemLeft = new ArrayList<RealSegment>();

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        isGeomOutOfDate_ = true;

        for (Integer i : segmentsToRemove) {
            segRem.add(((BifurcationCurve) curve).rightSegments().get(i));
            segRemLeft.add(((BifurcationCurve) curve).leftSegments().get(i));
        }

        ((BifurcationCurve) curve).rightSegments().removeAll(segRem);
        ((BifurcationCurve) curve).leftSegments().removeAll(segRemLeft);
        segRem.addAll(segRemLeft);
        curve.segments().removeAll(segRem);



            try {
                RPnCurve newCurve = (RPnCurve) calc_.recalc(areaToRefine);
                ((RPnCurve) geomSource_).segments().addAll(newCurve.segments());
            } catch (RpException ex) {
                ex.printStackTrace();
            }

        




        try {
            leftGeom_ = createLeftGeom();
            rightGeom_ = createRightGeom();

        } catch (RpException ex) {
            Logger.getLogger(BifurcationCurveGeomFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        RPnDataModule.LEFTPHASESPACE.update();
        isGeomOutOfDate_ = true;
        RPnDataModule.RIGHTPHASESPACE.update();

        geom_ = createGeomFromSource();

        isGeomOutOfDate_ = true;
        RPnDataModule.PHASESPACE.update();

    }
    // -----------------------------------

    public RpGeometry createGeomFromSource() {

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        RealSegGeom[] bifurcationArray = new RealSegGeom[curve.segments().size()];

        int i = 0;
        for (Object realSegment : curve.segments()) {

            bifurcationArray[i] = new RealSegGeom((RealSegment) realSegment, leftViewingAttr());
            i++;
        }


        return new BifurcationCurveGeom(bifurcationArray, this);

    }
//    public RpGeometry createGeomFromSource() {
//
//        BifurcationCurve curve = (BifurcationCurve) geomSource();
//
//        RealSegGeom[] bifurcationArray = new RealSegGeom[curve.segments().size()];
//
//        RealSegGeom[] bifurcationArrayRight = new RealSegGeom[curve.rightSegments().size()];
//        RealSegGeom[] bifurcationArrayLeft = new RealSegGeom[curve.leftSegments().size()];
//
//        System.out.println("curve.segments().size() ::::::: " + curve.segments().size());
//        System.out.println("curve.rightSegments().size() :: " + curve.rightSegments().size());
//        System.out.println("curve.leftSegments().size() ::: " + curve.leftSegments().size());
//
//        int i = 0;
//        for (Object realSegment : curve.rightSegments()) {
//            bifurcationArrayRight[i] = new RealSegGeom((RealSegment) realSegment, rightViewingAttr());
//            bifurcationArray[i] = bifurcationArrayRight[i];
//            i++;
//        }
//
//        int j = 0;
//        for (Object realSegment : curve.leftSegments()) {
//            bifurcationArrayLeft[j] = new RealSegGeom((RealSegment) realSegment, leftViewingAttr());
//            bifurcationArray[i + j] = bifurcationArrayLeft[j];
//            j++;
//        }
//
//
//        return new BifurcationCurveGeom(bifurcationArray, this);
//
//    }

    public RpGeometry leftGeom() {
        return leftGeom_;
    }

    public RpGeometry rightGeom() {
        return rightGeom_;
    }
}
