/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class PhasePoint extends RealVector {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors
    //
    public PhasePoint(PhasePoint copy) {
        this(copy.getCoords());
    }

    public PhasePoint(RealVector pCoords) {
        super(pCoords);

    }

    //
    // Accessors/Mutators
    //
    public final RealVector getCoords() {
        return this;
    }

    //
    // Methods
    //
    @Override
    public String toString() {

        StringBuffer str = new StringBuffer();
        
        for (int i=0;i < getCoords().getSize();i++){
            
            str.append(getCoords().getElement(i));
            str.append(" ");
            
        }
        return str.toString();



    }

    @Override
    public String toXML() {
        StringBuffer str = new StringBuffer();
        str.append("<PHASEPOINT dimension=\"" + getCoords().getSize() + "\">");
        str.append(getCoords().toString());
        str.append("</PHASEPOINT>" + "\n");

        return str.toString();
    }
}
