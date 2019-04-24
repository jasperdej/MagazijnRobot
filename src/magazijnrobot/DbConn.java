package magazijnrobot;

import java.sql.*;

public class DbConn {
        private static Connection con;

        public static void dbConnect() {
                try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wideworldimporters?useTimezone=true&serverTimezone=UTC", "root", "");
                } catch (Exception e) {
                        System.out.println("zet je XAMPP aan idioot ;)");
                }
        }

        public static void dbKill() {
                try {
                        con.close();
                } catch (Exception e) {
                        System.out.println(e);
                }
        }

        public Article[][] getDb(String sql) {
                 Article[][] articles = {};
                try {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);



                        stmt.close();
                        DbConn.dbKill();
                } catch (Exception e) {
                        System.out.println(e);
        }
                return articles;

        // Testen of de database connectie werkt
//        public static void main(String args[]) {
//                DbConn.dbConnect();
//                try {
//                        Statement stmt = con.createStatement();
//                        ResultSet rs = stmt.executeQuery("select * from stockitemholdings");
//                        while (rs.next())
//                                System.out.println("ID: " + rs.getInt(1) + "  Aantal: " + rs.getString(2) + "  Locatie: " + rs.getString(3) + "  StockBefore: " + rs.getString(4));
//                        stmt.close();
//                        DbConn.dbKill();
//                } catch (Exception e) {
//                        System.out.println(e);
//                }
//
//
        }
}


