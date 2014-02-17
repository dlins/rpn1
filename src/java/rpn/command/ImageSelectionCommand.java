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
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.RPnGenericSelectionPlotter;
import rpn.controller.ui.UIController;
import wave.multid.model.MultiPolygon;
import wave.util.RealVector;

public class ImageSelectionCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "Extension Image Selection";
    static private ImageSelectionCommand instance_ = null;

    private ImageSelectionCommand() {
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

    public static ImageSelectionCommand instance() {
        if (instance_ == null) {
            instance_ = new ImageSelectionCommand();
        }
        return instance_;
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
//        MultiPolygon polygon = (MultiPolygon)arg;
        GenericExtensionCurveCommand.instance().setSelectedArea((MultiPolygon)arg);
       

    }
}
