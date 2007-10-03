/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

import wave.multid.view.Viewing2DTransform;

/*
 * In terms of visualization we are assuming this
 * to be a Clipping Shape
 */

public interface Boundary {
    boolean inside(RealVector y);

    // this is only needed by ODE ? TODO remove
    RealVector getMinimums();

    RealVector getMaximums();

    RealVector intersect(RealVector y1, RealVector y2);
}
