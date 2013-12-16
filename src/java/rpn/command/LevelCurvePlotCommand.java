/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JToggleButton;
import static rpn.command.RpModelPlotCommand.curveID_;
import rpn.component.*;
import rpn.configuration.Configuration;
import rpn.controller.ui.UIController;
import rpn.message.RPnNetworkStatus;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.RpCalculation;
import wave.util.RealVector;

public class LevelCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Level Curve";
    //
    // Members
    //
    static private LevelCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected LevelCurvePlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        return null;
    }

    static public LevelCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new LevelCurvePlotCommand();
        }
        return instance_;
    }

    @Override
    public void execute() {

        Double level = new Double(RPNUMERICS.getParamValue("levelcurve", "level"));
        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createLevelCurveCalc(level));
        logLevelCurvePlotCommand(factory, level);
        UIController.instance().getActivePhaseSpace().plot(factory.geom());


    }

    @Override
    public void execute(int curveId) {
        
        Double level = new Double(RPNUMERICS.getParamValue("levelcurve", "level"));
        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createLevelCurveCalc(level));
        RpGeometry geometry = factory.geom();

        if (geometry == null) {
            return;
        }

        logLevelCurvePlotCommand(factory, level, curveId);

        UIController.instance().getActivePhaseSpace().plot(geometry);


    }

    private void logLevelCurvePlotCommand(RpCalcBasedGeomFactory factory, double level, int curveId) {
        RpCalculation calc = (RpCalculation) factory.rpCalc();
        Configuration configuration = calc.getConfiguration();
        configuration.setParamValue("level", String.valueOf(level));

        RPnCurve curve = (RPnCurve) factory.geomSource();
        curve.setId(curveId);

        Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
        PropertyChangeEvent event = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, factory.geom());

        ArrayList<RealVector> emptyInput = new ArrayList<RealVector>();
        logCommand(new RpCommand(event, emptyInput));

        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
            RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
        }



    }

    private void logLevelCurvePlotCommand(RpCalcBasedGeomFactory factory, double level) {

        RpCalculation calc = (RpCalculation) factory.rpCalc();
        Configuration configuration = calc.getConfiguration();
        configuration.setParamValue("level", String.valueOf(level));

        RPnCurve curve = (RPnCurve) factory.geomSource();
        if(curve!=null){
            curve.setId(curveID_);
        curveID_++;

        Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
        PropertyChangeEvent event = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, factory.geom());

        ArrayList<RealVector> emptyInput = new ArrayList<RealVector>();
        RpCommand rpCommand = new RpCommand(event, emptyInput);
        logCommand(rpCommand);

        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
            RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
        }
        }
        


    }
   
}
