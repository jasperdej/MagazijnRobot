import java.sql.*;

public class DbConn {
        private static Connection con;
        private Statement stmt;
        private ResultSet rs;

        public static void dbConnect() {
                try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wideworldimporters?useTimezone=true&serverTimezone=UTC", "root", "");
                } catch (Exception e) {
                        System.out.println("Error in connectie met database.");
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

        public void killStatement() {
                try{
                        stmt.close();
                        dbKill();
                } catch (SQLException sqle) {
                        System.out.println(sqle);
                }
        }

        public ResultSet getResultSetFromDb(String sql) {
                try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(sql);
                        return rs;
                } catch (Exception e) {
                        System.out.println(e);
                        return null;
                }
        }
}


