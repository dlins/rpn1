/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.Color;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import rpn.component.RpDiagramFactory;

public class RPnRiemannCursorMonitor  {
    //
    // Members
    //
    private NumberFormat formatter_;
    private  ArrayList<JLabel> labelList_;
    
    private JLabel speedLabel_;

    //
    // Constructor
    //
    public RPnRiemannCursorMonitor(int numberOfLines) {
 
        formatter_ = NumberFormat.getInstance();
        formatter_.setMaximumFractionDigits(4);
        labelList_ = new ArrayList();

        speedLabel_=new JLabel();
        speedLabel_.setForeground(Color.red);
        speedLabel_.setBackground(Color.black);
        speedLabel_.setOpaque(true);
       
        
        for (int i = 0; i < numberOfLines; i++) {
            JLabel label = new JLabel();
            label.setBackground(Color.black);
            label.setOpaque(true);
            labelList_.add(label);
            
        }
        
    }
    
    public void setCoords(double speed,java.util.List<Double> coords){
        
        for (int i = 0; i < coords.size(); i++) {
            JLabel label = labelList_.get(i);
            label.setForeground(RpDiagramFactory.colorChooser(i));
            label.setText(formatter_.format(coords.get(i)));
            
        }
        
        speedLabel_.setText(formatter_.format(speed));
        
    }
    

    public JLabel getSpeed() {
        return speedLabel_;
    }
    
    
    public  List<JLabel> getProfileCoords() {
        return labelList_;
    }
    
    
}
