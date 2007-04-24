/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.HugoniotCurveGeom;

public interface NUMCONFIG extends PhaseSpaceState {
    HugoniotCurveGeom hugoniotGeom();
}
