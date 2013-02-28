/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */



package rpnumerics;

import wave.util.JetMatrix;


public interface  RpFunction {
    
    
   public  int jet( WaveState input, JetMatrix output , int degree);

}
