package please.hire.me.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        indices = @Index(
                value = {"username","nric","phone"},
                unique = true
        )
)

public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String username;
    private String password;
    private String nric;
    private String phone;

    public User(String name, String username, String password, String nric, String phone) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.nric = nric;
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
