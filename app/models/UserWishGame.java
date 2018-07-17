package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Constraints;
import utils.ContainGame;

import io.ebean.*;

import controllers.Kiry;

@Entity
@Table(name = "userwishgame")
public class UserWishGame extends Model implements ContainGame {

  @EmbeddedId
  public UserWishGameKeys id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_email", insertable = false, updatable = false)
  public User account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id", insertable = false, updatable = false)
  public Game game;

  @Embeddable
  public static class UserWishGameKeys implements Serializable {

    public String account_email;
    public long game_id;

    public UserWishGameKeys(String idAccount, long idGame) {
      this.account_email = idAccount;
      this.game_id = idGame;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      UserWishGameKeys other = (UserWishGameKeys) obj;
      if ((this.account_email == null) ? (other.account_email != null)
          : !this.account_email.equals(other.account_email))
        return false;
      if (this.game_id != other.game_id)
        return false;
      return true;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 89 * hash
          + (this.account_email != null ? this.account_email.hashCode() : 0);
      hash = 89 * hash + (int) this.game_id;
      return hash;
    }
  }

  public short priority;

  @Constraints.Required
  @Temporal(TemporalType.DATE)
  public Date dateadded;

  public boolean archived;

  @Temporal(TemporalType.DATE)
  public Date wish_from;

  public UserWishGame(UserWishGameKeys id, short priority) {
    this.id = id;
    dateadded = new Date();
    this.priority = priority;
    Game.removeFromAllList(Kiry.getLoggedUser().email, id.game_id);
  }

  // -- Queries

  public static Finder<UserWishGameKeys, UserWishGame> find = new Finder<>(UserWishGame.class);

  public static List<UserWishGame> findAll() {
    return find.all();
  }

  @Override
  public Game getGame() {
    return game;
  }

  public static List<UserWishGame> findUserGames(String email, boolean archived) {
    return find.query().where().eq("account.email", email).where()
        .eq("archived", archived).orderBy("game.platform.name, game.gametitle")
        .findList();
  }

  public static List<UserWishGame> findUserGames(String email) {
    return findUserGames(email, false);
  }

  public static List<UserWishGame> findAllUserGames(String email) {
    return find.query().where().eq("account.email", email)
        .orderBy("game.platform.name, game.gametitle").findList();
  }

  public static UserWishGame findById(UserWishGameKeys id) {
    return find.query().where().eq("id", id).findList().get(0);
  }

  public static void removeWishGame(String email, long game) {
    Ebean
        .createSqlUpdate(
            "delete from userwishgame where account_email = :email and game_id = :game")
        .setParameter("game", game).setParameter("email", email).execute();
  }

  public static void reinitWishFromField(String email, Long game) {
    Ebean
        .createSqlUpdate(
            "update userwishgame set wish_from = now() where account_email = :email and game_id = :game")
        .setParameter("game", game).setParameter("email", email).execute();
  }

  public static boolean contains(String email, long game) {
    return find.query().where().eq("account.email", email).where().eq("game.id", game)
        .findList().size() > 0;
  }

  public static boolean isArchived(String email, long game) {
    return find.query().where().eq("account.email", email).where().eq("game.id", game)
        .where().eq("archived", true).findList().size() > 0;
  }

  public static void setArchivedGame(String email, long game, boolean archived) {
    if (!contains(email, game))
      throw new IllegalArgumentException(
          "Le jeux doit être dans votre liste de désirs pour être archivé.");

    Ebean
        .createSqlUpdate(
            "update userwishgame set archived = :archived where account_email = :email and game_id = :game;")
        .setParameter("archived", archived).setParameter("game", game)
        .setParameter("email", email).execute();
  }

  // --
}
