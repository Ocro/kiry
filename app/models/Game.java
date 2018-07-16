package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.ContainGame;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;

@Entity
@Table(name = "game")
public class Game extends Model implements ContainGame {

  @Id
  public long id;

  @ManyToOne
  public String account_email;

  public Long idgamedb;

  @Constraints.Required
  public String gametitle;

  @Constraints.Required
  @Temporal(TemporalType.DATE)
  public Date releasedate;

  public String youtube;

  public short rating;

  @Constraints.Required
  @ManyToOne
  public Platform platform;

  @ManyToMany
  public List<Genre> genres = new ArrayList<>();

  @ManyToOne
  public Compagny publisher;

  @ManyToOne
  public Compagny developer;

  @OneToMany(mappedBy = "game")
  public List<UserWishGame> userwishgames;

  @OneToMany(mappedBy = "game")
  public List<UserOwnGame> userowngames;

  @OneToMany(mappedBy = "game")
  public List<UserGameNotInterrested> usergamenotinterrested;

  public Game(String gameTitle) {
    if (gameTitle.equals(""))
      throw new IllegalArgumentException("Le nom du jeu est obligatoire.");
    this.gametitle = gameTitle;
  }

  // -- Queries

  public static Model.Finder<Long, Game> find = new Model.Finder<Long, Game>(
      Long.class, Game.class);
  
  public static List<Game> findByName(String name) {
    return find.where().icontains("gametitle", name)
        .orderBy("gametitle").findList();
  }

  public static List<Game> findAll() {
    return find.fetch("developer").fetch("publisher")
        .orderBy("platform.name, gametitle").findList();
  }

  public static Game findById(Long id) {
    return find.fetch("developer").fetch("publisher").where().eq("id", id)
        .findUnique();
  }

  public static Game findByIdGameDb(Long idGameDb) {
    return find.fetch("developer").fetch("publisher").where()
        .eq("idgamedb", idGameDb).findUnique();
  }

  public static List<Game> findGameWithoutState(String email) {
    List<Game> result = findAll();
    result.removeAll(find.fetch("userwishgames").where()
        .eq("userwishgames.account.email", email).findList());
    result.removeAll(find.fetch("userowngames").where()
        .eq("userowngames.account.email", email).findList());
    result.removeAll(find.fetch("usergamenotinterrested").where()
        .eq("usergamenotinterrested.account.email", email).findList());
    return result;
  }

  public static List<Game> getOldUserWishGames(String email) {
    return find
        .fetch("userwishgames")
        .where()
        .conjunction()
        .add(Expr.like("userwishgames.account.email", email))
        .add(
            Expr.or(
                Expr.lt("userwishgames.wish_from",
                    new Date(new Date().getTime() - 604800000)),// une semaine
                Expr.isNull("userwishgames.wish_from")))
        .add(Expr.eq("userwishgames.archived", false)).findList();
  }

  public static List<Game> getOldUserWishGames(String email, long except) {
    List<Game> result = getOldUserWishGames(email);
    result.removeAll(find.where().eq("account_email", email).where()
        .eq("id", except).findList());
    return result;
  }

  public static List<Game> getUserWishGames(String email) {
    return find.fetch("userwishgames").where()
        .eq("userwishgames.account.email", email).findList();
  }

  public static List<Game> getUserWishGames(String email, long except) {
    List<Game> result = getUserWishGames(email);
    result.removeAll(find.where().eq("account_email", email).where()
        .eq("id", except).findList());
    return result;
  }

  public static List<Game> getUserOwnGames(String email) {
    return find.fetch("userowngames").where()
        .eq("userowngames.account.email", email).findList();
  }

  public static List<Game> getUserOwnGames(String email, long except) {
    List<Game> result = getUserOwnGames(email);
    result.removeAll(find.where().eq("account_email", email).where()
        .eq("id", except).findList());
    return result;
  }

  public static List<Game> findGameWithoutState(String email, long except) {
    List<Game> result = findGameWithoutState(email);
    result.removeAll(find.where().eq("account_email", email).where()
        .eq("id", except).findList());
    return result;
  }

  public static void removeFromAllList(String email, long game) {
    UserWishGame.removeWishGame(email, game);
    UserGameNotInterrested.removeNotInterrestedGame(email, game);
    UserOwnGame.removeOwnGame(email, game);
  }
  
  public static void cleanGameFrom(String fromEmail, long fromId) {
    Ebean.delete(
        find.where().gt("id", fromId).eq("account_email", fromEmail).findList());
  }

  @Override
  public Game getGame() {
    return this;
  }

  public String getYoutubeEmbed() {
    if (youtube == null || youtube.isEmpty())
      return "";
    return youtube.substring(5).replace("watch?v=", "embed/");
  }

  public String getTruncatedGameTitle(int maxChar) {
    String result = gametitle;
    if (maxChar <= 3)
      return result;
    if (result.length() > maxChar)
      result = result.substring(0, maxChar - 3) + "...";
    return result;
  }
  // --

}
