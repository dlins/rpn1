/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.*;
import rpn.controller.ui.UIController;
import rpn.RPnUIFrame;

/**
 * <p>This class acts as a bridge between the dialog that configures the network communication parameters
 * and the class that stores these parameters </p>
 */

public class RPnNetworkStatusController extends AbstractAction {

    private static RPnNetworkStatusController controller_ = null;

    private RPnNetworkStatusController() {

    }

    public static RPnNetworkStatusController instance() {

        if (controller_ == null) {
            controller_ = new RPnNetworkStatusController();
            return controller_;
        }
        return controller_;

    }

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        String command = e.getActionCommand();

        Boolean isOnline = new Boolean(UIController.instance().
                                       getNetStatusHandler().isOnline());

        Boolean isMaster = new Boolean(UIController.instance().
                                       getNetStatusHandler().isMaster());

        if (source instanceof RPnNetworkDialog) {

            if (command.equals("ConnectPressed") ||
                command.equals("ConnectPressedWithMaster")) {

                if (isOnline.booleanValue() == false &&
                    command.equals("ConnectPressed")) {

                    //Nao seta como master

                    UIController.instance().getNetStatusHandler().setAsMaster(false);

                }
                if (isOnline.booleanValue() == false &&
                    command.equals("ConnectPressedWithMaster")) {

                    //Seta como master

                    UIController.instance().getNetStatusHandler().setAsMaster(true);
                }
                if (isOnline.booleanValue() == true &&
                    UIController.instance().getNetStatusHandler().isMaster()) {

                    RPnActionMediator.instance().sendMasterOffline();
                    UIController.instance().getNetStatusHandler().setAsMaster(false);

                }

                UIController.instance().getNetStatusHandler().online(!isOnline.
                        booleanValue());
                RPnActionMediator.instance();

                firePropertyChange("Online Status", isOnline,
                                   new Boolean(!isOnline.booleanValue()));
                firePropertyChange("Master Status", isMaster,
                                   new Boolean(!isMaster.booleanValue()));

            }

            if (command.equals("Display Status")) {

                if (UIController.instance().getNetStatusHandler().
                    isServerOnline()) {

                    firePropertyChange("Enabled", "oldValue", new Boolean(true));

                    if (isOnline.booleanValue()) {
                        firePropertyChange("Online Status", new Boolean(false),
                                           new Boolean(true));
                    } else {
                        firePropertyChange("Online Status", new Boolean(true),
                                           new Boolean(false));
                    }

                    if (isMaster.booleanValue()) {
                        firePropertyChange("Master Status", new Boolean(false),
                                           new Boolean(true));
                    } else {
                        firePropertyChange("Master Status", new Boolean(true),
                                           new Boolean(false));
                    }

                } else {
                    firePropertyChange("Enabled", "oldValue", new Boolean(false));
                    JOptionPane.showMessageDialog(null, "Server not available");
                }

            }

            if (command.equals("closeDialog")) {

                firePropertyChange("Dialog Closed", "oldValue", "newValue");

            }
        }

        if (source instanceof RPnMessageListener) {

            firePropertyChange("Master Offline", "oldValue", e.getActionCommand());

        }

        if (source instanceof RPnActionMediator) {

//            System.out.println("Evento vindo de ActionMediator");

        }

        if (source instanceof RPnUIFrame) {

            firePropertyChange("Network MenuItem Clicked", "oldValue",
                               "newValue");
        }

    }

}
