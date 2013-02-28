/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

import java.util.ArrayList;

public class BooleanMultiArray {

    //
    // Members
    //
    private int[] dim_;
    private int  boolDim_;
    private boolean[] array_;
    //
    // Constructors
    //
    public BooleanMultiArray(int[] dim, int boolDim) {

            dim_ = new int[dim.length];
            boolDim_ = boolDim;
            int volumeSize = 1;
            for (int i=0;i < dim.length;i++) {
                dim_[i] = dim[i];
                volumeSize*=dim[i];
            }
            volumeSize*=boolDim_;

        array_ = new boolean[volumeSize];

    }

    //
    // Methods
    //
    public boolean[] get(int[] indx) {

        int volumeIndx = 0;
        int volumeSubSize = 1;

        boolean[] result = new boolean[boolDim_];

        for (int i=0;i < dim_.length;i++) {

            volumeIndx += indx[i]*volumeSubSize;
            volumeSubSize *= dim_[i];
        }

        for (int i = 0;i < boolDim_;i++)
            result[i] = array_[volumeIndx*boolDim_+i];

        return result;
    }


     public void set(int[] indx, boolean[] value) {

        int volumeIndx = 0;
        int volumeSubSize = 1;

        for (int i=0;i < dim_.length;i++) {

            volumeIndx += indx[i]*volumeSubSize;
            volumeSubSize *= dim_[i];
        }

        for (int i = 0;i < boolDim_;i++)
            array_[volumeIndx*boolDim_+i] = value[i];
    }

    public int[] volumeDim() {return dim_;}
}