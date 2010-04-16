/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

public class RarefactionOrbit extends Orbit implements RpSolution {
  
    

    //
    // Constructor
    //
    
    public RarefactionOrbit(OrbitPoint[] points, int flag) {
        super(points, flag);
   
    }
    

    // Methods
    
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n points = ");
        for (int i = 0; i < getPoints().length; i++) {
            buf.append("[" + i + "] = " + getPoints()[i] + "  ");
            buf.append("\n");
        }
        return buf.toString();
    }
    
    @Override
    public String toXML() {
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("<ORBIT flag=\"" + getIntegrationFlag() + "\">\n");
        for (int i = 0; i < getPoints().length; i++) {
            
            buffer.append("<ORBITPOINT time=\"" +
                    ((OrbitPoint) getPoints()[i]).getTime() + "\">");
            buffer.append(getPoints()[i].toXML());
            buffer.append("</ORBITPOINT>\n");
            
        }
        buffer.append("</ORBIT>\n");
        return buffer.toString();
        
    }
    
    @Override
    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();
        if (calcReady) {
            
            buffer.append("<ORBIT flag=\"" + getIntegrationFlag() + "\">\n");
            for (int i = 0; i < getPoints().length; i++) {
                
                buffer.append("<ORBITPOINT time=\"" +
                        ((OrbitPoint) getPoints()[i]).getTime() + "\">");
                buffer.append(getPoints()[i].toXML());
                buffer.append("</ORBITPOINT>\n");
                
            }
            buffer.append("</ORBIT>\n");
        } else {}
        return buffer.toString();
        
    }
}
