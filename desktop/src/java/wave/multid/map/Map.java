/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;

import wave.multid.*;
import wave.util.RealMatrix2;

/** The main interface of the map package. Declares basic methods to get informations about a map transformation, to setup this transformations and to apply some basics transformations*/

public interface Map extends Inversible {
    /** Returns the domain of the map transformation */

    Space getDomain();

    /** Returns the codomain of the map transformation. */

    Space getCodomain();

    /** Apply the matrix transformation of the map. */

    void image(CoordsArray in, CoordsArray out);

    /** Apply the inverse transformation of the map.*/

    void inverse(CoordsArray in, CoordsArray out) throws NoInverseMapEx;

    /** Concatenate map with a map transformation. */

    void concatenate(Map map);
    /**  Returns the matrix of the map transformation .
     */

    RealMatrix2 getTransfMatrix();
    /** Returns the inverse matrix of the map transformation. */

    RealMatrix2 getInvTransfMatrix();

    /** Set a matrix to a map transformation. */

    void setTransfMatrix(RealMatrix2 matrix);
}
