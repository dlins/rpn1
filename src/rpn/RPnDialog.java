/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import rpn.controller.ui.CURVES_CONFIG;
import rpn.controller.ui.UIController;

public abstract class RPnDialog extends JDialog {

    protected JPanel buttonsPanel;
    protected JButton applyButton;
    protected JButton cancelButton;
    protected JButton beginButton;
    private DefaultApplyController defaultApplyAction_;
    private DefaultCancelController defaultCancelAction_;

    public RPnDialog() {
        getContentPane().setLayout(new BorderLayout());

        buttonsPanel = new JPanel(new FlowLayout());
        cancelButton = new JButton("Cancel");
        applyButton = new JButton("Apply");

        buttonsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");
        buttonsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");


//        buttonsPanel.getActionMap().put("Apply", new ActionApply());//TODO Enable key events
//        buttonsPanel.getActionMap().put("Cancel", new ActionCancel());


        buttonsPanel.add(applyButton);
        buttonsPanel.add(cancelButton);

        defaultCancelAction_=new DefaultCancelController();

        cancelButton.addActionListener(defaultCancelAction_);

        defaultApplyAction_ = new DefaultApplyController();

        applyButton.addActionListener(defaultApplyAction_);

        addBackButton();
        cancelButton.setEnabled(false);


        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


    }

    public RPnDialog(boolean displayBeginButton) {
        this();
        if (!displayBeginButton) {
            buttonsPanel.remove(beginButton);
        }

    }

    public RPnDialog(boolean displayBeginButton, boolean displayCancelButton) {
        this();
        if (!displayBeginButton) {
            buttonsPanel.remove(beginButton);
        }
        if (!displayCancelButton) {
            buttonsPanel.remove(cancelButton);
        } else {
            cancelButton.setEnabled(true);
        }

    }

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

    protected abstract void apply();

    protected  abstract void begin();

    
    protected void cancel() {
        dispose();
    }

    protected void removeDefaultApplyBehavior(){
        applyButton.removeActionListener(defaultApplyAction_);

    }


    protected void removeDefaultCancelBehavior() {
        cancelButton.removeActionListener(defaultCancelAction_);
    }

    private class ActionApply extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            apply();
        }
    }

    private class ActionCancel extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    }

    private class DefaultApplyController implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            apply();
            dispose();
        }
    }

    private class DefaultCancelController implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            cancel();
        }

    }


}
