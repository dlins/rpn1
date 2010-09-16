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
import rpn.controller.ui.UIController;

/**
 *
 * <p>The dialog used to connect , disconnect and setup the master status
 * in the network communication </p>
 */
public class RPnNetworkDialog extends JDialog implements PropertyChangeListener {

    JPanel mainPanel = new JPanel();
    JPanel inputPanel = new JPanel();
    JPanel infoPanel = new JPanel();
    JPanel masterPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JPanel statusPanel = new JPanel();
    JButton onlineButton = new JButton();
    JButton cancelButton = new JButton("Cancel");
    JScrollPane scrollPane = new JScrollPane();
    public static JTextField serverTextBox = new JTextField(RPnNetworkStatus.SERVERNAME);
    public static JTextField portTextBox = new JTextField((new Integer(
            RPnNetworkStatus.PORTNUMBER)).toString());
    BorderLayout gridLayout = new BorderLayout();
    JCheckBox masterCheckBox = new JCheckBox("Master");
    public static JLabel infoLabel = new JLabel();
    public JLabel serverLabel = new JLabel("Server");
    private JTextArea infoText = new JTextArea();

    public RPnNetworkDialog() {
        try {
            jbInit();
            this.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("Master Status")) {

            Boolean masterStatus = (Boolean) evt.getNewValue();

            if (masterStatus.booleanValue()) {

            } else {

            }

        }

        if (evt.getPropertyName().equals("Online Status")) {

            Boolean onlineStatus = (Boolean) evt.getNewValue();



            if (onlineStatus.booleanValue()) {
                onlineButton.setText("Disconnect");
                masterCheckBox.setEnabled(false);

            } else {
                onlineButton.setText("Connect");

                masterCheckBox.setEnabled(true);
            }

        }

        if (evt.getPropertyName().equals("Enabled")) {
            Boolean enabled = (Boolean) evt.getNewValue();
            if (enabled.booleanValue()) {
                onlineButton.setEnabled(true);
                masterCheckBox.setEnabled(true);

            } else {
                onlineButton.setText("Connect");
                onlineButton.setEnabled(false);
                masterCheckBox.setEnabled(false);

            }

            RPnNetworkDialog.infoLabel.setText("Server: " + RPnNetworkStatus.SERVERNAME + " Port: " + RPnNetworkStatus.PORTNUMBER);
        }

    }

    private void jbInit() throws Exception {

        this.getContentPane().add(mainPanel);
        scrollPane = new JScrollPane(infoText);
        mainPanel.setLayout(gridLayout);


        mainPanel.add(inputPanel,BorderLayout.NORTH);

        inputPanel.add(serverLabel);
        inputPanel.add(serverTextBox);
        inputPanel.add(portTextBox);
        inputPanel.add(masterCheckBox);


        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusPanel.add(scrollPane);


        infoText.setColumns(40);
        infoText.setRows(5);
        infoText.setLineWrap(false);


        infoText.setText(UIController.instance().getNetStatusHandler().getLogMessages());

        mainPanel.add(buttonsPanel,BorderLayout.CENTER);

        buttonsPanel.add(onlineButton);
        buttonsPanel.add(cancelButton);
        cancelButton.setEnabled(true);


        mainPanel.add(statusPanel,BorderLayout.SOUTH);


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



        this.setResizable(false);
        this.setTitle("Network");


        onlineButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onlineButton_actionPerformed(e);
            }
        });
        this.pack();


    }

    void configButton_actionPerformed(ActionEvent e) {
    }

    void onlineButton_actionPerformed(ActionEvent e) {

        if (masterCheckBox.isSelected()) {
            RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this, 0, "ConnectPressedWithMaster"));
        } else {
            RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this, 0, "ConnectPressed"));
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
