/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpn.RPnDialog;

public class PluginDirectoryDialog extends RPnDialog {

    private JLabel pluginDirLabel_;
    private JPanel pluginDirPanel_;
    private JTextField pluginDirTextField_;

    public PluginDirectoryDialog() {
        
        super(false);

        pluginDirLabel_ = new JLabel("Plugin Directory");
        pluginDirPanel_ = new JPanel();
        pluginDirPanel_.add(pluginDirLabel_);

        pluginDirTextField_ = new JTextField(PluginTableModel.getPluginDir());

        pluginDirTextField_.setColumns(20);
        pluginDirPanel_.add(pluginDirTextField_);
        this.getContentPane().add(pluginDirPanel_, BorderLayout.CENTER);
        
        pack();
        
        
    }

    @Override
    protected void apply() {
        
        File testFile = null;
        String tempDirString = null;
        
        try {
            tempDirString = pluginDirTextField_.getText();
            testFile = new File(pluginDirTextField_.getText());
            
            if (!testFile.canRead() || testFile.isFile()) {
                throw new Exception();
            }
            PluginTableModel.setPluginDir(tempDirString);
            
            PluginInfoController.update();
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(this, "Error in directory choose !", "ERROR", JOptionPane.ERROR_MESSAGE);
            pluginDirTextField_.setText(tempDirString);

        }
        
        

    }
}
