/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util; 

public interface VectorFunction {
    RealVector value(RealVector u);

    RealMatrix2 deriv(RealVector u);
}
