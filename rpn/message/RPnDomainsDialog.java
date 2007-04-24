package rpn.message;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;


/**
 *
 * <p>The dialog used to  add , remove and display the domains involved in
 * the network communication </p>
 *
 *
 */


public class RPnDomainsDialog extends JDialog implements PropertyChangeListener {

    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JList domainsList = new JList();
    ListModel domainsListModel_;
    BorderLayout borderLayout2 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    JButton addButton = new JButton();
    JButton okButton = new JButton();
    JButton removeButton = new JButton();
    JPanel jPanel3 = new JPanel();
    JTextField newDomainTextField = new JTextField();
    GridLayout gridLayout1 = new GridLayout();
    JScrollPane scrollPane ;


    public RPnDomainsDialog() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setSize(new Dimension(490, 300));
        this.setModal(true);
        setResizable(false);
        this.addWindowListener(new RPnDomainsDialog_this_windowAdapter(this));
        RPnDomainsDialogController.instance().addPropertyChangeListener(this);

        domainsList.addListSelectionListener(new DomainsDataListener());
        domainsList.setSelectionMode(domainsList.getSelectionModel().
                                     MULTIPLE_INTERVAL_SELECTION);

        jPanel1.setLayout(borderLayout1);
        this.getContentPane().setLayout(borderLayout2);
        addButton.setText("Add Domain");
        addButton.addActionListener(new
                                    RPnDomainsDialog_addButton_actionAdapter(this));
        okButton.setText("OK");
        okButton.addActionListener(new RPnDomainsDialog_okButton_actionAdapter(this));
        removeButton.setText("Remove Domain");
        removeButton.addActionListener(new
                                       RPnDomainsDialog_removeButton_actionAdapter(this));
        newDomainTextField.setText("");
        jPanel3.setLayout(gridLayout1);
        jPanel2.add(okButton);
        jPanel2.add(addButton);
        jPanel2.add(removeButton);
        this.getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);
        jPanel3.add(newDomainTextField);
        this.getContentPane().add(jPanel1, java.awt.BorderLayout.WEST);
        this.getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        scrollPane = new JScrollPane(domainsList);

        this.getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

    }


    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("Display Domains")) {

            domainsListModel_ = new DomainsListModel((ArrayList) evt.
                    getNewValue());

            domainsList.setModel(domainsListModel_);
        }

    }


    public void this_windowOpened(WindowEvent e) {
        RPnDomainsDialogController.instance().actionPerformed(new ActionEvent(this, 0,
                "Display Domains"));
    }

    public void removeButton_actionPerformed(ActionEvent e) {

        RPnDomainsDialogController.instance().actionPerformed(new ActionEvent(this, 0,
                "Remove Domains"));

    }

    public void addButton_actionPerformed(ActionEvent e) {


        RPnDomainsDialogController.instance().setNewDomain(newDomainTextField.getText());
        RPnDomainsDialogController.instance().actionPerformed(new ActionEvent(this, 0,
                "Add Domains"));
        newDomainTextField.setText("");

    }

    public void okButton_actionPerformed(ActionEvent e) {

//         RPnDomainsDialogController.instance().actionPerformed(new ActionEvent(this, 0,
//                "Ok Pressed"));


        this.dispose();

    }





    class RPnDomainsDialog_this_windowAdapter extends WindowAdapter {
        private RPnDomainsDialog adaptee;
        RPnDomainsDialog_this_windowAdapter(RPnDomainsDialog adaptee) {
            this.adaptee = adaptee;
        }

        public void windowOpened(WindowEvent e) {
            adaptee.this_windowOpened(e);
        }
    }

    class RPnDomainsDialog_okButton_actionAdapter implements ActionListener {
        private RPnDomainsDialog adaptee;
        RPnDomainsDialog_okButton_actionAdapter(RPnDomainsDialog adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.okButton_actionPerformed(e);
        }
    }


    class DomainsListModel extends AbstractListModel {

        private ArrayList domainsListArray_;

        public DomainsListModel(ArrayList domainsList) {

            domainsListArray_ = domainsList;

        }

        public Object getElementAt(int index) {

            return domainsListArray_.get(index);
        }

        public int getSize() {
            return domainsListArray_.size();
        }


    }


    class DomainsDataListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {

            if (e.getSource() instanceof JList) {

                RPnDomainsDialogController.instance().actionPerformed(new ActionEvent(e.
                        getSource(), 0, "Select Domains"));
            }
        }
    }




    class RPnDomainsDialog_removeButton_actionAdapter implements ActionListener {
        private RPnDomainsDialog adaptee;
        RPnDomainsDialog_removeButton_actionAdapter(RPnDomainsDialog adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.removeButton_actionPerformed(e);
        }
    }


    class RPnDomainsDialog_addButton_actionAdapter implements ActionListener {
        private RPnDomainsDialog adaptee;
        RPnDomainsDialog_addButton_actionAdapter(RPnDomainsDialog adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.addButton_actionPerformed(e);
        }
}

















}



