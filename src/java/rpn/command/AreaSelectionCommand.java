/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.controller.ui.RPnSelectionPlotter;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import wave.util.RealVector;

public class AreaSelectionCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "Area Selection";
    static private AreaSelectionCommand instance_ = null;
    

    private AreaSelectionCommand() {
        super(DESC_TEXT, null, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new AREASELECTION_CONFIG());

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnSelectionPlotter boxPlotter = new RPnSelectionPlotter();
            panel.addMouseListener(boxPlotter);
            panel.addMouseMotionListener(boxPlotter);
        }

    }

    public static AreaSelectionCommand instance() {
        if (instance_ == null) {
            instance_ = new AreaSelectionCommand();
        }
        return instance_;
    }

    @Override
    public void unexecute() {
    }

    

    @Override
    public void execute() {
    }

    

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {

       

    }

  
}
