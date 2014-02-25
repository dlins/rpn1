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
    
    
    
    @Override
    public String toXML(){
        
        StringBuilder buffer = new StringBuilder();
        
        
        for (Object object : segments()) {
            
            
            RealSegment segment = (RealSegment)object;
            
            buffer.append(segment.toXML());
            
        }
        
        return buffer.toString();
        
        
    }
    

}
