/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.JetMatrix;
import wave.util.RealVector;

public class ShockFlow extends WaveFlow {

    public static FluxFunction getFlux() {
        return flux_;
    }
    protected ShockFlowParams flowParams_;
    private static FluxFunction flux_;

    protected ShockFlow(ShockFlowParams flowParams, FluxFunction flux) {

        flowParams_ = flowParams;
        flux_ = flux;

    }

    /**
     * @deprecated To be implemented in native layer (plugin)
     * @param x Input
     * @return
     */
    public RealVector fluxDerivSigma(RealVector x) {
        RealVector result = new RealVector(x.getSize());
        result.sub(new RealVector(flowParams_.getPhasePoint().getCoords()), x);
        return result;
    }

//    /**
//     * @deprecated To be implemented in native layer (plugin)
//     * @param x Input
//     * @return
//     */
//    public RealMatrix2 fluxDeriv(RealVector x) {
//
//        
//        
//        WaveState input = new WaveState(new PhasePoint(x));
//        JetMatrix output = new JetMatrix(x.getSize());
//        getFlux().jet(input, output, 1);
//        
//
////        for (int i = 0; i < output.n_comps();i++){
////            for (int j=0; j< output.n_comps();j++){
////                 System.out.println(output.getElement(i, j));
////            }
////        }
//        
//
//
//        RealMatrix2 fluxDF_x = new RealMatrix2(output.n_comps(), output.n_comps()); //TODO Replace for JacobianMatrix
//
//        for (int i = 0; i < output.n_comps(); i++) {
//            for (int j = 0; j < output.n_comps(); j++) {
//                fluxDF_x.setElement(i, j, output.getElement(i, j));
//            }
//        }
//
//
////        RealMatrix2 fluxDF_x = getFlux().DF(x);//RPNUMERICS.fluxFunction().DF(x);
//        // flux.DFX(x) - sigma scaled matrix
//        RealMatrix2 identity = new RealMatrix2(fluxDF_x.getNumRow(),
//                fluxDF_x.getNumCol());
//        identity.scale(getSigma());
//        fluxDF_x.sub(identity);
//        
////        System.out.println("Saida");
////        System.out.println(fluxDF_x);
////        System.out.println("-------------Saida--------------------");
//        
//        return fluxDF_x;
//        
//        
//        
//        
//    }

    static public double sigmaCalc(RealVector coords1, RealVector coords2) {

        WaveState input1 = new WaveState(new PhasePoint(coords1));
        WaveState input2 = new WaveState(new PhasePoint(coords2));
        JetMatrix output1 = new JetMatrix(coords1.getSize());
        JetMatrix output2 = new JetMatrix(coords2.getSize());

        getFlux().jet(input1, output1, 0);
        getFlux().jet(input2, output2, 0);

//        RealVector F_coords1 = getFlux().F(coords1);
//        RealVector F_coords2 = getFlux().F(coords2);
        double[] F_diff = new double[coords1.getSize()];
        double[] COORDS_diff = new double[coords1.getSize()];
        double F_sum = 0;
        double COORDS_sum = 0;
        for (int i = 0; i < coords1.getSize(); i++) {

            F_diff[i] = output2.getElement(i) - output1.getElement(i); //F_diff[i] = F_coords2.getElement(i) - F_coords1.getElement(i);

            COORDS_diff[i] = coords2.getElement(i) - coords1.getElement(i);
            F_sum += Math.pow(F_diff[i], 2);
            COORDS_sum += Math.pow(COORDS_diff[i], 2);
        }
        double sigma = Math.sqrt(F_sum / COORDS_sum);
        for (int i = 0; i < coords1.getSize(); i++) {
            if (F_diff[i] * COORDS_diff[i] < 0) {
                sigma *= -1;
                break;
            }
        }
        return sigma;
    }

    public void setXZero(PhasePoint x0) {

        flowParams_.setPhasePoint(x0);
    }

    public double getSigma() {
        return flowParams_.getSigma();
    }

    public PhasePoint getXZero() {
        return flowParams_.getPhasePoint();
    }

    public void setSigma(double sigma) {
        flowParams_.setSigma(sigma);
    }

    public void setSigma(RealVector refPoint) {
        setSigma(ShockFlow.sigmaCalc(new RealVector(flowParams_.getPhasePoint().getCoords()), refPoint));
    }
}
