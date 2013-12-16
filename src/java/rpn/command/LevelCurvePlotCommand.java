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

        System.out.println("Chamando execute de level curve");
        Double level = new Double(RPNUMERICS.getParamValue("levelcurve", "level"));
        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createLevelCurveCalc(level));
        logLevelCurvePlotCommand(factory, level);
        UIController.instance().getActivePhaseSpace().plot(factory.geom());




    }

    @Override
    public void execute(int curveId) {


        ArrayList<Double> levelValues = processLevels(RPNUMERICS.getParamValue("levelcurve", "levels"));

        Double levelValue = levelValues.get(0);

        RpCalcBasedGeomFactory factory = factory = new LevelCurveGeomFactory(RPNUMERICS.createLevelCurveCalc(levelValue));

        RpGeometry geometry = factory.geom();

        if (geometry == null) {
            return;
        }

        logLevelCurvePlotCommand(factory, levelValue, curveId);

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
        curve.setId(curveID_);
        curveID_++;

        Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
        PropertyChangeEvent event = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, factory.geom());

        ArrayList<RealVector> emptyInput = new ArrayList<RealVector>();
        RpCommand rpCommand = new RpCommand(event, emptyInput);
        System.out.println("Logando comando de nivel: " + rpCommand.toXML());
        logCommand(rpCommand);
        
          if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
            RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
        }


    }

    /**
     * @deprecated Trocar
     * @param resolution
     * @return
     */
    private static ArrayList<Double> processLevels(String resolution) {

        String[] splitedResolution = resolution.split(" ");

        ArrayList<Double> result = new ArrayList<Double>();

        try {
            for (int i = 0; i < splitedResolution.length; i++) {
                String string = splitedResolution[i];

                result.add(new Double(string));
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error in resolution format !");
            ex.printStackTrace();
        }
        return result;

    }
}
