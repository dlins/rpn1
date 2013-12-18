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

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();
        buffer.append(p1_.toString()).append(" ").append(p2_.toString());
        return buffer.toString();



    }

    public String toXML() {


        StringBuilder buffer = new StringBuilder();
        buffer.append("<REALSEG coords_1=\"").append(p1_.toString()).
                append("\"" + ' ' + "coords_2=\"").append(p2_.toString()).
                append("\"" + "/>\n");

        return buffer.toString();

    }


    //
    // Accessors/Mutators
    //
    public RealVector p1() {
        return p1_;
    }

    public RealVector p2() {
        return p2_;
    }
}
