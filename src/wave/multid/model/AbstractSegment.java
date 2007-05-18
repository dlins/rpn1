/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.DimMismatchEx;
import wave.multid.map.Map;
import java.io.FileWriter;
import java.io.FileReader;

public class AbstractSegment implements AbstractGeomObj {
    //
    // Constants
    //
    public static final int SEG_MOVETO = 77;
    public static final int SEG_LINETO = 93;
    public static final int SEG_QUADTO = 11;
    public static final int SEG_CUBICTO = 175;
    // future use...
    public static final int SEG_CLOSE = 4;
//    public static final int MAX_DEF_POINTS = 4;

    public static final int MAX_DEF_POINTS = 2;
    public static final int QUAD_DEF_POINTS = 3;
    public static final int CUBIC_DEF_POINTS = 4;
    //
    // Members
    //
    private AbstractSegmentAtt attributes_;
    private CoordsArray[] definitionPoints_; // this can be > 2
    private BoundingBox boundary_;
    private Space space_;

    //
    // Constructors
    //
    public AbstractSegment(CoordsArray[] points, AbstractSegmentAtt att) throws WrongNumberOfDefPointsEx {
        // we assume coords 0 for dummy definition points
        if (points.length != MAX_DEF_POINTS)
            throw new WrongNumberOfDefPointsEx();
        // necessary to avoid array resize exception...
        definitionPoints_ = new CoordsArray[MAX_DEF_POINTS];
        for (int i = 0; i < MAX_DEF_POINTS; i++)
            definitionPoints_[i] = new CoordsArray(points[i]);
        attributes_ = new AbstractSegmentAtt(att);
        space_ = definitionPoints_[0].getSpace();
        try {
            // TODO create boundary for the segment...
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
    }

    //
    // Accessors/Mutators
    //
    public Space getSpace() { return space_; }

    public BoundingBox getBoundary() { return boundary_; }

    public CoordsArray[] getDefinitionPoints() {
        return definitionPoints_;
    }

    public AbstractSegmentAtt getAttributes() {
        return attributes_;
    }

    //
    // Methods
    //
    public boolean equals(Object obj) {
        if ((obj instanceof AbstractSegment) && ((AbstractSegment)obj).getAttributes().equals(getAttributes()) &&
            (((AbstractSegment)obj).getDefinitionPoints().length == getDefinitionPoints().length)) {
                for (int i = 0; i < getDefinitionPoints().length; i++)
                    if (!((AbstractSegment)obj).getDefinitionPoints() [i].equals(getDefinitionPoints() [i]))
                        return false;
                return true;
        }
        return false;
    }

    public void print(FileWriter cout) { }

    public void load(FileReader cin) { }

    public void applyMap(Map map) { }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Segment Attributes : " + attributes_ + '\n');
        buf.append("Segment Coordinates : " + '\n');
        for (int i = 0; i < definitionPoints_.length; i++)
            buf.append(definitionPoints_[i]);
        return buf.toString();
    }
}
