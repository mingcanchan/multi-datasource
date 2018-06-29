package com.ming.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源（需要继承AbstractRoutingDataSource）
 * 在调用mapper之前设置  DynamicDataSource.setDatabaseType(databaseType);
 *
 * @author chenmingcan
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DatabaseType> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDatabaseType(DatabaseType type) {
        CONTEXT_HOLDER.remove();
        CONTEXT_HOLDER.set(type);
    }

    private static DatabaseType getDatabaseType() {
        return CONTEXT_HOLDER.get();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDatabaseType();
    }
}
