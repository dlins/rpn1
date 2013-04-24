/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.ManifoldGeom;

public interface PROFILE_SETUP_READY {
    ManifoldGeom fwdManifoldGeom();
    ManifoldGeom bwdManifoldGeom();
}
