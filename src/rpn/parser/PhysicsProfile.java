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
    private  int dimension_;
    private int boundaryDimension_;

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


    public  int getDimension() {
        return dimension_;
    }

    public  void setDimension(int aDimension_) {
        dimension_ = aDimension_;
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

}
