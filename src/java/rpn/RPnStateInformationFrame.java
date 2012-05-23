/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import rpn.usecase.TrackPointAgent;
import rpnumerics.RPnStateInfo;


public class RPnStateInformationFrame extends JFrame  {



    private JPanel mainPanel_;
    private JTextArea infoArea_;
    private Document document_;

    public RPnStateInformationFrame(){
        super("State Information");


        mainPanel_= new JPanel();
        infoArea_ = new JTextArea();
        document_ = new PlainDocument();
        getContentPane().add(infoArea_);

        setPreferredSize(new Dimension (400,200));
        pack();

    }

    public void update() {
        RPnStateInfo info = TrackPointAgent.instance().getInfo();
        Iterator<Entry<String, String>> it = info.getInformation();
        try {
            document_.remove(0, document_.getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(RPnStateInformationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
            while (it.hasNext()) {

                Entry<String, String> entry = it.next();
            try {

                document_.insertString(0, entry.getValue(),null);
                document_.insertString(0, "-------" + entry.getKey() + "-------\n", null);

            } catch (BadLocationException ex) {
                Logger.getLogger(RPnStateInformationFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            }


        infoArea_.setDocument(document_);
    }

}
