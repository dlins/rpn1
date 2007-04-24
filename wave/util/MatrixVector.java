/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

import java.util.ArrayList;

public class MatrixVector {
    //
    // Constants
    //
    //
    // Members
    //
    private ArrayList vector_;

    //
    // Constructors
    //
    public MatrixVector() {
        vector_ = new ArrayList();
    }

    public MatrixVector(MatrixVector mArray) {
        this();
        for (int i = 0; i < mArray.size(); i++)
            insert(mArray.get(i));
    }

    //
    // Accessors/Mutators
    //
    public int size() { return vector_.size(); }

    //
    // Methods
    //
    public void scale(double s) {
        for (int i = 0; i < vector_.size(); i++)
            get(i).scale(s);
    }

    public void add(MatrixVector mArray) {
        // TODO
    }

    public void sub(MatrixVector mArray) {
        // TODO
    }

    public void insert(RealMatrix2 m) {
        vector_.add(m);
    }

    public RealMatrix2 get(int index) {
        return (RealMatrix2)vector_.get(index);
    }
}
