/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.ui.physics;

import rpn.ui.physics.RPnObserver;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author moreira
 */
public abstract class RPnSubject {

    private List <RPnObserver> list_ = new ArrayList<RPnObserver>();
    private String name_;
    private String[] paramValues_;   //*** Leandro teste
    

    public RPnSubject(String name, String[] paramValues) {   //*** Leandro teste
        name_ = name;
        paramValues_ = paramValues;
    }

    public void attach(RPnObserver obs) {
        list_.add(obs);
    }

    public void detach(RPnObserver obs) {
        list_.remove(obs);
    }

    public void notifyObserver() {

        for (RPnObserver rPnObserver : list_) {
            rPnObserver.update();
        }
    }

    public void notifyObserver(RPnSubject subject) {

        for (RPnObserver rPnObserver : list_) {
            rPnObserver.update(subject);
        }
    }
    
    public String getName() {
        return name_;
    }

    public abstract String[] getParamsNames();


    public String[] getParamsValues() {   //*** Leandro teste
        return paramValues_;
    }


    //public abstract void setValues(RealVector realVector);                      // MUDAR P/ RECEBER STRING[]


    //*** Leandro teste
    //TODO    ... throws Exception
    public abstract void setValues(String[] stringArray);


    public String[] getState() {   //*** Leandro teste
        return getParamsValues();
    }



}
