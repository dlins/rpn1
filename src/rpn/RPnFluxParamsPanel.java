/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.*;
import rpn.parser.ConfigurationProfile;
import rpn.parser.RPnInterfaceParser;
import rpnumerics.Configuration;
import rpnumerics.FluxParams;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class RPnFluxParamsPanel extends JPanel {

    private GridBagLayout gridLayout = new GridBagLayout();
    private ArrayList<JTextField> valuesArray_ = new ArrayList<JTextField>();
    private ConfigurationProfile physicsProfile_;

    public RPnFluxParamsPanel() {

        searchPhysics(RPNUMERICS.physicsID());
        buildPanel(false);
    }

    public RPnFluxParamsPanel(String physicsName) {
        searchPhysics(physicsName);
        buildPanel(true);
    }

    private void buildPanel(boolean useDefaults) {
        this.setLayout(gridLayout);

        GridBagConstraints gridConstraints = new GridBagConstraints();


        HashMap<String, String> fluxParamsArrayList =null;

        gridConstraints.ipadx = 40;
        if (useDefaults){
            ConfigurationProfile physicsConfiguration = RPnConfig.getConfigurationProfile("QuadraticR2");
            fluxParamsArrayList = physicsConfiguration.getParams();
        }
        else {
        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsProfile_.getName());
        fluxParamsArrayList = physicsConfiguration.getParams();
        }






//        for (int i = 0; i < fluxParamsArrayList.size(); i++) {
//            HashMap<String, String> fluxParams = fluxParamsArrayList.get(i);
        int i = 0;
            Set<Entry<String, String>> fluxParamsSet = fluxParamsArrayList.entrySet();

            Iterator<Entry<String, String>> paramsIterator = fluxParamsSet.iterator();

            while (paramsIterator.hasNext()) {
                Entry<String, String> entry = paramsIterator.next();
                JLabel fluxParamName = new JLabel(entry.getKey());
                fluxParamName.setName(entry.getKey());
                JTextField fluxValueField = new JTextField();
                valuesArray_.add(i, fluxValueField);
                if (useDefaults) {
                    Double paramValue = new Double(entry.getValue());
                    fluxValueField.setText(paramValue.toString());
                } else {

                    FluxParams fluxParam = RPNUMERICS.getFluxParams();
                    Double paramValue = fluxParam.getElement(i);
                    valuesArray_.get(i).setText(paramValue.toString());

                }
                fluxValueField.setName(entry.getKey());

                gridConstraints.gridx = 0;

                this.add(fluxParamName, gridConstraints);
                gridConstraints.gridx = 1;
                this.add(fluxValueField, gridConstraints);
                i++;
            }
//        }
    }

    private void searchPhysics(String physicsName) {

        Iterator<ConfigurationProfile> physics = RPnInterfaceParser.getPhysicsProfiles().iterator();

        while (physics.hasNext()) {
            ConfigurationProfile physicsProfile = physics.next();
            if (physicsProfile.getName().equals(physicsName)) {
                physicsProfile_ =  physicsProfile;
            }
        }

    }

    public void applyParams() {

        StringBuffer paramsBuffer = new StringBuffer();

        for (int i = 0; i < valuesArray_.size(); i++) {
            JTextField jTextField = valuesArray_.get(i);
            paramsBuffer.append(jTextField.getText());
            paramsBuffer.append(" ");
        }

        RealVector paramsVector = new RealVector(paramsBuffer.toString());
        RPNUMERICS.setFluxParams(new FluxParams(paramsVector));
        System.out.println(paramsVector);
        Integer teste = new Integer(0);
        Integer teste2 = new Integer(1);
        rpn.usecase.ChangeFluxParamsAgent.instance().applyChange(new PropertyChangeEvent(rpn.usecase.ChangeFluxParamsAgent.instance(), "", teste, teste2));

    }
}
