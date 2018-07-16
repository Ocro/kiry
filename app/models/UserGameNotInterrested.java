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
import play.db.ebean.Model;
import utils.ContainGame;

import com.avaje.ebean.Ebean;

import controllers.Kiry;

@Entity
@Table(name = "usergamenotinterrested")
public class UserGameNotInterrested extends Model implements ContainGame {

  @EmbeddedId
  public UserGameNotInterrestedKeys id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_email", insertable = false, updatable = false)
  public User account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id", insertable = false, updatable = false)
  public Game game;

  @Embeddable
  public static class UserGameNotInterrestedKeys implements Serializable {

    public String account_email;
    public long game_id;

    public UserGameNotInterrestedKeys(String idAccount, long idGame) {
      this.account_email = idAccount;
      this.game_id = idGame;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      UserGameNotInterrestedKeys other = (UserGameNotInterrestedKeys) obj;
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

  @Constraints.Required
  @Temporal(TemporalType.DATE)
  public Date dateadded;

  public UserGameNotInterrested(UserGameNotInterrestedKeys id) {
    this.id = id;
    dateadded = new Date();
    Game.removeFromAllList(Kiry.getLoggedUser().email, id.game_id);
  }

  // -- Queries

  public static Model.Finder<UserGameNotInterrestedKeys, UserGameNotInterrested> find = new Model.Finder<UserGameNotInterrestedKeys, UserGameNotInterrested>(
      UserGameNotInterrestedKeys.class, UserGameNotInterrested.class);

  public static List<UserGameNotInterrested> findAll() {
    return find.all();
  }

  @Override
  public Game getGame() {
    return game;
  }

  public static List<UserGameNotInterrested> findUserGames(String email) {
    return find.where().eq("account.email", email).orderBy("game.gametitle")
        .findList();
  }

  public static UserGameNotInterrested findById(UserGameNotInterrestedKeys id) {
    return find.where().eq("id", id).findUnique();
  }

  public static void removeNotInterrestedGame(String email, long game) {
    Ebean
        .createSqlUpdate(
            "delete from UserGameNotInterrested where account_email = :email and game_id = :game")
        .setParameter("game", game).setParameter("email", email).execute();
  }

  public static boolean contains(String email, long game) {
    return find.where().eq("account.email", email).where().eq("game.id", game)
        .findList().size() > 0;
  }

  // --
}
