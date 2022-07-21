package de.thedesigncraft.mattibot.manage;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LiteSQL {

    private static Connection conn;

    private static Statement stmt;

    public static void connect() {

        conn = null;

        try {

            File file = new File("database.db");
            if(!file.exists()) {

                file.createNewFile();

            }

            conn = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
            org.slf4j.Logger logger = LoggerFactory.getLogger(LiteSQL.class);
            logger.info("StatusUpdate: Online");

            stmt = conn.createStatement();

        } catch (SQLException | IOException e) {

            e.printStackTrace();

        }

    }

    public static void disconnect() {

        try {

            if(conn != null) {

                conn.close();
                org.slf4j.Logger logger = LoggerFactory.getLogger(LiteSQL.class);
                logger.info("StatusUpdate: Offline");

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static void onUpdate(String sql) {

        try {

            stmt.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static ResultSet onQuery(String sql) {

        try {

            return stmt.executeQuery(sql);

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

}
