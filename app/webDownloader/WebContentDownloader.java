package webDownloader;

import utils.DocumentFactory;

public class WebContentDownloader {

  public static void downloadContent(String urlXml, String filePath,
      String fileName, String fileExtension, InfoExtractor extractor) {
    try {
      // Téléchargement du fichier
      for (DistantFile df : 
          extractor.getInfo(DocumentFactory.createDocumentFromString(
              UrlReader.read(urlXml)), fileName))
        WebContentToFile.urlToFile(df.getUrl(), 
                                   filePath, df.getName() + fileExtension);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}