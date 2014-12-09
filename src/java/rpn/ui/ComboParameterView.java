/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import rpn.configuration.Parameter;
import rpn.configuration.ParameterLeaf;

/**
 *
 * @author edsonlan
 */
public class ComboParameterView extends ParameterView {

    private JComboBox comboBox_;

    private ActionListener listener_;

    public ComboParameterView(Parameter parameter) {
        super(parameter);

        List<String> optionsList = parameter.getOptions();

        String[] optionsArray = new String[optionsList.size()];
        listener_ = new ComboEventHandler();
        comboBox_ = new JComboBox(optionsList.toArray(optionsArray));
        comboBox_.addActionListener(listener_);


    }

    @Override
    public void associatedChanged(Parameter associatedParameter, Object arg) {

        
        List<String> optionsList = associatedParameter.getOptions();
        
        comboBox_.removeActionListener(listener_);
        
        comboBox_.removeAllItems();
        
        for (String string : optionsList) {
            comboBox_.addItem(string);
        }
        
        comboBox_.addActionListener(listener_);
        


    }

    @Override
    public JComponent getComponent() {
        return comboBox_;
    }

    @Override
    protected void decorate(JComponent component) {
       
    }

    @Override
    public void changeView(Object obj) {
       
        comboBox_.setSelectedItem(obj);
        
        
    }

    private class ComboEventHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedItem = (String)comboBox_.getSelectedItem();
            getParameter().setValue(selectedItem);
            
            if(!(getParameter() instanceof ParameterLeaf)){
                setChanged();
                notifyObservers(comboBox_.getSelectedIndex());
                
            }

        }


    }

}
