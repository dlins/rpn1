/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.component.PoincareSectionGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.controller.phasespace.*;
import rpn.usecase.*;
import rpn.parser.RPnDataModule;
import rpnumerics.RPNUMERICS;
import java.beans.PropertyChangeEvent;
import rpnumerics.ConnectionOrbit;

public class PoincareController implements RpController {
    private PoincareSectionGeomFactory geomFactory_;
    
    public PoincareController() {
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent change) {
        if (RPnDataModule.PHASESPACE.state() instanceof PROFILE_READY) {
            PROFILE_READY profileHolder = (PROFILE_READY)RPnDataModule.PHASESPACE.state();
            ConnectionOrbit connOrbit = (ConnectionOrbit)profileHolder.connectionGeom().geomFactory().geomSource();
            
            RPNUMERICS.changePoincareSection(connOrbit);
            
            /*To native layer */
            
//            RealVector center = connOrbit.orbitCenter();
//            RealVector normal = rpnumerics.RPNUMERICS.flow().flux(center);
//            ((SimplexPoincareSection)geomFactory_.geomSource()).shift(center, normal);
            
            
            
            // updates the numerics layers
//            ((ODESolverProfile)
//            rpnumerics.RPNUMERICS.odeSolver().getProfile()).setPoincareSection((SimplexPoincareSection)geomFactory_.geomSource());
            
            
            
            geomFactory_.updateGeom();
            
            
        }
    }
    
    public void install(RpGeomFactory geom) {
        geomFactory_ = (PoincareSectionGeomFactory)geom;
        
        /*Initializated in native layer */
        
//        ((ODESolverProfile) rpnumerics.RPNUMERICS.odeSolver().getProfile()).setPoincareSection((SimplexPoincareSection)geomFactory_.geomSource());
    }
    
    public void uninstall(RpGeomFactory geom) {
        if (geomFactory_ == geom) {
            geomFactory_ = null;
            
            
            /*Using native layer mutator*/
            
//            ((ODESolverProfile) rpnumerics.RPNUMERICS.odeSolver().getProfile()).setPoincareSectionFlag(false);
            
            RPNUMERICS.setPoincareSectionFlag(false);
            
            ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        }
    }
}
