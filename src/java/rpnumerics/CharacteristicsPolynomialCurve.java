package rpnumerics;

import wave.util.RealSegment;

import java.util.List;

public class CharacteristicsPolynomialCurve extends SegmentedCurve {
    //
    // Members
    //

    private final double level_;
    
    public CharacteristicsPolynomialCurve(List<RealSegment> segments, double level) {
        super(segments);

        level_ = level;

    }


    public double getLevel() {
        return level_;
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
