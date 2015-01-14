package rpnumerics;

import wave.util.RealSegment;
import java.util.List;

public class EllipticBoundaryExtension extends BifurcationCurve {
  

    public EllipticBoundaryExtension(List<RealSegment> curveSegments,List<RealSegment> domainSegments) {
        super(curveSegments,domainSegments);

    }

     public EllipticBoundaryExtension(List<RealSegment> hSegments) {
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
