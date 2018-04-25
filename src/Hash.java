package barray.fc;

import java.security.SecureRandom;

/**
 * Hash.java
 *
 * A simple class allowing hashes to be generated and checked for the filenames
 * of the comments.
 **/
public class Hash{
  private static final int CRYPTO_LENGTH = Config.instance.getInt("CRYPTO_LENGTH");
  private static final char[] CONSONANT_LIST = Config.instance.getString("CONSONANT_LIST").toCharArray();
  private static final char[] VOWEL_LIST = Config.instance.getString("VOWEL_LIST").toCharArray();
  private static final String[] COLOUR_LIST = Config.instance.getString("COLOUR_LIST").split(",");

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
    if(hash.length() != CRYPTO_LENGTH * 2){
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
    byte[] bytes = new byte[CRYPTO_LENGTH];
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

  /**
   * intToWord()
   *
   * Converts an integer to a series of words.
   *
   * @param i The integer to be converted.
   * @param salt The salt to be XOR'd with the int.
   * @param len The length of the String to produce, not including spacing.
   * @return The converted integer.
   **/
  public static String intToWord(int i, int salt, int len){
    /* Mix IP with salt */
    i ^= salt;
    /* Make sure the number is positive */
    i = i < 0 ? -i : i;
    /* Generate the output String */
    String r = "";
    for(int x = 0; x < len; x++){
      if(x % 4 == 0 || x % 4 == 3){
        r += CONSONANT_LIST[i % CONSONANT_LIST.length];
        i /= CONSONANT_LIST.length;
      }else{
        r += VOWEL_LIST[i % VOWEL_LIST.length];
        i /= VOWEL_LIST.length;
      }
      if(x % 4 == 3){
        r += " ";
      }
    }
    return r;
  }

  /**
   * intToSVG()
   *
   * Converts an integer to an SVG.
   *
   * @param i The integer to be converted.
   * @return The converted integer.
   **/
  public static byte[] intToSVG(int i){
    String pts =
      (((i >>  0) & 0x7) << 3) + "," +
      (((i >>  2) & 0x7) << 3) + " " +
      (((i >>  4) & 0x7) << 3) + "," +
      (((i >>  6) & 0x7) << 3) + " " +
      (((i >>  8) & 0x7) << 3) + "," +
      (((i >> 10) & 0x7) << 3) + " " +
      (((i >> 12) & 0x7) << 3) + "," +
      (((i >> 14) & 0x7) << 3) + " " +
      (((i >> 16) & 0x7) << 3) + "," +
      (((i >> 18) & 0x7) << 3) + " " +
      (((i >> 20) & 0x7) << 3) + "," +
      (((i >> 22) & 0x7) << 3) + " " +
      (((i >> 24) & 0x7) << 3) + "," +
      (((i >> 26) & 0x7) << 3);
    String c = COLOUR_LIST[((i >> 28) & 0xF) % COLOUR_LIST.length];
    return (
      "HTTP/1.1 200 OK\r\n" +
      "Content-Type: image/svg+xml\r\n\r\n" +
      "<?xml version=\"1.0\"?>" +
      "<svg width=\"64\" height=\"64\" xmlns=\"http://www.w3.org/2000/svg\">" +
        "<polygon points=\"" +
          pts +
          "\" style=\"fill:#" +
          c +
          ";stroke-width:0;fill-rule:evenodd;\" />" +
      "</svg>"
    ).getBytes();
  }
}
