/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.map;

import wave.multid.Space;

/* is it possible to have a n dimensional
translation map ?
*/

public class RotationMap extends IdentityMap {
    public RotationMap(Space domain, Space codomain) {
        super(domain, codomain);
    }
}
