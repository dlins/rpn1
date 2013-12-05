/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.RPnSelectionPlotter;
import rpn.controller.ui.UIController;
import rpn.message.RPnNetworkStatus;
import rpnumerics.RPnCurve;
import wave.util.RealVector;

public class AreaSelectionToExtensionCurveCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "AreaToExtensionCurve";
    static private AreaSelectionToExtensionCurveCommand instance_ = null;

    private AreaSelectionToExtensionCurveCommand() {
        super(DESC_TEXT, null, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new AREASELECTION_CONFIG(this));
        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnSelectionPlotter boxPlotter = new RPnSelectionPlotter();
            panel.addMouseListener(boxPlotter);
            panel.addMouseMotionListener(boxPlotter);

            // ---
            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom();
            Iterator phaseSpaceIterator = phaseSpace.getGeomObjIterator();
            while (phaseSpaceIterator.hasNext()) {
                RpGeometry phaseSpaceGeometry = (RpGeometry) phaseSpaceIterator.next();
                if (phaseSpaceGeometry.viewingAttr().isSelected()) {
                    GenericExtensionCurveCommand.instance().setGeometryAndPanel(phaseSpaceGeometry, panel);
                    GenericExtensionCurveCommand.instance().setEnabled(true);
                }
            }
            // ---

        }

    }

    public static AreaSelectionToExtensionCurveCommand instance() {
        if (instance_ == null) {
            instance_ = new AreaSelectionToExtensionCurveCommand();
        }
        return instance_;
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    public void update(Observable o, Object arg) {

        List<RpGeometry> geometryList = ((List<RpGeometry>) arg);
        if (geometryList.isEmpty() || geometryList.size() != 1) {
            setEnabled(false);
            GenericExtensionCurveCommand.instance().setEnabled(false);
        } else {
            setEnabled(true);
            GenericExtensionCurveCommand.instance().setEnabled(true);


            if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
                RpGeomFactory factory = geometryList.get(0).geomFactory();


                RPnCurve curve = (RPnCurve) factory.geomSource();

                RpCommand command = new RpCommand(curve.getId());

                System.out.println("Enviando comando" + command.toXML());
                RPnNetworkStatus.instance().sendCommand(command);
            }

        }

    }
}
