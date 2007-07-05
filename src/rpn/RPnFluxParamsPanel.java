/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import java.awt.*;
import javax.swing.*;
import java.util.StringTokenizer;
import wave.util.RealVector;

public class RPnFluxParamsPanel extends JPanel {
    GridLayout gridLayout1 = new GridLayout();
    JPanel jPanel1 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JLabel jLabel1 = new JLabel();
    JTextField paramIndexField = new JTextField();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JTextField paramValueField = new JTextField();

    public RPnFluxParamsPanel() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(flowLayout1);
        jPanel1.setLayout(flowLayout1);
        jLabel1.setText("Flux Params = ");
        paramValueField.setPreferredSize(new Dimension(274, 17));
//        paramValueField.setText(rpnumerics.RPNUMERICS.fluxFunction().fluxParams().toString());
        this.add(jPanel1, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(paramValueField, null);
    }

    public RealVector getParams() {
        StringTokenizer tokenizer = new StringTokenizer(paramValueField.getText());
        double[] values = new double[tokenizer.countTokens()];
        int i = -1;
        while (tokenizer.hasMoreElements()) {
            i++;
            String token = (String)tokenizer.nextElement();
            Double doubleToken = new Double(token);
            values[i] = doubleToken.doubleValue();
        }
        return new RealVector(values);
    }

    public void setParams(RealVector params) {
        paramValueField.setText(params.toString());
    }
}
