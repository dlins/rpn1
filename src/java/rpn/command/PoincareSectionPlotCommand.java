/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JToggleButton;
import rpn.component.*;
import wave.util.SimplexPoincareSection;
import rpn.configuration.RPnConfig;
import rpn.controller.ui.UIController;
import rpnumerics.RPnCurve;
import wave.util.RealVector;


public class PoincareSectionPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Poincare Section";

    //
    // Members
    //
    static private PoincareSectionPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected PoincareSectionPlotCommand() {
        super(DESC_TEXT, RPnConfig.POINCARE,new JToggleButton());
    }


    public RpGeometry createRpGeometry(RealVector[] input) {

        return new PoincareSectionGeomFactory(new SimplexPoincareSection(input)).geom();
    }


    // ---
    @Override
        public void execute() {

        RealVector[] userInputList = UIController.instance().userInputList();

        System.out.println("execute do  plot" + userInputList[0]);

        Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();

        RpGeometry geometry = createRpGeometry(userInputList);

        if (geometry == null) {
            return;
        }

        UIController.instance().getActivePhaseSpace().plot(geometry);

        PropertyChangeEvent event_ = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, geometry);

        ArrayList<RealVector> inputArray = new ArrayList<RealVector>();
        inputArray.addAll(Arrays.asList(userInputList));
        
        
        logCommand(new RpCommand(event_, inputArray));
        

        setInput(inputArray);

    }
    // ---


    static public PoincareSectionPlotCommand instance() {
        if (instance_ == null)
            instance_ = new PoincareSectionPlotCommand();
        return instance_;
    }
}
