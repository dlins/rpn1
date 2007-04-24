/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */



package rpnumerics.methods.continuation;

import java.util.ArrayList;
import wave.util.*;
import rpnumerics.HugoniotCurve;
import rpnumerics.RPNUMERICS;
import rpnumerics.PhasePoint;

public class HugoniotContinuation {
  private VectorField VF_;
  private VectorFunction f_;
  private double MinDistance_ = 0.001;

  public HugoniotContinuation(VectorFunction f) {
    VF_ = new HugoniotVectorField(f);
    f_ = f;
  }

  public HugoniotCurve curve(RealVector initialPoint) {

    ArrayList result = new ArrayList();
    HugoniotFunctionOnPlane HF = new HugoniotFunctionOnPlane(f_, initialPoint, VF_.f(initialPoint));

    VectorField flowVF = null;
    if (wave.util.NewtonSolver.Newton(HF, initialPoint)) {

      flowVF = RPNUMERICS.odeSolver().getProfile().getFunction();
      RPNUMERICS.odeSolver().getProfile().setFunction(VF_);
      wave.ode.ODESolution sol1 = RPNUMERICS.odeSolver().solve(initialPoint, 1);
      wave.ode.ODESolution sol2 = RPNUMERICS.odeSolver().solve(initialPoint, -1);
      for (int i = 0; i < sol1.getCoordinates().length - 1; i++) {
        RealVector point1 = sol1.getCoordinates() [i];
        HugoniotFunctionOnPlane HF1 = new HugoniotFunctionOnPlane(f_, point1, VF_.f(point1));
        RealVector point2 = sol1.getCoordinates() [i + 1];
        HugoniotFunctionOnPlane HF2 = new HugoniotFunctionOnPlane(f_, point2, VF_.f(point2));
        if ((wave.util.NewtonSolver.Newton(HF1, point1)) && (wave.util.NewtonSolver.Newton(HF2, point2))) {
          RPNUMERICS.odeSolver().getProfile().setFunction(VF_);
          if ((distance(point1) > MinDistance_) && (distance(point2) > MinDistance_))
            result.add(new RealSegment(new RealVector(point1), new RealVector(point2)));
        }
      }
      for (int i = 0; i < sol2.getCoordinates().length - 1; i++) {
        RealVector point1 = sol2.getCoordinates() [i];
        HugoniotFunctionOnPlane HF1 = new HugoniotFunctionOnPlane(f_, point1, VF_.f(point1));
        RealVector point2 = sol2.getCoordinates() [i + 1];
        HugoniotFunctionOnPlane HF2 = new HugoniotFunctionOnPlane(f_, point2, VF_.f(point2));
        if ((wave.util.NewtonSolver.Newton(HF1, point1)) && (wave.util.NewtonSolver.Newton(HF2, point2))) {
          RPNUMERICS.odeSolver().getProfile().setFunction(VF_);
          if ((distance(point1) > MinDistance_) && (distance(point2) > MinDistance_))
            result.add(new RealSegment(new RealVector(point1), new RealVector(point2)));
        }
      }
    }

    RPNUMERICS.odeSolver().getProfile().setFunction(flowVF);

    return new HugoniotCurve(new PhasePoint(rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus()), result);
  }

  private double distance(RealVector v) {
    RealVector delta = new RealVector(v);
    delta.sub(rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords());
    return RPNUMERICS.errorControl().ode().stateVectorNorm(delta);
  }
}
