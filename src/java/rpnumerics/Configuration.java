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

public class Configuration {

    private HashMap<String, String> params_;
    private ArrayList<String> paramOrder_;
    private HashMap<String, Configuration> configurationMap_ = new HashMap<String, Configuration>();
    private String name_;
    private String type_;

    public Configuration(String name, String type, HashMap<String, String> paramsAndValues) {

        params_ = paramsAndValues;
        paramOrder_ = new ArrayList<String>();
        name_ = name;
        type_ = type;
    }

    public Configuration(ConfigurationProfile profile) {
        System.out.println(profile);
        if (profile.getParams() != null) {
            params_ = profile.getParams();
        }

        if (!profile.getProfiles().isEmpty()) {

            Set<Entry<String, ConfigurationProfile>> profilesSet = profile.getProfiles().entrySet();
            for (Entry<String, ConfigurationProfile> profileEntry : profilesSet) {
                configurationMap_.put(profileEntry.getKey(), new Configuration(profileEntry.getValue()));
            }


        }

        name_ = profile.getName();
        type_ = profile.getType();
        paramOrder_ = new ArrayList<String>();
        for (int i = 0; i < profile.getIndicesSize(); i++) {
            HashMap<String, String> fluxParam = profile.getParam(i);
            Set<Entry<String, String>> fluxParamSet = fluxParam.entrySet();
            for (Entry<String, String> fluxEntry : fluxParamSet) {
                setParamValue(fluxEntry.getKey(), fluxEntry.getValue());
                setParamOrder(fluxEntry.getKey(), i);
            }
        }
    }

    public Configuration(String name, String type) {
        params_ = new HashMap<String, String>();
        paramOrder_ = new ArrayList<String>();
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

                HashMap<String, String> param = profile.getParam(i);

                Set<Entry<String, String>> paramSet = param.entrySet();

                for (Entry<String, String> paramEntry : paramSet) {

                    setParamOrder(paramEntry.getKey(), i);
                    setParamValue(paramEntry.getKey(), paramEntry.getValue());
                }

            }

        }

    }

    public int getParamOrder(String paramName) {

        return paramOrder_.indexOf(paramName);
    }

    public int getParamsSize() {
        return params_.size();
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

//    public void addConfiguration(int index, Configuration configuration) {
//        configurationArrayList_.add(index, configuration);
//    }
//
//    public int configurationArraySize() {
//        return configurationArrayList_.size();
//    }
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

        if (getType().equalsIgnoreCase("physics")) {
            buffer.append("<PHYSICS name=\"" + getName() + "\">\n");

            Set<Entry<String, Configuration>> configurationSet = configurationMap_.entrySet();

            for (Entry<String, Configuration> entry : configurationSet) {
                buffer.append(entry.getValue().toXML());//Printing main boundary

            }

            buffer.append("<FLUXFUNCTION>\n");
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<FLUXPARAMS name=\"" + entry.getKey() + "\" " + "position= \"" + getParamOrder(entry.getKey()) + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</FLUXFUNCTION>\n");

            buffer.append("</PHYSICS>\n");
        }


        if (getType().equalsIgnoreCase("boundary")) {
            buffer.append("<BOUNDARY name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<BOUNDARYPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</BOUNDARY>\n");
        }



        if (getType().equalsIgnoreCase("auxboundary")) {
            buffer.append("<AUXBOUNDARY name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<BOUNDARYPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</AUXBOUNDARY>\n");
        }



        if (getType().equalsIgnoreCase("curve")) {
            buffer.append("<CURVE name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<CURVEPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</CURVE>\n");
        }

        if (getType().equalsIgnoreCase("method")) {
            buffer.append("<METHOD name=\"" + getName() + "\">\n");
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<METHODPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</METHOD>\n");
        }


        if (getType().equalsIgnoreCase("visual")) {
            buffer.append("<VIEWCONFIGURATION modeldomain=\"" + getName() + "\">\n");
            Set<Entry<String, Configuration>> configurationEntry = configurationMap_.entrySet();
            for (Entry<String, Configuration> configEntry : configurationEntry) {

                buffer.append("<PROJDESC name=\"" + configEntry.getValue().getName() + "\">\n");
                HashMap<String, String> configParams = configEntry.getValue().getParams();
                Set<Entry<String, String>> configSet = configParams.entrySet();
                for(Entry<String,String> internalConfigEntry: configSet){
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
