/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import rpn.plugininterface.PluginInfoController;
import rpn.plugininterface.PluginInfoPanel;
import rpn.plugininterface.PluginInfoParser;


public class RPnPluginDialog extends RPnDialog {

    private PluginInfoController controller_;

    public RPnPluginDialog() {
        super(false,true);
        setTitle("Plugins");
        PluginInfoPanel pluginPanel = new PluginInfoPanel();
        PluginInfoParser parser = new PluginInfoParser();
        PluginInfoController controller = new PluginInfoController(pluginPanel, parser);
        controller_ = controller;
        getContentPane().add(pluginPanel, BorderLayout.NORTH);
        getContentPane().setPreferredSize(new Dimension(1200, 500));
        pack();
    }

    @Override
    protected void apply() {
        controller_.actionPerformed((ActionEvent) applyButton.getAction());
    }
    
    
    @Override
    protected void cancel(){
        PluginInfoPanel.unDoUpdateTable();
        apply();
        dispose();
    }

    @Override
    protected void begin() {

    }
    
    
}
