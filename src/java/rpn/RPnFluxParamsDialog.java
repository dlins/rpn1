/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;


import rpn.controller.ui.UIController;
import rpn.configuration.Configuration;
import rpn.configuration.Parameter;
import rpn.configuration.PhysicsConfigurationParams;
import rpn.ui.ConfigurationView;
import rpnumerics.RPNUMERICS;

public class RPnFluxParamsDialog extends RPnDialog {

  

    public RPnFluxParamsDialog() {
        super(true, false);

        setTitle(RPNUMERICS.physicsID());
        beginButton.setText("OK");

        removeDefaultApplyBehavior();
        
        setLocation(400, 400);


        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
        
        
//        PhysicsConfigurationParams physicsParams  = (PhysicsConfigurationParams) physicsConfiguration.getConfiguration("fluxfunction");

        HashMap<String, Configuration> configurationMap = physicsConfiguration.getConfigurationMap();
        
        
        
        
        
        Set<Map.Entry<String, Configuration>> entrySet = configurationMap.entrySet();
        Iterator<Map.Entry<String, Configuration>> iterator = entrySet.iterator();
        
        
//        Iterator<Map.Entry<String, Configuration>> iterator = configurationMap.entrySet().iterator();
        
        while (iterator.hasNext()) {
           
            Map.Entry<String, Configuration> entry = iterator.next();
            
            ConfigurationView configView = new ConfigurationView(entry.getValue());
            
            BoxLayout boxLayout = new BoxLayout(configView.getContainer(), BoxLayout.Y_AXIS);
            
//            configView.getContainer().setLayout(boxLayout);
            
            getContentPane().add(configView.getContainer());
            
            
            
        }
        
        
        
        
        
        
   
        pack();

    }

    @Override
    protected void apply() {
        
        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID()).getConfiguration("fluxfunction");
        java.util.List<Parameter> parameterList = physicsConfiguration.getParameterList();
        
        
        for (int i = 0; i < parameterList.size(); i++) {
            Parameter parameter = parameterList.get(i);
            
            RPNUMERICS.setPhysicsParams(i, parameter.getValue());
            
        }
     
        
        String phaseSpaceName = UIController.instance().getActivePhaseSpace().getName();
        rpn.command.ChangeFluxParamsCommand.instance().applyChange(new PropertyChangeEvent(rpn.command.ChangeFluxParamsCommand.instance(),phaseSpaceName,
                physicsConfiguration,physicsConfiguration));
        rpn.command.ChangeFluxParamsCommand.instance().updatePhaseDiagram();
    }

    @Override
    protected void begin() {

        dispose();
    }
    //----------------------------------------------------------
}
