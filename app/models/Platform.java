package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebean.Ebean;

@Entity
@Table(name = "platform")
public class Platform extends Model {
  @Id
  public long id;

  public long idgamedb;

  @Constraints.Required
  public String name;

  public Platform(String name) {

    this.name = name;
  }

  // -- Queries

  public static Model.Finder<Long, Platform> find = new Finder<Long, Platform>(
      Long.class, Platform.class);

  /**
   * Retrieve all users.
   */
  public static List<Platform> findAll() {
    return find.orderBy("name").findList();
  }

  public static Platform findById(Long id) {
    return find.where().eq("id", id).findUnique();
  }

  public static Platform findByName(String name) {
    return find.where().eq("name", name).findUnique();
  }

  public static Platform findByIdGameDb(Long id) {
    return find.where().eq("idgamedb", id).findUnique();
  }

  public static void deleteAll() {
    Ebean.createSqlUpdate("delete from platform;").execute();
  }

  // --
}
