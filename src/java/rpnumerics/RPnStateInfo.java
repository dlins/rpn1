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
        return information_.entrySet().iterator();
    }
}
