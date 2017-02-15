package barray.fc;

import java.io.File;
import java.net.Socket;

/**
 * Connection.java
 *
 * This class is responsible for accepting the connection, starting a new
 * thread and figuring out what kind of request we have been given.
 **/
public class Connection extends Thread{
  private static File path;

  private Socket sock;

  /**
   * Connection()
   *
   * The constructor for the connection object. This method only has the
   * intention of creating the object and allocating space to it.
   **/
  public Connection(){
    /* Do nothing */
  }

  /**
   * setSocket()
   *
   * Stores the socket from the main thread.
   *
   * NOTE: We pay for every cycle in here on the main thread!
   *
   * @param sock The client socket from the main thread.
   **/
  public void setSocket(Socket sock){
    this.sock = sock;
  }

  /**
   * run()
   *
   * Runs this method on a new thread.
   **/
  public void run(){
    /* TODO: Write this section. */
  }

  /**
   * setPath()
   *
   * Set the web directory.
   *
   * @param path The path of the web directory.
   **/
  public static void setPath(File path){
    Connection.path = path;
  }
}
