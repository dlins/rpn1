/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class TransitionalLine extends Orbit {

    //
    // Constructor
    //
    
    private String name_;
    public TransitionalLine(OrbitPoint[] points, String name) {

        super(points, BOTH_DIR);
        
        name_=name;

    }

    public String getName() {
        return name_;
    }
    
    
    

}
