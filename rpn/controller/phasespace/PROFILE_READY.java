/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.ProfileGeom;

public interface PROFILE_READY extends PROFILE_SETUP_READY {
    ProfileGeom connectionGeom();
}
