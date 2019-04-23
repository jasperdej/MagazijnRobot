package magazijnrobot;

import java.sql.*;

class DbConn{
public static void main(String args[]){
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wideworldimporters","root","");

        // Test om te kijken of connectie werkt
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from orders");
        while(rs.next())
        System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        con.close();


        }catch(Exception e){ System.out.println(e);}
        }
        }

