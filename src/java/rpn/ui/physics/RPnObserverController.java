/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.ui.physics;

import rpn.ui.RPnInputComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class RPnObserverController implements PropertyChangeListener {


    private RPnInputComponent inputComponent_;
    private RPnSubject subject_;


    public RPnObserverController(RPnInputComponent inputComponent, RPnSubject subject) {
        inputComponent_ = inputComponent;
        subject_= subject;
        
    }


    public void propertyChange(PropertyChangeEvent evt) {

        /*
         *
         entra aqui qdo o observer é atualizado

         passa os valores da aba ativa (subject_) para o "vetorzao"
         cada subject_ tem sua implementacao de setValues

         (RealVector) evt.getNewValue() é o vetor de cada aba
         Fazer setValues(qqer outro RealVector )
         *
         */

        
        //subject_.setValues((RealVector) evt.getNewValue());

        //System.out.println("Quer usar qual subject_ : " +subject_.getName());

        // aqui terá um try catch por causa do throws...
        subject_.setValues((String[]) evt.getNewValue());       //*** Leandro teste

    }   


}
