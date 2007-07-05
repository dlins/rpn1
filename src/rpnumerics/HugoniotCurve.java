/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import rpnumerics.facade.*;
import wave.util.RealVector;
import wave.util.RealSegment;
import java.util.List;
import wave.multid.CoordsArray;
import java.util.ArrayList;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;
import java.awt.Color;

public class HugoniotCurve implements RpSolution {
    //
    // Members
    //

    private PhasePoint xZero_;

    private List hugoniotSegments_;
    
    public HugoniotCurve (List hugoniotSegments){ hugoniotSegments_=hugoniotSegments;}
    
    public native double findSigma (PhasePoint pPoint);


    //
    // Accessors/Mutators
    //
    public List segments() {
        return hugoniotSegments_;
    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTCURVE xzero=\"" + xZero_.getCoords().toString() +
                      "\"" + ">" + "\n");
        for (int i = 0; i < hugoniotSegments_.size(); i++) {

            HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
                    i));

            RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
                    hSegment.rightPoint());
            buffer.append(rSegment.toXML());

        }

        buffer.append("</HUGONIOTCURVE>\n");

        return buffer.toString();

    }

    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();
        if (calcReady) {

            buffer.append("<HUGONIOTCURVE xzero=\"" +
                          xZero_.getCoords().toString() +
                          "\" >" + "\n");
            for (int i = 0; i < hugoniotSegments_.size(); i++) {

                HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.
                                            get(
                        i));
                RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
                        hSegment.rightPoint());
                buffer.append(rSegment.toXML());

            }

            buffer.append("</HUGONIOTCURVE>\n");
        } else {}

        return buffer.toString();

    }
}
