/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.*;

public class BlowUpFlow extends RarefactionFlow {

    public static int FIRST = 1;

    //
    // Members
    //
    private BlowUpLineFieldVector blowUpXZero_;

    //
    // Constructors
    //
    public BlowUpFlow(BlowUpLineFieldVector xZero) {

        super(new PhasePoint(xZero.getFamilyVector()), xZero.getFamilyIndex());
        blowUpXZero_ = xZero;
    }




    //
    // public BlowUpLineFieldVector getXZero() { return blowUpXZero_; }

    //
    // Methods
    //
    public RealVector flux(RealVector input) {


        /**
         * x = blowuplinefield is a 2n + 2 dim vector... (u,lambda,r,ll)
         */

        BlowUpLineFieldVector x = new BlowUpLineFieldVector(input,1);

        int blowUpDim = x.getSize();
        int stateSpaceDim = (blowUpDim - 2) / 2;

        RealVector u = new RealVector(stateSpaceDim);
        RealVector rVec = new RealVector(stateSpaceDim);

        for (int i = 0; i < stateSpaceDim; i++) {
            u.setElement(i, x.getElement(i));
            rVec.setElement(i, x.getElement(i + stateSpaceDim + 1));
        }

        double lambda = x.getElement(stateSpaceDim);
        double ll = x.getElement(2 * stateSpaceDim + 1);

        /*
         * we now build the blow up matrix
         */
        RealMatrix2 df = RPNUMERICS.fluxFunction().DF(u);
        HessianMatrix d2f = RPNUMERICS.fluxFunction().D2F(u);

        BlowUpLineFieldMatrix B = new BlowUpLineFieldMatrix(stateSpaceDim, df,
                d2f, lambda, rVec);


        /*
         * we will now obtain the vector field
         * doing Vi = (-1)**i detBi
         * where Bi is a shift through B columns (2n + 1) x (2n + 1)
         */
//        RealVector resultVec = new RealVector(blowUpDim);

        RealVector resultVec = new RealVector(stateSpaceDim);

        RealMatrix2 detBMatrix = new RealMatrix2(blowUpDim - 1, blowUpDim - 1);

        // initializes detBMatrix removing the first column
        double[] swapColumn = new double[detBMatrix.getNumRow()];
        for (int i = 1; i < blowUpDim; i++) {
            B.getColumn(i, swapColumn);
            detBMatrix.setColumn(i - 1, swapColumn);
        }

        int sign = 1;
        for (int i = 0; i < stateSpaceDim; i++) {
            resultVec.setElement(i, sign * detBMatrix.determinant());

            // if not the last loop
            if (i != resultVec.getSize() - 1) {
                sign = -sign;
                B.getColumn(i, swapColumn);
                detBMatrix.setColumn(i, swapColumn);
            }
        }

        return resultVec;

    }

    public RealMatrix2 fluxDeriv(RealVector u) {

        /** @todo  not implemented yet...*/
        int stateSpaceDim = u.getSize();
        return new RealMatrix2(stateSpaceDim, stateSpaceDim);
    }

    public HessianMatrix fluxDeriv2(RealVector u) {
        /** @todo  not implemented yet...*/
        int stateSpaceDim = u.getSize();
        return new HessianMatrix(stateSpaceDim);
    }


       // Accessors/Mutators


       public PhasePoint getXZero() {
           return new PhasePoint(blowUpXZero_.getFamilyVector());
       }

       public void setXZero(PhasePoint xzero) {

           blowUpXZero_ = new BlowUpLineFieldVector(xzero.getCoords(), 1);

    }



}
