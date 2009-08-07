/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import rpnumerics.RarefactionProfile;
import rpnumerics.ShockProfile;
import rpnumerics.plugin.RPnPluginManager;

public class PluginTableModel extends DefaultTableModel {

    private static PluginTableModel instance_;

 private static String pluginDir_ = System.getProperty("rpnhome") + System.getProperty("file.separator") + "lib" + System.getProperty("file.separator") + System.getenv("RPHOSTTYPE") + System.getProperty("file.separator") + "plugins" + System.getProperty("file.separator");

    private static HashMap<String, PluginProfile> pluginConfigMap_;
    private static Vector<String> columnNames_ = new Vector<String>();

    private PluginTableModel(Vector<Vector<String>> pluginNames, Vector<String> columnNames) {

        super(pluginNames, columnNames);
        setPluginDir(pluginDir_);

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

        Vector<Vector<String>> data = new Vector<Vector<String>>();

        getColumnNames().add("Plugin Type");
        getColumnNames().add("Library");
        getColumnNames().add("Class");
        getColumnNames().add("Constructor Method");
        getColumnNames().add("Destructor Method");

        Vector<String> type1 = new Vector<String>();

        type1.add(ShockProfile.SHOCKFLOW_NAME);

        type1.add("RPnDefaultPlugins.so");
        type1.add("ShockFlowPlugin");
        type1.add("createConservation");
        type1.add("destroyConservation");


        RPnPluginManager.configPlugin(ShockProfile.SHOCKFLOW_NAME, "RPnDefaultPlugins.so", "ShockFlowPlugin", "createConservation", "destroyConservation");


        Vector<String> type2 = new Vector<String>();

        type2.add(RarefactionProfile.RAREFACTIONFLOW_NAME);

        type2.add("RPnDefaultPlugins.so");
        type2.add("RarefactionFlowPlugin");
        type2.add("createRarefaction");
        type2.add("destroyRarefaction");


        RPnPluginManager.configPlugin(RarefactionProfile.RAREFACTIONFLOW_NAME, "RPnDefaultPlugins.so", "RarefactionFlowPlugin", "createRarefaction", "destroyRarefaction");


        data.add(type1);
        data.add(type2);

        return new PluginTableModel(data, getColumnNames());

    }

    public static String getPluginDir() {
        return pluginDir_;
    }

    public static Vector<String> getColumnNames() {
        return columnNames_;
    }

    public static HashMap<String, PluginProfile> getPluginConfigMap() {
        return pluginConfigMap_;
    }

    public static void setPluginConfig(String pluginType, PluginProfile profile) {

        pluginConfigMap_.put(pluginType, profile);
    }

    public static PluginProfile getPluginConfig(String pluginType) {
        return pluginConfigMap_.get(pluginType);

    }

    public static void setPluginConfigMap(HashMap<String, PluginProfile> pluginConfigMap) {
        pluginConfigMap_ = pluginConfigMap;
    }

    public static void setPluginDir(String aPluginDir_) {

        char slash = aPluginDir_.charAt(aPluginDir_.length() - 1);

        if (slash == File.separatorChar) {
            pluginDir_ = aPluginDir_;
        } else {
            pluginDir_ = aPluginDir_ + File.separatorChar;
        }
    }
}
