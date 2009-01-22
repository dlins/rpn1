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
    public BlowUpFlow(BlowUpLineFieldVector xZero, FluxFunction flux) {

        super(new PhasePoint(xZero.getFamilyVector()), xZero.getFamilyIndex(), flux);
        blowUpXZero_ = xZero;

    }


    //
    // public BlowUpLineFieldVector getXZero() { return blowUpXZero_; }

    //
    // Methods
    //
    public WavePoint flux(RealVector input) {

        /**
         * x = blowuplinefield is a 2n + 2 dim vector... (u,lambda,r,ll)
         */
        BlowUpLineFieldVector x = new BlowUpLineFieldVector(input, 1, getFlux());

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

        WaveState in = new WaveState(new PhasePoint(x));
        JetMatrix out = new JetMatrix(x.getSize());
        getFlux().jet(in, out, 2);

        RealMatrix2 df = new RealMatrix2(out.n_comps(), out.n_comps()); //TODO Replace for JacobianMatrix

        HessianMatrix d2f = new HessianMatrix(out.n_comps());

        for (int i = 0; i < out.n_comps(); i++) {
            for (int j = 0; j < out.n_comps(); j++) {
                for (int k = 0; k < out.n_comps(); k++)  {
                    d2f.setElement(i, j, k, out.getElement(i, j, k));
                }
            }

        }

        for (int i = 0; i < out.n_comps(); i++) {
            for (int j = 0; j < out.n_comps(); j++) {
                df.setElement(i, j, out.getElement(i, j));
            }
        }




//        RealMatrix2 df = getFlux().DF(u);
//        HessianMatrix d2f = getFlux().D2F(u);// RPNUMERICS.fluxFunction().D2F(u);

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

        WavePoint returned = new WavePoint(resultVec, lambda);

        return returned;

//        return resultVec;

    }

    @Override
    public JacobianMatrix fluxDeriv(RealVector u) {

        /** @todo  not implemented yet...*/
        int stateSpaceDim = u.getSize();
        return new JacobianMatrix(stateSpaceDim);
    }

    @Override
    public HessianMatrix fluxDeriv2(RealVector u) {
        /** @todo  not implemented yet...*/
        int stateSpaceDim = u.getSize();
        return new HessianMatrix(stateSpaceDim);
    }


    // Accessors/Mutators
    @Override
    public PhasePoint getXZero() {
        return new PhasePoint(blowUpXZero_.getFamilyVector());
    }

    public void setXZero(PhasePoint xzero) {

        blowUpXZero_ = new BlowUpLineFieldVector(xzero.getCoords(), 1, getFlux());

    }

    public String getName() {
        return "Blow Up";
    }
}
