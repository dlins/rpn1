/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.factories;


import rpnumerics.physics.Physics;
import rpnumerics.physics.NativeFluxFunctionFacade;
import rpnumerics.physics.*;
import rpnumerics.RPNumericsProfile;
import wave.util.RealVector;


public class FluxFunctionFactory {


  public static FluxFunction fluxFunctionCreate(FluxParams params, RPNumericsProfile profile) {

    return null;

  }

  public static Physics physicsCreation(RPNumericsProfile profile) {

    // Physics initialization
    if (profile.isNativePhysics()) {

      if (profile.getPhysicsID().equals("QuadraticR2")) {


        return  new Quad2(profile.libName(),new Quad2FluxParams());

      }

      if (profile.getPhysicsID().equals("QuadraticR4")) {

        return new Quad4(profile.libName(),new Quad4FluxParams());

      }

      if (profile.getPhysicsID().equals("Triphase")) {

         return new TriPhase(profile.libName(),new TriPhaseFluxParams());

      }

      if (profile.getPhysicsID().equals("Steam")) {

        return new Steam(profile.libName(),new SteamFluxParams());

      }

      if (profile.getPhysicsID().equals("SteamOil")) {

        return new SteamOil(profile.libName(),new SteamOilFluxParams());
      }


//      if (profile.getPhysicsID().equals("MultiExampleR2")) {
//
//          MultiExampleFluxParams [] paramsArray = new MultiExampleFluxParams[1];
//
//          paramsArray[0]= new MultiExampleFluxParams(new RealVector(2),0);
//          paramsArray[1]= new MultiExampleFluxParams(new RealVector(2),1);

//          return new MultiPhysicsExample(profile.libName(),paramsArray);


//          return new MultiPhysicsExample(profile.libName());
//      }


  }

    else {

      if (profile.getPhysicsID().equals("QuadraticR2")) {

        return new Quad2(new Quad2FluxParams());

      }

      if (profile.getPhysicsID().equals("QuadraticR4")) {
         return new Quad4(new Quad4FluxParams());
      }

      if (profile.getPhysicsID().equals("Triphase")) {

         return new TriPhase(new TriPhaseFluxParams(), new PermParams(),
                                new CapilParams(0.4d, 3d, 44d, 8d),
                                new ViscosityParams(0.5d));

      }

      if (profile.getPhysicsID().equals("P Gas")) {

         return new PGas(new PGasFluxParams());

      }

      if (profile.getPhysicsID().equals("Steam")) {
         return new Steam(new SteamFluxParams());

      }
    }

    return null ;
  }


}
