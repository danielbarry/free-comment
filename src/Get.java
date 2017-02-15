package barray.fc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Get.java
 *
 * Implements the get functionality for this server.
 **/
public class Get implements Request{
  private static final int DEF_CMDS_MAX_LENGTH = 256;
  private static final byte[] DEF_FAILURE = "Error: 404".getBytes();
  private static final byte[] DEF_HEADER =
    "HTTP/1.1 200 OK\n\rContent-Type: text/html\n\r\n\r".getBytes();

  private static String domain;

  private File file;

  /**
   * Get()
   *
   * Initially breaks apart the data in order for the process() function to do
   * it's job correctly.
   *
   * @param path The path of the web directory.
   * @param data The data to be analysed.
   **/
  public Get(File path, byte[] data){
    String[] cmds = new String(data, 0, DEF_CMDS_MAX_LENGTH).split(" ");
    file = new File(path.getPath() + cmds[1]);
  }

  public byte[] process(){
    /* Check whether the file exists */
    if(file.exists() && file.isFile()){
      FileInputStream fis = null;
      try{
        fis = new FileInputStream(file);
      }catch(FileNotFoundException e){
        Server.error("Get", "failure to find `" + file.getPath() + "`");
        return DEF_FAILURE;
      }
      byte[] buff = new byte[(int)(DEF_HEADER.length + file.length())];
      try{
        fis.read(buff, DEF_HEADER.length, (int)(file.length()));
      }catch(IOException e){
        Server.error("Get", "could not read `" + file.getPath() + "`");
        return DEF_FAILURE;
      }
      System.arraycopy(DEF_HEADER, 0, buff, 0, DEF_HEADER.length);
      return buff;
    }else{
      Server.error("Get", "invalid file request `" + file.getPath() + "`");
      return DEF_FAILURE;
    }
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
}
