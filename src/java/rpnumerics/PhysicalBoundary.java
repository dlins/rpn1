package rpnumerics;

import wave.util.RealSegment;
import java.util.List;

public class PhysicalBoundary extends SegmentedCurve {
  

    public PhysicalBoundary(List<RealSegment> hSegments) {

        super(hSegments);
        System.out.println("Tamanho da lista de segmentos : "+hSegments.size());

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
