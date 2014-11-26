/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui.diagram;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class DiagramLabel {

    //
    // Members
    //
    private NumberFormat formatter_;
    
    private JLabel speedLabel_;
    
    private JTextArea textField_;
    
    private String paramName_;
    
    private ArrayList<String> fieldNamesArray_;

    //
    // Constructor
    //
    public DiagramLabel(String paramName) {
        
        formatter_ = NumberFormat.getInstance();
        formatter_.setMaximumFractionDigits(4);
        
        textField_ = new JTextArea();
        
        textField_.setBackground(Color.black);
        
        textField_.setForeground(Color.yellow);
        
        textField_.setEditable(false);
        
        
        fieldNamesArray_=new ArrayList<String>();
        
        speedLabel_ = new JLabel(paramName);
        speedLabel_.setForeground(Color.red);
        speedLabel_.setBackground(Color.black);
        speedLabel_.setOpaque(true);
        paramName_ = paramName;
        
    }
    
    public void setX(double speed) {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append(paramName_).append("=").append(formatter_.format(speed));
        
        speedLabel_.setText(builder.toString());
        
    }
    
    
    public void setFieldName (int fieldIndex, String fieldName){
        
        fieldNamesArray_.add(fieldIndex,fieldName);
    }
    
    public void setText(double [] text) {
        
        
        StringBuilder builder = new StringBuilder();
        
        
        for (int i = 0; i < text.length; i++) {
            double d = text[i];
            
            
            String fieldName ;

            fieldName=fieldNamesArray_.get(i);

            
            builder.append(fieldName).append("= ").append(formatter_.format(d)).append("\n");
        }
        
        
        textField_.setText(builder.toString());
        
    }
    
    public JTextArea getTextField() {
        return textField_;
    }
    
    public JLabel getSpeed() {
        return speedLabel_;
    }
    
}
