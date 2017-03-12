package ee.schimke.oksocial.output.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PlatformUtil {
  private PlatformUtil() {
  }

  public static String versionString(Class mainClass, String propertiesFile) {
    try {
      Properties prop = new Properties();
      InputStream in = mainClass.getResourceAsStream(propertiesFile);
      prop.load(in);
      in.close();
      return prop.getProperty("version");
    } catch (IOException e) {
      throw new AssertionError("Could not load " + propertiesFile);
    }
  }

  public static boolean isOSX() {
    String osName = System.getProperty("os.name");
    return osName.contains("OS X");
  }

  public static boolean isLinux() {
    String osName = System.getProperty("os.name");
    return osName.contains("Linux");
  }
}
