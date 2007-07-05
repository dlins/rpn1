/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

public class RealSegment {
    //
    // Members
    //
    private RealVector p1_;
    private RealVector p2_;

    //
    // Constructors
    //
    public RealSegment(RealVector p1, RealVector p2) {
        p1_ = new RealVector(p1);
        p2_ = new RealVector(p2);
    }



    public String toXML(){


      StringBuffer buffer = new StringBuffer();

      buffer.append("<REALSEGMENT>"+"\n");
      buffer.append("<PHASEPOINT dimension=\"" + p1_.getSize() + "\">"+p1_.toString()+"</PHASEPOINT>\n");
      buffer.append("<PHASEPOINT dimension=\"" + p2_.getSize() + "\">"+p2_.toString()+"</PHASEPOINT>\n");
      buffer.append("</REALSEGMENT>"+"\n");

      return buffer.toString();

    }


    //
    // Accessors/Mutators
    //
    public RealVector p1() { return p1_; }

    public RealVector p2() { return p2_; }






}
