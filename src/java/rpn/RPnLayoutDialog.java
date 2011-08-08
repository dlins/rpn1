/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.component.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class RPnLayoutDialog extends RPnDialog {

    private String[] elements = {"Orbit", "Manifold", "Profile", "Inward Eigen", "Outward Eigen",
        "Poincare Section",
        "Hugoniot Curve",
        "Coincidence Extension",
        "Subinflection Extension",
        "Boundary Extension",
        "Bucklelevertti",
        "Subinflection",
        "Coincidence",};
    JPanel jPanel1 = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JButton colorButton = new JButton();
    JButton strokeButton = new JButton();
    JCheckBox visibleBox = new JCheckBox();
    JScrollPane jScrollPane1 = new JScrollPane();
//    JList jList1 = new JList(elements);
    JList curvesList_ = new JList(elements);
    JPanel jPanel4 = new JPanel();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();

    public RPnLayoutDialog() {
        super(false, false);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        setTitle("Visual Configurations");
        curvesList_.addListSelectionListener(new ListSelectionHandler());


        getContentPane().add(curvesList_, BorderLayout.CENTER);



    }

    void jButton2_actionPerformed(ActionEvent e) {
        dispose();
    }

    void jButton1_actionPerformed(ActionEvent e) {
        dispose();
    }

    void colorButton_actionPerformed(ActionEvent e) {
        // must be in synch with the array elements...
    }

    void strokeButton_actionPerformed(ActionEvent e) {
    }

    @Override
    protected void apply() {
        RPnPhaseSpaceFrame[] s = RPnUIFrame.getPhaseSpaceFrames();
        for (RPnPhaseSpaceFrame rPnPhaseSpaceFrame : s) {
            rPnPhaseSpaceFrame.phaseSpacePanel().scene().update();
        }
        dispose();

    }

    @Override
    protected void begin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            System.out.println(curvesList_.getSelectedIndex());

            switch (curvesList_.getSelectedIndex()) {

                // orbit
                case 0:
//                    OrbitGeom.VIEWING_ATTR.setColor(JColorChooser.showDialog(, "Orbit Color",
//                            OrbitGeom.VIEWING_ATTR.getColor()));
                    break;
                // manifold
                case 1:
//                    ManifoldGeom.VIEWING_ATTR.setColor(JColorChooser.showDialog(this, "Manifold Color",
//                            ManifoldGeom.VIEWING_ATTR.getColor()));
                    break;
                // profile
                case 12:
                    CoincidenceCurveGeom.COLOR = JColorChooser.showDialog(jPanel3, null, CoincidenceCurveGeom.COLOR);
                    break;

                case 8:
                    SubInflectionExtensionCurveGeom.COLOR = JColorChooser.showDialog(jPanel3, null, SubInflectionExtensionCurveGeom.COLOR);
                    break;
                case 9:
                    ExtensionCurveGeom.COLOR = JColorChooser.showDialog(jPanel3, null, ExtensionCurveGeom.COLOR);
                    break;

                case 10:
                    BuckleyLeverettinInflectionGeom.COLOR = JColorChooser.showDialog(jPanel3, null, BuckleyLeverettinInflectionGeom.COLOR);
                    break;
                case 11:
                    SubInflectionCurveGeom.COLOR = JColorChooser.showDialog(jPanel3, null, SubInflectionCurveGeom.COLOR);
                    break;

                case 7:
                    CoincidenceExtensionCurveGeom.COLOR = JColorChooser.showDialog(jPanel3, null, CoincidenceExtensionCurveGeom.COLOR);
                    break;

            }


        }
    }
}
