package please.hire.me.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import please.hire.me.model.ProductModel;

@Dao
public interface ProductDao {

    @Query("select * from productModel order by id")
    List<ProductModel> loadAllProduct();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(List<ProductModel> productModel);

    @Update
    void update(ProductModel productModel);

    @Delete
    void delete(ProductModel productModel);

    @Query("delete from productModel")
    void deleteAll();
}
