package barray.fc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Get.java
 *
 * Implements the get functionality for this server.
 **/
public class Get implements Request{
  private static final int CMDS_MAX_LENGTH = Config.instance.getInt("CMDS_MAX_LENGTH");
  private static final byte[] HEADER = Config.instance.getFile("HEADER");
  private static final int MAX_SEARCH = Config.instance.getInt("MAX_SEARCH");
  private static final String NEW_LINK = Config.instance.getString("NEW_LINK");
  private static final byte[] THREAD = Config.instance.getFile("THREAD");
  private static final byte[] CSS = Config.instance.getFile("CSS");
  private static final String ERROR = new String(HEADER) +
    new String(Config.instance.getFile("ERROR"));

  private static String domain;
  private static int port;

  private int user;
  private File file;

  /**
   * Get()
   *
   * Initially breaks apart the data in order for the process() function to do
   * it's job correctly.
   *
   * @param path The path of the web directory.
   * @param data The data to be analysed.
   * @param user The IP address hash of the client.
   **/
  public Get(File path, byte[] data, int user){
    this.user = user;
    String[] cmds = new String(data, 0, CMDS_MAX_LENGTH).split(" ");
    cmds[1] = cmds[1].replace("/", "");
    cmds[1] = cmds[1].split("\\?")[0];
    file = new File(path.getPath() + "/" + cmds[1]);
  }

  public byte[] process(){
    /* TODO: Should be performing admin checking here. */
    /* Check whether we have a special case */
    if(file.getName().equals("generate")){
      /* Keep checking until we find a unique name */
      for(int x = 0; x < MAX_SEARCH; x++){
        String newFile = Hash.generate();
        File newCheck = new File(
          file.getParentFile().getPath() + "/" + newFile
        );
        /* If the file doesn't exist, use it */
        if(!newCheck.exists()){
          createCommentFile(newCheck);
          /* Return a link */
          return (
            NEW_LINK + domain + ":" + port + "/" + newFile
          ).getBytes();
        }
      }
      /* If we got here we failed */
      return ERROR.replace("$f", "Unable to generate new thread").getBytes();
    }
    /* Check whether we have a special case */
    if(file.getName().startsWith("alias")){
      return Hash.intToSVG(
        file.getName().substring("alias".length()).hashCode()
      );
    }
    /* Check whether we have a special case */
    if(file.getName().equals("style.css")){
      byte[] buff = new byte[(int)(HEADER.length + CSS.length)];
      System.arraycopy(HEADER, 0, buff, 0, HEADER.length);
      System.arraycopy(CSS, 0, buff, HEADER.length, CSS.length);
      /* Return CSS file */
      return buff;
    }
    /* Check whether the file exists */
    if(file.exists() && file.isFile()){
      FileInputStream fis = null;
      try{
        fis = new FileInputStream(file);
      }catch(FileNotFoundException e){
        Server.error("Get", "failure to find `" + file.getPath() + "`");
        return ERROR.replace("$f", "Unable to access thread").getBytes();
      }
      byte[] buff = new byte[(int)(HEADER.length + THREAD.length + file.length())];
      try{
        fis.read(buff, HEADER.length + THREAD.length, (int)(file.length()));
      }catch(IOException e){
        Server.error("Get", "could not read `" + file.getPath() + "`");
        return ERROR.replace("$f", "Unable to read thread").getBytes();
      }
      System.arraycopy(HEADER, 0, buff, 0, HEADER.length);
      System.arraycopy(THREAD, 0, buff, HEADER.length, THREAD.length);
      return buff;
    }else{
      Server.error("Get", "invalid file request `" + file.getPath() + "`");
      return ERROR.replace("$f", "Unable to find thread").getBytes();
    }
  }

  /**
   * createCommentFile()
   *
   * Creates a comment file is possible, otherwise notifies the caller of the
   * failure.
   *
   * @return True upon success, otherwise false.
   **/
  public boolean createCommentFile(File file){
    try{
      file.createNewFile();
    }catch(IOException e){
      Server.error("Post", "failed to create comments file `" + file.getPath() + "`");
      return false;
    }
    return true;
  }

  /**
   * setDomain()
   *
   * Set the domain name of the server.
   *
   * @param domain The domain name of the server.
   **/
  public static void setDomain(String domain){
    Get.domain = domain;
  }

  /**
   * setPort()
   *
   * Set the port of the server.
   *
   * @param port The port of the server.
   **/
  public static void setPort(int port){
    Get.port = port;
  }
}
