/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.tads.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ArtVin
 */
public class ConnectionFactory {
    private static Connection con = null;

    public static Connection getConnection(){
        if( con == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://"+ DBCredentials.ADDRESS + ":" + DBCredentials.PORT + "/",
                    DBCredentials.USER, DBCredentials.PASSWORD);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            //closing connection when the server is shutting down
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run(){
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
        }
        return con;
    }
}
