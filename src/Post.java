package barray.fc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Post
 *
 * Implements the post functionality for this server.
 **/
public class Post implements Request{
  private static final int CMDS_MAX_LENGTH = Config.instance.getInt("CMDS_MAX_LENGTH");
  private static final byte[] INDICATOR = Config.instance.getString("INDICATOR").getBytes();
  private static final byte[] HEADER = Config.instance.getString("HEADER").getBytes();
  private static final byte[] FAILURE = Config.instance.getString("FAILURE").getBytes();

  private static int salt;

  private int user;
  private Get get;
  private File file;
  private String cmnt;

  /**
   * Post()
   *
   * Initially breaks apart the data in order for the process() function to do
   * it's job correctly.
   *
   * @param path The path of the web directory.
   * @param data The data to be analysed.
   * @param user The IP address hash of the client.
   **/
  public Post(File path, byte[] data, int user){
    this.user = user;
    /* Build get request to return file */
    get = new Get(path, data);
    /* Process header */
    String[] cmds = new String(data, 0, CMDS_MAX_LENGTH).split(" ");
    cmds[1] = cmds[1].replace("/", "");
    cmds[1] = cmds[1].split("\\?")[0];
    file = new File(path.getPath() + "/" + cmds[1]);
    /* Get comment */
    cmnt = null;
    for(int x = data.length - 1 - INDICATOR.length; x >= 0; x--){
      if(data[x] == '\n' || data[x] == '\r'){
        cmnt = new String(
          data,
          x + INDICATOR.length + 1,
          data.length - x - INDICATOR.length - 1
        ).trim();
        /* Make the comment look normal */
        cmnt = URLDecoder.decode(cmnt);
        cmnt = sanitizeString(cmnt);
        break;
      }
    }
    /* TODO: Apply filtering here. */
  }

  public byte[] process(){
    /* Check whether the file exists */
    if(!file.exists()){
      Server.error("Post", "failed to create new file `" + file.getPath() + "`");
      return FAILURE;
    }
    /* Check that the file is a valid comment file */
    if(!Hash.check(file.getName())){
      Server.error("Post", "invalid comments files `" + file.getPath() + "`");
      return FAILURE;
    }
    /* Append comment to file */
    try{
      FileWriter fw = new FileWriter(file, true);
      fw.write("\n<p><b>" + Hash.intToWord(user, salt, 8) + " says: </b>" + cmnt + "</p>");
      fw.flush();
      fw.close();
    }catch(IOException e){
      Server.error("Post", "failed to append comment to `" + file.getPath() + "`");
      return FAILURE;
    }
    /* Return the normal result of a GET request */
    return get.process();
  }

  /**
   * sanitizeString()
   *
   * Makes sure that the given String is web safe.
   *
   * @param str The String to be checked.
   **/
  private String sanitizeString(String str){
    return str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }

  /**
   * setSalt()
   *
   * Sets the salt for the username.
   *
   * @param salt The salted username.
   **/
  public static void setSalt(int salt){
    Post.salt = salt;
  }
}
