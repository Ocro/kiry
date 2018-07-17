package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.inject.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.Compagny;
import models.Game;
import models.Genre;
import models.Platform;
import models.User;
import models.UserGameNotInterrested;
import models.UserOwnGame;
import models.UserWishGame;

import org.hibernate.validator.constraints.Range;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import play.*;
import play.data.*;
import play.mvc.*;
import play.data.validation.Constraints;
import scala.Tuple2;
import scala.collection.Seq;
import utils.ContainGame;
import utils.DocumentFactory;
import validators.YoutubeAdress;
import views.html.gamedetails;
import views.html.index;
import views.html.newgame;
import views.html.selectPlatforms;
import views.html.welcome;
import webDownloader.InfoFanartExtractor;
import webDownloader.InfoFrontExtractor;
import webDownloader.InfoScreenshotExtractor;
import webDownloader.UrlReader;
import webDownloader.WebContentDownloader;

@Security.Authenticated(Secured.class)
public class Kiry extends Controller {

  @Inject FormFactory formFactory;

  public static final String SEPARATOR = System.getProperty("file.separator");
  public static final String GAMES_FOLDER = "public" + SEPARATOR + "images"
      + SEPARATOR + "games";

  public static final String FRONT_FOLDER = GAMES_FOLDER + SEPARATOR + "front";
  public static final String SCREENSHOT_FOLDER = 
      GAMES_FOLDER + SEPARATOR + "screenshot";
  public static final String FANART_FOLDER = 
      GAMES_FOLDER + SEPARATOR + "fanart";
  
  public static final String PREFIX_GET_PLATFORMS = 
      "http://thegamesdb.net/api/GetPlatformsList.php";
  
  public static final String PREFIX_GET_GAME = 
      "http://thegamesdb.net/api/GetGame.php?";
  
  public static final String PREFIX_GET_GAME_ID = PREFIX_GET_GAME + "id=";
  public static final String PREFIX_GET_GAME_NAME = PREFIX_GET_GAME + "name=";
  
  public static final String PREFIX_GET_ARTS = PREFIX_GET_GAME_ID;
      //"http://thegamesdb.net/api/GetArt.php?id=";

  // MESSAGES
  public static final String UNAUTHORIZED_ACTION = 
      "Vous n'avez pas l'autorisation d'effectuer cette action.";

  public static class AddNewGame {

    @Constraints.Required
    public String gameTitle;

    public Long idGameDb;

    @YoutubeAdress
    public String youtube;

    @Constraints.Required
    @Range(min = 0, max = 100)
    public short rating;

    @Temporal(TemporalType.DATE)
    public Date releaseDate;

    @Constraints.Required
    public String platform;

    public List<Genre> genres = new ArrayList<>();

    public Compagny publisher;

    public Compagny developer;

    public String validate() {
      try {
        Long.parseLong(platform);
      } catch (Exception e) {
        return e.getMessage();
      }
      return null;
    }
  }

  public enum ButtonAddList {
    WISHGAME("/addWishGame", "Désirer", "favorite-add-24 favorite-add-tiny"),

    ARCHIVED("/archivedGamed", "Archiver",
        "remove-24 remove-tiny zoom-middle mr1"),

    UNARCHIVED("/removearchived", "Désarchiver",
        "remove-24 remove-tiny opacity100 zoom-middle mr1"),

    OWNGAME("/addOwnGame", "Posséder", "own-add-24 own-add-tiny"),

    IGNOREDGAME("/addIgnoredGame", "Ignorer",
        "favorite-minus-24 favorite-minus-tiny");

    private String url;
    private String title;
    private String cssClass;

    private ButtonAddList(String url, String title, String cssClass) {
      this.url = url;
      this.title = title;
      this.cssClass = cssClass;
    }

    public String getUrl(long idGame) {
      return url + "/" + getLoggedUser().email + "/" + idGame;
    }

    public String getTitle() {
      return title;
    }

    public String getCssClass() {
      return cssClass;
    }
  }

  public static class ButtonAddListConcrete {
    private String cssClass = "";
    private ButtonAddList btn;

    public ButtonAddListConcrete(ButtonAddList btn) {
      this.btn = btn;
    }

    public String getUrl(long idGame) {
      return btn.getUrl(idGame);
    }

    public String getTitle() {
      return btn.getTitle();
    }

    public String getCssClass() {
      return btn.getCssClass() + cssClass;
    }

    public void addCssClass(String cssClass) {
      this.cssClass += " " + cssClass;
    }
  }

  public Result index(int list) {
    List<? extends ContainGame> gamesToShow;
    String email = session("email");
    switch (list) {
    case 1: // wish game
      gamesToShow = UserWishGame.findUserGames(email);
      break;
    case 5: // wish game archived
      gamesToShow = UserWishGame.findUserGames(email, true);
      break;
    case 2: // own game
      gamesToShow = UserOwnGame.findUserGames(email);
      break;
    case 3: // ignored game
      gamesToShow = UserGameNotInterrested.findUserGames(email);
      break;
    case 4: // all games
      gamesToShow = Game.findAll();
      break;
    default:
    case 0: // default view : game without state
      gamesToShow = Game.findGameWithoutState(email);
    }

    return ok(index.render(gamesToShow, list));
  }

  private static Boolean getDefaultView() {
    Boolean defaultView = true;
    Http.Cookie cookie = request().cookies().get("defaultView");
    if (cookie != null)
      defaultView = Boolean.parseBoolean(cookie.value());
    return defaultView;
  }

  private static Boolean getOrdoredView() {
    Boolean ordoredView = true;
    Http.Cookie cookie = request().cookies().get("ordoredView");
    if (cookie != null)
      ordoredView = Boolean.parseBoolean(cookie.value());
    return ordoredView;
  }

  public Result welcome() {
    return ok(welcome.render(User.findByEmail(session("email")),
        getDefaultView(), getOrdoredView()));
  }

  public Result newgame() {
    return ok(newgame.render(formFactory.form(AddNewGame.class), getPlatformOptions(),
        getDefaultView(), getOrdoredView()));
  }

  public Result addNewGame() {
    Form<AddNewGame> addNewGame = formFactory.form(AddNewGame.class).bindFromRequest();
    if (addNewGame.hasErrors()) {
      return badRequest(newgame.render(addNewGame, getPlatformOptions(),
          getDefaultView(), getOrdoredView()));
    } else {
      Game game = new Game(addNewGame.get().gameTitle);
      game.idgamedb = addNewGame.get().idGameDb;
      game.rating = addNewGame.get().rating;
      game.youtube = addNewGame.get().youtube;
      game.releasedate = addNewGame.get().releaseDate;
      game.platform = Platform
          .findByIdGameDb(Long.parseLong(addNewGame.get().platform));
      game.genres = addNewGame.get().genres;
      for (Genre g : game.genres)
        g.save();
      game.publisher = addNewGame.get().publisher;
      game.publisher.save();
      game.developer = addNewGame.get().developer;
      game.developer.save();
      game.account_email = session("email");
      game.save();
      importFrontGame(game);
      importScreenShotGame(game);
      flash("success", "Le jeu a été ajouté avec succès.");
      return redirect(routes.Kiry.gameDetails(game.id));
    }
  }

  private static Seq<Tuple2<String, String>> getPlatformOptions() {
    Map<String, String> m = new HashMap<>();
    for (Platform p : Platform.findAll())
      m.put(String.valueOf(p.idgamedb), p.name);
    return views.html.helper.options.apply(m);
  }

  public Result setDefaultView(Boolean enable) {
    response().setCookie("defaultView", String.valueOf(enable), 2592000, "user/view/default", "kiry.com", false, true, Http.Cookie.SameSite.STRICT);
    return redirectReferer();
  }

  public Result setOrdoredView(Boolean enable) {
    response().setCookie("ordoredView", String.valueOf(enable), 2592000, "user/view/order", "kiry.com", false, true, Http.Cookie.SameSite.STRICT);
    return redirectReferer();
  }

  private Result redirectReferer() {
    String returnUrl = request().getHeader("referer");
    if (returnUrl == null)
      return redirect(routes.Kiry.index(2));
    return redirect(returnUrl);
  }

  private static boolean actionPermitted(String email) {
    return email.equals(session("email"));
  }

  public Result addWishGame(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserWishGame uwg = new UserWishGame(new UserWishGame.UserWishGameKeys(
            email, game), (short) 0);
        uwg.save();
      } catch (Exception e) {
        flash("error", "Ce jeu est déjà dans votre liste de souhaits.");
      }
    return redirectReferer();
  }

  public Result addOwnGame(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserOwnGame uog = new UserOwnGame(new UserOwnGame.UserOwnGameKeys(
            email, game));
        uog.save();
      } catch (Exception e) {
        flash("error", "Vous possédez déjà ce jeu.");
      }
    return redirectReferer();
  }

  public Result addIgnoredGame(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserGameNotInterrested uog = new UserGameNotInterrested(
            new UserGameNotInterrested.UserGameNotInterrestedKeys(email, game));
        uog.save();
      } catch (Exception e) {
        flash("error", "Vous ignorez déjà ce jeu.");
      }
    return redirectReferer();
  }

  public Result removeWishGame(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserWishGame.removeWishGame(email, game);
      } catch (Exception e) {
        flash("error", "Un problème est survenu lors de la suppression du jeu "
            + "de la liste de souhaits.");
      }
    return redirectReferer();
  }

  public Result removeOwnGame(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserOwnGame.removeOwnGame(email, game);
      } catch (Exception e) {
        flash("error", "Un problème est survenu lors de la suppression du jeu "
            + "de votre liste de jeux.");
      }
    return redirectReferer();
  }

  public Result removeIgnoredGame(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserGameNotInterrested.removeNotInterrestedGame(email, game);
      } catch (Exception e) {
        flash("error", "Un problème est survenu lors de la suppression du jeu "
            + "de la liste de jeux ignorés.");
      }
    return redirectReferer();
  }

  public Result archivedGame(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserWishGame.setArchivedGame(email, game, true);
      } catch (Exception e) {
        flash("error", "Un problème est survenu lors de l'archivage du jeu.");
        System.err.println(e);
      }
    return redirectReferer();
  }

  public Result removearchived(String email, long game) {
    if (!actionPermitted(email))
      flash("error", UNAUTHORIZED_ACTION);
    else
      try {
        UserWishGame.setArchivedGame(email, game, false);
      } catch (Exception e) {
        flash("error", "Un problème est survenu lors du désarchivage du jeu.");
        System.err.println(e);
      }
    return redirectReferer();
  }
  
  public Document getLocalGameList(String search) {
    Document doc = DocumentFactory.createNewDocument();
    Element data = doc.createElement("Data");
    doc.appendChild(data);
    appendLocalGameList(search, doc);
    return doc;
  }
  
  public static void appendLocalGameList(String search, Document doc) {
    Node data = doc.getElementsByTagName("Data").item(0);
    for (Game game : Game.findByName(search)) {
      
      Element e = doc.createElement("GameLocal"),
              currentElement;
      
      // ID
      currentElement = doc.createElement("id");
      currentElement.setTextContent(String.valueOf(game.id));
      e.appendChild(currentElement);

      // GameTitle
      currentElement = doc.createElement("GameTitle");
      currentElement.setTextContent(game.gametitle);
      e.appendChild(currentElement);
      
      // ReleaseDate
      if (game.releasedate != null) {
        currentElement = doc.createElement("ReleaseDate");
        currentElement.setTextContent(
            new SimpleDateFormat("dd MMM yyyy").format(game.releasedate));
        e.appendChild(currentElement);
      }
      
      // Plateforme
      currentElement = doc.createElement("Platform");
      currentElement.setTextContent(game.platform.name);
      e.appendChild(currentElement);
      
      data.appendChild(e);
    }
  }

  public Result getGameList() {
    /*Map<String, String[]> datas = request().body().asFormUrlEncoded();
    Document doc = null;
    String search = datas.get("search")[0],
           searchLocal = search.trim().replace(' ', '%');*/
    Document doc = null;
    String searchLocal;
    String search = searchLocal = "golden";
    
    try {
      search = URLEncoder.encode(search, "UTF-8").replace(" ", "+");
      String content = UrlReader.read(PREFIX_GET_GAME_NAME + search);
      doc = DocumentFactory.createDocumentFromString(content);
    } catch (Exception e) {
      System.err.println("Problème lors de l'obtention de la liste de jeu.");
      System.err.println(e);
    }
    
    if (doc == null)
      doc = getLocalGameList(searchLocal);
    else 
      appendLocalGameList(searchLocal, doc);
    
    return ok(DocumentFactory.documentToString(doc));
  }

  public Result getGameInfo(String game) {
    try {
      long id = Long.parseLong(game);
      String content = UrlReader.read(PREFIX_GET_GAME_ID + id);
      return ok(content);
    } catch (Exception e) {
      return ok();
    }
  }

  public Result importFront() {

    for (Game game : Game.findAll()) {
      if (game.idgamedb == null)
        continue;
      importFrontGame(game);
    }
    return redirectReferer();
  }

  public static void importFrontGame(Game game) {
    WebContentDownloader.downloadContent(PREFIX_GET_GAME_ID + game.idgamedb,
        FRONT_FOLDER + SEPARATOR, String.valueOf(game.id), ".jpg",
        InfoFrontExtractor.getInstance());
  }

  public Result importScreenShot() {

    for (Game game : Game.findAll()) {
      if (game.idgamedb == null)
        continue;
      importScreenShotGame(game);
    }
    return redirectReferer();
  }

  public static void importScreenShotGame(Game game) {
    WebContentDownloader.downloadContent(PREFIX_GET_ARTS + game.idgamedb,
        SCREENSHOT_FOLDER + SEPARATOR + String.valueOf(game.id) + 
        SEPARATOR, "", ".jpg",
        InfoScreenshotExtractor.getInstance());
  }

  public Result importFanart() {

    for (Game game : Game.findAll()) {
      if (game.idgamedb == null)
        continue;
      importFanartGame(game);
    }
    return redirectReferer();
  }

  public void importFanartGame(Game game) {
    WebContentDownloader.downloadContent(PREFIX_GET_ARTS + game.idgamedb,
        FANART_FOLDER + SEPARATOR + String.valueOf(game.id) + 
        SEPARATOR, "", ".jpg",
        InfoFanartExtractor.getInstance());
  }

  public Long importGame(long id) 
      throws DOMException, ParseException, ParserConfigurationException, 
             SAXException, IOException {
    
    Game existGame = Game.findByIdGameDb(id);
    if (existGame != null)
      return existGame.id;

    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document document = 
        docBuilder.parse(PREFIX_GET_GAME_ID + String.valueOf(id));
    NodeList currentList;

    currentList = document.getElementsByTagName("GameTitle");
    
    if (currentList.getLength() > 0) {
    
      Game game = new Game(currentList.item(0).getTextContent());
      game.idgamedb = id;
      game.account_email = getLoggedUser().email;

      currentList = document.getElementsByTagName("Developer");
      if (currentList.getLength() > 0) {
        Compagny developer = new Compagny(currentList.item(0).getTextContent());
        developer.save();
        game.developer = developer;
      }
      
      currentList = document.getElementsByTagName("Publisher");
      if (currentList.getLength() > 0) {
        Compagny publisher = new Compagny(currentList.item(0).getTextContent());
        publisher.save();
        game.publisher = publisher;
      }

      currentList = document.getElementsByTagName("Platform");
      if (currentList.getLength() > 0) {
        Platform platform = Platform.findByName(currentList.item(0).getTextContent());
        game.platform = platform;
      }

      currentList = document.getElementsByTagName("Youtube");
      if (currentList.getLength() > 0)
        game.youtube = currentList.item(0).getTextContent();
      
      currentList = document.getElementsByTagName("Rating");
      if (currentList.getLength() > 0)
        game.rating = (short)Math.round(Float.parseFloat(
            currentList.item(0).getTextContent()) * 10.0);
      
      currentList = document.getElementsByTagName("ReleaseDate");
      if (currentList.getLength() > 0) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        game.releasedate = df.parse(currentList.item(0).getTextContent()); 
      }
      
      NodeList listGenres =document.getElementsByTagName("genre");
      for (int i = 0; i < listGenres.getLength(); i++) {
        String strGenre = listGenres.item(i).getTextContent();
        Genre genre = Genre.findByName(strGenre);
        if (genre == null) {
          genre = new Genre(strGenre);
          genre.save();
        }
        game.genres.add(genre);
      }
      
      game.save();
      
      importFrontGame(game);
      importFanartGame(game);
      importScreenShotGame(game);
      
      return game.id;
    }
    
    return -1L;
  }

  public Result importPlatform() {
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document document = docBuilder.parse(PREFIX_GET_PLATFORMS);
      importDocument(document.getDocumentElement());

      return ok(selectPlatforms.render(formFactory.form(AddNewGame.class), "platform",
          getPlatformOptions()));
    } catch (Exception e) {
      System.out.println(e);
      return badRequest();
    }
  }

  private static void importDocument(Node node) {
    NodeList nodeList = node.getChildNodes();
    if (node.getNodeName().equals("Platform")) {
      String name = "undefined";
      long id = -1;
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node currentNode = nodeList.item(i);
        if (currentNode.getNodeName().equals("id"))
          id = Long.parseLong(currentNode.getTextContent());
        if (currentNode.getNodeName().equals("name"))
          name = currentNode.getTextContent();
      }

      if (Platform.findByIdGameDb(id) == null) {
        Platform p = new Platform(name);
        p.idgamedb = id;
        p.save();
      }
    } else {
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node currentNode = nodeList.item(i);
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
          importDocument(currentNode);
        }
      }
    }
  }

  public Result getGenresList() {
    return ok(listToString(Genre.getStringList()));
  }

  public Result getCompagniesList() {
    return ok(listToString(Compagny.getStringList()));
  }

  private static String listToString(List<String> list) {
    if (list.isEmpty())
      return "[]";

    String l = "[";
    for (String s : list)
      l += "\"" + s + "\",";
    return l.substring(0, l.length() - 1) + "]";
  }

  public static User getLoggedUser() {
    return User.findByEmail(session("email"));
  }

  public Result gameDetails(Long id) {
    String suffix = SEPARATOR + String.valueOf(id) + SEPARATOR + "original",
           email = getLoggedUser().email;
    int countScreenshots = 0, 
        countFanarts = 0;
    File fileScreenshots = new File(SCREENSHOT_FOLDER + suffix),
         fileFanarts =  new File(FANART_FOLDER + suffix);

    if (fileScreenshots.exists())
      countScreenshots = fileScreenshots.listFiles().length;

    if (fileFanarts.exists())
      countFanarts = fileFanarts.listFiles().length;

    List<Game> listGiveYourOpinion = Game.findGameWithoutState(email, id);
    Collections.shuffle(listGiveYourOpinion);

    List<Game> listGameWishes = Game.getOldUserWishGames(email, id);
    Collections.shuffle(listGameWishes);
    File front = new File(FRONT_FOLDER + SEPARATOR + id + ".jpg");

    return ok(gamedetails.render(
        Game.findById(id),
        front.exists(),
        getBtnList(id),
        listGiveYourOpinion,
        listGameWishes,
        Game.getUserWishGames(email, id), 
        Game.getUserOwnGames(email, id),
        countScreenshots,
        countFanarts));
  }

  public Result gameDetailsDb(Long id) {
    try {
      return redirect(routes.Kiry.gameDetails(importGame(id)));
    } catch (DOMException | ParseException | ParserConfigurationException
        | SAXException | IOException e) {
      e.printStackTrace();
    }
    
     return badRequest();
  }
  
  public static List<ButtonAddListConcrete> getBtnList(long idGame) {
    final String CSS_CLASS = "opacity100";
    List<ButtonAddListConcrete> list = new LinkedList<ButtonAddListConcrete>();
    String email = getLoggedUser().email;
    ButtonAddListConcrete btnWish = new ButtonAddListConcrete(
        ButtonAddList.WISHGAME), btnUnarchived = new ButtonAddListConcrete(
        ButtonAddList.UNARCHIVED), btnArchived = new ButtonAddListConcrete(
        ButtonAddList.ARCHIVED), btnOwn = new ButtonAddListConcrete(
        ButtonAddList.OWNGAME), btnIgnored = new ButtonAddListConcrete(
        ButtonAddList.IGNOREDGAME);

    list.add(btnWish);

    if (UserWishGame.contains(email, idGame)) {
      btnWish.addCssClass(CSS_CLASS);
      if (UserWishGame.isArchived(email, idGame))
        list.add(btnUnarchived);
      else
        list.add(btnArchived);
    }

    if (UserOwnGame.contains(email, idGame))
      btnOwn.addCssClass(CSS_CLASS);

    if (UserGameNotInterrested.contains(email, idGame))
      btnIgnored.addCssClass(CSS_CLASS);

    list.add(btnOwn);
    list.add(btnIgnored);

    return list;
  }

  public static String getExtension(File file) {
    String path = file.getAbsolutePath();
    return path.substring(path.lastIndexOf("."));
  }

  public Result uploadBoxArt() {
    Http.MultipartFormData multipartFormData = request().body()
        .asMultipartFormData();
    Http.MultipartFormData.FilePart picture = multipartFormData.getFile("file");
    String id = (String)multipartFormData.asFormUrlEncoded().get("id");

    if (picture != null) {
      File file = (File)picture.getFile();

      if (file.length() > 80000) {
        flash("error", "File is too big");
        return redirectReferer();
      }

      File output = new File(FRONT_FOLDER);
      String outputFile = String.format("%s%s%s.jpg", output.getAbsolutePath(),
          SEPARATOR, id);

      try {
        if (!output.exists())
          output.mkdirs();

        file.renameTo(new File(outputFile));
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
      return redirectReferer();
    } else {
      flash("error", "Missing file");
      return redirect(routes.Kiry.index(2));
    }
  }

  public Result updateWishFrom() {
    DynamicForm requestData = formFactory.form().bindFromRequest();

    String email = requestData.get("email");
    Long idGame = Long.parseLong(requestData.get("idGame"));

    // Vérification des droits.
    if (!email.equals(getLoggedUser().email))
      throw new IllegalArgumentException(
          "Vous n'avez pas l'authorisation d'effectuer cette opération.");

    UserWishGame.reinitWishFromField(email, idGame);

    return redirectReferer();
  }
}
