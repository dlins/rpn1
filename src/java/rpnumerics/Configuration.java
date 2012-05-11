/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import rpn.parser.ConfigurationProfile;
import wave.util.RealVector;

public class Configuration {

    private HashMap<String, String> params_;
    private ArrayList<String> paramOrder_;
    private HashMap<String, Configuration> configurationMap_;
    private String name_;
    private String type_;


    public Configuration(ConfigurationProfile profile) {

        configurationMap_ = new HashMap<String, Configuration>();
        params_ = profile.getParams();
        paramOrder_ = new ArrayList<String>();

        int index = 0;


        while (index < profile.getIndicesSize()) {
            Entry<String, String> param = profile.getParam(index);
            setParamOrder(param.getKey(), index);
            index++;


        }

        if (!profile.getProfiles().isEmpty()) {

            Set<Entry<String, ConfigurationProfile>> profilesSet = profile.getProfiles().entrySet();
            for (Entry<String, ConfigurationProfile> profileEntry : profilesSet) {
                configurationMap_.put(profileEntry.getKey(), new Configuration(profileEntry.getValue()));
            }
        }

        name_ = profile.getName();
        type_ = profile.getType();

    }

    public Configuration(String name, String type) {
        params_ = new HashMap<String, String>();
        paramOrder_ = new ArrayList<String>();
        configurationMap_ = new HashMap<String, Configuration>();
        name_ = name;
        type_ = type;
    }

    public void removeParam(String paramName) {
        params_.remove(paramName);
    }

    public void setParamOrder(String paramName, int order) {
        paramOrder_.add(order, paramName);
    }

    public void setParams(ConfigurationProfile profile) {
        if (!profile.getParams().isEmpty()) {

            for (int i = 0; i < profile.getIndicesSize(); i++) {

                Entry<String, String> param = profile.getParam(i);

                setParamOrder(param.getKey(), i);
                setParamValue(param.getKey(), param.getValue());


            }

        }

    }


    public int getParamOrder(String paramName) {

        return paramOrder_.indexOf(paramName);
    }

    public int getParamsSize() {
        return params_.size();
    }

    public HashMap<String, Configuration> getConfigurationMap() {
        return configurationMap_;
    }

    public String getParam(int paramOrder) {  
        try {

            String paramName = paramOrder_.get(paramOrder);

            return params_.get(paramName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;

    }
    

    public String getParamName(int paramOrder) {

        try {
            String paramName = paramOrder_.get(paramOrder);

            return paramName;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public String[] getParamNames() {
        String[] paramNames = new String[params_.keySet().size()];


        int i = 0;
        for (String string : params_.keySet()) {
            paramNames[i] = string;
            i++;
        }

        return paramNames;
    }

    public HashMap<String, String> getParams() {
        return params_;
    }

    public void setParamValue(String paramName, String paramValue) {
        params_.put(paramName, paramValue);

        if (paramOrder_.indexOf(paramName) == -1) {
            paramOrder_.add(paramName);
        }

    }

    public String getParam(String paramName) {

        return params_.get(paramName);
    }

    public void addConfiguration(String name, Configuration configuration) {

        configurationMap_.put(name, configuration);
    }

    public Configuration getConfiguration(String name) {
        return configurationMap_.get(name);
    }

    public String getName() {
        return name_;
    }

    public String getType() {
        return type_;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Configuration name: " + name_ + " Configuration type: " + type_ + "\n");
        Set<Entry<String, String>> paramsSet = params_.entrySet();
        for (Entry<String, String> paramsEntry : paramsSet) {
            stringBuffer.append(paramsEntry.getKey() + " " + paramsEntry.getValue() + "\n");
        }

        Set<Entry<String, Configuration>> configurationSet = configurationMap_.entrySet();

        for (Entry<String, Configuration> entry : configurationSet) {
            stringBuffer.append(entry.getValue().toString());//Printing main boundary

        }
        return stringBuffer.toString();

    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();
        Set<Entry<String, String>> paramsSet = params_.entrySet();

        if (getType().equalsIgnoreCase(ConfigurationProfile.PHYSICS_PROFILE)) {
            buffer.append("<PHYSICS name=\"" + getName() + "\">\n");

            Set<Entry<String, Configuration>> configurationSet = configurationMap_.entrySet();

            for (Entry<String, Configuration> entry : configurationSet) {//Printing boundary first

                if(entry.getValue().getType().equals(ConfigurationProfile.BOUNDARY))

                buffer.append(entry.getValue().toXML());

            }

            for (Entry<String, Configuration> entry : configurationSet) {//Printing the rest

                if (!entry.getValue().getType().equals(ConfigurationProfile.BOUNDARY))

                    buffer.append(entry.getValue().toXML());

            }

            buffer.append("</PHYSICS>\n");
        }


        if (getType().equalsIgnoreCase(ConfigurationProfile.PHYSICS_CONFIG)) {
            buffer.append("<PHYSICSCONFIG name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {

                buffer.append("<PHYSICSPARAM name=\"" + entry.getKey() + "\" " + "position=\"" + getParamOrder(entry.getKey()) + "\"" + " value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }

            buffer.append("</PHYSICSCONFIG>\n");

        }


        if (getType().equalsIgnoreCase(ConfigurationProfile.BOUNDARY)) {
            buffer.append("<BOUNDARY name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {

                buffer.append("<BOUNDARYPARAM name=\"" + entry.getKey() + "\" " + " value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }

            buffer.append("</BOUNDARY>\n");

        }


        if (getType().equalsIgnoreCase(ConfigurationProfile.CURVE)) {
            buffer.append("<CURVE name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<CURVEPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</CURVE>\n");
        }

        if (getType().equalsIgnoreCase(ConfigurationProfile.METHOD)) {
            buffer.append("<METHOD name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<METHODPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</METHOD>\n");
        }


        if (getType().equalsIgnoreCase(ConfigurationProfile.VISUALIZATION)) {
            buffer.append("<VIEWCONFIGURATION modeldomain=\"" + getName() + "\">\n");
            Set<Entry<String, Configuration>> configurationEntry = configurationMap_.entrySet();
            for (Entry<String, Configuration> configEntry : configurationEntry) {

                buffer.append("<PROJDESC name=\"" + configEntry.getValue().getName() + "\">\n");
                HashMap<String, String> configParams = configEntry.getValue().getParams();
                Set<Entry<String, String>> configSet = configParams.entrySet();
                for (Entry<String, String> internalConfigEntry : configSet) {
                    buffer.append("<VIEWPARAM name=\"" + internalConfigEntry.getKey() + "\" " + "value= \"" + internalConfigEntry.getValue() + "\"/>");
                    buffer.append("\n");
                }
                buffer.append("</PROJDESC>\n");
            }
            buffer.append("</VIEWCONFIGURATION>\n");
        }


        return buffer.toString();
    }

}
