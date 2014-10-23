/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpn.plugininterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTree;


public class PluginTreeClickController implements ActionListener{

    JTree clickedTree_;
    
    public PluginTreeClickController(JTree clickedTree) {
        clickedTree_ = clickedTree;
    }

    public void actionPerformed(ActionEvent e) {
        
        PluginInfoPanel.transferTreeToTable(clickedTree_);

    }

}
