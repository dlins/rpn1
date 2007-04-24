/**
 * @(#) QuadND.java
 */

package rpnumerics.physics;

public abstract class QuadND implements Physics
{
	public static final String FLUX_ID = "Generic Quadratic";

	private QuadNDFluxParams params_;
        public  FluxFunction [] fluxFunctionArray(){return null;}

        public FluxFunction fluxFunction(int index){return null;}

}


/**@todo 1 - Adicionar no parser indices menos e mais para escolher o elemento do array de funcoes de fluxo
 * 2- Quad2 em Java nao sera mais usada . Havera somente a Quad2 nativa com as funcoes de fluxo do Rodrigo.
 * 3 - Fazer o dialogo de parametros de fluxo passar esses paramentros para asa fisicas nativas
 **/

