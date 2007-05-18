/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.ManifoldGeom;


public interface FIRSTDIR_MANIFOLD_READY extends POINCARE_READY {
    ManifoldGeom manifoldGeom();
}
