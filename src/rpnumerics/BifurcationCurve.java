/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;


//import rpnumerics.methods.contour.ContourCurve;

public class BifurcationCurve extends RPnCurve implements RpSolution {
    //
    // Members
    //
   
    private int familyIndex_;
//    private ContourCurve curve_;

    //
    // Constructor
    //
//    public BifurcationCurve(ContourCurve curve,  int familyIndex) {
//        super(curve, new ViewingAttr(Color.white));
//
//        familyIndex_ = familyIndex;
//       
//    }

   

    public int findClosestSegment(wave.util.RealVector coords, double alpha) {
        return 0;
    }


    //
    // Accessors/Mutators
    //
   
  
    public int getFamilyIndex() {
        return familyIndex_;
    }

//    public ContourCurve getCurve() {
//        return curve_;
//    }

    //
    // Methods
    //
    // there is a possibility that the concatenation of
    // Orbits not exist...
   

//    public String toString() {
//        StringBuffer buf = new StringBuffer();
//        buf.append("\n points = ");
//        for (int i = 0; i < points_.length; i++) {
//            buf.append("[" + i + "] = " + points_[i] + "  ");
//            buf.append("\n");
//        }
//        return buf.toString();
//    }

//    public String toXML() {
//
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("<ORBIT flag=\"" + getIntegrationFlag() + "\">\n");
//        for (int i = 0; i < points_.length; i++) {
//
//            buffer.append("<ORBITPOINT time=\"" +
//                          ((OrbitPoint) points_[i]).getTime() + "\">");
//            buffer.append(points_[i].toXML());
//            buffer.append("</ORBITPOINT>\n");
//
//        }
//        buffer.append("</ORBIT>\n");
//        return buffer.toString();
//
//    }

//    public String toXML(boolean calcReady) {
//        StringBuffer buffer = new StringBuffer();
//        if (calcReady) {
//
//            buffer.append("<ORBIT flag=\"" + getIntegrationFlag() + "\">\n");
//            for (int i = 0; i < points_.length; i++) {
//
//                buffer.append("<ORBITPOINT time=\"" +
//                              ((OrbitPoint) points_[i]).getTime() + "\">");
//                buffer.append(points_[i].toXML());
//                buffer.append("</ORBITPOINT>\n");
//
//            }
//            buffer.append("</ORBIT>\n");
//        } else {}
//        return buffer.toString();
//
//    }
}
