package webDownloader;

import java.util.List;

import org.w3c.dom.Document;


public interface InfoExtractor {
  public List<DistantFile> getInfo(Document doc, String fileName);
}