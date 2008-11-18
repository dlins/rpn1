/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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

        buttonsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");
        buttonsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        

        buttonsPanel.getActionMap().put("Apply", new ActionTeste());
        buttonsPanel.getActionMap().put("Cancel", cancelButton.getAction());

        
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

    private class TextFieldKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                applyButton.doClick();
                System.out.println("Apertando enter");
            }
        }
    }
    
    
      private class ActionTeste extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            System.out.println("Acao");
        }
    }
   
}
