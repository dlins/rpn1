/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpn.parser;

import java.util.ArrayList;
import java.util.HashMap;


public class PhysicsProfile {
    
    private String name_;
    private ArrayList<HashMap<String, String>> fluxParamsArray_ = new ArrayList<HashMap<String, String>>();
    private  String [] boundary_;
    private int boundaryDimension_;
    private boolean iso2equiBoundary_;

    public  String getName() {
        return name_;
    }

    public void addFluxParam(String paramName, String defaulValue,int index) {
        
        HashMap<String, String> fluxParam = new HashMap<String, String>(1);
        
        fluxParam.put(paramName, defaulValue);
        
        fluxParamsArray_.add(index, fluxParam);
        
        
    }

    public  ArrayList <HashMap<String,String>> getFluxParamArrayList(){
        return fluxParamsArray_;
    }
    
    public  void setName(String aName_) {
        name_ = aName_;
    }

    public int getFluxSize() {
        return fluxParamsArray_.size();
    }
    
    public  void setBoundary(String [] boundary){
        boundary_=boundary;
    }
    
    public  String [] getBoundary(){
        return boundary_;
    }

    public int getBoundaryDimension() {
        return boundaryDimension_;
    }

    public void setBoundaryDimension(int boundaryDimension_) {
        this.boundaryDimension_ = boundaryDimension_;
    }

    public boolean isIso2equiBoundary() {
        return iso2equiBoundary_;
    }

    public void setIso2equiBoundary(boolean iso2equiBoundary_) {
        this.iso2equiBoundary_ = iso2equiBoundary_;
    }

}
