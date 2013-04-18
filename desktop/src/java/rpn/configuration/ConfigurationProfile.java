/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ConfigurationProfile {

    private String name_;
    private String type_;
    private HashMap<String, ConfigurationProfile> configurationProfileMap_;
    private HashMap<String, String> paramsMap_;
    private HashMap<Integer, String> paramsIndexMap_;
    public static final String PHYSICS_PROFILE = "PHYSICS";
    public static final String PHYSICS_CONFIG = "PHYSICS CONFIGURATION";
    public static final String CURVECONFIGURATION = "CURVE CONFIGURATION";
    public static final String COMMANDCONFIGURATION = "COMMAND";
    public static final String METHOD = "METHOD";
    public static final String BOUNDARY = "BOUNDARY";
    public static final String VISUALIZATION = "VISUAL";

    public ConfigurationProfile(String name, String type) {
        type_ = type;
        name_ = name;
        configurationProfileMap_ = new HashMap<String, ConfigurationProfile>();
        paramsMap_ = new HashMap<String, String>();
        paramsIndexMap_ = new HashMap<Integer, String>();
    }

    public int getIndicesSize() {
        return paramsIndexMap_.size();
    }

    public HashMap<String, ConfigurationProfile> getProfiles() {
        return configurationProfileMap_;
    }

    public void addParam(int index, String paramName, String value) {

        paramsMap_.put(paramName, value);

        paramsIndexMap_.put(index, paramName);
    }


    public void addParam(String paramName, String value) {

      //System.out.println("Adicionando o parametro: " + paramName + "ao profile: " + name_);

        if (!paramsMap_.containsKey(paramName)) {

            int index = paramsIndexMap_.size() ;
            paramsIndexMap_.put(index, paramName);
        }

        paramsMap_.put(paramName, value);


    }

    public Entry<String, String> getParam(int index) {

        String paramName = paramsIndexMap_.get(index);
        Set<Entry<String, String>> paramValueSet = paramsMap_.entrySet();
        for (Entry<String, String> entry : paramValueSet) {
            if (entry.getKey().equals(paramName)) {
                return entry;
            }
        }
        System.out.println("Parametro: "+ paramName+ "indice "+ index +"nao encontrado no profile "+ name_);
        return null;
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
        if (paramsMap_.size() == 0) {
        }

        Set<Entry<String, String>> paramsSet = paramsMap_.entrySet();
        for (Entry<String, String> paramsEntry : paramsSet) {
            stringBuffer.append(paramsEntry.getKey() + " " + paramsEntry.getValue() + "\n");

        }

        Set<Entry<String, ConfigurationProfile>> configurationSet = configurationProfileMap_.entrySet();
        for (Entry<String, ConfigurationProfile> configurationEntry : configurationSet) {
                stringBuffer.append(configurationEntry.getValue().toString() + "\n");
        }

        return stringBuffer.toString();

    }

    public int getParamsSize() {

        return paramsMap_.size();

    }
}
