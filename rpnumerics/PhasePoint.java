/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */



package rpnumerics;

import wave.util.RealVector;
import wave.util.RealVector;

public class PhasePoint extends RealVector {
    //
    // Constants
    //
    //
    // Members
    //
//    private RealVector pCoords_;

    //
    // Constructors
    //
    public PhasePoint(PhasePoint copy) {
        this(copy.getCoords());
    }

    public PhasePoint(RealVector pCoords) {
      super(pCoords);
//        pCoords_ = new RealVector(pCoords);
    }

    //
    // Accessors/Mutators
    //
    public final RealVector getCoords() { return
          this;
//          pCoords_;
    }

    //
    // Methods
    //



    public String toString() {

      return      super.toString();
    }

    public String toXML() {
      StringBuffer str = new StringBuffer();
      //        str.append("<PHASEPOINT coords=\"" + getCoords().toString() + "\"></PHASEPOINT>");
      str.append("<PHASEPOINT dimension=\"" + getCoords().getSize() + "\">");
      str.append(getCoords().toString());
      str.append("</PHASEPOINT>"+"\n");

        return str.toString();
    }
}
