/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.io.File;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class PluginTableModel extends DefaultTableModel {

    private static PluginTableModel instance_;
    private static String pluginDir_ = "/"; //TODO Change initial value

    private PluginTableModel(Vector<Vector<String>> pluginNames, Vector<String> columnNames) {

        super(pluginNames, columnNames);

    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public static PluginTableModel instance() {

        if (instance_ == null) {

            instance_ = initModel();

        }
        return instance_;

    }

    private static PluginTableModel initModel() {

        Vector<String> columnNames_ = new Vector<String>();
        Vector<Vector<String>> data_ = new Vector<Vector<String>>();

        columnNames_.add("Plugin Type");
        columnNames_.add("Library");
        columnNames_.add("Class");
        columnNames_.add("Constructor Method");

        Vector<String> type1 = new Vector<String>();

        type1.add("WaveFlow");

        data_.add(type1);


        return new PluginTableModel(data_, columnNames_);


    }

    public static String getPluginDir() {
        return pluginDir_;
    }

    public static void setPluginDir(String aPluginDir_) {

        char slash = aPluginDir_.charAt(aPluginDir_.length() - 1);

        if (slash == File.separatorChar) {
            pluginDir_ = aPluginDir_;
        } else {
            pluginDir_ = aPluginDir_ + "/";
        }
    }
}
