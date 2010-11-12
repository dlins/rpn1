/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.*;
import rpn.parser.ConfigurationProfile;
import rpnumerics.Configuration;
import rpnumerics.FluxParams;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class RPnFluxParamsPanel extends JPanel implements PropertyChangeListener {

    private GridBagLayout gridLayout = new GridBagLayout();
    private ArrayList<RPnInputComponent> valuesArray_;
    private ConfigurationProfile physicsProfile_;
    private boolean isDefault_;

    public RPnFluxParamsPanel() {
        System.out.println("Consturindo com a fisica de rpnumerics" + RPNUMERICS.physicsID());
        searchPhysics(RPNUMERICS.physicsID());
        buildPanel(false);
    }

    public RPnFluxParamsPanel(String physicsName) {
        System.out.println("consturindo default com a fisica:" + physicsName);
        searchPhysics(physicsName);
        buildPanel(true);
    }

    private void buildPanel(boolean useDefaults) {
        this.setLayout(gridLayout);
        isDefault_ = useDefaults;
        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.BOTH;

        gridConstraints.gridwidth = 3;
        gridConstraints.gridheight = 12;
        valuesArray_ = new ArrayList<RPnInputComponent>();

        gridConstraints.ipadx = 80;

        if (useDefaults) {
            for (int i = 0; i < physicsProfile_.getIndicesSize(); i++) {

                HashMap<String, String> param = physicsProfile_.getParam(i);
                Set<Entry<String, String>> paramSet = param.entrySet();

                for (Entry<String, String> paramEntry : paramSet) {

                    RPnInputComponent inputComponent = new RPnInputComponent(new Double(paramEntry.getValue()));
                    inputComponent.setLabel(paramEntry.getKey());
                    inputComponent.addPropertyChangeListener(this);
                    valuesArray_.add(i, inputComponent);
//                    gridConstraints.gridx = 1;
                    this.add(inputComponent.getContainer(), gridConstraints);
                        gridConstraints.gridx=1;

                }

            }
        } else {
//            gridConstraints.gridy = 0;
            gridConstraints.gridx = 1;
            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsProfile_.getName());
            for (int i = 0; i < physicsConfiguration.getParamsSize(); i++) {

                RPnInputComponent inputComponent = new RPnInputComponent(new Double(physicsConfiguration.getParam(i)));
                inputComponent.setLabel(physicsConfiguration.getParamName(i));
                inputComponent.addPropertyChangeListener(this);
                valuesArray_.add(i, inputComponent);
                this.add(inputComponent.getContainer(), gridConstraints);

                
            }
        }

    }

    private void searchPhysics(String physicsName) {
        Iterator<ConfigurationProfile> physicsIterator = RPnConfig.getAllPhysicsProfiles().iterator();

        while (physicsIterator.hasNext()) {
            ConfigurationProfile physicsProfile = physicsIterator.next();
            if (physicsProfile.getName().equals(physicsName)) {
                physicsProfile_ = physicsProfile;
            }
        }

    }

    public void applyParams() {
        RealVector newParamsVector = new RealVector(valuesArray_.size());

        FluxParams oldParams = RPNUMERICS.getFluxParams();
        for (int i = 0; i < valuesArray_.size(); i++) {
            System.out.println((Double) valuesArray_.get(i).getValue(RPnInputComponent.NUMERIC_VALUE));
            newParamsVector.setElement(i, (Double) valuesArray_.get(i).getValue(RPnInputComponent.NUMERIC_VALUE));

        }
//        System.out.println("Old params: " + oldParams);
        FluxParams newParams = new FluxParams(newParamsVector);
//        System.out.println("New params: " + newParams);
        RPNUMERICS.setFluxParams(newParams);
        rpn.usecase.ChangeFluxParamsAgent.instance().applyChange(new PropertyChangeEvent(rpn.usecase.ChangeFluxParamsAgent.instance(), "", oldParams, newParams));

    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (!isDefault_) {
            applyParams();
        }


    }
}
