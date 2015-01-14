package rpn.controller.phasespace.extensions;

import java.util.ArrayList;
import rpn.command.GenericExtensionCurveCommand;
import rpn.component.RpGeometry;
import rpn.component.util.GraphicsUtil;
import wave.util.RealSegment;

public class ExtensionCalcWaitState extends ExtensionCalcState {
   
    
    public ExtensionCalcWaitState() {

        super(new ArrayList<RealSegment>());
    }

    @Override
    public void setCurve(RpGeometry geom) {
        GenericExtensionCurveCommand.instance().setState(new ExtensionCurveReadyState(geom));

    }

    @Override
    public void select(GraphicsUtil selection) {

    }

}
