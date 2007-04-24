/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.factories;

import rpnumerics.ShockFlow;
import rpnumerics.ShockFlowParams;
import rpnumerics.RPNumericsProfile;
import rpnumerics.RarefactionFlow;
import rpn.controller.ui.*;
import rpnumerics.physics.*;
import rpnumerics.*;

public class WaveFlowFactory{

  public static WaveFlow shockFlowCreate(ShockFlowParams params, RPNumericsProfile profile) {

    System.out.println("initial state is shock !");

    rpnumerics.RPNumericsProfile.setInitialState(new SHOCK_CONFIG());

    if (profile.isNativePhysics()) {

      NativeShockFlowFacade  flowFacade_ = new NativeShockFlowFacade(profile.libName());


      return new NativeShockFlow(flowFacade_, params);
    }

    if (profile.isSpecificShockFlow()) {

      if (profile.getPhysicsID().equals("Comb"))

          return new CombShockFlow(params);

//      SpecificShockFlowPhysics specificPhysics = ( (SpecificShockFlowPhysics) RPNUMERICS.getPhysics());
//      return specificPhysics.createShockFlowInstance(params);

    }

    return new ConservationShockFlow(params);

  }

  public static RarefactionFlow rarefactionFlowCreate(PhasePoint xZero,
      RPNumericsProfile profile) {

    System.out.println("initial state is rarefaction !");

        rpnumerics.RPNumericsProfile.setInitialState(new RAREFACTION_CONFIG() );

    return new RarefactionFlow(xZero,profile.getFamilyIndex());

  }

  public static RarefactionFlow blowUpFlowCreate(BlowUpLineFieldVector xZero,
      RPNumericsProfile profile) {

    System.out.println("initial state is blowuprarefaction !");

    rpnumerics.RPNumericsProfile.setInitialState(new RAREFACTION_CONFIG());

    return new BlowUpFlow(xZero);

  }
}
