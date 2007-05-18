/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;


/** A interface to define if a map transformation is inversible */

public interface Inversible {


    /** Verify if the map transformation is inversible.
     * @return true if the map transformation has a inverse and false if not
     */

    boolean hasInverse();
}
