package webDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import controllers.Kiry;

public class WebContentToFile {
  public static void urlToFile(String url, String filePath, String fileName)
      throws Exception {
    
    String createPath = filePath;
    if (fileName.contains(Kiry.SEPARATOR))
      createPath += fileName.substring(0, fileName.lastIndexOf(Kiry.SEPARATOR));

    File directory = new File(createPath);
    if (!directory.exists())
      directory.mkdirs();

    URL website = new URL(url);
    FileOutputStream fos = new FileOutputStream(filePath + fileName);
    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    fos.close();
  }
}