/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import java.io.*;
import java.util.*;

import javax.management.*;
import javax.naming.*;

import org.apache.log4j.*;
import org.apache.log4j.varia.*;

/**
 *
 *
 * <p>The class that stores and manages the network communications parameters </p>

 */


public class RPnNetworkStatus {

    private String clientID_;
    private MBeanServerConnection server_;
    private ObjectName nameBean_;
    private InitialContext ctx;
    private String[] params_;
    private String[] sig_;

    private boolean isMaster_;
    private boolean isOnline_;
    private boolean serverOnline_;

    private StringBuffer logBuffer_;

     public static int PORTNUMBER = 1099;

     public static String SERVERNAME = new String("gluck.fluid.impa.br");

    public RPnNetworkStatus(String clientID) {

        clientID_ = clientID;
        isOnline_ = false;
        isMaster_ = false;
        logBuffer_ = new StringBuffer();

    }

    /**
     *  Initalization method. This method is used to create a JMX server connection, define MBean names , etc
     *
     */

    public void init() {

        try {

            Hashtable env = new Hashtable(2);
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.jnp.interfaces.NamingContextFactory");

            BasicConfigurator.configure(new NullAppender()); // sending server log messages to null

            env.put(Context.PROVIDER_URL,
                    "jnp://" + RPnNetworkStatus.SERVERNAME + ":" +
                    RPnNetworkStatus.PORTNUMBER);

            ctx = new InitialContext(env);
            nameBean_ = new ObjectName(RPnActionMediator.SERVICE);

            server_ = (MBeanServerConnection) ctx.lookup(RPnActionMediator.
                    RMIADAPTOR);



            params_ = new String[1];
            params_[0] = clientID_;
            sig_ = new String[1];
            sig_[0] = "java.lang.String";
            serverOnline_ = true;
        }


        catch (javax.naming.CommunicationException ex) {
            logBuffer_.append(Calendar.getInstance().getTime()+" "+SERVERNAME+ " is not available\n");
            logBuffer_.append(Calendar.getInstance().getTime()+" "+ex.getMessage()+"\n");
            serverOnline_ = false;

        }
        catch (Exception ex) {
            logBuffer_.append(Calendar.getInstance().getTime()+ "Error in init de NetWorkStatus\n");
            logBuffer_.append(Calendar.getInstance().getTime()+" "+ex.getMessage() + "\n");
            System.out.println(ex);
        }

    }


    /**
     * Sets the client master status
     *
     *
     * @param isMaster boolean true to set the client as master , false to not set it
     */

    public void setAsMaster(boolean isMaster) {

        try {

            if (isMaster) {
                params_[0] = clientID_;
                sig_[0] = "java.lang.String";
                server_.invoke(nameBean_, "setAsMaster", params_, sig_);

            }

            isMaster_ = isMaster;

        } catch (Exception ex) {

            System.out.println("Error in setMaster");

            System.out.println(ex);
        }

    }


    /**
     * Sets the client connection status
     *
     * @param isOnline boolean true if the client is online, false if not
     */


    public void online(boolean isOnline) {

        try {
            params_[0] = clientID_;

            sig_[0] = "java.lang.String";

            if (isOnline == true) {

                server_.invoke(nameBean_, "addClient", params_, sig_);

            }
            if (isOnline == false) {

                server_.invoke(nameBean_, "removeClient", params_, sig_);

            }

            isOnline_ = isOnline;

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }



    public void addDomain(String domain) {

        try {
            params_[0] = domain;

            sig_[0] = "java.lang.String";

            server_.invoke(nameBean_, "addDomain", params_, sig_);
        } catch (IOException ex) {
            ex.printStackTrace();

        } catch (ReflectionException ex) {
            ex.printStackTrace();
        } catch (MBeanException ex) {
            ex.printStackTrace();
        } catch (InstanceNotFoundException ex) {

            ex.printStackTrace();
        }

    }

    public void removeDomain(String domain) {

           try {
               params_[0] = domain;

               sig_[0] = "java.lang.String";

               server_.invoke(nameBean_, "removeDomain", params_, sig_);
           } catch (IOException ex) {
               ex.printStackTrace();

           } catch (ReflectionException ex) {
               ex.printStackTrace();
           } catch (MBeanException ex) {
               ex.printStackTrace();
           } catch (InstanceNotFoundException ex) {

               ex.printStackTrace();
           }

    }


    /**
         * Returns the master status
         *
         *
         * @return boolean true if the client is the master , false if not
         */

        public boolean isMaster() { return isMaster_;}

        /**
         *
         * Returns the connection status
         *
         *
         * @return boolean true if the client is online, false if not
         */
        public boolean isOnline() { return isOnline_; }

        /**
         * Returns the server status
         *
         *
         * @return boolean true if the server to change messages is available, false if not
         */

        public boolean isServerOnline() { return serverOnline_;  }

        /**
         *
         * Retruns the clientID
         *
         *
         * @return String The clientID
         */

        public String getClientID() {  return clientID_;  }


        public ArrayList getDomains() {

            ArrayList returnedList = null;

            try {
                returnedList= (ArrayList) ctx.lookup("rpnDomains");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }


            return returnedList;

        }

    public String getLogMessages() {
        return logBuffer_.toString();
    }

    }
