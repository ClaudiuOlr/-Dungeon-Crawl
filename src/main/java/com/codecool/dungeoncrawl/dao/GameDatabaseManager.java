package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class GameDatabaseManager {
    private PlayerDao playerDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
    }

    public void savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        ApplicationProperties properties = new ApplicationProperties();

        dataSource.setDatabaseName(properties.readProperty("DB_NAME"));
        dataSource.setUser(properties.readProperty("USER"));
        dataSource.setPassword(properties.readProperty("PASSWORD"));

        System.out.println("Trying to connect...");
        dataSource.getConnection();
        System.out.println("Connection OK");

        return dataSource;
    }
}
