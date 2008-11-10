/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JTable;
import javax.swing.table.TableModel;
import rpnumerics.plugin.RPnPluginManager;


public class PluginInfoController implements ActionListener {

    private static PluginInfoPanel pluginPanel_;
    private static PluginInfoParser parser_;

    public PluginInfoController(PluginInfoPanel pluginPanel, PluginInfoParser parser) {
        pluginPanel_ = pluginPanel;
        parser_ = parser;
        update();
    }

    public static void update() {

        parser_.parse();
        pluginPanel_.fillPluginTree(parser_.getLibrary());
        RPnPluginManager.setPluginDir(PluginTableModel.getPluginDir());


    }

    public void actionPerformed(ActionEvent e) {
        
            JTable configTable = pluginPanel_.getConfigTable();

            TableModel configModel = configTable.getModel();

            String data[] = new String[configModel.getColumnCount()];

            for (int i = 0; i < configModel.getRowCount(); i++) {

                int j = 0;

                while (j < configModel.getColumnCount()) {


                    data[j] = (String) configModel.getValueAt(i, j);

                    j++;

                }

                if (argsComplete(data)){

                    RPnPluginManager.configPlugin(data[0], data[1], data[2], data[3]);
                }
            }
    }

    private boolean argsComplete(String[] argsData) {
        int i = 0;
        while (i < argsData.length) {

            if (argsData[i] == null) {
                return false;
            }
            i++;

        }

        return true;
    }
}
