/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import rpn.configuration.Configuration;
import rpnumerics.RPNUMERICS;

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

        JLabel methodLabel = new JLabel(configurationParameter_);

        HashMap<String, String> paramsValues = new HashMap<String, String>();

        if (configurationParameter_.contains("sort")) {//To eigen sort functions

            List<String> sortFunctionList = RPNUMERICS.getEigenSortFunctionNames();

            for (String string : sortFunctionList) {

                paramsValues.put(string, string);
            }

        } else {//To characteristics 

            paramsValues.put("on curve", "1");
            paramsValues.put("on domain", "0");
        }

        JComboBox comboBox = new JComboBox(paramsValues.keySet().toArray());

        comboBox.addItemListener(new ComboEventHandler());
        panel_.add(methodLabel);

        panel_.add(comboBox);
        
        
         if (configurationParameter_.contains("sort")) {//To eigen sort functions
             
             System.out.println("em sort eigen value: "+ configurationParameter_ + " "+ configuration_.getParam(configurationParameter_));
             comboBox.setSelectedItem(configuration_.getParam(configurationParameter_));
             
             
         }
         
         else {
             int charFlag = new Integer(configuration_.getParam(configurationParameter_));
             comboBox.setSelectedItem(chooseCharacteristic(charFlag));
         }
        
       

        return panel_;

    }
    
    
    private String chooseCharacteristic(int charFlag) {

        if (charFlag == 0) {
            return "on curve";
        } else {
            return "on domain";
        }

    }

    private class ComboEventHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            System.out.println("Chamando item changed" + configuration_.getName() + " " + configurationParameter_);
            JComboBox combo = (JComboBox) e.getSource();

            String selectedItem = (String) combo.getSelectedItem();

            if (!configurationParameter_.contains("sort")) {//To characteristics 
                if (selectedItem.equals("on curve")) {
                    RPNUMERICS.setParamValue(configuration_.getName(), configurationParameter_, "0");
                }

                if (selectedItem.equals("on domain")) {
                    RPNUMERICS.setParamValue(configuration_.getName(), configurationParameter_, "1");
                }
            }
            
            else {//To eigen sort function
                RPNUMERICS.setParamValue(configuration_.getName(), configurationParameter_, (String)combo.getSelectedItem());
                RPNUMERICS.setEigenSortFunction((String)combo.getSelectedItem());
            }

        }

    }
}
