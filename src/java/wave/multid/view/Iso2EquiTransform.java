/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.view;

import wave.multid.graphs.ViewPlane;
import wave.multid.map.ProjectionMap;

public class Iso2EquiTransform extends Viewing2DTransform {

    
    public final static double  sqrt3half = Math.sqrt(3) / 2.0;
    
    public Iso2EquiTransform(ProjectionMap projection, ViewPlane viewPlane) {

        super(projection, viewPlane);

        scale(1., sqrt3half);
        shear(.5, 0.);

                
    }

    public void setViewPlane(ViewPlane viewPlane){

      super.setViewPlane(viewPlane);

      scale(1., Math.sqrt(3) / 2.);
      shear(.5, 0.);

     }





}
