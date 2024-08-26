package com.ice.sybase;

import java.util.List;
import java.util.Map;

public class JDBCMain {

    public  void test1() {
        String sql = "SELECT column1, column2 FROM your_table WHERE condition_column = ?";
        JDBCUtil.executeQuery(sql, rs -> {
            while (rs.next()) {
                System.out.println(rs.getString("column1") + ", " + rs.getString("column2"));
            }
        }, "condition_value");

        String updateSql = "UPDATE your_table SET column1 = ? WHERE condition_column = ?";
        int rowsAffected = JDBCUtil.executeUpdate(updateSql, "new_value", "condition_value");
        System.out.println("Rows affected: " + rowsAffected);
    }

    public void test2() {
        String sql = "SELECT column1, column2 FROM your_table WHERE condition_column = ?";
        GenericQueryHandler handler = new GenericQueryHandler();
        JDBCUtil.executeQuery(sql, handler, "condition_value");

        // 获取结果
        List<Map<String, Object>> results = handler.getResults();
        for (Map<String, Object> row : results) {
            System.out.println("Column1: " + row.get("column1"));
            System.out.println("Column2: " + row.get("column2"));
        }
    }

    public void test3() {
        String sql = "SELECT column1, column2 FROM your_table WHERE condition_column = ?";
        GenericObjectQueryHandler<YourEntity> handler = new GenericObjectQueryHandler<>(YourEntity.class);
        JDBCUtil.executeQuery(sql, handler, "condition_value");

        // 获取结果
        List<YourEntity> results = handler.getResults();
        for (YourEntity entity : results) {
            System.out.println("Column1: " + entity.getColumn1());
            System.out.println("Column2: " + entity.getColumn2());
        }
    }
}

class YourEntity {
    @ColumnMapping("column1_db") // 这里的"column1_db" 是数据库中的列名
    private String column1;

    @ColumnMapping("column2_db")
    private String column2;

    // Getters and Setters
    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }
}
