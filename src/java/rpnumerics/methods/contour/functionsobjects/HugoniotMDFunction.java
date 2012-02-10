/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.FluxFunction;
import rpnumerics.HugoniotCurveCalc;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;
import rpnumerics.HugoniotParams;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;

public class HugoniotMDFunction extends MDVectorFunction {

    private FluxFunction flux_;

    public HugoniotMDFunction(FluxFunction fluxFunction,
            HugoniotParams params) {

        super(rpnumerics.RPNUMERICS.domainDim(),
                rpnumerics.RPNUMERICS.domainDim() - 1, fluxFunction, params);

        flux_ = fluxFunction;

    }

    @Override
    public RealVector value(PointNDimension point) {

        double x = 0.0;
        double y = 0.0;

        RealVector result = new RealVector(RPNUMERICS.domainDim() - 1);

        try {
//      deltaU.sub(RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords());// edsonlan
            x = point.getCoordinate(1);
            y = point.getCoordinate(2);
            RealVector U = new RealVector(2);
            U.setElement(0, x);
            U.setElement(1, y);

            HugoniotCurveCalc hugoniotCurveCalc = RPNUMERICS.createHugoniotCalc(U);//edsonlan
            PhasePoint uMinus = hugoniotCurveCalc.getUMinus();//edsonlan

            //PhasePoint uMinus = null;//edsonlan

            RealVector fMinus = null;//edsonlan

            RealVector deltaF = flux_.F(U);



            deltaF.sub(fMinus);

            RealVector deltaU = new RealVector(U);

            deltaU.sub(uMinus);//edsonlan


            for (int i = 0; i < RPNUMERICS.domainDim() - 1; i++) {
                result.setElement(i,
                        deltaF.getElement(i) * deltaU.getElement(RPNUMERICS.domainDim() - 1)
                        - deltaF.getElement(RPNUMERICS.domainDim() - 1)
                        * deltaU.getElement(i));
            }

        } catch (Exception e) {

            System.out.println("Erro em value");

        }

        return result;

    }

    public RealMatrix2 deriv(PointNDimension point) {
        double x = 0.0;
        double y = 0.0;

        RealMatrix2 result = new RealMatrix2(RPNUMERICS.domainDim() - 1, RPNUMERICS.domainDim());

        try {
            x = point.getCoordinate(1);
            y = point.getCoordinate(2);
            RealVector U = new RealVector(2);
            U.setElement(0, x);
            U.setElement(1, y);

            RealVector deltaU = new RealVector(U);


//            PhasePoint UMinus = null;
//
//
//            deltaU.sub(UMinus);//edsonlan
//
//            RealVector deltaF = flux_.F(U);
//
//            PhasePoint FMinus = null;
//            deltaF.sub(FMinus);//edsonlan

//      deltaU.sub(RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords());
            deltaU.sub(RPNUMERICS.createHugoniotCalc(deltaU).getUMinus().getCoords());//edsonlan

            RealVector deltaF = flux_.F(U);

//      deltaF.sub(RPNUMERICS.hugoniotCurveCalc().getFMinus());
            deltaF.sub(RPNUMERICS.createHugoniotCalc(deltaF).getFMinus());//edsonlan


            RealMatrix2 DF = flux_.DF(U);
            RealMatrix2 DU = new RealMatrix2(RPNUMERICS.domainDim(), RPNUMERICS.domainDim());

            for (int i = 0; i < RPNUMERICS.domainDim() - 1; i++) {
                for (int j = 0; j < RPNUMERICS.domainDim(); j++) {
                    result.setElement(i, j,
                            DF.getElement(i, j) * deltaU.getElement(RPNUMERICS.domainDim() - 1)
                            + deltaF.getElement(i)
                            * DU.getElement(RPNUMERICS.domainDim() - 1, j)
                            - DF.getElement(RPNUMERICS.domainDim() - 1, j) * deltaU.getElement(i)
                            - deltaF.getElement(RPNUMERICS.domainDim() - 1)
                            * DU.getElement(i, j));
                }
            }

        } catch (Exception e) {

            System.out.println("Erro em derive de HugoniotMDFunction");

        }

        return result;

    }
}
