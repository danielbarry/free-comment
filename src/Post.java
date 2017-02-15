package barray.fc;

import java.io.File;

/**
 * Post
 *
 * Implements the post functionality for this server.
 **/
public class Post implements Request{
  private static final int DEF_CMDS_MAX_LENGTH = 256;

  private File file;

  /**
   * Post()
   *
   * Initially breaks apart the data in order for the process() function to do
   * it's job correctly.
   *
   * @param path The path of the web directory.
   * @param data The data to be analysed.
   **/
  public Post(File path, byte[] data){
    String[] cmds = new String(data, 0, DEF_CMDS_MAX_LENGTH).split(" ");
    file = new File(path.getPath() + cmds[1]);
  }

  public byte[] process(){
    /* TODO: Implement this section. */
    return "<h1>POST: I am alive!</h1>".getBytes();
  }
}
