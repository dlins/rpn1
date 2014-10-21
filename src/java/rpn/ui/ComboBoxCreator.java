/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import rpn.configuration.Configuration;
import rpnumerics.RPNUMERICS;
import static rpnumerics.RPNUMERICS.getWaveCurveCaseNames;

public class ComboBoxCreator extends UIComponentCreator {

    public ComboBoxCreator(Configuration configuration, RPnInputComponent inputComponent) {
        super(configuration, inputComponent);
        addObserver(inputComponent);
    }

    public ComboBoxCreator(Configuration configuration, String configurationParameter) {
        super(configuration, configurationParameter);

//          if (configuration.getName().equals("hugoniotcurve")) {
//                String[] hugoniotNames = RPNUMERICS.getHugoniotNames();
//
//                configuration.setParamValue("method", hugoniotNames[0]);
//                String[] hugoniotCaseNames = RPNUMERICS.getHugoniotCaseNames(hugoniotNames[0]);
//
//                configuration.setParamValue( "case", hugoniotCaseNames[0]);
//
////                System.out.println("Configuration apos end : " + RPNUMERICS.getConfiguration("hugoniotcurve"));
//                
//                RPNUMERICS.setConfiguration(configuration.getName(), configuration);
//                
//                System.out.println("Configuration apos end : " +configuration);
//                
//            }
    }

    @Override
    public JComponent createUIComponent() {

        JPanel panel_ = new JPanel();

        JLabel methodLabel = new JLabel(configurationParameter_);

        HashMap<String, String> paramsValues = new HashMap<String, String>();

//        if (configurationParameter_.contains("sort")) {//To eigen sort functions
//
//            List<String> sortFunctionList = RPNUMERICS.getEigenSortFunctionNames();
//
//            for (String string : sortFunctionList) {
//
//                paramsValues.put(string, string);
//            }
//
//        } else {//To characteristics 
//
//            paramsValues.put("on curve", "1");
//            paramsValues.put("on domain", "0");
//        }\
        if (configurationParameter_.contains("method")) {//To eigen sort functions

            String[] hugoniotMethodNames = RPNUMERICS.getHugoniotNames();

            String[] hugoniotCaseNames = RPNUMERICS.getHugoniotCaseNames(hugoniotMethodNames[0]);

            JComboBox casesNameCombo = new JComboBox(hugoniotCaseNames);

            JComboBox hugoniotMethodsComboBox = new JComboBox(hugoniotMethodNames);

            casesNameCombo.addActionListener(new DefaultComboEventHandler());

            hugoniotMethodsComboBox.addActionListener(new HugoniotMethodComboEventHandler(casesNameCombo));

            panel_.add(hugoniotMethodsComboBox);

            panel_.add(casesNameCombo);

        }

        if (configurationParameter_.contains("origin")) {

            String[] waveCurveCaseNames = getWaveCurveCaseNames();
            
            
            JComboBox casesNameCombo = new JComboBox(waveCurveCaseNames);
            
            
            casesNameCombo.addActionListener(new DefaultComboEventHandler());
            
            
            panel_.add(casesNameCombo);
            
            

        }
        
        
         if (configurationParameter_.contains("name")) {

             String [] transName= new String[RPNUMERICS.getTransisionalLinesNames().size()] ;
             
             ArrayList<String> transitionalNames = RPNUMERICS.getTransisionalLinesNames();
             
             JComboBox casesNameCombo = new JComboBox(transitionalNames.toArray(transName));
            
            casesNameCombo.addActionListener(new DefaultComboEventHandler());
            
            
            panel_.add(casesNameCombo);
            
            

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

    private class HugoniotMethodComboEventHandler implements ActionListener {

        JComboBox caseCombo_;

        public HugoniotMethodComboEventHandler(JComboBox caseCombo_) {
            this.caseCombo_ = caseCombo_;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            JComboBox combo = (JComboBox) e.getSource();

            String selectedItem = (String) combo.getSelectedItem();

//            System.out.println("Chamando nome item changed" + configuration_.getName() + " " + configurationParameter_ + " " + selectedItem);

            RPNUMERICS.setParamValue(configuration_.getName(), "method", selectedItem);

            caseCombo_.removeAllItems();

            String[] hugoniotCaseNames = RPNUMERICS.getHugoniotCaseNames(selectedItem);

            for (String string : hugoniotCaseNames) {

                caseCombo_.addItem(string);

//                System.out.println("Adicionando casos: " + string);

            }

        }

    }

   
    
        
        
        private class DefaultComboEventHandler implements ActionListener {
              
              @Override
              public void actionPerformed(ActionEvent e) {
                  
                  JComboBox combo = (JComboBox) e.getSource();
                  
                  String selectedItem = (String) combo.getSelectedItem();
                  
                  
                  
                  RPNUMERICS.setParamValue(configuration_.getName(), configurationParameter_, selectedItem);
                  
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
            } else {//To eigen sort function
//                RPNUMERICS.setParamValue(configuration_.getName(), configurationParameter_, (String)combo.getSelectedItem());
//                RPNUMERICS.setEigenSortFunction((String)combo.getSelectedItem());
            }

        }

    }
}
     

