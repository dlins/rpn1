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
//        System.out.println("Consturindo com a fisica de rpnumerics: " + RPNUMERICS.physicsID());
        searchPhysics(RPNUMERICS.physicsID());
        buildPanel(false);
    }

    public RPnFluxParamsPanel(String physicsName) {
//        System.out.println("consturindo default com a fisica:" + physicsName);
        searchPhysics(physicsName);

        buildPanel(true);
    }

    private void buildPanel(boolean useDefaults) {
        this.setLayout(gridLayout);
        isDefault_ = useDefaults;
        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.BOTH;

        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        valuesArray_ = new ArrayList<RPnInputComponent>();

        gridConstraints.ipadx = 50;

        if (useDefaults) {
            for (int i = 0; i < physicsProfile_.getIndicesSize(); i++) {

                HashMap<String, String> param = physicsProfile_.getParam(i);
                Set<Entry<String, String>> paramSet = param.entrySet();

                for (Entry<String, String> paramEntry : paramSet) {

                    RPnInputComponent inputComponent = new RPnInputComponent(new Double(paramEntry.getValue()));
                    inputComponent.removeSlider();
                    inputComponent.setNumericFormat(RPnInputComponent.DOUBLE_FORMAT);
                    inputComponent.setLabel(paramEntry.getKey());
                    inputComponent.addPropertyChangeListener(this);
                    valuesArray_.add(i, inputComponent);
                    gridConstraints.gridx++;
                    this.add(inputComponent.getContainer(), gridConstraints);


                }
            }
        } else {

            gridConstraints.gridx = 1;
            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsProfile_.getName());
 
            for (int i = 0; i < physicsConfiguration.getParamsSize(); i++) {

                RPnInputComponent inputComponent = new RPnInputComponent(new Double(physicsConfiguration.getParam(i)));
                inputComponent.removeSlider();
                inputComponent.setNumericFormat(RPnInputComponent.DOUBLE_FORMAT);
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
         Configuration physicsConfiguration = RPNUMERICS.getConfiguration(physicsProfile_.getName());

         System.out.println ( physicsConfiguration.toString());

        FluxParams oldParams = RPNUMERICS.getFluxParams();
        for (int i = 0; i < valuesArray_.size(); i++) {
            newParamsVector.setElement(i, new Double((String)valuesArray_.get(i).getValue(RPnInputComponent.NUMERIC_VALUE)));
        }

        FluxParams newParams = new FluxParams(newParamsVector);
        RPNUMERICS.configFluxParams(newParams);
        rpn.usecase.ChangeFluxParamsAgent.instance().applyChange(new PropertyChangeEvent(rpn.usecase.ChangeFluxParamsAgent.instance(), "", oldParams, newParams));

    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (!isDefault_) {
            applyParams();
        }

    }
}
