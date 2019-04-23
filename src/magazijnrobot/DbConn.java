package magazijnrobot;

import java.sql.*;

class DbConn {
        private static Connection con;

        public static void dbConnect() {
                try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wideworldimporters", "root", "");
                } catch (Exception e) {
                        System.out.println(e);
                }
        }

        public static void dbKill() {
                try {
                        con.close();
                } catch (Exception e) {
                        System.out.println(e);
                }
        }

        // Testen of de database connectie werkt
        public static void main(String args[]) {
                DbConn.dbConnect();
                try {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery("select * from orders");
                        while (rs.next())
                                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));
                        stmt.close();
                        DbConn.dbKill();
                } catch (Exception e) {
                        System.out.println(e);
                }


        }
}


