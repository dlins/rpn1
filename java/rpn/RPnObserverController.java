/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author edsonlan
 */
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
