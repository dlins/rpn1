package rpn.component;

import java.awt.Color;
import rpn.RPnDesktopPlotter;
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

    public RpGeometry leftGeom() {
        return leftGeom_;
    }

    public RpGeometry rightGeom() {
        return rightGeom_;
    }
}
