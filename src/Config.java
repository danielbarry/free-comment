package barray.fc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/* TODO: Should iProps be favoured over eProps in loading? */

/**
 * Config.java
 *
 * Processes the configuration file for the program, allowing various settings
 * to be loaded.
 **/
public class Config{
  public static Config instance;
  private Properties iProps;
  private Properties eProps;

  /**
   * Config()
   *
   * Initialize the configuration file from either an internal or external
   * source, to be processed as a read-only file.
   *
   * @param internal The internal filename to load the configuration from.
   * @param external The filename to be read from the external source, if it
   * exists - otherwise default values are to be used.
   **/
  public Config(String internal, String external){
    /* Set the current instance */
    instance = this;
    /* Set the streams as NULL for try-catch checks */
    InputStream is = null;
    FileInputStream fis = null;
    /* Load the internal properties */
    try{
      is = getClass().getResourceAsStream(internal);
      iProps = new Properties();
      iProps.load(is);
    }catch(IOException e){
      iProps = null;
      Main.error("Config", "Unable to load internal configuration file");
    }
    /* Load the external properties */
    try{
      fis = new FileInputStream(external);
      eProps = new Properties();
      eProps.load(fis);
    }catch(IOException e){
      eProps = null;
    }
  }

  /**
   * getBoolean()
   *
   * Get a value from configuration file, otherwise return false.
   *
   * @param key The key identifier for the value to be found.
   * @return The object for a given key, otherwise false.
   **/
  public boolean getBoolean(String key){
    try{
      /* Get the initial String */
      String val = getString(key);
      /* Try to cast */
      if(val != null){
        return Boolean.parseBoolean(val);
      }
    }catch(NumberFormatException e){
      /* Do nothing */
    }
    /* Default return value */
    return false;
  }

  /**
   * getInt()
   *
   * Get a value from configuration file, otherwise return -1.
   *
   * @param key The key identifier for the value to be found.
   * @return The object for a given key, otherwise -1.
   **/
  public int getInt(String key){
    try{
      /* Get the initial String */
      String val = getString(key);
      /* Try to cast */
      if(val != null){
        return Integer.parseInt(val);
      }
    }catch(NumberFormatException e){
      /* Do nothing */
    }
    /* Default return value */
    return -1;
  }

  /**
   * getLong()
   *
   * Get a value from configuration file, otherwise return -1.
   *
   * @param key The key identifier for the value to be found.
   * @return The object for a given key, otherwise -1.
   **/
  public long getLong(String key){
    try{
      /* Get the initial String */
      String val = getString(key);
      /* Try to cast */
      if(val != null){
        return Long.parseLong(val);
      }
    }catch(NumberFormatException e){
      /* Do nothing */
    }
    /* Default return value */
    return -1;
  }

  /**
   * getDouble()
   *
   * Get a value from configuration file, otherwise return -1.0.
   *
   * @param key The key identifier for the value to be found.
   * @return The object for a given key, otherwise -1.0.
   **/
  public double getDouble(String key){
    try{
      /* Get the initial String */
      String val = getString(key);
      /* Try to cast */
      if(val != null){
        return Double.parseDouble(val);
      }
    }catch(NumberFormatException e){
      /* Do nothing */
    }
    /* Default return value */
    return -1.0;
  }

  /**
   * getString()
   *
   * Get a value from configuration file, otherwise return NULL.
   *
   * @param key The key identifier for the value to be found.
   * @return The object for a given key, otherwise NULL.
   **/
  public String getString(String key){
    /* Make sure that key exists */
    if(exists(key)){
      /* Check first whether internal properties has value */
      if(iProps.getProperty(key) != null){
        return iProps.getProperty(key);
      }
      /* Check second whether external properties has value */
      if(eProps.getProperty(key) != null){
        return eProps.getProperty(key);
      }
    }
    /* Default return case for failure */
    return null;
  }

  /**
   * getFile()
   *
   * Get a value from configuration file, otherwise return NULL.
   *
   * @param key The key identifier for the value to be found.
   * @return The object for a given key, otherwise NULL.
   **/
  public byte[] getFile(String key){
    byte[] buff = new byte[]{};
    /* Make sure that key exists */
    if(exists(key)){
      File file = null;
      /* Check first whether internal properties has value */
      if(file == null && iProps.getProperty(key) != null){
        file = new File(iProps.getProperty(key));
      }
      /* Check second whether external properties has value */
      if(file == null && eProps.getProperty(key) != null){
        file = new File(eProps.getProperty(key));
      }
      /* Attempt to read the file */
      if(file != null && file.exists() && file.isFile()){
        FileInputStream fis = null;
        try{
          fis = new FileInputStream(file);
          buff = new byte[(int)(file.length())];
          fis.read(buff, 0, (int)(file.length()));
          fis.close();
        }catch(IOException e){
          Main.error("Config", "Unable to load file " + key);
        }
      }
    }
    /* Default return case for failure */
    return buff;
  }

  /**
   * exists()
   *
   * Checks whether a key has an associated entry.
   *
   * @param key The key identifier for the value to be checked.
   * @return True if exists, false if doesn't exist.
   **/
  public boolean exists(String key){
    /* Make sure the key value is not NULL */
    if(key != null){
      /* Make sure the internal properties file exists */
      if(iProps != null){
        /* Check whether internal properties has value */
        if(iProps.getProperty(key) != null){
          return true;
        }
      }
      /* Make sure the external properties file exists */
      if(eProps != null){
        /* Check whether external properties has value */
        if(eProps.getProperty(key) != null){
          return true;
        }
      }
    }
    /* Default failure case to find key */
    return false;
  }
}
