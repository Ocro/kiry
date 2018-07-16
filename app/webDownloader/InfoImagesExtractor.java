package webDownloader;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import controllers.Kiry;

public abstract class InfoImagesExtractor implements InfoExtractor {
  
  protected abstract String getType();

  @Override
  public List<DistantFile> getInfo(Document doc, String fileName) {
    List<DistantFile> results = new ArrayList<>();
    
    NodeList l = doc.getElementsByTagName("baseImgUrl");
    String baseUrl = l.item(0).getTextContent();
    
    // Recherche des screenshots
    l = doc.getElementsByTagName(getType());
    for (int i = 0; i < l.getLength(); ++i) {
      // Original
      results.add(
          new DistantFile(baseUrl + l.item(i).getChildNodes().item(0).getTextContent(),
                          "original" + Kiry.SEPARATOR + String.valueOf(i)));      
      // thumb
      results.add(
          new DistantFile(baseUrl + l.item(i).getChildNodes().item(1).getTextContent(),
                          "thumbs" + Kiry.SEPARATOR + String.valueOf(i)));
    }
    return results;
  }

}
