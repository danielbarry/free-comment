package barray.fc;

import java.net.Socket;

/**
 * Connection.java
 *
 * This class is responsible for accepting the connection, starting a new
 * thread and figuring out what kind of request we have been given.
 **/
public class Connection extends Thread{
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
}
