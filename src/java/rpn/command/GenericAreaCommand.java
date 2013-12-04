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
import rpn.RPnUIFrame;
import rpn.component.RpGeometry;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.RPnAreaChooser;
import rpn.controller.ui.RPnGenericSelectionPlotter;
import rpn.controller.ui.UIController;
import wave.multid.model.MultiPolygon;
import wave.util.RealVector;

public class GenericAreaCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "Generic Area";
    static private GenericAreaCommand instance_ = null;

    private GenericAreaCommand() {
        super(DESC_TEXT, null, new JToggleButton());
    }

//    @Override
    public void actionPerformed(ActionEvent event) {
        RPnGenericSelectionPlotter boxPlotter = new RPnGenericSelectionPlotter();

        UIController.instance().setState(new AREASELECTION_CONFIG(this));

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {

            RPnPhaseSpacePanel panel = iterator.next();
            panel.setPhysicalBoundarySelected(false);

            if (button_.isSelected()) {

                boxPlotter.addObserver(this);
                panel.addMouseListener(boxPlotter);
                panel.addMouseMotionListener(boxPlotter);


            }
            

        }


    }

    public static GenericAreaCommand instance() {
        if (instance_ == null) {
            instance_ = new GenericAreaCommand();
        }
        return instance_;
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        MultiPolygon polygon = (MultiPolygon)arg;
        System.out.println(polygon.toXML());
       GenericExtensionCurveCommand.instance().setSelectedArea((MultiPolygon)arg);
       

    }
}
