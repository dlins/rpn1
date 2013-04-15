/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import rpn.configuration.Configuration;
import rpnumerics.RPNUMERICS;

/**
 *
 * @author edsonlan
 */
public class ComboBoxCreator extends UIComponentCreator {


    
    public ComboBoxCreator(Configuration configuration, RPnInputComponent inputComponent) {
        super(configuration, inputComponent);
        addObserver(inputComponent);
    }

    public ComboBoxCreator(Configuration configuration, String configurationParameter) {
        super(configuration, configurationParameter);

    }
    

    @Override
    public JComponent createUIComponent() {

        JPanel panel_ = new JPanel();
        
        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.fill = GridBagConstraints.BOTH;

        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        gridConstraints.ipadx = 50;
        gridConstraints.gridy = 0;
        gridConstraints.gridx = 0;

        GridBagLayout gridBayLayout = new GridBagLayout();

        panel_.setLayout(gridBayLayout);

        HashMap<String, String> paramsValues = configuration_.getParams();

        JComboBox comboBox = new JComboBox(paramsValues.keySet().toArray());

        comboBox.addActionListener(new ComboEventHandler());

        panel_.add(comboBox);
        return panel_;

    }

    private class ComboEventHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JComboBox combo = (JComboBox) e.getSource();


            String selectedItem = (String) combo.getSelectedItem();


            HashMap<String, String> paramsValues = configuration_.getParams();

            Set<Entry<String, String>> paramsSet = paramsValues.entrySet();

            int i=0;

            String[] newValue = new String[configuration_.getParamsSize()];
            
            for (Entry<String, String> value : paramsSet) {
                
                
                if (value.getKey().equals(selectedItem)){
                    
                    newValue[i]="1";
                    
                    configuration_.setParamValue(selectedItem, "1");
                    
                    RPNUMERICS.setMethod(configuration_.getName(), selectedItem);
                    
                }
                else{
                    newValue[i]="0";
                    configuration_.setParamValue(selectedItem, "0");                    
                }

                i++;
                
            }
           
        }
    }
}
