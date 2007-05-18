package rpn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author unascribed
 * @version 1.3
 */
public class RPnErrorControlDialog extends JDialog {
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JPanel jPanel3 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JLabel jLabel1 = new JLabel();
    JTextField epslonField = new JTextField();

    public RPnErrorControlDialog() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        jPanel1.setLayout(borderLayout1);
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
        jPanel3.setLayout(flowLayout1);
        jLabel1.setText("EPSLON = ");
        epslonField.setPreferredSize(new Dimension(74, 17));
        epslonField.setText(new Double(rpnumerics.RPNUMERICS.errorControl().eps()).toString());
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jPanel2, BorderLayout.SOUTH);
        jPanel2.add(jButton1, null);
        jPanel2.add(jButton2, null);
        jPanel1.add(jPanel3, BorderLayout.CENTER);
        jPanel3.add(jLabel1, null);
        jPanel3.add(epslonField, null);
    }

    protected double getEps() {
        return new Double(epslonField.getText()).doubleValue();
    }

    void jButton1_actionPerformed(ActionEvent e) {
        rpnumerics.RPNUMERICS.errorControl().reset(getEps(), rpnumerics.RPNUMERICS.boundary());
        this.dispose();
    }

    void jButton2_actionPerformed(ActionEvent e) {
        this.dispose();
    }
}
