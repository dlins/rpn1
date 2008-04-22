
/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpn;

import java.awt.*;
import javax.swing.*;


public class RPnViscosityPanel extends JPanel {
    FlowLayout flowLayout1 = new FlowLayout();
    JPanel jPanel1 = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JLabel viscoLabel = new JLabel();
    JTextField viscoField = new JTextField();

    public RPnViscosityPanel() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setBackground(Color.lightGray);
        this.setLayout(flowLayout1);
        jPanel1.setLayout(gridLayout1);
        viscoLabel.setText("Viscosity Params :");
        this.add(jPanel1, null);
        jPanel1.add(viscoLabel, null);
        jPanel1.add(viscoField, null);
    }
}
