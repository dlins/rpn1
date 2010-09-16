/*
  * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.usecase.*;
import java.beans.PropertyChangeEvent;

/** This class implements the methods to manipulate some calculus controllers, this calculus difines the geometric model ofa geometric visualization . When specifics elements (XZero , Profile and Hugoniot )changes its controllers, with methods implemented by this class, do the  visualization properties updates. */

public class RpCalcController implements RpController {
    //
    // Members
    //
    private RpCalcBasedGeomFactory geomFactory_;


    //
    // Constructors
    //

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //

    /** Registers  a controller to handle the numerics parameters changes .*/
    protected void register() {
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        ChangeDirectionAgent.instance().addPropertyChangeListener(this);
        ChangeSigmaAgent.instance().addPropertyChangeListener(this);


    }


    /** Unregisters a controller that handle the changes in numerics parameters.*/

    protected void unregister() {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeDirectionAgent.instance().removePropertyChangeListener(this);
        ChangeSigmaAgent.instance().removePropertyChangeListener(this);
      
        
    }

    /** Updates the properties of a geometry when any change in its geometry is made.*/

    public void propertyChange(PropertyChangeEvent change) {
        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0){
            // updates only if visible
	        if (geomFactory_.geom().viewingAttr().isVisible())
            	geomFactory_.updateGeom();
        }
            
    }

    /** Installs a controller in a geometric model.*/

    public void install(RpGeomFactory geom) {
        register();
        geomFactory_ = (RpCalcBasedGeomFactory)geom;
    }

    /** Uninstalls a controller in a geometric model.*/

    public void uninstall(RpGeomFactory geom) {
        unregister();
        geomFactory_ = null;
    }
}
