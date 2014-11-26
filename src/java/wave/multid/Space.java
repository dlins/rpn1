/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid;

public class Space implements Cloneable {
    //
    // Members
    //
    private String name_;
    private int dim_;

    //
    // Constructors
    //
    public Space(String name, int dim) {
        name_ = name.toString();
        dim_ = dim;
    }

    //
    // Accessors/Mutators
    //
    public String getName() { return name_; }

    public void setName(String name) { name_ = name.toString(); }

    public int getDim() { return dim_; }

    public void setDim(int dim) { dim_ = dim; }

    //
    // Methods
    //
    public boolean equals(Space space) {
        return getDim() == space.getDim();
    }

    public int hashCode() {
        return getDim() + getName().hashCode();
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new Error("ASSERTION FAILED");
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Space Name : " + getName() + '\n');
        buf.append("Space Dimension : " + getDim());
        return buf.toString();
    }
}
