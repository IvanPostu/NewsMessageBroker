package com.ivan.custom_message_broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataAccessObject {
    private static final Logger log = LoggerFactory.getLogger(DataAccessObject.class);
    // private static final String DB_URL =
    //         "jdbc:sqlite:C:/Users/cheburashka/Desktop/NewsMessageBroker/database/local.running.db";


    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    // private Connection connection;

    // public DataAccessObject() {
    //     try {
    //         connection = DriverManager.getConnection(DB_URL);
    //     } catch (SQLException e) {
    //         log.error(e.getMessage());
    //     }
    // }

}
