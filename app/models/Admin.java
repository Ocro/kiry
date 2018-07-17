package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints;
import io.ebean.*;

@Entity
@Table(name = "admin")
public class Admin extends Model {
  
  @Id
  @Constraints.Required
  public String email;

  public Admin(String email) {
    this.email = email;
  }
  
  public static Finder<String, Admin> find = new Finder<>(Admin.class);
  
  public static boolean isAdmin(User user) {
    return find.query().where().eq("email", user.email).findList().get(0) != null;
  }
}
