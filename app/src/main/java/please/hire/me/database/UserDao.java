package please.hire.me.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import please.hire.me.model.User;

@Dao
public interface UserDao {

    @Query("select * from user order by id")
    List<User> loadAllUser();

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void delete(User user);

    @Query("select * from user where id = :id")
    User loadUserById(int id);

    @Query("select * from user where username = :username and password = :password")
    User selectUser(String username, String password);

}
