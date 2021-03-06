package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.ebean.*;

@Entity
@Table(name = "configuration")
public class Configuration extends Model {
  @Id
  public String account_email;

  public Short view;

  @OneToOne
  @JoinColumn(name = "account_email", insertable = false, updatable = false)
  public User user;

  public Configuration(String account_email) {
    this.account_email = account_email;
  }

  public static Finder<Short, Configuration> find = new Finder<>(Configuration.class);

  public static List<Configuration> findAll() {
    return find.all();
  }
}
