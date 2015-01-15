/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealVector;

public class Diagram implements RpSolution {
    //
    // Members
    //

    private String info_;

    private final List<DiagramLine> lines_;
    //
    // Constructor
    //

    public Diagram(List<DiagramLine> coords) {
        lines_ = coords;
    }

    public DiagramLine getLine(int lineIndex) {
        return lines_.get(lineIndex);
    }

    public List<DiagramLine> getLines() {
        return lines_;
    }

    public String getInfo() {
        return info_;
    }

    public void setInfo(String info) {

        String cleanString = info.replaceAll("\\(|\\)|,", ""); //Removing () and , from C++ RealVector if needed.        

        info_ = cleanString;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < lines_.size(); i++) {

            System.out.println("Linha: " + i);

            DiagramLine diagramLine = lines_.get(i);

            builder.append(diagramLine.toString());

        }

        return builder.toString();

    }

    @Override
    public String toXML() {

        return toString();
    }

    @Override
    public String toMatlab() {

        StringBuilder output = new StringBuilder();
        
        for (int i = 0; i < lines_.size(); i++) {
            DiagramLine line = lines_.get(i);
            output.append(line.getSize()).append(" ").append("2").append("\n");
            List<List<RealVector>> parts = line.getCoords();
            
            for (List<RealVector> part : parts) {
                
                for (RealVector coord : part) {
                    
                    output.append(coord).append("\n");
                    
                }
                
            }
        }
        return output.toString();
       
    }
        
}
