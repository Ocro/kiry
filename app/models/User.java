package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Constraints;
import io.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity
@Table(name = "account")
public class User extends Model {

  @Id
  @Constraints.Required
  public String email;

  @OneToMany(mappedBy = "account")
  public List<UserWishGame> userwishgames;

  @Constraints.Required
  public String password;

  @Constraints.Required
  public String name;

  public String authkey;

  @Constraints.Required
  @Temporal(TemporalType.TIMESTAMP)
  public Date datesubscription;

  @OneToOne(mappedBy = "user")
  public Configuration configuration;

  public User(String email, String password, String name) {
    //if (findByEmail(email) != null)
      //throw new IllegalArgumentException("L'adresse e-mail existe déjà!");

    if (email == "" || password == "" || name == "")
      throw new IllegalArgumentException("Certain champs sont vide!");

    this.email = email;
    this.password = /*play.libs.Crypto.sign(*/password/*)*/;
    this.name = name;
    this.datesubscription = new Date();
  }

  // -- Queries

  public static Finder<String, User> find = new Finder<>(User.class);

  /**
   * Retrieve all users.
   */
  public static List<User> findAll() {
    return find.all();
  }

  /**
   * Retrieve a User from email.
   */
  public static User findByEmail(String email) {
    return new User("david@ddd.com", "1234", "David");
    //return find.query().where().eq("email", email).findList().get(0);
  }

  /**
   * Retrieve a User from auth key.
   */
  public static User findByAuthKey(String authKey) {
    return new User("david@ddd.com", "1234", "David");
    //return find.query().where().eq("authkey", authKey).findList().get(0);
  }

  /**
   * Authenticate a User.
   */
  public static User authenticate(String email, String password) {
    return find.query().where().eq("email", email)
        .eq("password", /*play.libs.Crypto.sign(*/password/*)*/).findList().get(0);
  }

  // --

  public String toString() {
    return "User(" + email + ", " + name + ")";
  }

  public String getDecoratedName() {
    return name.substring(0, 1).toUpperCase().concat(name.substring(1));
  }
}
