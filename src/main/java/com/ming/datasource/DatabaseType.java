package com.ming.datasource;

/**
 * 列出所有的数据源key（常用数据库名称来命名）
 * 注意：
 * 1）这里数据源与数据库是一对一的
 * 2）DatabaseType中的变量名称就是数据库的名称
 * @author chenmingcan
 */
public enum DatabaseType {
    /**
     * 主数据源
     */
    master,
    /**
     * 其他数据源
     */
    other
}
