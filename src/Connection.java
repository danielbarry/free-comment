package barray.fc;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Connection.java
 *
 * This class is responsible for accepting the connection, starting a new
 * thread and figuring out what kind of request we have been given.
 **/
public class Connection extends Thread{
  private static final int DEF_BUFFER_SIZE = 1024;
  private static final int DEF_CMDS_MAX_LENGTH = 256;

  private static File path;

  private Socket sock;
  private byte[] buff;
  private int read;
  private Request req;

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
    /* Initialise variable */
    buff = new byte[DEF_BUFFER_SIZE];
    read = -1;
    /* Pull data from stream */
    try{
      read = sock.getInputStream().read(buff);
    }catch(IOException e){
      /* Do nothing */
    }
    /* Was a full request? */
    if(read >= 0){
      /* Break down the request */
      String[] cmds = new String(buff, 0, DEF_CMDS_MAX_LENGTH).split(" ");
      /* Make sure we got some parameters */
      if(cmds.length >= 2){
        /* Work out if GET or POST */
        switch(cmds[0]){
          case "GET" :
            req = new Get(path, buff);
            break;
          case "POST" :
            req = new Post(path, buff, sock.getInetAddress().hashCode());
            break;
          default :
            Server.error("Connection", "bad server mode `" + cmds[0] + "`");
            break;
        }
        /* Return GET or POST errand */
        try{
          sock.getOutputStream().write(req.process());
        }catch(IOException e){
          Server.error("Connection", "unable to write to socket");
        }
      }else{
        Server.error("Connection", "invalid request format");
      }
    }else{
      Server.error("Connection", "failed read of size `" + read + "`");
    }
    /* Close the stream */
    try{
      sock.close();
    }catch(IOException e){
      Server.error(
        "Connection",
        "failed close on thread `" + Thread.currentThread().getId() + "`"
      );
    }
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
