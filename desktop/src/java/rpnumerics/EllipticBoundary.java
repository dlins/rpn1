package rpnumerics;

import wave.util.RealSegment;
import java.util.List;

public class EllipticBoundary extends SegmentedCurve {
  

    public EllipticBoundary(List<RealSegment> hSegments) {
        super(hSegments);

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
