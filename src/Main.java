package barray.fc;

/**
 * Main.java
 *
 * The main class that begins the program and handles the parameters from the
 * command line.
 **/
public class Main{
  private static final boolean DEF_SERVER_START = true;
  private static final String DEF_PATH = "dat/";
  private static final int DEF_PORT = 80;

  private boolean serverStart;
  private String path;
  private int port;

  /**
   * main()
   *
   * Gets the program into an instance context and passes the arguments to that
   * instance.
   *
   * @param args The arguments to the program.
   **/
  public static void main(String[] args){
    new Main(args);
  }

  /**
   * Main()
   *
   * This constructor is responsible for passing the parameters and creating
   * the relevant settings to initialise the server.
   *
   * @param args The arguments to be parsed.
   **/
  public Main(String[] args){
    /* Setup the arguments */
    serverStart = DEF_SERVER_START;
    path = DEF_PATH;
    port = DEF_PORT;
    /* Parse the parameters */
    for(int x = 0; x < args.length; x++){
      switch(args[x]){
        case "-a" :
        case "--addr" :
          x = addr(args, x);
          break;
        case "-h" :
        case "--help" :
          x = help(args, x);
          break;
        case "-p" :
        case "--path" :
          x = path(args, x);
          break;
        default :
          error("Main", "`" + args[x] + "`parameter not understood");
          break;
      }
    }
    /* Start the server */
    if(serverStart){
      (new Server(port, path)).start();
    }
  }

  /**
   * addr()
   *
   * Set the port for the server.
   *
   * @param args The arguments to be passed by this function.
   * @param ofst The offset in the arguments to start processing.
   * @return The offset to jump over the processed parameters.
   **/
  private int addr(String[] args, int ofst){
    try{
      port = Integer.parseInt(args[++ofst]);
    }catch(NumberFormatException e){
      error("Main", "`" + args[ofst] + "` is not valid port");
    }
    return ofst;
  }

  /**
   * help()
   *
   * Display help for this program.
   *
   * @param args The arguments to be passed by this function.
   * @param ofst The offset in the arguments to start processing.
   * @return The offset to jump over the processed parameters.
   **/
  private int help(String[] args, int ofst){
    serverStart = false;
    System.out.println(
      "\nfc.jar [OPT]" +
      "\n" +
      "\n  OPTions" +
      "\n" +
      "\n    -a  --addr  Set the server port" +
      "\n                  <INT> The server port" +
      "\n    -h  --help  Display the help" +
      "\n    -p  --path  The data path" +
      "\n                  <STR> The data folder" +
      "\n"
    );
    return ofst;
  }

  /**
   * path()
   *
   * Set the path for the server data.
   *
   * @param args The arguments to be passed by this function.
   * @param ofst The offset in the arguments to start processing.
   * @return The offset to jump over the processed parameters.
   **/
  private int path(String[] args, int ofst){
    path = args[++ofst];
    return ofst;
  }

  /**
   * error()
   *
   * Display error message and stop program execution.
   *
   * @param cls The identifier to be displayed.
   * @param msg The message to be displayed.
   **/
  public static void error(String cls, String msg){
    System.out.println("[ERR]::" + cls + " " + msg);
    System.exit(0);
  }
}
