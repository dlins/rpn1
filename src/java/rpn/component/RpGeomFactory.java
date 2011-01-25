/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpn.controller.RpController;

/** This interface declares aditional methods to manipulate the geometric models. Each geometric model has a geometric visual form associated , this class declares methods to handle the model part of the pair model/visual form. */


public interface RpGeomFactory {

    /** Do the recalculations required to update de geometric model of any curve. */
    void updateGeom();


    /** Returns the geometric model of the geometric visualization. */

    RpGeometry geom();

    // TODO we could have something like createSource() ???


    /** Returns the calculus required to obtain the geometric visualization. */

    Object geomSource();

    /** Returns a XML string that contains the data required to obtain the geometric model.*/

    String toXML();
    
    /** Returns a string in Matlab's format of the model.*/

    String toMatlab();

    /** Verify if the geometric model is updated.*/

    boolean isGeomOutOfDate();

    /** Sets the geometric model as out of date.*/

    void setGeomOutOfDate(boolean flag);

    /** Sets a controller to the geometric model. */

    void setUI(RpController ui);

    /** Gets the controller of the geometric model. */

    RpController getUI();
}
