/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.command.*;
import java.beans.PropertyChangeEvent;
import rpnumerics.Area;

/** This class implements the methods to manipulate some calculus controllers, this calculus defines the geometric model of geometric visualization . When specifics elements (XZero , Profile and Hugoniot )changes its controllers, with methods implemented by this class, do the  visualization properties updates. */
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
        ChangeFluxParamsCommand.instance().addPropertyChangeListener(this);
        ChangeSigmaCommand.instance().addPropertyChangeListener(this);
        ReferencePointSelectionCommand.instance().addPropertyChangeListener(this);
    }

    /** Unregisters a controller that handle the changes in numerics parameters.*/
    protected void unregister() {
        ChangeFluxParamsCommand.instance().removePropertyChangeListener(this);
        ChangeSigmaCommand.instance().removePropertyChangeListener(this);
        ReferencePointSelectionCommand.instance().removePropertyChangeListener(this);
    }

    /** Updates the properties of a geometry when any change in its geometry is made.*/
    public void propertyChange(PropertyChangeEvent change) {

        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("refine") == 0) {

            System.out.println("Refinamento");
            
            Area area = (Area) change.getNewValue();
//            geomFactory_.updateGeom(area);

            return;
        }


        if (change.getPropertyName().compareTo("enabled") != 0) {
            //System.out.println("Entrando em propertyChange");
            // updates only if visible
//	        if (geomFactory_.geom().viewingAttr().isVisible())
            geomFactory_.updateGeom();
        }



    }

    /** Installs a controller in a geometric model.*/
    @Override
    public void install(RpGeomFactory geom) {
        register();
        geomFactory_ = (RpCalcBasedGeomFactory) geom;
    }

    /** Uninstalls a controller in a geometric model.*/
    public void uninstall(RpGeomFactory geom) {
        unregister();
        geomFactory_ = null;
    }
}
