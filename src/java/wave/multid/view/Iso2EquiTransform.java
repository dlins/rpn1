/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.view;

import wave.multid.map.*;
import wave.multid.view.Viewing2DTransform;
import wave.multid.graphs.*;
import wave.multid.Multid;

public class Iso2EquiTransform extends Viewing2DTransform {

    public Iso2EquiTransform(ProjectionMap projection, ViewPlane viewPlane) {

        super(projection, viewPlane);

	Viewing2DMap map = new Viewing2DMap();
	map.scale(1., Math.sqrt(3) / 2.);
        map.shear(.5, 0.);

	viewPlane().update(map);
	makeCoordSysTransform();
	makeCompositeTransform();
        
    }
}
