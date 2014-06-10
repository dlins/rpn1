/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

public class RiemannProfileGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RiemannProfileGeomFactory(RiemannProfileCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected ViewingAttr selectViewingAttr() {

        return new ViewingAttr(Color.white);
    }

    public RpGeometry createGeomFromSource() {
        RiemannProfile riemannProfile = (RiemannProfile) geomSource();
        

        return new RiemannProfileGeom(MultidAdapter.converseRiemannProfileToCoordsArray(riemannProfile.getPoints()), this);
    }

}
