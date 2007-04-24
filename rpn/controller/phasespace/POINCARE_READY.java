/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.PoincareSectionGeom;

public interface POINCARE_READY extends NUMCONFIG_READY {
    PoincareSectionGeom poincareGeom();
}
