/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import rpn.controller.phasespace.*;
import rpn.component.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class RPnLayoutDialog extends JDialog {
    String[] elements = {"Orbit","Manifold","Profile","Inward Eigen","Outward Eigen","Poincare Section","Hugoniot Curve"};
    JPanel jPanel1 = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JButton colorButton = new JButton();
    JButton strokeButton = new JButton();
    JCheckBox visibleBox = new JCheckBox();
    JScrollPane jScrollPane1 = new JScrollPane();
    JList jList1 = new JList(elements);
    JPanel jPanel4 = new JPanel();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();

    public RPnLayoutDialog() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        jList1.addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    visibleBox.setEnabled(true);
                    switch (((JList)e.getSource()).getSelectedIndex()) {
                        // orbit
                        case 0:
                            visibleBox.setSelected(OrbitGeom.VIEWING_ATTR.isVisible());
                            break;
                            // manifold
                        case 1:
                            visibleBox.setSelected(ManifoldGeom.VIEWING_ATTR.isVisible());
                            break;
                            // profile
                        case 2:
                            visibleBox.setSelected(ProfileGeom.VIEWING_ATTR.isVisible());
                            break;
                            // inward eigen
                        case 3:
                            visibleBox.setEnabled(false);
                            break;
                            // outward eigen
                        case 4:
                            visibleBox.setEnabled(false);
                            break;
                            // poincare section
                        case 5:
//                            visibleBox.setSelected(PoincareSectionGeom.VIEWING_ATTR.isVisible());
                            break;
                            // hugoniot
                        case 6:
//                            visibleBox.setSelected(HugoniotCurveGeom.VIEWING_ATTR.isVisible());
                            break;
                    }
                }
            });
        jPanel1.setLayout(gridLayout1);
        jPanel3.setLayout(new FlowLayout());
        gridLayout1.setColumns(2);
        colorButton.setText("Color");
        colorButton.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    colorButton_actionPerformed(e);
                }
            });
        strokeButton.setText("Stroke");
        strokeButton.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    strokeButton_actionPerformed(e);
                }
            });
        visibleBox.setText("Visible");
        visibleBox.setEnabled(false);
        visibleBox.setSelected(true);
        visibleBox.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    visibleBox_actionPerformed(e);
                }
            });
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jButton1.setText("Ok");
        jButton1.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jButton1_actionPerformed(e);
                }
            });
        jButton2.setText("Cancel");
        jButton2.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jButton2_actionPerformed(e);
                }
            });
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jPanel2, null);
        jPanel2.add(jScrollPane1, null);
        jScrollPane1.getViewport().add(jList1, null);
        jPanel1.add(jPanel3, null);
        jPanel3.add(colorButton, null);
        jPanel3.add(strokeButton, null);
        jPanel3.add(visibleBox, null);
        this.getContentPane().add(jPanel4, BorderLayout.SOUTH);
        jPanel4.add(jButton1, null);
        jPanel4.add(jButton2, null);
    }

    void jButton2_actionPerformed(ActionEvent e) {
        dispose();
    }

    void jButton1_actionPerformed(ActionEvent e) {
        dispose();
    }

    void colorButton_actionPerformed(ActionEvent e) {
        // must be in synch with the array elements...
        switch (jList1.getSelectedIndex()) {
            // orbit
            case 0:
                OrbitGeom.VIEWING_ATTR.setColor(JColorChooser.showDialog(this, "Orbit Color",
                    OrbitGeom.VIEWING_ATTR.getColor()));
                break;
                // manifold
            case 1:
                ManifoldGeom.VIEWING_ATTR.setColor(JColorChooser.showDialog(this, "Manifold Color",
                    ManifoldGeom.VIEWING_ATTR.getColor()));
                break;
                // profile
            case 2:
                ProfileGeom.VIEWING_ATTR.setColor(JColorChooser.showDialog(this, "Profile Color",
                    ProfileGeom.VIEWING_ATTR.getColor()));
                break;
                // inward eigen
            case 3:
                StationaryPointView.DEFAULT_INWARD_COLOR =
                    JColorChooser.showDialog(this, "Inward Eigen Vector Color", StationaryPointView.DEFAULT_INWARD_COLOR);
                break;
                // outward eigen
            case 4:
                StationaryPointView.DEFAULT_OUTWARD_COLOR =
                    JColorChooser.showDialog(this, "Outward Eigen Vector Color", StationaryPointView.DEFAULT_OUTWARD_COLOR);
                break;
                // poincare section
            case 5:
                PoincareSectionGeom.VIEWING_ATTR.setColor(JColorChooser.showDialog(this, "Orbit Color",
                    PoincareSectionGeom.VIEWING_ATTR.getColor()));
                break;
                // hugoniot
            case 6:
//                HugoniotCurveGeom.VIEWING_ATTR.setColor(JColorChooser.showDialog(this, "Hugoniot Orbit Color",
//                    HugoniotCurveGeom.VIEWING_ATTR.getColor()));
                break;
        }
    }

    void strokeButton_actionPerformed(ActionEvent e) {
    }

    void visibleBox_actionPerformed(ActionEvent e) {
        switch (jList1.getSelectedIndex()) {
            // poincare section
            case 5:
                PoincareSectionGeom.VIEWING_ATTR.setVisible(visibleBox.isSelected());
                if ((visibleBox.isSelected()) && (rpn.parser.RPnDataModule.PHASESPACE.state() instanceof POINCARE_READY))
                    ((POINCARE_READY)rpn.parser.RPnDataModule.PHASESPACE.state()).poincareGeom().geomFactory().updateGeom();
                break;
                // hugoniot
            case 6:
//                HugoniotCurveGeom.VIEWING_ATTR.setVisible(visibleBox.isSelected());
                if (visibleBox.isSelected())
                    ((NUMCONFIG_READY)rpn.parser.RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().updateGeom();
                break;
        }
    }
}
