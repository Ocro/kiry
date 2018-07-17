package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints;

import io.ebean.*;

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

  public static Finder<Long, Platform> find = new Finder<>(Platform.class);

  /**
   * Retrieve all users.
   */
  public static List<Platform> findAll() {
    return find.query().orderBy("name").findList();
  }

  public static Platform findById(Long id) {
    return find.query().where().eq("id", id).findList().get(0);
  }

  public static Platform findByName(String name) {
    return find.query().where().eq("name", name).findList().get(0);
  }

  public static Platform findByIdGameDb(Long id) {
    return find.query().where().eq("idgamedb", id).findList().get(0);
  }

  public static void deleteAll() {
    Ebean.createSqlUpdate("delete from platform;").execute();
  }

  // --
}
