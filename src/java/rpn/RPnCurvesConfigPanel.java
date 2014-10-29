/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.ui.RPnInputComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.*;
import rpn.command.ChangeDirectionCommand;
import rpn.configuration.Configuration;
import rpn.configuration.ConfigurationProfile;
import rpn.configuration.Parameter;
import rpn.ui.UIComponentCreator;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;

public class RPnCurvesConfigPanel extends Observable implements PropertyChangeListener, ActionListener, Observer {
    
    private JRadioButton forwardCheckBox_;
    private JRadioButton backwardCheckBox_;
    private JRadioButton bothCheckBox_;
    private static Integer currentOrbitDirection_ = Orbit.FORWARD_DIR;
    private JTabbedPane curvesTabbedPanel_;
    private ArrayList<RPnInputComponent> inputComponentArray_;
    private JButton okButton_;
    private JPanel mainPainel_;
    private OrbitDirectionListener orbitDirectionListener_;
    private HashMap<String, Component> componentMap_;
    
    public RPnCurvesConfigPanel() {
        
        addObserver(ChangeDirectionCommand.instance());
        

        
        mainPainel_ = new JPanel();
        GridLayout grid = new GridLayout(1, 1);
        mainPainel_.setLayout(grid);
        componentMap_ = new HashMap<String, Component>();
        curvesTabbedPanel_ = new JTabbedPane();
        buildPanel();
        
    }
    
    public void setFocus(String panelName) {
        curvesTabbedPanel_.setSelectedComponent(componentMap_.get(panelName));
        
    }
    
    private void buildPanel() {
        
        inputComponentArray_ = new ArrayList<RPnInputComponent>();
        okButton_ = new JButton("OK");
        okButton_.setEnabled(false);
        orbitDirectionListener_ = new OrbitDirectionListener();
        
        HashMap<String, Configuration> configMap = RPNUMERICS.getConfigurations();
        curvesTabbedPanel_.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        
        Set<Entry<String, Configuration>> configSet = configMap.entrySet();
        
        for (Entry<String, Configuration> entry : configSet) {
            
            String configurationType = entry.getValue().getType();
            
            
            if (configurationType.equalsIgnoreCase(ConfigurationProfile.CURVECONFIGURATION)){
                
                Configuration config = entry.getValue();
                
                HashMap<String, String> configParams = config.getParams();
                
                Set<Entry<String, String>> paramSet = configParams.entrySet();
                
                JPanel paramsPanel = new JPanel();
                
                BoxLayout box = new BoxLayout(paramsPanel, BoxLayout.X_AXIS);
                paramsPanel.setLayout(box);
                paramsPanel.invalidate();
                paramsPanel.setSize(new Dimension(200,20));
                
                
                for (Entry<String, String> paramEntry : paramSet) {
                    if (!paramEntry.getKey().contains("resolution")) {
                              
                        UIComponentCreator componentCreator = new UIComponentCreator(config, paramEntry.getKey());
                        JComponent component = componentCreator.createUIComponent();
                        paramsPanel.add(component);
                      
                    }
                    
                }
                java.util.List<Parameter> parameterList = config.getParameterList();
                
                
                for (Parameter parameter : parameterList) {
                    
                    UIComponentCreator componentCreator = new UIComponentCreator(parameter);
                    JComponent component = componentCreator.createUIComponent();
                    paramsPanel.add(component);
                    
                    
                }
                
                
                
                
                if (paramsPanel.getComponentCount() > 0) {
                    curvesTabbedPanel_.addTab(config.getName(), paramsPanel);
                    componentMap_.put(config.getName(), paramsPanel);
                }
                
            }
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
//            if (!configurationType.equalsIgnoreCase(ConfigurationProfile.PHYSICS_CONFIG) && !configurationType.equalsIgnoreCase("VISUAL") && !configurationType.equalsIgnoreCase("METHOD")) {
//                
//                Configuration config = entry.getValue();
//                
//                HashMap<String, String> configParams = config.getParams();
//                
//                Set<Entry<String, String>> paramSet = configParams.entrySet();
//                
//                JPanel paramsPanel = new JPanel();
//                
//                BoxLayout box = new BoxLayout(paramsPanel, BoxLayout.X_AXIS);
//                paramsPanel.setLayout(box);
//                paramsPanel.invalidate();
//                paramsPanel.setSize(new Dimension(200,20));
//                
//                
//                for (Entry<String, String> paramEntry : paramSet) {
//                    if (!paramEntry.getKey().contains("resolution")) {
//                        UIComponentCreator componentCreator = new UIComponentCreator(config, paramEntry.getKey());
//                        JComponent component = componentCreator.createUIComponent();
//                        paramsPanel.add(component);
//                    }
//                    
//                }
//                
//                if (paramsPanel.getComponentCount() > 0) {
//                    curvesTabbedPanel_.addTab(config.getName(), paramsPanel);
//                    componentMap_.put(config.getName(), paramsPanel);
//                }
//                
//            }
            
        }
        
        mainPainel_.add(curvesTabbedPanel_);

        
    }
    
    public static Integer getOrbitDirection() {
        return currentOrbitDirection_;
    }
    
    public JPanel getContainer() {
        return mainPainel_;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        if (evt.getNewValue().equals("bifurcationcurve")) {//Bifurcation Curves selected

            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(false);
                backwardCheckBox_.setEnabled(false);
                bothCheckBox_.setEnabled(false);
            }
        }
        
        if (evt.getNewValue().equals("phasediagram")) {//Phase Diagram Curves selected

            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(true);
                backwardCheckBox_.setEnabled(true);
                bothCheckBox_.setEnabled(true);
                
            }
            
        }
        
        if (evt.getNewValue().equals("wavecurve")) {//Wave Curves selected

            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(true);
                backwardCheckBox_.setEnabled(true);
                bothCheckBox_.setEnabled(true);
                
            }
            
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        
        for (RPnInputComponent rPnInputComponent : inputComponentArray_) {
            rPnInputComponent.applyConfigurationChange();
        }
        
        orbitDirectionListener_.actionPerformed(e);
        okButton_.setEnabled(false);
    }
    
    public void update(Observable o, Object arg) {
        okButton_.setEnabled(true);
    }
    
    private class OrbitDirectionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            
            if (forwardCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.FORWARD_DIR;
            } else if (backwardCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.BACKWARD_DIR;
            } else if (bothCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.BOTH_DIR;
            }
            
            RPNUMERICS.setParamValue("fundamentalcurve", "direction", currentOrbitDirection_.toString());
        }
    }
}
