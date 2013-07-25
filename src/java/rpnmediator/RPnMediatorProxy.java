package rpnmediator;

/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

import java.util.Vector;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.net.*;
import javax.servlet.annotation.WebListener;
import javax.naming.*;
import javax.jms.*;



import rpn.message.*;


/**
 *
 * @author mvera
 */
@WebListener
public class RPnMediatorProxy extends HttpServlet  implements ServletContextListener {

    private String reqId_ = "";
    private String reqAddress_ = "";
    private String reqSession_ = "";
    private String reqInboundMsg_ = "";
    private String reqOutboundMsg_ = "";
    
    /*
     * Queues are java.util.Vector for simplicity. Concurrency ???
     */
    private Vector commandQueue_;

    /*
     * The listening component
     */
    private QueueReceiver receiver_;

    /*
     * Our JMS Queue Connection
     */
    private QueueConnection queueConnection_ = null;
    /*
     * a log for init issues...    
     */
    

//    static String PAGE_HEADER = "<html><head /><body>";
//    static String PAGE_FOOTER = "</body></html>";

    public static String WRONG_INPUT_ERROR_MSG = "WRONG INPUT PARAMETERS...";
    public static String RECEIVER_INITIALIZATION_ERROR_MSG = "RECEIVER NOT INITIALIZED...";
    public static String QUEUECONNECTION_CLOSE_ERROR_MSG = "WAS UNABLE TO CLOSE QUEUE CONNECTION...";

    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        

        if (!fillInputParameters(request))

            responseErrorMsg(response,WRONG_INPUT_ERROR_MSG);

        else {

            initJMSConnection(response);

            if (reqId_.compareTo(RPnNetworkStatus.RPN_MEDIATORPROXY_COMMAND_TAG) == 0) {

                SEND((String) request.getParameter("RPN_COMMAND_MSG"));
            }

            if (reqId_.compareTo(RPnNetworkStatus.RPN_MEDIATORPROXY_POLL_TAG) == 0) {



                //response.setContentType("text/xml");
                //response.setContentType("text/html");
                response.setContentType( "text/xml;charset=UTF-8" );
                PrintWriter writer = response.getWriter();

                //writer.println("<html>");
                //writer.println("<head>");
                //writer.println("<title>JMS Servlet</title>");
                //writer.println("</head>");
                //writer.println("<body bgcolor=white>");

                if (receiver_ == null) {
                    responseErrorMsg(response,RECEIVER_INITIALIZATION_ERROR_MSG);                    ;
                } else {
                    try {
                        Message msg = receiver_.receiveNoWait();
                        //Message msg = receiver_.receive((long)15000);
                        //Message msg = receiver_.receive();

                        if (msg != null) {
                            if (msg instanceof TextMessage) {

                                //writer.println("<h1>There are Messages on queue : " + RPnNetworkStatus.RPN_COMMAND_QUEUE_NAME_LOCAL + " </h1>");
                                String text = ((TextMessage) msg).getText();
                                
                                System.out.println(text.toString());
                                
                                writer.println(text.toString());

                            } else {
                                // DEBUG ONLY
                                //writer.println("not text message");
                            }
                        } else {
                            // DEBUG ONLY
                            //writer.println("no message on queue");
                        }

                        queueConnection_.close();

                    } catch (JMSException ex) {

                        responseErrorMsg(response,ex2str(ex));
                    }

                }               

                //writer.println("</body>");
                //writer.println("</html>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         doGet(request,response);
    }

    @Override
    public void init() throws ServletException {
 
    }

    protected void initJMS_CommandTopicConnection(HttpServletResponse response) {

        
    }
    protected void initJMS_SlaveReqQueueConnection(HttpServletResponse response) {

        commandQueue_ = new Vector();
        

        try {


            // starts listening
            System.out.println("Will now listen to " + RPnNetworkStatus.RPN_MEDIATORPROXY_COMMAND_TAG + '\n');

            //final Context context = RPnSender.getInitialMDBContext();
            final Context context = new InitialContext();
            //QueueConnectionFactory cf = (QueueConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
            QueueConnectionFactory cf = (QueueConnectionFactory) context.lookup("java:/ConnectionFactory");
            
            //javax.jms.Queue queue = (javax.jms.Queue) context.lookup(RPnNetworkStatus.RPN_COMMAND_QUEUE_NAME);
            javax.jms.Queue queue = (javax.jms.Queue) context.lookup(RPnNetworkStatus.RPN_SLAVE_REQ_QUEUE_NAME_LOCAL);

            queueConnection_ = cf.createQueueConnection("rpn","rpn.fluid");
            QueueSession queueSession = queueConnection_.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);


            receiver_ = queueSession.createReceiver(queue);

            queueConnection_.start();

        } catch (Exception exc) {
            
            responseErrorMsg(response,ex2str(exc));
            
            try {
                queueConnection_.close();    
            } catch (JMSException jmsex) {
                responseErrorMsg(response,ex2str(jmsex));
            }

        } 
    }   

    protected boolean fillInputParameters(HttpServletRequest request) {


        reqSession_ = request.getParameter(RPnNetworkStatus.RPN_MEDIATORPROXY_SESSION_ID_TAG);
        reqAddress_ = request.getParameter(RPnNetworkStatus.RPN_MEDIATORPROXY_CLIENT_ID_TAG);
        reqId_ = request.getParameter(RPnNetworkStatus.RPN_MEDIATORPROXY_REQ_ID_TAG);

        // TODO : REMOVE THIS
        reqSession_ = "";
        reqAddress_ = "";

        if ((reqSession_ == null) || (reqAddress_ == null) || (reqId_ == null)) {

            return false;
        }
            

        return true;

    }

    protected void responseErrorMsg(HttpServletResponse response,String errMessage) {

        

            response.setContentType("text/html");

            try {

                PrintWriter writer = response.getWriter();

                writer.println("<html>");
                writer.println("<head>");
                writer.println("<title>JMS Servlet</title>");
                writer.println("</head>");
                writer.println("<body bgcolor=white>");
              

                writer.println("<h1>Response Error... " + errMessage + "</h1>");

                writer.println("</body>");
                writer.println("</html>");

            } catch (IOException exc) {
                
                exc.printStackTrace();
            }
    }

    protected String ex2str(Throwable t)
    {

       try {

          ByteArrayOutputStream os = new ByteArrayOutputStream();
          PrintWriter pw = new PrintWriter(os);
          t.printStackTrace(pw);
          pw.flush();
          
          return new String(os.toByteArray());

       } catch (Throwable e) {
          return t.toString();
       }
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Do your job here during webapp startup.
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

/*        try {

            if (queueConnection_ != null)
             queueConnection_.close();


        } catch (JMSException ex) {

            ex.printStackTrace();
        }*/
    }

    public void REG(String clientID,String sessionID,Boolean isMaster) {

    }

    public void PUB(String cliendID,String topic,String msg) {

    }

    public void SUB(String clientID,String topic) {

    }

    public void SEND(String msg) {

    }

    public void RECEIVE(String clientID,String queue) {

    }

    public void UNREG(String clientID) {

    }
}
