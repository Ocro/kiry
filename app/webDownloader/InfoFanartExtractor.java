package webDownloader;

public class InfoFanartExtractor extends InfoImagesExtractor {

  public static InfoFanartExtractor getInstance() {
    if (instance == null)
      instance = new InfoFanartExtractor();
    return instance;
  }

  private static InfoFanartExtractor instance;

  private InfoFanartExtractor() {

  }

  @Override
  protected String getType() {
    return "fanart";
  }

}
