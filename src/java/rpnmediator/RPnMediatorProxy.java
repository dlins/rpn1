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
     * The listening component
     */
    private static RPnHttpSubscriber subscriber_ = null;
    private static RPnSubscriberThread commandSubscriberThread_ = null;

    
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

           

            if (reqId_.compareTo(RPnNetworkStatus.RPN_MEDIATORPROXY_COMMAND_TAG) == 0) {

                //SEND((String) request.getParameter("RPN_COMMAND_MSG"));
            }

            if (reqId_.compareTo(RPnNetworkStatus.RPN_MEDIATORPROXY_POLL_TAG) == 0) {



                //response.setContentType("text/xml");
                //response.setContentType("text/html");

                // this will enable the browser output...
                response.setContentType( "text/xml;charset=UTF-8" );

                PrintWriter writer = response.getWriter();

                //writer.println("<html>");
                //writer.println("<head>");
                //writer.println("<title>JMS Servlet</title>");
                //writer.println("</head>");
                //writer.println("<body bgcolor=white>");

        //        if (commandSubscriberThread_ == null) {
          //          responseErrorMsg(response,RECEIVER_INITIALIZATION_ERROR_MSG);                    ;
            //    } else {

                
                    while (!subscriber_.commandQueue_.isEmpty()) {

                        String command = (String)subscriber_.commandQueue_.remove(subscriber_.commandQueue_.size() - 1);

                        // for DEBUGING
                        System.out.println("Message received at RPnMediatorProxy : " + '\n' + command);
                                                
                        writer.println(command);

                        
                    }
                //}

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

        // SLAVE SUBS to RPN COMMAND TOPIC
        System.out.println("Will now listen to : " + RPnNetworkStatus.RPN_COMMAND_TOPIC_NAME);
        subscriber_ = new RPnHttpSubscriber(RPnNetworkStatus.trimLocalJmsPrefix(RPnNetworkStatus.RPN_COMMAND_TOPIC_NAME));


        if (commandSubscriberThread_ == null) {
            commandSubscriberThread_ = new RPnSubscriberThread(subscriber_);
        }

        commandSubscriberThread_.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

        if (commandSubscriberThread_ != null)
            commandSubscriberThread_.unsubscribe();
    }

    
}
