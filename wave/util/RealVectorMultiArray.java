/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

import java.util.ArrayList;

public class RealVectorMultiArray {

    //
    // Members
    //
    private int[] dim_;
    private ArrayList array_;
    //
    // Constructors
    //
    public RealVectorMultiArray(int[] dim) {

        dim_ = new int[dim.length];
		int volumeSize = 1;
		for (int i=0;i < dim.length;i++) {
            dim_[i] = dim[i];
            volumeSize*=dim[i];
        }

        array_ = new ArrayList(volumeSize);

    }

    //
    // Methods
    //
    public RealVector get(int[] indx) {

        int volumeIndx = 0;
        int volumeSubSize = 1;

        for (int i=0;i < dim_.length;i++) {

            volumeIndx += indx[i]*volumeSubSize;
            volumeSubSize *= dim_[i];
        }

        return ((RealVector)array_.get(volumeIndx));
    }

    public void set(int[]indx, RealVector value) {

        int volumeIndx = 0;
        int volumeSubSize = 1;

        for (int i=0;i < dim_.length;i++) {

            volumeIndx += indx[i]*volumeSubSize;
            volumeSubSize *= dim_[i];
        }

        array_.set(volumeIndx,new RealVector(value));
    }

    public int[] volumeDim() {return dim_;}
}