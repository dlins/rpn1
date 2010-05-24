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
    private ArrayList<Configuration> configurationArrayList_ = new ArrayList<Configuration>();
    private String name_;
    private String type_;

    public Configuration(String name, String type, HashMap<String, String> paramsAndValues) {

        params_ = paramsAndValues;
        paramOrder_ = new ArrayList<String>();
        name_ = name;
        type_ = type;
    }

    public Configuration(ConfigurationProfile profile) {
        params_ = profile.getParams();
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

    public HashMap<String, String> getParams() {
        return params_;
    }

    public void setParamValue(String paramName, String paramValue) {


        params_.put(paramName, paramValue);

        if (paramOrder_.indexOf(paramName) == -1) {
            paramOrder_.add(paramName);
        }



    }

    public String getParamValue(String paramName) {

        return params_.get(paramName);
    }

    public void addConfiguration(Configuration configuration) {
        configurationArrayList_.add(configuration);
    }

    public void addConfiguration(int index, Configuration configuration) {
        configurationArrayList_.add(index, configuration);
    }

    public int configurationArraySize() {
        return configurationArrayList_.size();
    }

    public Configuration getConfiguration(int index) {
        return configurationArrayList_.get(index);
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
        for (Configuration config : configurationArrayList_) {
            stringBuffer.append(config.toString() + "\n");
        }

        return stringBuffer.toString();

    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();
        Set<Entry<String, String>> paramsSet = params_.entrySet();

        if (getType().equalsIgnoreCase("physics")) {
            buffer.append("<PHYSICS name=\"" + getName() + "\">\n");
            buffer.append(getConfiguration(0).toXML());//Printing main boundary

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


        return buffer.toString();
    }
}
