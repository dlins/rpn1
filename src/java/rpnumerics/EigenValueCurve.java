package rpnumerics;

import wave.util.RealSegment;
import java.util.List;

public class EigenValueCurve extends CharacteristicsPolynomialCurve {
    //
    // Members
    //

    private final int family_;

    public EigenValueCurve(int family, List<RealSegment> segments, double level) {
        super(segments, level);

        family_ = family;
    }

    public int getFamily() {
        return family_;
    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        for (Object object : segments()) {

            RealSegment segment = (RealSegment) object;

            buffer.append(segment.toXML());

        }

        return buffer.toString();

    }

}
