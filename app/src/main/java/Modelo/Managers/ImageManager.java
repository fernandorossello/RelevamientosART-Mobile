package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import Helpers.DBHelper;
import Modelo.Image;


public class ImageManager extends Manager<Image> {

    public ImageManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void persist(Image item) throws SQLException {
        Dao daoImagenes = dbHelper.getImageDao();

        if (!daoImagenes.idExists(item.id)) {
            daoImagenes.create(item);
        } else {
            daoImagenes.update(item);
        }
    }
}
