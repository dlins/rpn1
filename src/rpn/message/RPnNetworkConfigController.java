/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpn.message;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import rpn.controller.ui.UIController;

/**
 *
 * <p>Description: This class acts as a bridge between the classes that contains the network configurations parameters
 *  and the dialog that configures these parameters </p>
 *
 */
public class RPnNetworkConfigController extends AbstractAction{

private static RPnNetworkConfigController controller_=null;


  private RPnNetworkConfigController() {

  }
  /**
   *
   * @return RPnNetworkConfigController
   */

public static RPnNetworkConfigController instance(){

  if (controller_==null){
   controller_= new RPnNetworkConfigController();
    return controller_;
  }
  return controller_;

}

  public void actionPerformed(ActionEvent e) {


    Object source =e.getSource();

    if (source instanceof RPnNetworkConfigDialog){


        RPnNetworkStatus.SERVERNAME = RPnNetworkConfigDialog.serverTextBox.getText();

        RPnNetworkStatus.PORTNUMBER = (new Integer(RPnNetworkConfigDialog.portTextBox.getText())).intValue();


//        System.out.println(RPnNetworkStatus.SERVERNAME);
//        System.out.println(RPnNetworkStatus.PORTNUMBER);

        UIController.instance().getNetStatusHandler().init();

        firePropertyChange("Server Name","oldValue",RPnNetworkStatus.SERVERNAME);

        firePropertyChange("Port Number","oldValue",new Integer(RPnNetworkStatus.PORTNUMBER));

    }








  }





}
