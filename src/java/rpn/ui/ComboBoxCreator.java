/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
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
        
        JLabel methodLabel = new JLabel(configurationParameter_);

        panel_.setLayout(gridBayLayout);

        HashMap<String, String> paramsValues = new HashMap<String,String>();
        
        
        paramsValues.put("on curve", "1");
        paramsValues.put("on domain", "0");

        JComboBox comboBox = new JComboBox(paramsValues.keySet().toArray());

        Set<Entry<String, String>> entrySet = paramsValues.entrySet();
        
        
        for (Entry<String, String> entry : entrySet) {
            
            if (entry.getValue().equals("1")){
                comboBox.setSelectedItem(entry.getKey());
            }
            
        }

        comboBox.addActionListener(new ComboEventHandler());

        gridConstraints.gridy = 0;
        gridConstraints.gridx = 0;
        panel_.add(methodLabel,gridConstraints);
   
        
        gridConstraints.gridy = 1;
        gridConstraints.gridx = 0;
        
        panel_.add(comboBox,gridConstraints);

        
        return panel_;

    }

    private class ComboEventHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JComboBox combo = (JComboBox) e.getSource();


            String selectedItem = (String) combo.getSelectedItem();
            
            
            RPNUMERICS.setParamValue(configuration_.getName(), configurationParameter_, selectedItem);

           
        }
    }
}
