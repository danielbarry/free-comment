package barray.fc;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server.java
 *
 * This class is the main server, responsible for handling the main client
 * connection loop.
 **/
public class Server{
  private File path;
  private ServerSocket ss;

  /**
   * Server()
   *
   * Initialise the server, ready to be started.
   *
   * @param port The port to run the server on.
   * @param path The path to use to search for server data.
   **/
  public Server(int port, String path){
    this.path = new File(path);
    if(!validPort(port)){
      Main.error("Server", "`" + port + "` invalid port");
    }
    if(!validPath(this.path)){
      Main.error("Server", "`" + path + "` invalid folder");
    }
    try{
      ss = new ServerSocket(port);
    }catch(IOException e){
      Main.error("Server", "failed to bind to port `" + port + "`");
    }
    /* Statically set the path */
    Connection.setPath(this.path);
  }

  /**
   * start()
   *
   * Starts the listening loop for the client connections.
   **/
  public void start(){
    /* Outer safety loop */
    for(;;){
      /* Catch any exceptions that leak through */
      try{
        /* Prepare allocated space outside of the main loop */
        Connection conn;
        /* Accept incoming connections infinitely */
        for(;;){
          /* Pay for initialisation whilst waiting for new connection */
          conn = new Connection();
          /* Sit and wait for parameter */
          conn.setSocket(ss.accept());
          /* Kick the thread off instantly */
          conn.start();
        }
      }catch(Exception e){
        /* Warn ourselves that somebody was able to kill the inner loop */
        error("Server", "failure in main loop");
      }
    }
  }

  /**
   * validPort()
   *
   * Validates whether the port is correct.
   *
   * @param port The port to be validated.
   * @return If the port is valid, true, otherwise false.
   **/
  private boolean validPort(int port){
    /* Port number should be positive */
    if(port <= 0){
      return false;
    }
    return true;
  }

  /**
   * validPath()
   *
   * Validates whether the path is correct.
   *
   * @param path The path to be validated.
   * @return If the path is valid, true, otherwise false.
   **/
  private boolean validPath(File path){
    /* Check if the path exists */
    if(!path.exists()){
      /* Attempt creation and check whether that worked */
      if(!path.mkdirs()){
        return false;
      }
    }
    return true;
  }

  /**
   * error()
   *
   * Display error message.
   *
   * @param cls The identifier to be displayed.
   * @param msg The message to be displayed.
   **/
  public static void error(String cls, String msg){
    System.err.println("[ERR]::" + cls + " " + msg);
  }
}
