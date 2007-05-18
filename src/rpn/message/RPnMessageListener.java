/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.jms.*;
import javax.swing.*;
import rpn.usecase.*;
import rpn.controller.ui.*;
import wave.util.*;
import java.awt.event.ActionEvent;

/**
 *
 * <p>This class contains the actions that will be done when a message arrives</p>
 */

public class RPnMessageListener implements MessageListener {

    /**
     * Method to select what action must to be done or response must to be send to manipulate the master status.
     *
     * @param type String The type of request .
     * @param toClient String Client to send the response, when necessary.
     */

    private void selectRequestType(String type, String toClient) {

        if (type.equals("Request")) {
            int flag = JOptionPane.showConfirmDialog(null, "Change master ?",
                    "Master changing",
                    JOptionPane.YES_NO_OPTION);

            if (flag == 0) {
                RPnActionMediator.instance().sendMasterRequestResponse("Yes",
                        toClient);
                UIController.instance().getNetStatusHandler().setAsMaster(false);

            } else {
                RPnActionMediator.instance().sendMasterRequestResponse("No",
                        toClient);

            }
        }

        if (type.equals("Yes")) {
            JOptionPane.showMessageDialog(null, "Master change accepted");
            UIController.instance().getNetStatusHandler().setAsMaster(true);
        }

        if (type.equals("No")) {
            JOptionPane.showMessageDialog(null, "Master change rejected");
        }

        if (type.equals("MCheck")) {

                //            System.out.println("Entrei em MCheck");
            Boolean isMaster = new Boolean(UIController.instance().
                                           getNetStatusHandler().isMaster());

            if (toClient.equals(UIController.instance().getNetStatusHandler().
                                getClientID())) {
                UIController.instance().getNetStatusHandler().setAsMaster(true);
//                System.out.println("Sou o master");
            } else {
//                System.out.println("Não sou o master");
                UIController.instance().getNetStatusHandler().setAsMaster(false);
            }
        }

        if (type.equals("noMaster")) {
            RPnNetworkStatusController.instance().actionPerformed(new
                    ActionEvent(this, 0, null));
        }

    }

    /**
     * Method to select a command to be executed.
     * @param desc String The string ID of the command
     * @return int A number that represents this command
     */

    private int selectCommand(String desc) {

        if (desc.equals("Forward Orbit")) {
            return 1;
        }

        if (desc.equals("Backward Orbit")) {
            return 2;
        }

        if (desc.equals("Poincare Section")) {
            return 3;
        }

        if (desc.equals("Backward Manifold")) {
            return 4;
        }

        if (desc.equals("Forward Manifold")) {
            return 5;
        }

        if (desc.equals("Stationary Point")) {
            return 6;
        }

        if (desc.equals("Find Saddle to Saddle Profile")) {
            return 7;
        }

        if (desc.equals("Change X-Zero")) {
            return 8;
        }

        if (desc.equals("Change Flow Speed")) {
            return 9;
        }

        if (desc.equals("Clears the Phase Space")) {
            return 10;
        }

        return 0;

    }

    /**
     * The default constructor
     */

    public RPnMessageListener() {

    }

    /**
     * Method called when a message arrives
     * @param message Message The message arrived
     */

    public void onMessage(Message message) {
        try {

            if (message instanceof TextMessage) {



                System.out.println(((TextMessage)message).getText());

                if (!message.getStringProperty("MRequest").equals("State")) {

                    selectRequestType(message.getStringProperty("MRequest"),
                                      message.getStringProperty("From"));
                } else {

                    int select = selectCommand(((TextMessage) message).getText());


                    switch (select) {

                    case 1:

                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                ForwardOrbitPlotAgent.instance()));

                        break;

                    case 2:
                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                BackwardOrbitPlotAgent.instance()));

                        break;

                    case 3:
                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                PoincareSectionPlotAgent.instance()));

                        break;

                    case 4:
                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                BackwardManifoldPlotAgent.instance()));

                        break;

                    case 5:
                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                ForwardManifoldPlotAgent.instance()));

                        break;

                    case 6:
                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                StationaryPointPlotAgent.instance()));

                        break;

                    case 7:

                        FindProfileAgent.instance().findProfile();
                        break;

                    case 8:

                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                ChangeXZeroAgent.instance()));

                        break;

                    case 9:

                        UIController.instance().setState(new UI_ACTION_SELECTED(
                                ChangeSigmaAgent.instance()));

                        break;

                    case 10:
                        ClearPhaseSpaceAgent.instance().clear();

                        break;

                    default:

                        System.out.println("No command");
                        break;

                    }

                }
            }

            if (message instanceof ObjectMessage) {

                ObjectMessage mensagemObj = (ObjectMessage) message;

                if (mensagemObj.getObject() instanceof RealVector) {

                    RealVector userInput = (RealVector) mensagemObj.getObject();
                    UIController.instance().getState().userInputComplete(
                            UIController.
                            instance(), userInput);

                }

            }

        } catch (JMSException e) {
            System.out.println("JMSException in onMessage(): " +
                               e.toString());

            e.printStackTrace();

        } catch (Throwable t) {
            System.out.println("Exception in onMessage():" +

                               t.getMessage());

            t.printStackTrace();

        }

    }

}
