/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JDialog;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 *

 *
 * <p> The dialog that configures the network communication parameters </p>
 *

 *

 */
public class RPnNetworkConfigDialog extends JDialog implements
        PropertyChangeListener {
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();

//  GridLayout gridLayoutAll = new GridLayout(4,1,0,10);

    BorderLayout gridLayoutAll = new BorderLayout(0, 10);
    GridLayout gridLayout1 = new GridLayout(2, 2, 10, 10);

    GridLayout gridLayout2 = new GridLayout(1, 2, 10, 10);


    JLabel serverLabel = new JLabel("Server", JLabel.CENTER);
    JLabel portLabel = new JLabel("Port", JLabel.CENTER);


    public static JTextField serverTextBox = new JTextField(RPnNetworkStatus.
            SERVERNAME);
    public static JTextField portTextBox = new JTextField((new Integer(
            RPnNetworkStatus.PORTNUMBER)).toString());


    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancel");

    public RPnNetworkConfigDialog() {
        try {

            jbInit();
//      this.pack();

            RPnNetworkConfigController.instance().addPropertyChangeListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setSize(new Dimension(455, 160));

        this.getContentPane().setLayout(gridLayoutAll);

        jPanel1.setLayout(gridLayout1);
        jPanel2.setLayout(gridLayout2);
        this.setModal(true);

        this.setResizable(false);
        this.setTitle("Network Configuration");

        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });

        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });

        jPanel1.add(serverLabel, 0);
        jPanel1.add(serverTextBox, 1);
        jPanel1.add(portLabel, 2);
        jPanel1.add(portTextBox, 3);

        jPanel2.add(okButton, 0);
        jPanel2.add(cancelButton, 1);

    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("Server Name")) {
            String newServerName = (String) evt.getNewValue();
            RPnNetworkDialog.infoLabel.setText("Server: " + newServerName +
                                               " Port: " +
                                               RPnNetworkStatus.PORTNUMBER);
        }

        if (evt.getPropertyName().equals("Port Number")) {

            Integer newPortNumber = (Integer) evt.getNewValue();
            RPnNetworkDialog.infoLabel.setText("Server: " +
                                               RPnNetworkStatus.SERVERNAME +
                                               " Port: " + newPortNumber);

        }

    }


    void cancelButton_actionPerformed(ActionEvent e) {

        dispose();

    }


    void okButton_actionPerformed(ActionEvent e) {
        RPnNetworkConfigController.instance().actionPerformed(new
                ActionEvent(this, 0, null));
        dispose();

    }

}
