/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealSegment;
import java.util.List;

public class SubInflectionCurve extends BifurcationCurve {
    //
    // Members
    //


    public SubInflectionCurve(List<HugoniotSegment> hSegments) {
        super(hSegments);
      }

    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();
        if (calcReady) {

            buffer.append("<HUGONIOTCURVE>\n");

            for (int i = 0; i < segments().size(); i++) {

                HugoniotSegment hSegment = ((HugoniotSegment) segments().get(
                        i));
                RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
                        hSegment.rightPoint());
                buffer.append(rSegment.toXML());

            }
            buffer.append("</HUGONIOTCURVE>\n");

        }
        return buffer.toString();
    }
}
