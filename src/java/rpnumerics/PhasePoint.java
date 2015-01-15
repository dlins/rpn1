/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class PhasePoint extends RealVector implements RpSolution {
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
    public String toXML() {
        StringBuffer str = new StringBuffer();
        str.append("<PHASEPOINT dimension=\"" + getCoords().getSize() + "\">");
        str.append(getCoords().toString());
        str.append("</PHASEPOINT>" + "\n");

        return str.toString();
    }

    public String toMatlab() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
