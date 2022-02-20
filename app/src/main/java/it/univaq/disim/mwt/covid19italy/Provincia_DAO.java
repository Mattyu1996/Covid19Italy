package it.univaq.disim.mwt.covid19italy;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface Provincia_DAO {

@Query("SELECT * FROM province ORDER BY sigla ASC")
List<Provincia> getAll();

@Insert(onConflict = REPLACE)
List<Long> save(List<Provincia> province);
}
