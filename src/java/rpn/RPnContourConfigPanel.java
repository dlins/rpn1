/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import rpnumerics.ContourConfiguration;
import rpnumerics.RPNUMERICS;

public class RPnContourConfigPanel extends JPanel {

    private JPanel paramsPanel_ = new JPanel();
    private JTextField [] textFieldsArray_;

    public RPnContourConfigPanel() {
     
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        GridLayout gridLayout = new GridLayout(3,3,10,10);

        paramsPanel_.setLayout(gridLayout);

        ContourConfiguration contourConfiguration= RPNUMERICS.getContourConfiguration();

        HashMap<String, String> paramsMap = contourConfiguration.getParams();

        textFieldsArray_ = new JTextField[paramsMap.size()];

        Set<Entry<String, String> > paramsSet = paramsMap.entrySet();

        Iterator <Entry<String,String>> paramsIterator=paramsSet.iterator();
        int i = 0;
        while (paramsIterator.hasNext()){

            Entry<String,String> paramsEntry= paramsIterator.next();
            JLabel label = new JLabel(paramsEntry.getKey());
            JTextField textField = new JTextField(paramsEntry.getValue());
            textFieldsArray_[i]=textField;
            textField.setName(paramsEntry.getKey());
            paramsPanel_.add(label);
            paramsPanel_.add(textField);
            i++;
        }

        this.add(paramsPanel_, BorderLayout.CENTER);


    }


    public void apply() {

        for (int i = 0; i < textFieldsArray_.length; i++) {
            RPNUMERICS.getContourConfiguration().setParamValue(textFieldsArray_[i].getName(), textFieldsArray_[i].getText());
        }


    }
}
