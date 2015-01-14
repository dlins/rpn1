/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.ui;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import rpn.configuration.ChooseParameter;
import rpn.configuration.Configuration;
import rpn.configuration.Parameter;
import rpn.configuration.ParameterComposite;
import rpn.configuration.ParameterLeaf;
import rpn.configuration.RangedParameter;
import rpn.configuration.TextParameter;

/**
 *
 * @author edsonlan
 */
public class ConfigurationView extends Observable implements Observer {

    private JPanel panel_;
    private Configuration config_;

    private HashMap<String, ParameterView> componentMap_;

    public ConfigurationView(Configuration config) {
        this.config_ = config;
        panel_ = new JPanel();

        config.addObserver(this);
        componentMap_ = new HashMap<String, ParameterView>();

        java.util.List<Parameter> parameterList = config.getParameterList();

        for (Parameter parameter : parameterList) {

            if (!parameter.getName().equals("resolution")) {

                if (parameter.isVisible()) {
                    if ((parameter instanceof ParameterLeaf) || (parameter instanceof ParameterComposite)) {//Combo box

                        ParameterView view = new ComboParameterView(parameter);

                        view.addObserver(this);

                        addParameterView(parameter.getName(), view);

                        for (int i = 0; i < parameter.getAssociatedParameterSize(); i++) {

                            ParameterView associatedView = new ComboParameterView(parameter.getAssociatedParameter(i));

                            associatedView.addObserver(this);

                            addParameterView(parameter.getName(), associatedView);

                        }

                    }

                    if (parameter instanceof RangedParameter) {//SpinButton

                        RangedParameterView rangedView = new RangedParameterView(parameter);

                        addParameterView(parameter.getName(), rangedView);

                    }

                    if (parameter instanceof ChooseParameter) {//Radio Group
                        ChooseParameterView chooseView = new ChooseParameterView(parameter);
                        addParameterView(parameter.getName(), chooseView);
                    }

                    if (parameter instanceof TextParameter) { //TextField

                        TextParameterView textParameterView = new TextParameterView(parameter);

                        addParameterView(parameter.getName(), textParameterView);

                    }

                }

            }
        }

    }

    public final void addParameterView(String parameterName, ParameterView parameterView) {

        if (!componentMap_.containsKey(parameterName)) {
            componentMap_.put(parameterName, parameterView);

            JPanel panel = new JPanel();
            parameterView.decorate(panel);
            panel.add(parameterView.getComponent());
            panel_.add(panel);

        }

    }

    public int getParameterViewNumber() {
        return componentMap_.size();
    }

    public JPanel getContainer() {
        return panel_;
    }

    public Configuration getConfiguration() {
        return config_;
    }

    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof Configuration) {
            Configuration config = (Configuration) o;
            for (Parameter parameter : config.getParamList()) {
                
                if (componentMap_.containsKey(parameter.getName())){
                    componentMap_.get(parameter.getName()).changeView(parameter.getValue());
                }
                
                
            }
            
          

        }

        if (o instanceof ParameterView) {

            ParameterView paramView = (ParameterView) o;

            Parameter parameter = paramView.getParameter();

            Integer optionIndex = (Integer) arg;

            ParameterView parameterView = componentMap_.get(parameter.getAssociatedParameter(optionIndex).getName());
            parameterView.associatedChanged(parameter.getAssociatedParameter(optionIndex), optionIndex);

        }

    }

}
