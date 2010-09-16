/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

public interface WaveCurve {
    WaveState initialState();

    WaveState finalState();
}
