/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import rpnumerics.RPNUMERICS;

public class RPnContourConfigDialog extends RPnDialog {

    private JPanel paramsPanel_ = new JPanel();
    private JLabel resolutionLabel_ = new JLabel("Resolution");
    private JTextField xResolutionJTextField_ = new JTextField();
    private JTextField yResolutionJTextField_ = new JTextField();

    public RPnContourConfigDialog() {
        super(false,true);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void jbInit() throws Exception {
        setTitle("Contour Method Configuration");

        setMinimumSize(new Dimension(getTitle().length()*10,40));
        
        xResolutionJTextField_.setText(new Integer (RPNUMERICS.getContourResolution()[0]).toString());
        yResolutionJTextField_.setText(new Integer(RPNUMERICS.getContourResolution()[1]).toString());
        
        xResolutionJTextField_.setColumns(xResolutionJTextField_.getText().length() + 4);
        yResolutionJTextField_.setColumns(yResolutionJTextField_.getText().length()+4);
        
        paramsPanel_.add(resolutionLabel_);
        paramsPanel_.add(xResolutionJTextField_);
        paramsPanel_.add(yResolutionJTextField_);
        
        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();

    }

    @Override
    protected void cancel() {
        dispose();
    }

    protected void apply() {
        
        RPNUMERICS.getContourResolution()[0] = new Integer(xResolutionJTextField_.getText());
        RPNUMERICS.getContourResolution()[1] = new Integer(yResolutionJTextField_.getText());

        dispose();

    }
}
