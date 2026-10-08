package com.campuskart;
import java.sql.*;
public final class CleanupUtil{private CleanupUtil(){} public static void removeExpiredSoldListings(){try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM products WHERE status='SOLD' AND sold_at < NOW() - INTERVAL 2 DAY")){p.executeUpdate();}catch(SQLException ignored){}}}