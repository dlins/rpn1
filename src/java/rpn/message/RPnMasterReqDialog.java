/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;


import java.awt.*;
import javax.swing.*;
import javax.swing.JDialog;
import java.awt.event.*;

/**
 *
 * <p>The dialog used to switch MASTER control to a Slave user on a RPN SESSION </p>
 */
public class RPnMasterReqDialog extends JDialog {

    private String clientID;

    JPanel mainPanel = new JPanel();
    JPanel infoPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    JButton allowButton = new JButton("Allow");
    JButton denyButton = new JButton("Deny");

    BorderLayout gridLayout = new BorderLayout();

    public JLabel infoLabel = new JLabel("A request for joining the session has arrived from : ");



    public RPnMasterReqDialog(String reqClientID) {

        try {

            init(reqClientID);

            setLocationRelativeTo(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(String reqClientID) throws Exception {


        clientID = new String(reqClientID);

        setResizable(false);
        setTitle("RPn Session Access Grant");

        infoLabel.setText(infoLabel.getText() + clientID);
        infoPanel.add(infoLabel);

        buttonsPanel.add(allowButton);
        buttonsPanel.add(denyButton);

        mainPanel.setLayout(gridLayout);
        mainPanel.add(infoPanel,BorderLayout.NORTH);
        mainPanel.add(buttonsPanel,BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        allowButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                allowButton_actionPerformed(e);
            }
        });

        denyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                denyButton_actionPerformed(e);
            }
        });


        pack();


    }

    void allowButton_actionPerformed(ActionEvent e) {

        RPnPublisher publisher = new RPnPublisher(RPnNetworkStatus.RPN_MASTER_ACK_TOPIC_NAME);
        publisher.publish(RPnNetworkStatus.MASTER_ACK_LOG_MSG + '|' + clientID);

        dispose();

    }

    void denyButton_actionPerformed(ActionEvent e) {

        dispose();
    }

}