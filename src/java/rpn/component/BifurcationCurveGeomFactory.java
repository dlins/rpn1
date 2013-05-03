package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnDesktopPlotter;
import rpn.controller.ui.UIController;
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

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        // TODO confirm that all geomSource() calls will return a SegmentedCurve instance type
        BifurcationCurve geomSource = (BifurcationCurve) geomSource();

        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim())  + '\"';


        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<" + BifurcationCurve.XML_TAG + " curve_name=" + ' ' + curve_name + ' ' +  "dimension=" + ' ' +  dimension + ' ' +  "format_desc=\"1 segment per row\">" + "\n");


        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        buffer.append(rpCalc().getConfiguration().toXML());
        
           
        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        buffer.append("<" + BifurcationCurve.LEFT_TAG +">" + "\n");
        
        for (int i = 0; i < geomSource.leftSegments().size(); i++) {

            RealSegment realSegment =(RealSegment) geomSource.leftSegments().get(i);
            buffer.append(realSegment.toXML() + "\n");
        }
        buffer.append("</" + BifurcationCurve.LEFT_TAG +">" + "\n");

        
        buffer.append("<" + BifurcationCurve.RIGHT_TAG +">" + "\n");
        
        for (int i = 0; i < geomSource.rightSegments().size(); i++) {

            RealSegment realSegment =(RealSegment) geomSource.rightSegments().get(i);
            buffer.append(realSegment.toXML() + "\n");
        }
        buffer.append("</" + BifurcationCurve.RIGHT_TAG +">" + "\n");
      buffer.append("</" + BifurcationCurve.XML_TAG +">" );

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

    protected final RpGeometry createLeftGeom() throws RpException {

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

    protected final RpGeometry createRightGeom() throws RpException {

        BifurcationCurve curve = (BifurcationCurve) geomSource();
        if (curve == null) {
            throw new RpException("Bifurcation curve empty");
        }

        RealSegGeom[] bifurcationArray = new RealSegGeom[curve.rightSegments().size()];

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


        try {
            leftGeom_ = createLeftGeom();
            rightGeom_ = createRightGeom();

            ((BifurcationCurveGeom) leftGeom_).setOtherSide(rightGeom_);
            ((BifurcationCurveGeom) rightGeom_).setOtherSide(leftGeom_);

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

    public RpGeometry createGeomFromSource() {

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        RealSegGeom[] bifurcationArray = new RealSegGeom[curve.segments().size()];

        RealSegGeom[] bifurcationArrayRight = new RealSegGeom[curve.rightSegments().size()];
        RealSegGeom[] bifurcationArrayLeft = new RealSegGeom[curve.leftSegments().size()];


        System.out.println("curve.segments().size() ::::::: " + curve.segments().size());
        System.out.println("curve.rightSegments().size() :: " + curve.rightSegments().size());
        System.out.println("curve.leftSegments().size() ::: " + curve.leftSegments().size());


        int i = 0;
        for (Object realSegment : curve.rightSegments()) {
            bifurcationArrayRight[i] = new RealSegGeom((RealSegment) realSegment, rightViewingAttr());
            bifurcationArray[i] = bifurcationArrayRight[i];

            i++;
        }
        System.out.println("createGeomFromSource() : valor de i ::: " + i);

        int j = 0;
        for (Object realSegment : curve.leftSegments()) {
            bifurcationArrayLeft[j] = new RealSegGeom((RealSegment) realSegment, leftViewingAttr());
            bifurcationArray[i + j] = bifurcationArrayLeft[j];
            j++;
        }
        System.out.println("createGeomFromSource() : valor de j ::: " + j);

        System.out.println("createGeomFromSource() : valor de i+j ::: " + (i + j));



        return new BifurcationCurveGeom(bifurcationArray, this);

    }

    public RpGeometry leftGeom() {
        return leftGeom_;
    }

    public RpGeometry rightGeom() {
        return rightGeom_;
    }
}
