package com.ice.sybase;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericQueryHandler implements JDBCUtil.QueryHandler {

    private List<Map<String, Object>> results;

    public GenericQueryHandler() {
        this.results = new ArrayList<>();
    }

    @Override
    public void handle(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            results.add(row);
        }
    }

    public List<Map<String, Object>> getResults() {
        return results;
    }
}