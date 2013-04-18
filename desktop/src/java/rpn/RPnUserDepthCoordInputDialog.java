/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

public class RPnUserDepthCoordInputDialog extends JDialog {
    FlowLayout flowLayout1 = new FlowLayout();
    JLabel jLabel1 = new JLabel();
    JTextField coordField = new JTextField();
    JButton okButton = new JButton();
    double coord;

    /** Creates new form JDialog */
    public RPnUserDepthCoordInputDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initGUI();
    }

    /** This method is called from within the constructor to initialize the form. */
    private void initGUI() {
        addWindowListener(
            new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    closeDialog(evt);
                }
            });
        this.getContentPane().setBackground(Color.lightGray);
        this.getContentPane().setLayout(flowLayout1);
        jLabel1.setText("Z Coord : ");
        coordField.setPreferredSize(new Dimension(34, 17));
        okButton.setText("Ok");
        okButton.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        getContentPane().add(jLabel1);
        getContentPane().add(coordField);
        getContentPane().add(okButton);
        pack();
    }

    public double coord() { return coord; }

    /** Closes the dialog */
    private void closeDialog(WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    private void okButton_actionPerformed(ActionEvent e) {
        coord = new Double(coordField.getText()).doubleValue();
        dispose();
    }
}
