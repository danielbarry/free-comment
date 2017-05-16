package barray.fc;

import java.util.HashMap;

/**
 * Spam()
 *
 * Basic implementation for system spam detection against the commenting
 * system.
 **/
public class Spam{
  private static final int MAX_CONNECTION_BUFFER = Config.instance.getInt("MAX_CONNECTION_BUFFER");
  private static final int MIN_CONNECTION_WAIT = Config.instance.getInt("MIN_CONNECTION_WAIT");
  private static final long PUNISH_TIMEOUT = Config.instance.getLong("PUNISH_TIMEOUT");

  private static HashMap<Integer, Long> connections = new HashMap<Integer, Long>();

  /**
   * isSpamDetection()
   *
   * This method is supposed to detect the most basic of DDoS attacks from one
   * or many locations to prevent to many repeat interactions with the system.
   *
   * @param identifier The identifier for this particular client.
   * @param timestamp The time at which they connected in milliseconds.
   * @return Whether this is spam, true, or a normal system use-case, false.
   **/
  public static boolean isSpamConnect(int identifier, long timestamp){
    int id = identifier % MAX_CONNECTION_BUFFER;
    /* Check whether we are dealing with a recent connection or a new one */
    if(connections.get(id) != null){
      /* Test time since previous connection */
      if(timestamp - connections.get(id) < MIN_CONNECTION_WAIT){
        /* We have a spammer, punish them */
        connections.put(id, timestamp + PUNISH_TIMEOUT);
        return true;
      }
    }
    connections.put(id, timestamp);
    return false;
  }
}
