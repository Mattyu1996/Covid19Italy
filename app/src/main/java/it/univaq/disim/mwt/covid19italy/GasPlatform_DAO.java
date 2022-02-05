package it.univaq.disim.mwt.covid19italy;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface GasPlatform_DAO {

@Query("SELECT * FROM trivelle ORDER BY denominazione ASC")
List<GasPlatform> getAll();

@Insert(onConflict = OnConflictStrategy.REPLACE)
List<Long> save(List<GasPlatform> gasPlatforms);
}
