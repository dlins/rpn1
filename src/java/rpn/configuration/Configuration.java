/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

public abstract class Configuration extends Observable{

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


        name_ = profile.getName();
        type_ = profile.getType();

    }

    public Configuration(ConfigurationProfile profile, HashMap<String, Configuration> innerConfigurations) {


        this(profile);

        configurationMap_ = innerConfigurations;

    }

    public Configuration(String name) {
        params_ = new HashMap<String, String>();
        paramOrder_ = new ArrayList<String>();
        configurationMap_ = new HashMap<String, Configuration>();
        name_ = name;

    }

    public void removeParam(String paramName) {
        params_.remove(paramName);
    }
    
    public void keepParameters(String [] parametersToKeep){
        
      ArrayList<String> tempMap = new ArrayList<String>();
      

      for (int i=0; i < parametersToKeep.length; i++){
          
          tempMap.add(getParam(parametersToKeep[i]));
          
          
      }
      
        
        params_.clear();
        paramOrder_.clear();       
        
        for (int i =0; i < tempMap.size(); i++) {
            
            params_.put(parametersToKeep[i], tempMap.get(i));
            paramOrder_.add(parametersToKeep[i]);
        }
       

        
    } 

    public final void setParamOrder(String paramName, int order) {
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
        
        setChanged();
        notifyObservers();

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
    
    protected void setType(String type){
        
        type_=type;
    }
    

    @Override
    public String toString() {

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("Configuration name: ").append(name_).append(" Configuration type: ").append(type_).append("\n");
        Set<Entry<String, String>> paramsSet = params_.entrySet();
        for (Entry<String, String> paramsEntry : paramsSet) {
            stringBuffer.append(paramsEntry.getKey()).append(" ").append(paramsEntry.getValue()).append("\n");
        }

        Set<Entry<String, Configuration>> configurationSet = configurationMap_.entrySet();

        for (Entry<String, Configuration> entry : configurationSet) {
            stringBuffer.append(entry.getValue().toString());//Printing main boundary

        }
        return stringBuffer.toString();

    }

    public abstract  String toXML();

  }
