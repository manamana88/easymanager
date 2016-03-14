/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.classes;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author vincenzo
 */
@Entity
@Table(name="account")
public class AccountEmail implements Serializable{
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String smtp;

    public AccountEmail() {
    }

    public AccountEmail(Long id, String username, String password, String smtp) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.smtp = smtp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AccountEmail{" + "id=" + id + "username=" + username + "password=" + password + "smtp=" + smtp + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountEmail other = (AccountEmail) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if ((this.smtp == null) ? (other.smtp != null) : !this.smtp.equals(other.smtp)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 29 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 29 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 29 * hash + (this.smtp != null ? this.smtp.hashCode() : 0);
        return hash;
    }

    
}
