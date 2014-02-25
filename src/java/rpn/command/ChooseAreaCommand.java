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
import rpn.component.util.AreaSelected;
import rpn.controller.ui.RPnSelectionPlotter;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.RPnAreaChooser;
import rpn.controller.ui.UIController;
import wave.util.RealVector;

public class ChooseAreaCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "Choose Area";
    static private ChooseAreaCommand instance_ = null;

    private ChooseAreaCommand() {
        super(DESC_TEXT, null, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {



        UIController.instance().setState(new AREASELECTION_CONFIG(this));

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnAreaChooser boxPlotter = new RPnAreaChooser();
            if (button_.isSelected()) {

                boxPlotter.addObserver(this);
                panel.addMouseListener(boxPlotter);
                panel.addMouseMotionListener(boxPlotter);
            }


        }

    }

    public static ChooseAreaCommand instance() {
        if (instance_ == null) {
            instance_ = new ChooseAreaCommand();
        }
        return instance_;
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
