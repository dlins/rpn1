/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.*;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.CurveSelector;
import rpn.controller.ui.UIController;
import wave.util.RealVector;

public class CurveSelectionCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Selection";
    //
    // Members
    //
    static private CurveSelectionCommand instance_ = null;


    //
    // Constructors/Initializers
    //
    protected CurveSelectionCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new AREASELECTION_CONFIG(this));

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RpGeometry lastGeometry = UIController.instance().getActivePhaseSpace().getLastGeometry();
            if(lastGeometry==null){
                return;
            }
                
            
            CurveSelector curveSelector = new CurveSelector(lastGeometry);
            panel.addMouseListener(curveSelector);
            panel.addMouseMotionListener(curveSelector);
        }

    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] input) {

       
        return  null;

    }

    @Override
    public void execute() {


    }

    static public CurveSelectionCommand instance() {
        if (instance_ == null) {
            instance_ = new CurveSelectionCommand();
        }
        return instance_;
    }


    
}
