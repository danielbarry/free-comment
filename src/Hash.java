package barray.fc;

import java.security.SecureRandom;

/**
 * Hash.java
 *
 * A simple class allowing hashes to be generated and checked for the filenames
 * of the comments.
 **/
public class Hash{
  private static final int DEF_CRYPTO_LENGTH = 32;

  /**
   * check()
   *
   * Checks whether a hash is correct.
   *
   * @param hash The hash to be checked.
   * @return True if a proper hash, otherwise false.
   **/
  public static boolean check(String hash){
    /* Check length */
    if(hash.length() != DEF_CRYPTO_LENGTH * 2){
      return false;
    }
    /* Check used characters */
    for(int x = 0; x < hash.length(); x++){
      char c = hash.charAt(x);
      if((c < 'a' || c > 'z') && (c < '0' || c > '9')){
        return false;
      }
    }
    return true;
  }

  /**
   * generate()
   *
   * Generates a new cryptographically random hash.
   *
   * @return A string containing the hash.
   **/
  public static String generate(){
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[DEF_CRYPTO_LENGTH];
    random.nextBytes(bytes);
    return hashByteArrayToString(bytes);
  }

  /**
   * hashByteArrayToString()
   *
   * Converts a hash byte array to a printable String.
   *
   * @param array The array to be converted.
   * @return The String representation of the array.
   **/
  public static String hashByteArrayToString(byte[] array){
    String buff = "";
    for(int i = 0; i < array.length; i++){
      if((0xff & array[i]) < 0x10){
        buff += "0" + Integer.toHexString((0xFF & array[i]));
      }else{
        buff += Integer.toHexString(0xFF & array[i]);
      }
    }
    return buff;
  }
}
