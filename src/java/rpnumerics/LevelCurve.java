package rpnumerics;

import wave.util.RealSegment;
import java.util.List;

public class LevelCurve extends SegmentedCurve {
    //
    // Members
    //

    private double level_;
    private int family_;

    public LevelCurve(int family, List<RealSegment> segments, double level) {
        super(segments);

        level_ = level;
        family_ = family;
    }

    public int getFamily() {
        return family_;
    }

    public double getLevel() {
        return level_;
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
