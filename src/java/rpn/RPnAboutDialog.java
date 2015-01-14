/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author mvera
 */
public class RPnAboutDialog extends JDialog {

    public static String VERSION_FILE = "etc/version";

    protected JPanel buttonsPanel_ = new JPanel();
    protected JPanel versionPanel_ = new JPanel();
    protected JButton closeButton_ = new JButton();
    protected JTextArea infoText_ = new JTextArea();

    private String prodVersion_ = "UNKNOWN";
    private FileInputStream versionFileInputStream_;
    private ByteArrayInputStream buff_;

    public RPnAboutDialog(Frame frame, String title, boolean modal) {

        super(frame, title, modal);
        loadProductVersion();
        init();

    }

    protected void loadProductVersion() {

        try {

            versionFileInputStream_ = new FileInputStream(VERSION_FILE);

            File configFile = new File(VERSION_FILE);

            byte[] bufArray = new byte[(int) configFile.length()];

            versionFileInputStream_.read(bufArray, 0, (int) configFile.length());

            prodVersion_ = new String(bufArray, "UTF-8");

        } catch (Exception e) {

            System.err.println(e.toString());
        }

    }

    protected void init() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        closeButton_.setText("Close");
        closeButton_.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });

        buttonsPanel_.add(closeButton_);

        infoText_.setColumns(40);
        infoText_.setRows(5);
        infoText_.setLineWrap(false);
        infoText_.setBackground(Color.white);

        infoText_.setText("Product Version : " + prodVersion_);

        mainPanel.add(infoText_, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel_, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setResizable(false);

        pack();
    }

}
