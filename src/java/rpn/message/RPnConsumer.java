/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.naming.*;
import javax.jms.*;
import java.io.StringBufferInputStream;
import rpn.parser.*;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 *
 * @author mvera
 */
public class RPnConsumer implements RPnMessageListener {

    private boolean end_ = false;
    private QueueConnection queueConnection_ = null;
    private QueueReceiver receiver_ = null;
    private QueueConnectionFactory cf_ = null;
    private javax.jms.Queue queue_ = null;
    private String listeningName_;
    
    

    public RPnConsumer(String queueName,int ACK_MODEL) {

        listeningName_ = queueName;

        try {

            final Context context = RPnSender.getInitialMDBContext();

            cf_ = (QueueConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
            queue_ = (javax.jms.Queue) context.lookup(queueName);

            queueConnection_ = cf_.createQueueConnection("rpn", "rpn.fluid");
            QueueSession queueSession = queueConnection_.createQueueSession(false, ACK_MODEL);


            // this will keep the messages on the queue_...
            //QueueSession queueSession = queueConnection_.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

            receiver_ = queueSession.createReceiver(queue_);

            queueConnection_.start();

    /*MBeanServer mBeanServer  = java.lang.management.ManagementFactory.getPlatformMBeanServer();
    ObjectName on = ObjectNameBuilder.DEFAULT.getJMSServerObjectName();
    MBeanInfo mbi = mBeanServer.getMBeanInfo(on);
    System.out.println(mbi.getClassName());
    MBeanAttributeInfo[] mbas = mbi.getAttributes();
    for (MBeanAttributeInfo mba : mbas)
    {
      System.out.println("attr: " + mba.getName() + " of type " + mba.getType());
    }

    MBeanOperationInfo[] mbos = mbi.getOperations();
    for (MBeanOperationInfo mbo : mbos)
    {
     System.out.println("oper: " + mbo.getName() );
     MBeanParameterInfo[] mbps = mbo.getSignature();
     for (MBeanParameterInfo mbp : mbps)
     {
       System.out.println("  param: " + mbp.getName());
     }
     System.out.println("   returns: " + mbo.getReturnType());
    }

//get attributes on the JMSServerControl

String[] qnames = (String[]) mBeanServer.getAttribute(on, "QueueNames");

//invoke methods on the JMSServerControl
mBeanServer.invoke(on, "createQueue" ...)

            JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(JMX_URL), new HashMap());

            MBeanServerConnection mbsc = connector.getMBeanServerConnection();

            ObjectName name=new ObjectName("org.jboss.messaging:module=JMS,type=Server");
            JMSServerControlMBean control = (JMSServerControlMBean)MBeanServerInvocationHandler.newProxyInstance(mbsc,name,JMSServerControlMBean.class,false);
            control.createQueue("TestQ","test");
    */
        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void startsListening() {
           
        try {

     

            while (!end_) {

                RPnNetworkDialog.infoText.append("Will now listen to rpn command queue..." + '\n');

                //Message message = receiver_.receive((long)15000);
                Message message = consume();
                

                if (message instanceof TextMessage) {

                    RPnNetworkDialog.infoText.append("Message recieved from rpn command queue..." + '\n');
                    
                    String text = ((TextMessage) message).getText();
                    parseMessageText(text);

                } 
            }

        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void stopsListening() {

        end_ = false;

        if (queueConnection_ != null) {
            try {
                queueConnection_.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }       
    }

    public Message consume() {

        try {
                RPnNetworkDialog.infoText.append("Will now consume from queue..." + '\n');
                return receiver_.receiveNoWait();

        } catch (Exception exc) {

            exc.printStackTrace();
            return null;

        }
    }

    public void parseMessageText(String text) {

        try {
            /*
             * checks if CONTROL MSG or COMMAND MSG
             */


            // CONTROL MESSAGES PARSING
            if (text.startsWith(RPnNetworkStatus.MASTER_ACK_LOG_MSG)) {



            } else if (text.startsWith(RPnNetworkStatus.MASTER_REQUEST_LOG_MSG)) {

                if (RPnNetworkStatus.instance().isMaster()) {

                    RPnMasterReqDialog reqDialog = new RPnMasterReqDialog(RPnNetworkStatus.filterClientID(text));
                    reqDialog.setVisible(true);

                }

            } else if (text.startsWith(RPnNetworkStatus.SLAVE_REQ_LOG_MSG)) {

                if (RPnNetworkStatus.instance().isMaster()) {

                    RPnSlaveReqDialog reqDialog = new RPnSlaveReqDialog(RPnNetworkStatus.filterClientID(text));
                    reqDialog.setVisible(true);

                }

            } else {

                // COMMAND MESSAGES PARSING
                RPnCommandModule.init(XMLReaderFactory.createXMLReader(), new StringBufferInputStream(text));

                // updates the PhaseSpace Frames...
                rpn.RPnPhaseSpaceFrame[] framesList = rpn.RPnUIFrame.getPhaseSpaceFrames();

                for (int i = 0; i < framesList.length; i++) {
                    framesList[i].invalidate();
                    framesList[i].repaint();
                }
            }

        } catch (Exception exc) {

            exc.printStackTrace();

        }
    }

    public String listeningName() {
        return listeningName_;
    }
}
