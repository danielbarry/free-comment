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
   * @param ip The IP address of the client.
   **/
  public Post(File path, byte[] data, String ip){
    String[] cmds = new String(data, 0, DEF_CMDS_MAX_LENGTH).split(" ");
    file = new File(path.getPath() + cmds[1]);
  }

  public byte[] process(){
    /* TODO: Implement this section. */
    return "<h1>POST: I am alive!</h1>".getBytes();
  }

  /**
   * createCommentFile()
   *
   * Creates a comment file is possible, otherwise notifies the caller of the
   * failure.
   *
   * @return True upon success, otherwise false.
   **/
  public static boolean createCommentFile(File file){
    try{
      file.createNewFile();
    }catch(IOException e){
      Server.error("Post", "failed to create comments file `" + file.getPath() + "`");
      return false;
    }
    FileOutputStream fos = null;
    try{
      fos = new FileOutputStream(file);
    }catch(FileNotFoundException e){
      Server.error("Post", "failed to open file `" + file.getPath() + "`");
      return false;
    }
    try{
      fos.write(DEF_COMMENT_DATA);
    }catch(IOException e){
      Server.error("Post", "failed to write file `" + file.getPath() + "`");
      return false;
    }
    try{
      fos.close();
    }catch(IOException e){
      Server.error("Post", "failed to close file `" + file.getPath() + "`");
      return false;
    }
    return true;
  }
}
