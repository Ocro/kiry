package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import play.data.validation.Constraints;
import io.ebean.*;

@Entity
@Table(name = "genre")
public class Genre extends Model {

  @Id
  public long id;

  @Constraints.Required
  public String name;

  @ManyToMany(mappedBy = "genres")
  public List<Game> games = new ArrayList<Game>();

  public Genre(String name) {
    this.name = name;
  }

  public static Finder<Long, Genre> find = new Finder<>(Genre.class);

  public static List<Genre> findAll() {
    return find.all();
  }
  
  public static Genre findByName(String name) {
    return find.query().where().eq("name", name).findList().get(0);
  }

  public static List<String> getStringList() {
    List<Genre> list = findAll();
    List<String> results = new LinkedList<>();
    for (Genre g : list)
      if (!results.contains(g.name))
        results.add(g.name);
    return results;
  }

  public static void removeAllGenres() {
    Ebean.createSqlUpdate("delete from genre;").execute();
  }
}
