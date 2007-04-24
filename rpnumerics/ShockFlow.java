/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;
import wave.util.RealVector;

public abstract class ShockFlow implements WaveFlow{


    protected ShockFlowParams flowParams_;

    protected ShockFlow (ShockFlowParams flowParams){

	flowParams_= flowParams;

    }

    static public double sigmaCalc(RealVector coords1, RealVector coords2) {
        RealVector F_coords1 = RPNUMERICS.fluxFunction().F(coords1);
        RealVector F_coords2 = RPNUMERICS.fluxFunction().F(coords2);
        double[] F_diff = new double[coords1.getSize()];
        double[] COORDS_diff = new double[coords1.getSize()];
        double F_sum = 0;
        double COORDS_sum = 0;
        for (int i = 0; i < coords1.getSize(); i++) {
            F_diff[i] = F_coords2.getElement(i) - F_coords1.getElement(i);
            COORDS_diff[i] = coords2.getElement(i) - coords1.getElement(i);
            F_sum += Math.pow(F_diff[i], 2);
            COORDS_sum += Math.pow(COORDS_diff[i], 2);
        }
        double sigma = Math.sqrt(F_sum / COORDS_sum);
        for (int i = 0; i < coords1.getSize(); i++)
            if (F_diff[i] * COORDS_diff[i] < 0) {
                sigma *= -1;
                break;
            }
        return sigma;
    }

    public void setXZero(PhasePoint x0) {

        flowParams_.setPhasePoint(x0);
    }


    public double getSigma() { return flowParams_.getSigma(); }

    public PhasePoint getXZero() { return flowParams_.getPhasePoint(); }

    public void setSigma(double sigma) { flowParams_.setSigma(sigma); }

    public void setSigma(RealVector refPoint) {
        setSigma(ShockFlow.sigmaCalc(new RealVector(flowParams_.getPhasePoint().getCoords()), refPoint));
    }

}
