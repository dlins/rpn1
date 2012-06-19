/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class RPnStateInfo {

    private HashMap<String, String> information_;

    public RPnStateInfo(HashMap<String, String> information_) {
        this.information_ = information_;
    }

    public Iterator<Entry<String, String>> getInformation() {
        System.out.println("information_.size() ::::::::::::::::::::::::::::::" +information_.size());
        System.out.println("information_.values() ::::::::::::::::::::::::::::::" +(information_.values()).toArray()[0]);
        System.out.println("information_.get(fluxfunction) xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx : " +information_.get("fluxfunction").toString());
        return information_.entrySet().iterator();
    }
}
