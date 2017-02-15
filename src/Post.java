package barray.fc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Post
 *
 * Implements the post functionality for this server.
 **/
public class Post implements Request{
  private static final int DEF_CMDS_MAX_LENGTH = 256;
  private static final byte[] DEF_INDICATOR = "cmnt=".getBytes();
  private static final byte[] DEF_HEADER =
    "HTTP/1.1 200 OK\n\rContent-Type: text/html\n\r\n\r".getBytes();
  private static final byte[] DEF_FAILURE = "Error: Failed to post".getBytes();

  private String ip;
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
   * @param ip The IP address of the client.
   **/
  public Post(File path, byte[] data, String ip){
    this.ip = ip;
    /* Build get request to return file */
    get = new Get(path, data);
    /* Process header */
    String[] cmds = new String(data, 0, DEF_CMDS_MAX_LENGTH).split(" ");
    cmds[1] = cmds[1].replace("/", "");
    cmds[1] = cmds[1].split("\\?")[0];
    file = new File(path.getPath() + "/" + cmds[1]);
    /* Get comment */
    cmnt = null;
    for(int x = data.length - 1 - DEF_INDICATOR.length; x >= 0; x--){
      if(data[x] == '\n' || data[x] == '\r'){
        cmnt = new String(
          data,
          x + DEF_INDICATOR.length + 1,
          data.length - x - DEF_INDICATOR.length - 1
        ).trim();
        break;
      }
    }
    /* TODO: Apply filtering here. */
  }

  public byte[] process(){
    /* Check whether the file exists */
    if(!file.exists()){
      Server.error("Post", "failed to create new file `" + file.getPath() + "`");
      return DEF_FAILURE;
    }
    /* Check that the file is a valid comment file */
    if(!Hash.check(file.getName())){
      Server.error("Post", "invalid comments files `" + file.getPath() + "`");
      return DEF_FAILURE;
    }
    /* Append comment to file */
    try{
      FileWriter fw = new FileWriter(file, true);
      fw.write("\n<p><b>" + ip + " says: </b>" + cmnt + "</p>");
      fw.flush();
      fw.close();
    }catch(IOException e){
      Server.error("Post", "failed to append comment to `" + file.getPath() + "`");
      return DEF_FAILURE;
    }
    /* Return the normal result of a GET request */
    return get.process();
  }
}
