/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.*;
import java.util.StringTokenizer;
import rpn.parser.PhysicsProfile;
import rpn.parser.RPnInterfaceParser;
import rpnumerics.FluxParams;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class RPnFluxParamsPanel extends JPanel {

    private GridLayout gridLayout = new GridLayout(2, 2);
    private ArrayList<JTextField> valuesArray = new ArrayList<JTextField>();
    private PhysicsProfile physicsProfile_;

    public RPnFluxParamsPanel() {

        searchPhysics(RPNUMERICS.physicsID());
        buildPanel(false);
    }

    public RPnFluxParamsPanel(String physicsName) {
        searchPhysics(physicsName);
        buildPanel(true);
    }

    private void buildPanel(boolean useDefaults) {

        ArrayList<HashMap<String, String>> fluxParamsArrayList = physicsProfile_.getFluxParamArrayList();
        
        for (int i = 0; i < fluxParamsArrayList.size(); i++) {
            HashMap<String, String> fluxParams = fluxParamsArrayList.get(i);

            Set<Entry<String, String>> fluxParamsSet = fluxParams.entrySet();

            Iterator<Entry<String, String>> paramsIterator = fluxParamsSet.iterator();

            while (paramsIterator.hasNext()) {
                Entry<String, String> entry = paramsIterator.next();
                JLabel fluxParamName = new JLabel(entry.getKey());
                fluxParamName.setName(entry.getKey());
                JTextField fluxValueField = new JTextField();
                valuesArray.add(i,fluxValueField);
                if (useDefaults) {
                    fluxValueField.setText(entry.getValue());
                }
                else {
                    
                    FluxParams fluxParam = RPNUMERICS.getFluxParams();
                    Double paramValue = fluxParam.getElement(i);
                    valuesArray.get(i).setText(paramValue.toString());
                    
                }
                fluxValueField.setName(entry.getKey());

                this.add(fluxParamName);
                this.add(fluxValueField);
            }
        }
        this.setLayout(gridLayout);
    }

    private void searchPhysics(String physicsName) {

        Iterator<PhysicsProfile> physics = RPnInterfaceParser.getPhysicsProfiles().iterator();

        while (physics.hasNext()) {
            PhysicsProfile physicsProfile = physics.next();
            if (physicsProfile.getName().equals(physicsName)) {
                physicsProfile_ = physicsProfile;
            }
        }

    }

    public void applyParams() {

        StringBuffer paramsBuffer = new StringBuffer();

        for (int i = 0; i < valuesArray.size(); i++) {
            JTextField jTextField = valuesArray.get(i);
            paramsBuffer.append(jTextField.getText());
            paramsBuffer.append(" ");
        }

        RealVector paramsVector = new RealVector(paramsBuffer.toString());
        RPNUMERICS.setFluxParams(new FluxParams( paramsVector));
    }
    
}
