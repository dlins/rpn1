/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.jms.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import rpn.controller.ui.UIController;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 *
 *

 * <p>The main class of network communication. </p>

 */

public class RPnActionMediator {

    private Hashtable env;
    private Context ctx;
    private QueueConnectionFactory connFactory;
    private static QueueConnection conn;
    private QueueSession session;
    private Queue qReceive_;
    private Queue qSend_;
    private QueueReceiver receiver;
    private RPnMessageListener receiverListener;
    private QueueSender sender;
    private String clientID_;
    private static RPnActionMediator instance_;

    private static RPnNetworkStatus netStatus_;

    protected static String RMIADAPTOR = new String("jmx/invoker/RMIAdaptor");
    protected static String SERVICE = new String(
            "rpnmediator:service=RPNMEDIATOR");


    private RPnActionMediator() {

        try {
            env = new Hashtable(2);

            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.jnp.interfaces.NamingContextFactory");
            env.put(Context.PROVIDER_URL,
                    "jnp://" + RPnNetworkStatus.SERVERNAME + ":" +
                    RPnNetworkStatus.PORTNUMBER);
            ctx = new InitialContext(env);
            connFactory = (QueueConnectionFactory) ctx.lookup(
                    "ConnectionFactory");
            conn = connFactory.createQueueConnection();
           
            
            
            
            
            session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            clientID_ = UIController.instance().getNetStatusHandler().
                        getClientID();
            MBeanServerConnection server = (MBeanServerConnection) ctx.lookup(
                    RPnActionMediator.
                    RMIADAPTOR);
            ObjectName nameBean = new ObjectName(RPnActionMediator.SERVICE);
            String[] params = new String[1];
            String[] sig = new String[1];

            params[0] = clientID_;
            sig[0] = "java.lang.String";

            qReceive_ = (Queue) server.invoke(nameBean, "receiveQueue", params,
                                              sig);
            qSend_ = (Queue) server.invoke(nameBean, "sendQueue", null, null);

            receiver = session.createReceiver(qReceive_);
            receiverListener = new RPnMessageListener();
            receiver.setMessageListener(receiverListener);
            sender = session.createSender(qSend_);
            conn.start();

        }

        catch (javax.naming.NamingException e) {
            e.printStackTrace();
        }

        catch (javax.jms.JMSException ex) {
            System.out.println(ex);

        } catch (Exception t) {

            t.printStackTrace();
        }

    }


    private static void updateConnection() {

        try {
            conn.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        instance_ = new RPnActionMediator();

    }


    public void propertyChange(PropertyChangeEvent evt) {

        if ((evt.getPropertyName().equals("Master Offline"))) {

            JOptionPane.showMessageDialog(null, "Master Offline");
        }

    }


    /**
     *
     *
     * Method to request the master status
     *
     */

    public void sendMasterRequest() {

        try {
            TextMessage message = session.createTextMessage();
            message.setText(clientID_);
            message.setStringProperty("MRequest", "Request");
            message.setStringProperty("From", clientID_);
            sender.send(message);
        } catch (javax.jms.JMSException ex) {
            System.out.println("Error in sendMasterRequest");
            System.out.println(ex);
        }

    }

    /**
     * Method to verify if exists a master
     */
    public void sendMasterCheck() {

        try {
            TextMessage tmessage = session.createTextMessage();
            tmessage.setStringProperty("MRequest", "MCheck");
            tmessage.setStringProperty("From", clientID_);

            tmessage.setText(clientID_);

            sender.send(tmessage);

        } catch (JMSException ex) {

            System.out.println("Error in master check");
        }

    }

    /**
     * Method to notify all clients that the master is offline
     *
     */

    public void sendMasterOffline() {

        UIController.instance().getNetStatusHandler().setAsMaster(false);
        RPnNetworkStatusController.instance().actionPerformed(new
                ActionEvent(this, 0, "Master Offline"));

    }

    /**
     * Method to send the response of a master request
     *
     * @param resp String The response , Yes or No
     * @param toClient String The client that will recive the response
     */

    public void sendMasterRequestResponse(String resp, String toClient) {

        try {
            TextMessage tmessage = session.createTextMessage();
            tmessage.setStringProperty("MRequest", resp);
            tmessage.setStringProperty("To", toClient);
            sender.send(tmessage);
        } catch (Exception t) {
            System.out.println("Error in sendMasterRequestResponse" +
                               t.getMessage());
            t.printStackTrace();
        }

    }

    /**
     *
     * Method to send a text message
     *
     * @param msg String The text to put in the message
     */

    public void setState(String state) {
        if (UIController.instance().getNetStatusHandler().isOnline() &&
            UIController.instance().getNetStatusHandler().isMaster()) {
            try {
                String newDesc = state.replace(' ', '_');
                String xmlTag = "<" + newDesc + " type=\"state\"" + "></" +
                                newDesc + ">"; //Creating XML tag of Clear Phase Space
                TextMessage message = session.createTextMessage();
                message.setText(xmlTag);
                message.setStringProperty("From", clientID_);
                message.setStringProperty("MRequest", "State");
                sender.send(message);

//                RPnMessageThread mt = new RPnMessageThread(sender, message);
//                mt.setPriority(Thread.MAX_PRIORITY);
//                mt.start();

            }

            catch (javax.jms.JMSException ex) {
                ex.printStackTrace();
                System.out.println(ex);
            }

        }

    }

    /**
     * Method to send a message object
     *
     *
     * @param obj Serializable The object to put in the message
     */

    public void sendMessage(Serializable obj) {

        if (UIController.instance().getNetStatusHandler().isOnline() &&
            UIController.instance().getNetStatusHandler().isMaster()) {

            try {

                ObjectMessage message = session.createObjectMessage(obj);

                message.setObject(obj);

                message.setStringProperty("From", clientID_);

                RPnMessageThread mt = new RPnMessageThread(sender, message);
                mt.setPriority(Thread.MAX_PRIORITY);
                mt.start();

            } catch (javax.jms.JMSException ex) {
                ex.printStackTrace();
            }

        }

    }

    protected void finalize() {

        try {

            if (conn != null) {

                conn.close();

            }
            if (session != null) {

                session.close();
            }
        }

        catch (Exception e) {

            System.out.println("Error in finalize");
            e.printStackTrace();

        }

    }

    /**
     * Returns a instance of RPnActionMediator
     *
     * @return RPnActionMediator the instance returned
     */



    public static RPnActionMediator instance() {

        if (instance_ == null) {

            instance_ = new RPnActionMediator();
            return instance_;

        }

        else {

            return instance_;
        }

    }


}
