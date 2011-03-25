/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ConfigurationProfile {

    private String name_;
    private String type_;
    private HashMap<String, ConfigurationProfile> configurationProfileMap_;
    private HashMap<String, String> paramsMap_ = new HashMap<String, String>();
    private HashMap<Integer, String> paramsIndexMap_ = new HashMap<Integer, String>();
    public static final String PHISICS_PROFILE = "PHYSICS";
    public static final String CURVE_PROFILE = "CURVE";
    public static final String METHOD_PROFILE = "METHOD";
    public static final String BOUNDARY_PROFILE = "BOUNDARY";
    public static final String VISUALIZATION_PROFILE = "VISUAL";

    public ConfigurationProfile(String name, String type) {
        type_ = type;
        name_ = name;
        configurationProfileMap_ = new HashMap<String, ConfigurationProfile>();
    }

    public int getIndicesSize() {
        return paramsIndexMap_.size();
    }

    public HashMap<String, ConfigurationProfile> getProfiles() {
        return configurationProfileMap_;
    }

    public void addParam(int index, String paramName, String defaultValue) {

        paramsMap_.put(paramName, defaultValue);

        paramsIndexMap_.put(index, paramName);
    }

    public void addParam(String paramName, String defaulValue) {
        paramsMap_.put(paramName, defaulValue);
    }

    public HashMap<String, String> getParam(int index) {

        String paramName = paramsIndexMap_.get(index);
        String paramValue = paramsMap_.get(paramName);
        HashMap<String, String> paramValuePair = new HashMap<String, String>();
        paramValuePair.put(paramName, paramValue);
        return paramValuePair;

    }

    public String getParam(String paramName) {
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

    public void addConfigurationProfile(String configurationType, ConfigurationProfile configProfile) {
        configurationProfileMap_.put(configurationType, configProfile);
    }

    public ConfigurationProfile getConfigurationProfile(String configurationType) {

        return configurationProfileMap_.get(configurationType);
    }

    public int profileArraySize() {
        return configurationProfileMap_.size();
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Name: " + name_ + "\n");

        Set<Entry<String, String>> paramsSet = paramsMap_.entrySet();
        for (Entry<String, String> paramsEntry : paramsSet) {
            stringBuffer.append(paramsEntry.getKey() + " " + paramsEntry.getValue() + "\n");

        }

        Set<Entry<String, ConfigurationProfile>> configurationSet = configurationProfileMap_.entrySet();
        for (Entry<String, ConfigurationProfile> configurationEntry : configurationSet) {
            if (configurationEntry.getValue() == null) {
                System.out.println("Valor nulo");
            } else {
                stringBuffer.append(configurationEntry.getValue().toString() + "\n");
            }


        }

        return stringBuffer.toString();

    }

    public int getParamsSize() {

        return paramsMap_.size();

    }
}
