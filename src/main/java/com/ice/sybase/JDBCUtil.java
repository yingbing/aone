package com.ice.sybase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtil {

    private static HikariDataSource dataSource;

    // 静态代码块，用于初始化连接池
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sybase:Tds:your_host:your_port/your_database");
        config.setUsername("your_username");
        config.setPassword("your_password");
        config.setDriverClassName("com.sybase.jdbc4.jdbc.SybDriver");

        // 可选配置
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);

        // 初始化数据源
        dataSource = new HikariDataSource(config);
    }

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 关闭资源的方法（连接、语句和结果集）
    public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close(); // 注意，这里不会真正关闭连接，而是将连接返回给连接池
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 关闭连接和语句（用于更新操作）
    public static void close(Connection conn, PreparedStatement stmt) {
        close(conn, stmt, null);
    }

    // 关闭数据源（应用程序结束时调用）
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    // 执行查询的方法
    public static void executeQuery(String sql, QueryHandler handler, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 设置查询参数
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                // 处理查询结果
                handler.handle(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 执行更新操作（INSERT、UPDATE、DELETE）
    public static int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 设置更新参数
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 查询结果处理接口
    public interface QueryHandler {
        void handle(ResultSet rs) throws SQLException;
    }
}