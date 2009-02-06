/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import rpnumerics.RPNUMERICS;

public class RPnContinuationMethodConfigDialog extends RPnDialog {

    private JPanel paramsPanel_ = new JPanel();
    private JLabel stepsLengthLabel_ = new JLabel("Step Length");
    private JTextField stepsLengthJTextField_ = new JTextField();

    public RPnContinuationMethodConfigDialog() {
        super(true);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void jbInit() throws Exception {
        setTitle("Continuation Method Configuration");
        
        
        setMinimumSize(new Dimension(getTitle().length()*10,40));
        
        
        
        
        stepsLengthJTextField_.setText(new Double (RPNUMERICS.errorControl().ode().maxStateStepLength()).toString());
        
        paramsPanel_.add(stepsLengthLabel_);
        paramsPanel_.add(stepsLengthJTextField_);
        
        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();

    }

//    @Override
//    protected void cancel() {

//    }

    protected void apply() {
        RPNUMERICS.errorControl().ode().setMaxStateStepLength((new Double(stepsLengthJTextField_.getText())).doubleValue());

        dispose();

    }
}
