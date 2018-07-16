package webDownloader;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class InfoFrontExtractor implements InfoExtractor {

  public static InfoFrontExtractor getInstance() {
    if (instance == null)
      instance = new InfoFrontExtractor();
    return instance;
  }

  private static InfoFrontExtractor instance;

  private InfoFrontExtractor() {

  }

  public List<DistantFile> getInfo(Document doc, String fileName) {
    List<DistantFile> results = new ArrayList<>();
    
    // Recherche de la pochette
    NodeList l = doc.getElementsByTagName("baseImgUrl");
    String baseUrl = l.item(0).getTextContent();
    l = doc.getElementsByTagName("boxart");

    // Recherche du boxart avec l'attribut side="front".
    for (int i = 0; i < l.getLength(); ++i)
      if ("front".equals(l.item(i).getAttributes().getNamedItem("side")
          .getTextContent())) {
        results.add(new DistantFile(
            baseUrl + l.item(i).getAttributes().getNamedItem("thumb").getTextContent(),
            (fileName.isEmpty() ? String.valueOf(i) : fileName)));
        break;
      }
    return results;
  }
}