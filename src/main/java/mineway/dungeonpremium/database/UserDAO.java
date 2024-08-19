package mineway.dungeonpremium.database;

import mineway.dungeonpremium.objects.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class UserDAO {

    private final String TABLE = "dungeonpremium_users";
    private Database database;
    private Logger logger;

    public UserDAO(Database database, Logger logger) {
        this.database = database;
        this.logger = logger;
        createTable();
    }

    public void createTable() {
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                    "uuid VARCHAR(100) NOT NULL PRIMARY KEY," +
                    "reward VARCHAR(100) NOT NULL" +
                    ");";
            try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
                statement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public User get(UUID uuid) {
        String sql = "SELECT * FROM `"+TABLE+"` WHERE uuid = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                logger.info("User '"+uuid+"' is loaded of DataBase.");
                return new User(
                        LocalDateTime.parse(resultSet.getString(2))
                );
            }else{
                return new User();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void asyncInsert(UUID uuid, User user) {
        CompletableFuture.runAsync(() -> {
            insert(uuid, user);
        });
    }

    public void insert(UUID uuid, User user) {
        String sql;
        sql = "REPLACE INTO " + TABLE + " VALUES(?,?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, user.getLastReward().toString());
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void remove(UUID uuid) {
        CompletableFuture.runAsync(() -> {
            String sql = "DELETE FROM " + TABLE + " WHERE uuid = ?";
            try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                statement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

}
