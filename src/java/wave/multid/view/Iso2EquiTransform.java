/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.view;

import wave.multid.map.*;
import wave.multid.graphs.*;



public class Iso2EquiTransform extends Viewing2DTransform {

    public Iso2EquiTransform(ProjectionMap projection, ViewPlane viewPlane) {

        super(projection, viewPlane);

	Viewing2DMap map = new Viewing2DMap();
	map.scale(1., Math.sqrt(3) / 2.);
        map.shear(.5, 0.);

//	viewPlane().update(map);
	makeCoordSysTransform();
	makeCompositeTransform();
        
    }
}



//public class Iso2EquiTransform extends Viewing2DTransform {
//
//    public Iso2EquiTransform(ProjectionMap projection, ViewPlane viewPlane) {
//
//        super(projection, viewPlane);
//    }
//
//    public void makeCoordSysTransform() {
//        /*
//         * COORDINATE SYSTEM TRANSFORMATION
//         *
//         * this is the translation + scaling + translation coordinate
//         * System transformation
//         */
//
//	// for initialization porpouses...
//	super.makeCoordSysTransform();
//
//        double XScaleFactor = viewPlane().getViewport().getWidth() / (viewPlane().getWindow().getWidth() + viewPlane().getWindow().getWidth());//*.5);
//        // not to be upside down...
//        double YScaleFactor = -viewPlane().getViewport().getHeight() / viewPlane().getWindow().getHeight();
//
//	YScaleFactor *= Math.sqrt(3)/2.;
//
//        double XTranslateFactor = -(viewPlane().getWindow().getOriginPosition().x) * XScaleFactor;
//        // we are working with RASTER
//        double YTranslateFactor = -viewPlane().getWindow().getOriginPosition().y * YScaleFactor
//                + viewPlane().getViewport().getHeight();
//
//	double XShearFactor = .5*XScaleFactor;
//	double YShearFactor = 0.*YScaleFactor;
// 
//        // the viewport translation
//        XTranslateFactor += viewPlane().getViewport().getOriginPosition().x;
//        YTranslateFactor += viewPlane().getViewport().getOriginPosition().y;
//        coordSysTransform().getTransfMatrix().setElement(0, 0, XScaleFactor);
//        coordSysTransform().getTransfMatrix().setElement(1, 0, XShearFactor);
//        coordSysTransform().getTransfMatrix().setElement(1, 1, YScaleFactor);
//        coordSysTransform().getTransfMatrix().setElement(0, 1, YShearFactor);
//        coordSysTransform().getTransfMatrix().setElement(2, 0, XTranslateFactor);
//        coordSysTransform().getTransfMatrix().setElement(2, 1, YTranslateFactor);
//        //coordSysTransform().setInversible(true);
//
//    }
//}
