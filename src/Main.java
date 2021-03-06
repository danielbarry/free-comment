package barray.fc;

/**
 * Main.java
 *
 * The main class that begins the program and handles the parameters from the
 * command line.
 **/
public class Main{
  private static final String DEF_INTERNAL_CONFIG = "/default.properties";
  private static final String DEF_EXTERNAL_CONFIG = "config.properties";

  private boolean serverStart;
  private String domn;
  private String path;
  private int port;
  private int salt;

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
    /* Load the configuration */
    new Config(DEF_INTERNAL_CONFIG, DEF_EXTERNAL_CONFIG);
    /* Setup the arguments */
    serverStart = Config.instance.getBoolean("SERVER_START");
    path = Config.instance.getString("PATH");
    domn = Config.instance.getString("DOMN");
    port = Config.instance.getInt("PORT");
    salt = Config.instance.getInt("SALT");
    /* Parse the parameters */
    for(int x = 0; x < args.length; x++){
      switch(args[x]){
        case "-a" :
        case "--addr" :
          x = addr(args, x);
          break;
        case "-d" :
        case "--domn" :
          x = domn(args, x);
          break;
        case "-h" :
        case "--help" :
          x = help(args, x);
          break;
        case "-p" :
        case "--path" :
          x = path(args, x);
          break;
        case "-s" :
        case "--salt" :
          x = salt(args, x);
          break;
        case "-v" :
        case "--vers" :
          x = vers(args, x);
          break;
        default :
          error("Main", "`" + args[x] + "`parameter not understood", true);
          break;
      }
    }
    /* Start the server */
    if(serverStart){
      (new Server(port, path, domn, salt)).start();
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
      error("Main", "`" + args[ofst] + "` is not valid port", true);
    }
    return ofst;
  }

  /**
   * domn()
   *
   * Set the domain for the server.
   *
   * @param args The arguments to be passed by this function.
   * @param ofst The offset in the arguments to start processing.
   * @return The offset to jump over the processed parameters.
   **/
  private int domn(String[] args, int ofst){
    domn = args[++ofst];
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
      "\n    -d  --domn  The server domain name" +
      "\n                  <STR> The domain name" +
      "\n    -h  --help  Display the help" +
      "\n    -p  --path  The data path" +
      "\n                  <STR> The data folder" +
      "\n    -s  --salt  Set the server salt" +
      "\n                  <INT> The server salt" +
      "\n    -v  --vers  Display the version" +
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
   * salt()
   *
   * Set the salt for the server.
   *
   * @param args The arguments to be passed by this function.
   * @param ofst The offset in the arguments to start processing.
   * @return The offset to jump over the processed parameters.
   **/
  private int salt(String[] args, int ofst){
    try{
      salt = Integer.parseInt(args[++ofst]);
    }catch(NumberFormatException e){
      error("Main", "`" + args[ofst] + "` is not valid salt", true);
    }
    return ofst;
  }

  /**
   * vers()
   *
   * Display version information for this program.
   *
   * @param args The arguments to be passed by this function.
   * @param ofst The offset in the arguments to start processing.
   * @return The offset to jump over the processed parameters.
   **/
  private int vers(String[] args, int ofst){
    serverStart = false;
    System.out.println(Config.instance.getString("VERS"));
    return ofst;
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
    /* TODO: Log event to file. */
  }

  /**
   * error()
   *
   * Display error message and stop program execution.
   *
   * @param cls The identifier to be displayed.
   * @param msg The message to be displayed.
   * @param exit True if the program should exit, otherwise false.
   **/
  public static void error(String cls, String msg, boolean exit){
    error(cls, msg);
    if(exit){
      System.exit(0);
    }
  }
}
