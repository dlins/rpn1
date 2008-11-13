/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import rpn.controller.ui.UIController;

public abstract class RPnDialog extends JDialog {

    protected JPanel buttonsPanel;
    protected JButton applyButton;
    protected JButton cancelButton;
    protected JButton beginButton;

    public RPnDialog() {
        getContentPane().setLayout(new BorderLayout());

        buttonsPanel = new JPanel(new FlowLayout());
        cancelButton = new JButton("Cancel");
        applyButton = new JButton("Apply");

        buttonsPanel.add(applyButton);
        buttonsPanel.add(cancelButton);

        cancelButton.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        cancel();
                    }
                });

        applyButton.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        apply();
                        dispose();
                    }
                });
        addBackButton();
        cancelButton.setEnabled(false);


        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


    }

    public RPnDialog(boolean enableResetButton) {
        this();
        if (!enableResetButton) {
            buttonsPanel.remove(beginButton);
        }
        cancelButton.setEnabled(true);



    }

    protected abstract void apply();

    private void addBackButton() {

        beginButton = new JButton("Reset");
        beginButton.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        begin();
                    }
                });


        beginButton.setEnabled(true);
        buttonsPanel.add(beginButton);
    }

    protected void begin() {
        dispose();

        UIController.instance().resetApplication();

    }

    protected void cancel() {
        dispose();
    }
}
