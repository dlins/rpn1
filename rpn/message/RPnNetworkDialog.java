/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import java.awt.*;
import javax.swing.*;
import java.beans.*;
import java.awt.event.*;
import javax.swing.JDialog;

/**
 *
 * <p>The dialog used to connect , disconnect and setup the master status
 * in the network communication </p>
 */

public class RPnNetworkDialog extends JDialog implements PropertyChangeListener {
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JButton onlineButton = new JButton();
    GridLayout gridLayout1 = new GridLayout();
    public JLabel masterLabel = new JLabel();
    JButton configButton = new JButton();
    JCheckBox masterCheckBox = new JCheckBox();
    JPanel jPanel4 = new JPanel();
    public static JLabel infoLabel = new JLabel();
    JButton domainsButton = new JButton();

    public RPnNetworkDialog() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("Master Status")) {

            Boolean masterStatus = (Boolean) evt.getNewValue();

            if (masterStatus.booleanValue()) {

                masterLabel.setText("Master");
                domainsButton.setEnabled(true);
            } else {
                masterLabel.setText("");
                domainsButton.setEnabled(false);
            }

        }

        if (evt.getPropertyName().equals("Online Status")) {

            Boolean onlineStatus = (Boolean) evt.getNewValue();

//            Boolean oldStatus = (Boolean) evt.getOldValue();

            if (onlineStatus.booleanValue()) {
                onlineButton.setText("Disconnect");
                masterCheckBox.setEnabled(false);
                configButton.setEnabled(false);
            }

            else {
                onlineButton.setText("Connect");
                configButton.setEnabled(true);
                masterCheckBox.setEnabled(true);
            }

        }

        if (evt.getPropertyName().equals("Enabled")) {
            Boolean enabled = (Boolean) evt.getNewValue();
            if (enabled.booleanValue()){
                onlineButton.setEnabled(true);
                masterCheckBox.setEnabled(true);

            }
            else {
                onlineButton.setText("Connect");
                masterLabel.setText("");
                onlineButton.setEnabled(false);
                masterCheckBox.setEnabled(false);
                domainsButton.setEnabled(false);
            }
            configButton.setEnabled(true);
            RPnNetworkDialog.infoLabel.setText ("Server: "+RPnNetworkStatus.SERVERNAME+" Port: "+RPnNetworkStatus.PORTNUMBER);
        }

    }


    private void jbInit() throws Exception {
        setSize(new Dimension(490, 155));



        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                this_componentShown(e);


            }
        });
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });


        RPnNetworkStatusController.instance().addPropertyChangeListener(this);


        jPanel1.setLayout(borderLayout1);
        this.setResizable(false);
        this.setTitle("Network");
        jPanel3.setLayout(gridLayout1);

        onlineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onlineButton_actionPerformed(e);
            }
        });



        configButton.setText("Configuration ...");
        configButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configButton_actionPerformed(e);
            }
        });
        masterCheckBox.setText("Connect as Master");
        domainsButton.setText("Domains ...");
        domainsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                domainsButton_actionPerformed(e);
            }
        });

        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jPanel2.add(masterLabel, null);
        jPanel2.add(masterCheckBox, null);
        jPanel2.add(domainsButton);
        jPanel1.add(jPanel3, BorderLayout.CENTER);
        jPanel3.add(onlineButton, null);

        jPanel3.add(configButton, null);
        jPanel1.add(jPanel2, BorderLayout.SOUTH);
        this.getContentPane().add(jPanel4, BorderLayout.SOUTH);
        jPanel4.add(infoLabel, null);
    }


    void configButton_actionPerformed(ActionEvent e) {
        RPnNetworkConfigDialog configDialog = new RPnNetworkConfigDialog();

        configDialog.setVisible(true);
    }

    void onlineButton_actionPerformed(ActionEvent e) {

        if (masterCheckBox.isSelected()) {
            RPnNetworkStatusController.instance().actionPerformed(new
                    ActionEvent(this, 0, "ConnectPressedWithMaster"));
        }

        else {
            RPnNetworkStatusController.instance().actionPerformed(new
                    ActionEvent(this, 0, "ConnectPressed"));
        }

        RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this,
               0, "Display Status"));

    }


    void this_windowClosing(WindowEvent e) {
        RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this,
                0, "closeDialog"));
    }



    public void this_componentShown(ComponentEvent e) {

        RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this,
               0, "Display Status"));


    }

    public void domainsButton_actionPerformed(ActionEvent e) {



        RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this,
               0, "Domains Pressed"));

        RPnDomainsDialog domainsDialog = new RPnDomainsDialog();

        domainsDialog.setVisible(true);
    }

}
