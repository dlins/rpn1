package rpnumerics;

import wave.util.RealSegment;
import java.util.List;
import wave.util.Boundary;
import wave.util.IsoTriang2DBoundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class PhysicalBoundary extends SegmentedCurve {

    public PhysicalBoundary(List<RealSegment> hSegments) {

        super(hSegments);
        System.out.println("Tamanho da lista de segmentos : " + hSegments.size());



    }

    public int edgeSelection(RealVector point) {
        Boundary boundary = RPNUMERICS.boundary();

        // THREE_PHASE_BOUNDARY_SW_ZERO 0
       // THREE_PHASE_BOUNDARY_SO_ZERO 1
       // THREE_PHASE_BOUNDARY_SG_ZERO 2
        
        if (RPNUMERICS.boundary() instanceof IsoTriang2DBoundary) {

            if (point.getElement(0) == boundary.getMinimums().getElement(0)) {
                return 1;
            }
            if (point.getElement(1) == boundary.getMinimums().getElement(1)) {
                return 0;
            }
            return 2;
        }

        if (RPNUMERICS.boundary() instanceof RectBoundary) {
        }

        return 20;


    }

    public String toXML() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < segments().size(); i++) {
            RealSegment hSegment = ((RealSegment) segments().get(
                    i));
            buffer.append(hSegment.toXML());

        }
        return buffer.toString();

    }
}
