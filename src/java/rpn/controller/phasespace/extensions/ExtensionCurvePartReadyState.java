package rpn.controller.phasespace.extensions;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import rpn.command.GenericExtensionCurveCommand;
import rpn.command.ImageSelectionCommand;
import rpn.component.util.AreaSelected;
import rpn.component.util.GraphicsUtil;
import rpn.parser.RPnDataModule;
import rpnumerics.ContourParams;
import rpnumerics.ExtensionCurveCalc;
import rpnumerics.RPNUMERICS;
import static rpnumerics.RPNUMERICS.getParamValue;
import rpnumerics.RpException;
import wave.util.RealSegment;
import wave.util.RealVector;

public class ExtensionCurvePartReadyState extends ExtensionCurveReadyState {

    private final GraphicsUtil area_;

    public ExtensionCurvePartReadyState(List<RealSegment> geom, GraphicsUtil area) {
        super(geom);

        area_ = area;
        System.out.println("Em Extension Curve Part Ready");

       

        GenericExtensionCurveCommand.instance().setEnabled(true);
        ImageSelectionCommand.instance().setEnabled(true);

    }

    @Override
    public void select(GraphicsUtil area) {
        GenericExtensionCurveCommand.instance().setState(new ExtensionImageSelectionReady(getSegmentsToExtend(), area));

    }

    @Override
    public ExtensionCurveCalc createCalc() throws RpException {
        int withImageSelection = 0;
        System.out.println("Chamando curve part ready");
        int[] resolution_ = RPNUMERICS.processResolution(getParamValue("extensioncurve", "resolution"));
        int family_ = new Integer(getParamValue("extensioncurve", "family"));
        int characteristic_ = new Integer(getParamValue("extensioncurve", "characteristic"));
        boolean singular_ = Boolean.valueOf(getParamValue("extensioncurve", "singular"));
        System.out.println("Valor de singular: " + singular_);

        List<RealVector> areaVertices = area_.getWCVertices();

        ExtensionCurveCalc calc = new ExtensionCurveCalc(new ContourParams(resolution_), getSegmentsToExtend(), areaVertices, family_, characteristic_, singular_, withImageSelection);

        return calc;

    }

   

    private List<RealSegment> extendsPartOfCurve() throws RpException {

        List<Integer> indexToRemove = new ArrayList<Integer>();

        AreaSelected areaSelected = (AreaSelected) area_;

        List<Integer> segmentIndex = containsCurve(getSegmentsToExtend(),area_);

        if (!segmentIndex.isEmpty()) {//Tests if there are some segments inside the area
            indexToRemove.addAll(segmentIndex);
        } else {
            throw new RpException("No curve segments inside the area");
        }

        return segmentsIntoArea(getSegmentsToExtend(), indexToRemove);

    }

}
