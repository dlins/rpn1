/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;


public class RPnPersistentConsumer extends RPnConsumer {

    public RPnPersistentConsumer(String queueName) {

        super(queueName,true);

    }
}
