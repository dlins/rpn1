/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class PluginProfile {

    private Vector<HashMap<String, String>> paramsVector_;

    public PluginProfile(PluginProfile profile) {
        this.paramsVector_ = (Vector<HashMap<String, String>>) profile.getParamsVector().clone();
    }

    public PluginProfile() {
        paramsVector_ = new Vector<HashMap<String, String>>();
    }

    public PluginProfile(Vector<HashMap<String, String>> paramsVector) {
        this.paramsVector_ = paramsVector;
    }

    public String getParamValue(String paramName) {

        Set<Entry<String, String>> paramsSet;
        Iterator<Entry<String, String>> paramsIterator;
        for (int i = 0; i < paramsVector_.size(); i++) {

            HashMap<String, String> paramsHash = paramsVector_.get(i);

            paramsSet = paramsHash.entrySet();

            paramsIterator = paramsSet.iterator();

            Entry<String, String> data = paramsIterator.next();

            if (data.getKey().equals(paramName)) {

                 return data.getValue();

            }

        }
        return "";
    }

    public String[] getParamsDefValues() {


        String[] paramsNames = new String[paramsVector_.size()];

        Set<Entry<String, String>> paramsSet;
        Iterator<Entry<String, String>> paramsIterator;
        for (int i = 0; i < paramsVector_.size(); i++) {

            HashMap<String, String> paramsHash = paramsVector_.get(i);

            paramsSet = paramsHash.entrySet();

            paramsIterator = paramsSet.iterator();

            Entry<String, String> data = paramsIterator.next();

            paramsNames[i] = data.getValue();

        }


        return paramsNames;


    }

    public String[] getParamsName() {

        String[] paramsNames = new String[paramsVector_.size()];

        Set<Entry<String, String>> paramsSet;
        Iterator<Entry<String, String>> paramsIterator;
        for (int i = 0; i < paramsVector_.size(); i++) {

            HashMap<String, String> paramsHash = paramsVector_.get(i);

            paramsSet = paramsHash.entrySet();

            paramsIterator = paramsSet.iterator();

            Entry<String, String> data = paramsIterator.next();

            paramsNames[i] = data.getKey();

        }
        return paramsNames;

    }

    public void addPluginParam(String paramName, String defaultValue) {

        HashMap<String, String> parmHash = new HashMap<String, String>(1);
        parmHash.put(paramName, defaultValue);
        paramsVector_.add(parmHash);

    }

    public void setPluginParm(String paramName, String value) {
        Set<Entry<String, String>> paramsSet;
        Iterator<Entry<String, String>> paramsIterator;

        for (int i = 0; i < paramsVector_.size(); i++) {
            HashMap<String, String> paramsHash = paramsVector_.get(i);
            paramsSet = paramsHash.entrySet();
            paramsIterator = paramsSet.iterator();
            Entry<String, String> data = paramsIterator.next();

            if (data.getKey().equals(paramName)) {

                paramsHash.put(data.getKey(), value);
                paramsVector_.setElementAt(paramsHash, i);

            }
        }

    }

    public Vector<HashMap<String, String>> getParamsVector() {
        return paramsVector_;
    }

    public void setParamsVector(Vector<HashMap<String, String>> paramsVector_) {
        this.paramsVector_ = paramsVector_;
    }

    @Override
    public String toString() {

        String out = "";
        Set<Entry<String, String>> paramsSet;
        Iterator<Entry<String, String>> paramsIterator;

        for (int i = 0; i < paramsVector_.size(); i++) {

            HashMap<String, String> paramsHash = paramsVector_.get(i);

            paramsSet = paramsHash.entrySet();

            paramsIterator = paramsSet.iterator();

            Entry<String, String> data = paramsIterator.next();

            out = out + data.getKey() + " " + data.getValue() + "\n";

        }
        return out;
    }
}
