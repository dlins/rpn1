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
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

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
        cancelButton = new JButton(new DefaultCancelAction());
        applyButton = new JButton(new DefaultApplyAction());

        buttonsPanel.add(applyButton);
        buttonsPanel.add(cancelButton);


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

    protected abstract void begin();

    protected void cancel() {
        dispose();
    }

    protected void removeDefaultApplyBehavior() {
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

    private class DefaultApplyAction implements Action {

        public Object getValue(String key) {
            if (key.equals(Action.NAME)) {
                return "Apply";
            }
            return null;
        }

        public void putValue(String key, Object value) {
        }

        public void setEnabled(boolean b) {
        }

        public boolean isEnabled() {
            return true;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
        }

        public void actionPerformed(ActionEvent e) {
            apply();
        }
    }

    private class DefaultCancelAction implements Action {

        public Object getValue(String key) {
            if (key.equals(Action.NAME)) {
                return "Cancel";
            }
            return null;


        }

        public void putValue(String key, Object value) {
        }

        public void setEnabled(boolean b) {
        }

        public boolean isEnabled() {
            return true;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
        }

        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    }

    private class DefaultApplyController implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            apply();

        }
    }

    private class DefaultCancelController implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    }
}
