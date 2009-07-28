/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import rpn.controller.ui.BIFURCATION_CONFIG;
import rpn.controller.ui.UIController;
import rpn.usecase.BifurcationPlotAgent;
import rpnumerics.RPNUMERICS;

public class RPnBifurcationConfigDialog extends RPnDialog {

    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    private JPanel minusfamilyPanel_ = new JPanel();
    private JPanel plusfamilyPanel_ = new JPanel();
    private JComboBox minusFamilyComboBox_;
    private JComboBox plusFamilyComboBox_;

    public RPnBifurcationConfigDialog() {
        super(false);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RPnBifurcationConfigDialog(boolean enableResetButton) {
        super(enableResetButton);
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RPnBifurcationConfigDialog(boolean b, boolean b0) {
        super(b0, b0);
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFamilyIndex() {
        minusfamilyPanel_.add(new JLabel("Minus Family", SwingConstants.LEFT));
        plusfamilyPanel_.add(new JLabel("Plus Family", SwingConstants.LEFT));

        minusFamilyComboBox_ = new JComboBox();
        plusFamilyComboBox_ = new JComboBox();
        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
            minusFamilyComboBox_.addItem(new Integer(i));
            plusFamilyComboBox_.addItem(new Integer(i));
        }
        minusfamilyPanel_.setLayout(flowLayout1);
        plusfamilyPanel_.setLayout(flowLayout1);

        minusfamilyPanel_.add(minusFamilyComboBox_);
        plusfamilyPanel_.add(plusFamilyComboBox_);
    }

    private void jbInit() throws Exception {
        addFamilyIndex();
        jPanel1.setLayout(borderLayout1);
        this.getContentPane().add(plusfamilyPanel_, BorderLayout.NORTH);
        this.getContentPane().add(minusfamilyPanel_, BorderLayout.CENTER);
        pack();
    }

    protected void apply() {

        RPNUMERICS.getBifurcationProfile().setMinusFamily(((Integer) minusFamilyComboBox_.getSelectedItem()).intValue());
        RPNUMERICS.getBifurcationProfile().setPlusFamily(((Integer) plusFamilyComboBox_.getSelectedItem()).intValue());

        dispose();

    }
}
