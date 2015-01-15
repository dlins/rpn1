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

public class CoincidenceCurve extends BifurcationCurve{// RPnCurve implements RpSolution {
    //
    // Members
    //

//    private List hugoniotSegments_;

    public CoincidenceCurve(List<RealSegment> hSegments) {
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

    public String toMatlab() {

        StringBuffer buffer = new StringBuffer();    
        buffer.append("%%\nclose all;clear all;\n");
        //buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
        buffer.append(toMatlabData(0));

        buffer.append("%%\n% begin plot x y\n");
        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(createMatlabPlotLoop(0,1,0));

        return buffer.toString();

    }

}
