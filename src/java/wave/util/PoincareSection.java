/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

public interface PoincareSection {

    RealVector[] getPoints();

    RealVector getNormal();

    boolean intersect(RealVector P1, RealVector P2) throws Exception;

    RealVector intersectionPoint(RealVector P1, RealVector P2);
}
