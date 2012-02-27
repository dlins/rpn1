/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class LevelCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Level Curve";
    //
    // Members
    //
    static private LevelCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected LevelCurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        return null;
    }

    static public LevelCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new LevelCurvePlotAgent();
        }
        return instance_;
    }

    @Override
    public void execute() {


        ArrayList<Double> levelValues = processLevels(RPNUMERICS.getParamValue("levelcurve", "levels"));

        for (Double levelValue : levelValues) {
            LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createLevelCurveCalc(levelValue));
            RPnDataModule.PHASESPACE.join(factory.geom());
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
