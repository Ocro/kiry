package webDownloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlReader {
  public static String read(String url) throws Exception {

    URL oracle = new URL(url);
    BufferedReader in = new BufferedReader(new InputStreamReader(
        oracle.openStream(), "UTF-8"));
    String inputLine, content = "";
    while ((inputLine = in.readLine()) != null)
      content += inputLine;
    in.close();
    return content;
  }
}
