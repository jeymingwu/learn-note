package mybatis.jdbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jeymingwu
 * @date 2020/9/17 16:36
 */
public class JdbcExample {

    private static final String CLASSNAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8" +
            "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&nullCatalogMeansCurrent=true&zeroDateTimeBehavior=convertToNull";
    private static final String ACCOUNT = "root";
    private static final String PASSWORD = "123456";

    public Connection getConnection () {
        Connection connection = null;
        try {
            Class.forName(CLASSNAME);
            connection = DriverManager.getConnection(URL, ACCOUNT, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        return connection;
    }

    public Item getItem(Integer id) {
        Connection connection = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM items WHERE id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                return Item.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .city(rs.getString("city"))
                        .price(rs.getInt("price"))
                        .number(rs.getInt("number"))
                        .picture(rs.getString("picture"))
                        .build();
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(rs, ps, connection);
        }
        return null;
    }

    public void close(ResultSet rs, Statement statement, Connection connection) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JdbcExample jdbcExample = new JdbcExample();
        Item item = jdbcExample.getItem(1);
        System.out.println(item.toString());
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Item {

    private Integer id;
    private String name;
    private String city;
    private Integer price;
    private Integer number;
    private String picture;
}
