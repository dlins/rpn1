/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ConfigurationProfile {

    private String name_;
    private String type_;
    private ArrayList<ConfigurationProfile> configurationProfileArray_;
    private HashMap<String, String> paramsMap_ = new HashMap<String, String>();
    private HashMap < Integer,String> paramsIndexMap_ = new HashMap<Integer,String>();

    public ConfigurationProfile(String name, String type) {
        type_ = type;
        name_ = name;
        configurationProfileArray_ = new ArrayList<ConfigurationProfile>();
    }

    public int getIndicesSize() {
        return paramsIndexMap_.size();
    }



    public void addParam(int index, String paramName, String defaultValue) {

        paramsMap_.put(paramName, defaultValue);

        paramsIndexMap_.put(index,paramName);
    }

    public void addParam(String paramName, String defaulValue) {
        paramsMap_.put(paramName, defaulValue);
    }

    public HashMap<String, String> getParam(int index) {

        String paramName = paramsIndexMap_.get(index);
        String paramValue = paramsMap_.get(paramName);
        HashMap<String,String> paramValuePair = new HashMap<String, String>();
        paramValuePair.put(paramName,paramValue);
        return paramValuePair;

    }

    public String getParam(String paramName){
        return paramsMap_.get(paramName);
    }

    public HashMap<String, String> getParams() {
        return paramsMap_;
    }

    public String getName() {
        return name_;
    }

    public String getType() {
        return type_;
    }

    public void addConfigurationProfile(ConfigurationProfile configProfile) {
        configurationProfileArray_.add(configProfile);
    }

    public void addConfigurationProfile(int index, ConfigurationProfile configProfile) {
        configurationProfileArray_.add(index, configProfile);
    }

    public ConfigurationProfile getConfigurationProfile(int index) {

        return configurationProfileArray_.get(index);
    }

    public int profileArraySize(){
        return configurationProfileArray_.size();
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(name_ + "\n");

        Set<Entry<String, String>> paramsSet = paramsMap_.entrySet();
        for (Entry<String, String> paramsEntry : paramsSet) {
            stringBuffer.append(paramsEntry.getKey() + " " + paramsEntry.getValue() + "\n");

        }
        for (ConfigurationProfile confProfile : configurationProfileArray_) {

            stringBuffer.append(confProfile.toString() + "\n");

        }

        return stringBuffer.toString();

    }
}
