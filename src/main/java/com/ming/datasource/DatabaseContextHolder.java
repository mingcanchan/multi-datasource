package com.ming.datasource;

/**
 * 作用：
 * 1)保存一个线程安全的DatabaseType容器
 *
 * @author chenmingcan
 */
public class DatabaseContextHolder {

    private static final ThreadLocal<DatabaseType> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDatabaseType(DatabaseType type) {
        CONTEXT_HOLDER.remove();
        CONTEXT_HOLDER.set(type);
    }

    static DatabaseType getDatabaseType() {
        return CONTEXT_HOLDER.get();
    }
}
