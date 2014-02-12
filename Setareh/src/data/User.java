package data;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
  @Id @GeneratedValue
  Long id;
  public Long getId() {return id;}
  public void setId(Long id) {this.id = id;}

  String name;
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}
  
  String password;
  public String getPassword() {return password;}
  public void setPassword(String password) {this.password = password;}

  
}