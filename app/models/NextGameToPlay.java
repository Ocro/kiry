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
import io.ebean.*;
import utils.ContainGame;
import controllers.Kiry;

@Entity
@Table(name = "nextgametoplay")
public class NextGameToPlay extends Model implements ContainGame {

  @EmbeddedId
  public NextGameToPlayKeys id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_email", insertable = false, updatable = false)
  public User account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id", insertable = false, updatable = false)
  public Game game;

  @Embeddable
  public static class NextGameToPlayKeys implements Serializable {

    public String account_email;
    public long game_id;

    public NextGameToPlayKeys(String idAccount, long idGame) {
      this.account_email = idAccount;
      this.game_id = idGame;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      NextGameToPlayKeys other = (NextGameToPlayKeys) obj;
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
  
  public int order;

  public NextGameToPlay(NextGameToPlayKeys id) {
    this.id = id;
    dateadded = new Date();
    Game.removeFromAllList(Kiry.getLoggedUser().email, id.game_id);
  }

  // -- Queries

  public static Finder<NextGameToPlayKeys, NextGameToPlay> find = new Finder<>(NextGameToPlay.class);

  public static List<NextGameToPlay> findAll() {
    return find.all();
  }

  @Override
  public Game getGame() {
    return game;
  }

  public static List<NextGameToPlay> findUserGames(String email) {
    return find.query().where().eq("account.email", email)
        .orderBy("game.platform.name, game.gametitle").findList();
  }

  public static NextGameToPlay findById(NextGameToPlayKeys id) {
    return find.query().where().eq("id", id).findList().get(0);
  }

  public static boolean contains(String email, long game) {
    return find.query().where().eq("account.email", email).where().eq("game.id", game)
        .findList().size() > 0;
  }

  // --
}
