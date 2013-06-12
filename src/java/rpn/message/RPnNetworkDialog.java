/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.message;

import java.net.*;
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



    JPanel mainPanel = new JPanel();
    JPanel inputPanel = new JPanel();
    JPanel infoPanel = new JPanel();
    JPanel masterPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JPanel statusPanel = new JPanel();
    JButton onlineButton = new JButton();    
    JScrollPane scrollPane = new JScrollPane();

    public static JTextField serverTextBox = new JTextField(RPnNetworkStatus.SERVERNAME);
    public static JTextArea infoText = new JTextArea();
    
    BorderLayout gridLayout = new BorderLayout();
    JCheckBox masterCheckBox = new JCheckBox("Master");
    public static JLabel infoLabel = new JLabel();
    
    

    public RPnNetworkDialog() {
        try {
            init();
            this.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("Master Status")) {

            Boolean masterNewStatus = (Boolean) evt.getNewValue();

            if (masterNewStatus.booleanValue()) {

                // willing to become master ???
                if (RPnNetworkStatus.instance().isOnline()) {

                    RPnNetworkStatus.instance().disconnect();
                    RPnNetworkStatus.instance().sendMasterRequest();
                }

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

            RPnNetworkDialog.infoLabel.setText("Server: " + RPnNetworkStatus.SERVERNAME);
        }

    }

    private void init() throws Exception {

        onlineButton.setText("Connect");

        getContentPane().add(mainPanel);
        scrollPane = new JScrollPane(infoText);
        mainPanel.setLayout(gridLayout);


        mainPanel.add(inputPanel,BorderLayout.NORTH);

    
        inputPanel.add(serverTextBox);
        inputPanel.add(masterCheckBox);


        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusPanel.add(scrollPane);


        infoText.setColumns(40);
        infoText.setRows(5);
        infoText.setLineWrap(false);


        infoText.setText(RPnNetworkStatus.instance().log());

        mainPanel.add(buttonsPanel,BorderLayout.CENTER);

        buttonsPanel.add(onlineButton);
    


        mainPanel.add(statusPanel,BorderLayout.SOUTH);


        this.addComponentListener(new ComponentAdapter() {

            public void componentShown(ComponentEvent e) {
               // this_componentShown(e);


            }
        });

        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
               // this_windowClosing(e);
            }
        });
       
        this.setResizable(false);
        this.setTitle("Network");


        onlineButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onlineButton_actionPerformed(e);
            }
        });

        pack();


    }

    void configButton_actionPerformed(ActionEvent e) {
    }

    void onlineButton_actionPerformed(ActionEvent e) {

        
        try {
            
            InetAddress ip_ = InetAddress.getLocalHost();
            String from2_ = ip_.getHostAddress();
            String clientID = from2_.replace('.', '_');
                      

            if (RPnNetworkStatus.instance().isOnline()) {
               
                RPnNetworkStatus.instance().disconnect();

                // stops listening...
                infoText.append("RPn user : " +  clientID + " is now off RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');
                               
                onlineButton.setText("Connect");
                onlineButton.repaint();

            } else {

                // either starts listening or becomes master...
                onlineButton.setText("Disconnect");
                onlineButton.repaint();

                if (masterCheckBox.isSelected())
                    infoText.append("RPn user : " +  clientID + " is now master of RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');
                else
                    infoText.append("RPn user : " +  clientID + " is now following RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

                RPnNetworkStatus.instance().connect(clientID,masterCheckBox.isSelected());
                masterCheckBox.setEnabled(false);

            }

        } catch (UnknownHostException ex) {
                    System.out.println(ex);
        }

    }


}
