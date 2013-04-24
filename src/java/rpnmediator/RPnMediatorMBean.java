package rpnmediator;

import javax.jms.*;

public interface RPnMediatorMBean {
  /**
   *
   * @param ID String
   */
  public void addClient(String ID);

  /**
   *
   * @param ID String
   */
  public void setAsMaster(String ID);

  /**
   *
   * @param ID String
   */
  public void removeClient(String ID);

  /**
   *
   * @param clientID String
   * @return Queue
   */
  public Queue receiveQueue(String clientID);

  /**
   * Method to obtain the queue to recive messages
   *
   * @return Queue
   */
  public Queue sendQueue();

  /**
   * Remove all messages of backup queue
   */
  public void clearBackupQueue();

  /**
   * Write in backup queue
   */
  public void writeBackupQueue(Message msg);

  /**
   *
   * Method to obtain the queue to send messages
   *
   * @return Queue
   */
//  public Queue backUpQueue();
  /**
   *
   * @param domain String
   */
  public void addDomain(String domain);

  /**
   *
   * @param domain String
   */
  public void removeDomain(String domain);

  /**
   *
   * @throws Exception
   */
  public void start() throws Exception;

  /**
   *
   * @throws Exception
   */
  public void stop() throws Exception;

}
