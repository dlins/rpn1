package rpn.message;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import rpn.controller.ui.*;


/**
 *
 * <p>This class acts as a bridge between the dialog that configures the domains used in the network
 * communication and the class that manages these domains </p>
 *
 */

public class RPnDomainsDialogController extends AbstractAction {


    private static RPnDomainsDialogController controller_;
    private ArrayList selectedDomains_ = new ArrayList();
    private String newDomain_;

    /**
     *
     * @return RPnDomainsDialogController
     */
    public static RPnDomainsDialogController instance() {

        if (controller_ == null) {
            controller_ = new RPnDomainsDialogController();
            return controller_;
        }
        return controller_;
    }
    /**
     * Invoke when a event occurs .
     * @param e ActionEvent
     */

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source instanceof RPnDomainsDialog) {

//            if (e.getActionCommand().equals("Display Domains"))

            if (e.getActionCommand().equals("Add Domains")) {
                UIController.instance().getNetStatusHandler().addDomain(
                        newDomain_);
            }

            if (e.getActionCommand().equals("Remove Domains")) {

                for (int i = 0; i < selectedDomains_.size(); i++) {

                    UIController.instance().getNetStatusHandler().removeDomain((
                            String) selectedDomains_.get(i));
                }

                if (e.getActionCommand().equals("Ok Pressed")) {

                    firePropertyChange("Enabled","oldValue",new Boolean(true));
                }



            }
            syncView();
        }
        if (source instanceof JList) {

            if (e.getActionCommand().equals("Select Domains")) {

                selectedDomains_ = new ArrayList();

                Object[] domains = ((JList) source).getSelectedValues();

                for (int i = 0; i < domains.length; i++) {

                    selectedDomains_.add(domains[i]);

                }

            }

        }
    }
    /**
     * Used to set the new domain that will be added in the domains list
     *
     * @param newDomain String
     */
    public void setNewDomain(String newDomain) {

        newDomain_ = newDomain;

    }

    private void syncView() {

        firePropertyChange("Display Domains","oldValue",
                           UIController.instance().getNetStatusHandler().
                           getDomains());

    }
}
