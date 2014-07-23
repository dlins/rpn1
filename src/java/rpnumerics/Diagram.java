/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class Diagram implements  RpSolution  {
    //
    // Members
    //



    private final List<DiagramLine> lines_;


    //
    // Constructor
    //
   
    public Diagram(List<DiagramLine> coords) {

        lines_= coords;
    
    }
    
    
    public DiagramLine getLine (int lineIndex){
        return lines_.get(lineIndex);
    }

    public List<DiagramLine> getLines() {
        return lines_;
    }
    
    
    
     
    
    
    
    
    
    
    
    
    
    
    
    
    
    

   


    @Override
    public String toXML() {

        return toString();
    }

    
    @Override
    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 

   
}
