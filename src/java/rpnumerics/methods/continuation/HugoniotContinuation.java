/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.methods.continuation;

import java.util.ArrayList;
import java.util.List;
import wave.util.*;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotParams;
import rpnumerics.RPNUMERICS;
import rpnumerics.PhasePoint;
import rpnumerics.WaveState;
import rpnumerics.ShockFlow;
import wave.ode.ODESolver;

public class HugoniotContinuation {

    private VectorField VF_;
    private VectorFunction f_;
    private double MinDistance_ = 0.001;
    private HugoniotParams continuationParams_;
    private ODESolver solver_;

    public HugoniotContinuation(VectorFunction f, HugoniotParams hugoniotParams, ODESolver solver) {
        VF_ = new HugoniotVectorField(f);
        f_ = f;
        solver_ = solver;
        continuationParams_ = hugoniotParams;
    }

    public HugoniotCurve curve(RealVector initialPoint) {
        
        ArrayList statesResult = new ArrayList();

        HugoniotFunctionOnPlane HF = new HugoniotFunctionOnPlane(f_,
                initialPoint, VF_.f(initialPoint));

        VectorField flowVF = null;
        if (wave.util.NewtonSolver.Newton(HF, initialPoint)) {

            flowVF = solver_.getProfile().getFunction(); //RPNUMERICS.odeSolver().getProfile().getFunction();
            solver_.getProfile().setFunction(VF_);
//            RPNUMERICS.odeSolver().getProfile().setFunction(VF_);
            wave.ode.ODESolution sol1 = solver_.solve(
                    initialPoint, 1);
            wave.ode.ODESolution sol2 = solver_.solve(
                    initialPoint, -1);
            for (int i = 0; i < sol1.getWavePoints().length - 1; i++) {
                RealVector point1 = sol1.getWavePoints()[i];
                HugoniotFunctionOnPlane HF1 = new HugoniotFunctionOnPlane(f_,
                        point1, VF_.f(point1));
                RealVector point2 = sol1.getWavePoints()[i + 1];
                HugoniotFunctionOnPlane HF2 = new HugoniotFunctionOnPlane(f_,
                        point2, VF_.f(point2));
                if ((wave.util.NewtonSolver.Newton(HF1, point1)) &&
                        (wave.util.NewtonSolver.Newton(HF2, point2))) {
                    solver_.getProfile().setFunction(VF_);
//                    RPNUMERICS.odeSolver().getProfile().setFunction(VF_);
                    if ((distance(point1) > MinDistance_) &&
                            (distance(point2) > MinDistance_)) {

                        WaveState state = new WaveState(new PhasePoint(point1),
                                new PhasePoint(point2),
                                ShockFlow.sigmaCalc(point1, point2));
                        statesResult.add(state);
                    }

//            result.add(new RealSegment(new RealVector(point1), new RealVector(point2)));
                }
            }
            for (int i = 0; i < sol2.getWavePoints().length - 1; i++) {
                RealVector point1 = sol2.getWavePoints()[i];
                HugoniotFunctionOnPlane HF1 = new HugoniotFunctionOnPlane(f_,
                        point1, VF_.f(point1));
                RealVector point2 = sol2.getWavePoints()[i + 1];
                HugoniotFunctionOnPlane HF2 = new HugoniotFunctionOnPlane(f_,
                        point2, VF_.f(point2));
                if ((wave.util.NewtonSolver.Newton(HF1, point1)) &&
                        (wave.util.NewtonSolver.Newton(HF2, point2))) {
                    solver_.getProfile().setFunction(VF_);
//                    RPNUMERICS.odeSolver().getProfile().setFunction(VF_);
                    if ((distance(point1) > MinDistance_) &&
                            (distance(point2) > MinDistance_)) {

                        WaveState state = new WaveState(new PhasePoint(point1),
                                new PhasePoint(point2),
                                ShockFlow.sigmaCalc(point1, point2));
                        statesResult.add(state);
                    }

                }
            }
        }

//        RPNUMERICS.odeSolver().getProfile().setFunction(flowVF);
        solver_.getProfile().setFunction(flowVF);
        List<RealVector> tList = null;

        return new HugoniotCurve(new PhasePoint(continuationParams_.getUMinus()), statesResult,tList);

    }

    private double distance(RealVector v) {
        RealVector delta = new RealVector(v);
//        delta.sub(rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus().
//                  getCoords());

        delta.sub(continuationParams_.getUMinus());


        return RPNUMERICS.errorControl().ode().stateVectorNorm(delta);
    }
}
