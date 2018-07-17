package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints;
import io.ebean.*;

@Entity
@Table(name = "compagny")
public class Compagny extends Model {
  @Id
  public long id;

  @Constraints.Required
  public String name;

  @OneToMany(mappedBy = "publisher")
  public List<Game> publishers = new ArrayList<>();

  @OneToMany(mappedBy = "developer")
  public List<Game> developers = new ArrayList<>();

  public Compagny(String name) {
    this.name = name;
  }

  public static Finder<Long, Compagny> find = new Finder<>(Compagny.class);

  public static List<Compagny> findAll() {
    return find.all();
  }

  public static List<String> getStringList() {
    List<Compagny> list = findAll();
    List<String> results = new LinkedList<>();
    for (Compagny c : list)
      if (!results.contains(c.name))
        results.add(c.name);
    return results;
  }
}
