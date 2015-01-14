package rpn.controller.phasespace.extensions;

import java.util.List;
import rpn.command.GenericExtensionCurveCommand;
import rpn.component.util.GraphicsUtil;
import rpnumerics.ContourParams;
import rpnumerics.ExtensionCurveCalc;
import rpnumerics.RPNUMERICS;
import static rpnumerics.RPNUMERICS.getParamValue;
import rpnumerics.RpException;
import wave.util.RealSegment;
import wave.util.RealVector;

public class ExtensionImageSelectionReady extends ExtensionCurveReadyState {


  
    private final GraphicsUtil imageSelection_;

     public ExtensionImageSelectionReady(List<RealSegment> geom, GraphicsUtil imageSelection) {
        super(geom);
        imageSelection_=imageSelection;

        GenericExtensionCurveCommand.instance().setEnabled(true);

    }

    @Override
    public void select(GraphicsUtil area) {
        

    }

    

    @Override
    public ExtensionCurveCalc createCalc() throws RpException {
        int withImageSelection = 1;

        int[] resolution_ = RPNUMERICS.processResolution(getParamValue("bifurcationcurve", "resolution"));
        int family_ = new Integer(getParamValue("extensioncurve", "family"));
        int characteristic_ = new Integer(getParamValue("extensioncurve", "characteristic"));
        boolean singular_ = Boolean.valueOf(getParamValue("extensioncurve", "singular"));
        System.out.println("Valor de singular: " + singular_);

        List<RealSegment> segmentsToExtend = getSegmentsToExtend();
        List<RealVector> areaVertices = imageSelection_.getWCVertices();

        ExtensionCurveCalc calc = new ExtensionCurveCalc(new ContourParams(resolution_), segmentsToExtend, areaVertices, family_, characteristic_, singular_, withImageSelection);

        return calc;

    }

    

}
