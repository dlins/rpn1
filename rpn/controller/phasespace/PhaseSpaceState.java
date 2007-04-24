/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.RpGeometry;
import rpn.RPnPhaseSpaceAbstraction;
import wave.util.RealVector;

/** This interface is the main class of rpn.controller.phasespace package. This package contains classes that controls the state of the application. These classes enables and disables the menu options to keep the application syncronized with the user inputs.
*<p> This three methods declared by this interface manipulates the geometric models of the curves shown by the application. </p>
*/

public interface PhaseSpaceState {

    /** Plots a geometric model .
     * @param geom The geometric model to plot.
     * @param phaseSpace The list that contain the geomertic model to plot.
     */

    void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom);

    /** Delete a geometric model .

    * @param geom The geometric model to delete.
    * @param phaseSpace The list that contain the geomertic model to delete.
    */

    void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom);

    /** Selects a geometric model.
    * @param coords  The positon to select.
    * @param phaseSpace The list that contain the geomertic model to select.
    */

    void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords);
}
