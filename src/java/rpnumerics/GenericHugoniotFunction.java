package rpnumerics;

import wave.util.*;

public class GenericHugoniotFunction implements VectorFunction {
    //
    // Constructors
    //
    private HugoniotParams hugoniotParams_;
    private FluxFunction fluxFunction_;

    public GenericHugoniotFunction(HugoniotParams hparams) {
        hugoniotParams_ = hparams;
        fluxFunction_ = hparams.getFluxFunction();

    }

    //
    // Methods
    //
    public RealVector value(RealVector U) {

        WaveState input = new WaveState(new PhasePoint(U));
        JetMatrix output = new JetMatrix(U.getSize());
        fluxFunction_.jet(input, output, 1);

//        RealVector deltaF = fluxFunction_.F(U);//RPNUMERICS.fluxFunction().F(U);

        RealVector deltaF = output.f();
        deltaF.sub(getHugoniotParams().getFMinus());//RPNUMERICS.hugoniotCurveCalc().getFMinus());
        RealVector deltaU = new RealVector(U);
        deltaU.sub(getHugoniotParams().getUMinus());//RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords());
        int dim = U.getSize();
        RealVector result = new RealVector(dim - 1);
        for (int i = 0; i < dim - 1; i++) {
            result.setElement(i, deltaF.getElement(i) * deltaU.getElement(dim - 1) - deltaF.getElement(dim - 1) *
                    deltaU.getElement(i));
        }

        return result;
    }

    public RealMatrix2 deriv(RealVector U) {

        WaveState input = new WaveState(new PhasePoint(U));
        JetMatrix output = new JetMatrix(U.getSize());
        fluxFunction_.jet(input, output, 1);

        int dim = U.getSize();
        RealVector deltaU = new RealVector(U);

        deltaU.sub(getHugoniotParams().getUMinus());//RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords());
//        RealVector deltaF = fluxFunction_.F(U);
        RealVector deltaF = output.f();//RPNUMERICS.fluxFunction().F(U);

        deltaF.sub(getHugoniotParams().getFMinus());//RPNUMERICS.hugoniotCurveCalc().getFMinus());
//        RealMatrix2 DF = RPNUMERICS.fluxFunction().DF(U);

//        RealMatrix2 DF = fluxFunction_.DF(U);

        RealMatrix2 DF = new RealMatrix2(dim, dim); //TODO Replace for JacobianMatrix

        for (int i = 0; i < output.n_comps(); i++) {
            for (int j = 0; j < output.n_comps(); j++) {
                DF.setElement(i, j, output.getElement(i, j));
            }
        }



        RealMatrix2 DU = new RealMatrix2(dim, dim);
        RealMatrix2 result = new RealMatrix2(dim - 1, dim);
        for (int i = 0; i < dim - 1; i++) {
            for (int j = 0; j < dim; j++) {
                result.setElement(i, j, DF.getElement(i, j) * deltaU.getElement(dim - 1) + deltaF.getElement(i) *
                        DU.getElement(dim - 1, j) - DF.getElement(dim - 1, j) * deltaU.getElement(i) - deltaF.getElement(dim - 1) *
                        DU.getElement(i, j));
            }
        }

        return result;
    }

    public HugoniotParams getHugoniotParams() {
        return hugoniotParams_;
    }

    public void setHugoniotParams(HugoniotParams hugoniotParams_) {
        this.hugoniotParams_ = hugoniotParams_;
    }
}
