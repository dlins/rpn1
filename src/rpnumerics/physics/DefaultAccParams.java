/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;


public class DefaultAccParams extends AccumulationParams {



  public DefaultAccParams(){
    /** @todo How to create DefaultAccParams before complete physics initalization ?
     *
     */

    super (rpnumerics.RPNUMERICS.physicsID(),new RealVector(rpnumerics.RPNUMERICS.domainDim()));// can´t call RPNUMERICS methods before complete physics initalization !


  }

  public AccumulationParams defaultParams(){return new DefaultAccParams();}





}
