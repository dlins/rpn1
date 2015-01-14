/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.ui.physics;

import java.awt.*;
import javax.swing.*;

public class RPnQuadParamsPanel extends JPanel {
    GridLayout gridLayout1 = new GridLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JLabel jLabel1 = new JLabel();
    JTextField paramAIndexField = new JTextField();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JTextField paramAValueField = new JTextField();
    JLabel jLabel4 = new JLabel();
    JTextField paramCIndexField0 = new JTextField();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel6 = new JLabel();
    JTextField paramCIndexField1 = new JTextField();
    JLabel jLabel7 = new JLabel();
    JLabel jLabel8 = new JLabel();
    JTextField paramCIndexField2 = new JTextField();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel10 = new JLabel();
    JTextField paramCValueField = new JTextField();
    JTextField paramBValueField = new JTextField();
    JLabel jLabel11 = new JLabel();
    JTextField paramBIndexField1 = new JTextField();
    JTextField paramBIndexField0 = new JTextField();
    JLabel jLabel14 = new JLabel();
    JLabel jLabel15 = new JLabel();
    JLabel jLabel16 = new JLabel();
    JLabel jLabel17 = new JLabel();
    JPanel jPanel3 = new JPanel();

    public RPnQuadParamsPanel() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        gridLayout1.setColumns(1);
        gridLayout1.setRows(3);
        gridLayout1.setVgap(10);
        this.setLayout(gridLayout1);
        jPanel1.setLayout(flowLayout1);
        jLabel1.setText("A[");
        jLabel2.setText("]");
        paramAIndexField.setPreferredSize(new Dimension(34, 17));
        jLabel3.setText("=");
        jLabel4.setText("C[");
        paramCIndexField0.setPreferredSize(new Dimension(34, 17));
        jLabel5.setText("]");
        jLabel6.setText("[");
        paramCIndexField1.setPreferredSize(new Dimension(34, 17));
        paramAValueField.setPreferredSize(new Dimension(74, 17));
        jLabel7.setText("]");
        jLabel8.setText("[");
        paramCIndexField2.setPreferredSize(new Dimension(34, 17));
        jLabel9.setText("]");
        jLabel10.setText("=");
        paramCValueField.setPreferredSize(new Dimension(74, 17));
        paramBValueField.setPreferredSize(new Dimension(74, 17));
        jLabel11.setText("=");
        paramBIndexField1.setPreferredSize(new Dimension(34, 17));
        paramBIndexField0.setPreferredSize(new Dimension(34, 17));
        jLabel14.setText("]");
        jLabel15.setText("[");
        jLabel16.setText("]");
        jLabel17.setText("B[");
        jPanel2.add(jLabel4, null);
        jPanel2.add(paramCIndexField0, null);
        jPanel2.add(jLabel5, null);
        jPanel2.add(jLabel6, null);
        jPanel2.add(paramCIndexField1, null);
        jPanel2.add(jLabel7, null);
        jPanel2.add(jLabel8, null);
        jPanel2.add(paramCIndexField2, null);
        jPanel2.add(jLabel9, null);
        jPanel2.add(jLabel10, null);
        jPanel2.add(paramCValueField, null);
        this.add(jPanel1, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(paramAIndexField, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(jLabel3, null);
        jPanel1.add(paramAValueField, null);
        this.add(jPanel3, null);
        jPanel3.add(jLabel17, null);
        jPanel3.add(paramBIndexField0, null);
        jPanel3.add(jLabel16, null);
        jPanel3.add(jLabel15, null);
        jPanel3.add(paramBIndexField1, null);
        jPanel3.add(jLabel14, null);
        jPanel3.add(jLabel11, null);
        jPanel3.add(paramBValueField, null);
        this.add(jPanel2, null);
    }
}
