package barray.fc;

/**
 * Request.java
 *
 * An interface that defines how any supported command for the server should
 * work.
 **/
public interface Request{
  /**
   * process()
   *
   * This method is responsible for processing the input data and returning a
   * byte array that can be sent to the client.
   *
   * @return The byte array to be sent back to the client.
   **/
  public byte[] process();
}
