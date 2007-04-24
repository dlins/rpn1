package rpnumerics;

import wave.util.*;
import rpnumerics.physics.FluxFunction;



public class GenericHugoniotFunction implements VectorFunction {
    //
    // Constructors
    //
    public GenericHugoniotFunction(FluxFunction fluxFunction, HugoniotParams hparams) {

    }

    //
    // Methods
    //
    public RealVector value(RealVector U) {

        RealVector deltaF = RPNUMERICS.fluxFunction().F(U);
        deltaF.sub(RPNUMERICS.hugoniotCurveCalc().getFMinus());
        RealVector deltaU = new RealVector(U);
        deltaU.sub(RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords());
        int dim = U.getSize();
        RealVector result = new RealVector(dim - 1);
        for (int i = 0; i < dim - 1; i++)
            result.setElement(i, deltaF.getElement(i) * deltaU.getElement(dim - 1) - deltaF.getElement(dim - 1) *
                deltaU.getElement(i));

        return result;
    }

    public RealMatrix2 deriv(RealVector U) {

        int dim = U.getSize();
        RealVector deltaU = new RealVector(U);

        deltaU.sub(RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords());
        RealVector deltaF = RPNUMERICS.fluxFunction().F(U);
        deltaF.sub(RPNUMERICS.hugoniotCurveCalc().getFMinus());
        RealMatrix2 DF = RPNUMERICS.fluxFunction().DF(U);
        RealMatrix2 DU = new RealMatrix2(dim, dim);
        RealMatrix2 result = new RealMatrix2(dim - 1, dim);
        for (int i = 0; i < dim - 1; i++)
            for (int j = 0; j < dim; j++)
                result.setElement(i, j, DF.getElement(i, j) * deltaU.getElement(dim - 1) + deltaF.getElement(i) *
                    DU.getElement(dim - 1, j) - DF.getElement(dim - 1, j) * deltaU.getElement(i) - deltaF.getElement(dim - 1) *
                    DU.getElement(i, j));

        return result;
    }






}
