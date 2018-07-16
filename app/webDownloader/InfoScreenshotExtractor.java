package webDownloader;

public class InfoScreenshotExtractor extends InfoImagesExtractor {

  public static InfoScreenshotExtractor getInstance() {
    if (instance == null)
      instance = new InfoScreenshotExtractor();
    return instance;
  }

  private static InfoScreenshotExtractor instance;

  private InfoScreenshotExtractor() {

  }

  @Override
  protected String getType() {
    return "screenshot";
  }

}
