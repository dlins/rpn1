/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

public class AbstractSegmentAtt implements Cloneable {
    //
    // Constants
    //
    public static final boolean DEFAULT_VISIBILITY = true;
    //
    // Members
    //
    private int type_;
    private boolean visible_ = DEFAULT_VISIBILITY;

    //
    // Constructors
    //
    public AbstractSegmentAtt(int type) {
        type_ = type;
    }

    public AbstractSegmentAtt(int type, boolean isVisible) {
        this(type);
        setVisible(isVisible);
    }

    public AbstractSegmentAtt(AbstractSegmentAtt copy) {
        this(copy.getType(), copy.isVisible());
    }

    //
    // Accessors/Mutators
    //
    public int getType() { return type_; }

    public void setType(int type) { type_ = type; }

    public boolean isVisible() { return visible_; }

    public void setVisible(boolean visible) { visible_ = visible; }

    //
    // Methods
    //
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new Error("ASSERTION FAILED");
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Segment Type : " + getType() + '\n');
        buf.append("Segment Visibility : " + isVisible());
        return buf.toString();
    }

    public boolean equals(Object compare) {
        if ((compare instanceof AbstractSegmentAtt) && (((AbstractSegmentAtt)compare).getType() == getType()) &&
            (((AbstractSegmentAtt)compare).isVisible() == isVisible()))
                return true;
        return false;
    }
}
