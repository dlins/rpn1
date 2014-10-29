/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.ui;

import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import rpn.configuration.Configuration;

/**
 *
 * @author edsonlan
 */
public class ConfigurationView {
    
    private JPanel panel_;
    private Configuration config_;
    
    private HashMap<String,ParameterView> componentMap_;

    public ConfigurationView(Configuration config_) {
        this.config_ = config_;
        
        componentMap_ = new HashMap<String,ParameterView>();
        
        
    }
    
    
    public void addComponent(String parameterName, ParameterView component){
        componentMap_.put(parameterName, component);

        JPanel panel = new JPanel();
        panel_.add(component.getComponent());
    }
    
    

    public JPanel getContainer() {
        return panel_;
    }

    public Configuration getConfiguration() {
        return config_;
    }
    
    
    
    
    
    
    
    
}
