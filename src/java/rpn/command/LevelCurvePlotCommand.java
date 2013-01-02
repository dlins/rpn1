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
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
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


        ArrayList<Double> levelValues = processLevels(RPNUMERICS.getParamValue("levelcurve", "levels"));

        for (Double levelValue : levelValues) {
            LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createLevelCurveCalc(levelValue));

            UIController.instance().getActivePhaseSpace().join(factory.geom());

            Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
            PropertyChangeEvent event = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, factory.geom());



            ArrayList<RealVector> emptyInput = new ArrayList<RealVector>();
            logCommand(new RpCommand(event, emptyInput));










        }

    }

    /**
     * @deprecated  Trocar
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
