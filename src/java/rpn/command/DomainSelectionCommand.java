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

public class DomainSelectionCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "Extension Domain Selection";
    static private DomainSelectionCommand instance_ = null;

    private DomainSelectionCommand() {
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
                    ImageSelectionCommand.instance().setEnabled(true);
                }
            }
            // ---

        }

    }

    public static DomainSelectionCommand instance() {
        if (instance_ == null) {
            instance_ = new DomainSelectionCommand();
        }
        return instance_;
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        

        if (!UIController.instance().getSelectedGeometriesList().isEmpty()) {
            
            
            List<RpGeometry> geometryList = UIController.instance().getSelectedGeometriesList();
//            if (geometryList.size() != 1) {
//                setEnabled(false);
//                getContainer().setSelected(false);
//                GenericExtensionCurveCommand.instance().setEnabled(false);
//
//            } else {
                setEnabled(true);
                GenericExtensionCurveCommand.instance().setEnabled(true);

                if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
                    RpGeomFactory factory = geometryList.get(0).geomFactory();
                    RPnCurve curve = (RPnCurve) factory.geomSource();
                    RpCommand command = new RpCommand(curve.getId());
                    RPnNetworkStatus.instance().sendCommand(command.toXML());
                }

//            }

            
            
            
            
            
            
            
            
            
            
            
            
            
        } else {
            setEnabled(false);
            getContainer().setSelected(false);
            GenericExtensionCurveCommand.instance().setEnabled(false);
            ImageSelectionCommand.instance().getContainer().setSelected(false);
            ImageSelectionCommand.instance().setEnabled(false);
        }
    }
}
