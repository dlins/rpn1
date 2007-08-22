/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package wave.multid.view;

import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.map.ProjectionMap;
import wave.multid.map.Map;
import wave.multid.graphs.ViewPlane;

/** A interface that declares basics methods to create a 2D or 3D transformation to obtain a visual form of a multidimensional object. */

public interface ViewingTransform {

    /** Returns the map transformation applied to obtain a reduced dimenensional version of the multidimensional object. */

    ProjectionMap projectionMap();

    /** Returns the map transformation associeted with the projection and redution of dimension of a multidimensional object. */

    Map viewingMap();

    /** Returns a map transformation associeted with translation, scaling and translation coordinate transformations. */
    Map coordSysTransform();

    /** Returns the view plane of a visual form.   */

    ViewPlane viewPlane();

    /** Change the view plane of a visual form. */


    public void setViewPlane(ViewPlane vPlane);

    /** Apply the view plane to the transformation. */

    void viewPlaneTransform(CoordsArray worldCoords, Coords2D dcCoords);

    /** Apply the inverse transform of that was used to obtain a visual form of a multidimensional object. */


    void dcInverseTransform(Coords2D dcCoords, CoordsArray worldCoords);




}
