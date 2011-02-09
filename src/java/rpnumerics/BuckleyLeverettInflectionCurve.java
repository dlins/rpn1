/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealSegment;
import java.util.List;
import wave.multid.CoordsArray;
import java.util.ArrayList;

public class BuckleyLeverettInflectionCurve extends SegmentedCurve {
    //
    // Members
    //

    private List hugoniotSegments_;

    public BuckleyLeverettInflectionCurve(List<HugoniotSegment> hSegments) {
        super(hSegments);
        hugoniotSegments_=hSegments;


    }

    private static CoordsArray[] coordsArrayFromRealSegments(List segments) {

        ArrayList tempCoords = new ArrayList(segments.size());
        for (int i = 0; i < segments.size(); i++) {
            RealSegment segment = (RealSegment) segments.get(i);
            tempCoords.add(new CoordsArray(segment.p1()));
            tempCoords.add(new CoordsArray(segment.p2()));

        }

        CoordsArray[] coords = new CoordsArray[tempCoords.size()];
        for (int i = 0; i < tempCoords.size(); i++) {
            coords[i] = (CoordsArray) tempCoords.get(i);
        }
        tempCoords = null;
        return coords;

    }

    //
    // Accessors/Mutators
    //
    public List segments() {
        return hugoniotSegments_;
    }

    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();
        if (calcReady) {

            buffer.append("<HUGONIOTCURVE>\n");

            for (int i = 0; i < hugoniotSegments_.size(); i++) {

                HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
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
