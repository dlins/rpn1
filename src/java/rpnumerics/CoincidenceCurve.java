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

public class CoincidenceCurve extends SegmentedCurve{// RPnCurve implements RpSolution {
    //
    // Members
    //

//    private List hugoniotSegments_;

    public CoincidenceCurve(List<HugoniotSegment> hSegments) {
        super(hSegments);//, new ViewingAttr(Color.RED));
//        hugoniotSegments_ = hSegments;

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
//    //
//    public List segments() {
//        return hugoniotSegments_;
//    }

//    public String toMatlabPlot(int x, int y) {
//        StringBuffer buffer = new StringBuffer();
//
//        for (int i = 0; i < hugoniotSegments_.size(); i++) {
//
//            HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
//                    i));
//
//            int type = hSegment.getType() + 1;
//            buffer.append("% type of segment: " + type + "\n");
//
//            buffer.append("plot([data");
//            buffer.append(i);
//            buffer.append("(" + (x + 1) + ") ");
//            buffer.append("data");
//            buffer.append(i);
//            buffer.append("(" + (x + 4) + ")],");
//
//            buffer.append("[data");
//            buffer.append(i);
//            buffer.append("(" + (y + 1) + ") ");
//            buffer.append("data");
//            buffer.append(i);
//            buffer.append("(" + (y + 4) + ")]");
//
////            buffer.append("plot (data");
////            buffer.append( i );
////            buffer.append("(:,");
////            buffer.append(x+1 );
////            buffer.append("), data");
////            buffer.append( i);
////            buffer.append("(:, ");
////            buffer.append( y+1 );
//            buffer.append(", \'Color\', [toc(");
//            buffer.append(type);
//            buffer.append(", 1) toc(");
//            buffer.append(type);
//            buffer.append(", 2) toc(");
//            buffer.append(type);
//            buffer.append(", 3)])\n");
//            if (i < hugoniotSegments_.size() - 1) {
//                buffer.append("hold on\n\n");
//            }
//        }
//        return buffer.toString();
//    }
//
//    public String toMatlabData() {
//
//        StringBuffer buffer = new StringBuffer();
//        for (int i = 0; i < hugoniotSegments_.size(); i++) {
//
//
//            HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
//                    i));
//
//
//            RealVector leftPoint = hSegment.leftPoint();
//            RealVector rightPoint = hSegment.rightPoint();
////            RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
////                    hSegment.rightPoint());
//
//            buffer.append("% type of segment: " + hSegment.getType() + "\n");
//
//            buffer.append("data" + i + "= [" + leftPoint + " " + rightPoint + "];\n\n");
//        }
//        return buffer.toString();
//    }
//
//    public String toXML(boolean calcReady) {
//        StringBuffer buffer = new StringBuffer();
//        if (calcReady) {
//
//            buffer.append("<HUGONIOTCURVE>\n");
//
//            for (int i = 0; i < hugoniotSegments_.size(); i++) {
//
//                HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
//                        i));
//                RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
//                        hSegment.rightPoint());
//                buffer.append(rSegment.toXML());
//
//            }
//            buffer.append("</HUGONIOTCURVE>\n");
//
//
//        }
//
//        return buffer.toString();
//
//    }
}
