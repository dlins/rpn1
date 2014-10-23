/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.methods;

import rpnumerics.GenericHugoniotFunction;
import rpnumerics.HugoniotParams;
import rpnumerics.methods.continuation.HugoniotContinuation;
import wave.util.RealVector;
import rpnumerics.HugoniotCurve;
import wave.ode.ODESolver;
import wave.util.VectorFunction;

public class HugoniotContinuationMethod extends HugoniotMethod {

    private HugoniotParams params_;
    private HugoniotContinuation continuation_;
    private VectorFunction vectorFunction_;

    public HugoniotContinuationMethod(GenericHugoniotFunction hugoniotFunction, HugoniotParams hparams, ODESolver solver) {
        vectorFunction_ = hugoniotFunction;
        params_=hparams;
        continuation_ = new HugoniotContinuation(hugoniotFunction, hparams,solver);
    }

    public VectorFunction getFunction() {
        return vectorFunction_;
    }

    public HugoniotCurve curve(RealVector initialPoint) {

        return continuation_.curve(initialPoint);

    }

    public HugoniotParams getParams() {
        return params_;
    }
}
