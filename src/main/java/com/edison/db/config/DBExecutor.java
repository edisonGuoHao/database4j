package com.edison.db.config;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @ClassName: DBExecutor
 * @description:
 * @author: edison_Kwok
 * @Date: create in 2019/5/6 18:28
 * @Version: 1.0
 */
public class DBExecutor {
    private DbConfig dbConfig;

    public DBExecutor(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }


    /**
     * @Author edison_Kwok
     * @Description //TODO 创建表
     * @Date 2019/5/6
     **/
    class DBCreateTable implements Callable {
        private String tableName;

        private Set<String> columnNameSet;

        public DBCreateTable(String tableName, Set<String> columnNameSet) {
            this.tableName = tableName;
            this.columnNameSet = columnNameSet;
        }

        @Override
        public Boolean call() throws Exception {
            return dbConfig.createTable(this.tableName, this.columnNameSet);
        }
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 增加数据
     * @Date 2019/5/6
     **/
    class DBInsert implements Callable {
        private String tableName;

        private Map<String, String> insertMap;

        public DBInsert(String tableName, Map<String, String> insertMap) {
            this.tableName = tableName;
            this.insertMap = insertMap;
        }

        @Override
        public Integer call() throws Exception {
            return dbConfig.insert(this.tableName, this.insertMap);
        }
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 根据主键删除数据
     * @Date 2019/5/6
     **/
    class DBDelete implements Callable {
        private String tableName;

        private String primaryKeyColumnValue;

        public DBDelete(String tableName, String primaryKeyColumnValue) {
            this.tableName = tableName;
            this.primaryKeyColumnValue = primaryKeyColumnValue;
        }

        @Override
        public Integer call() throws Exception {
            return dbConfig.delete(this.tableName, this.primaryKeyColumnValue);
        }
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 修改数据
     * @Date 2019/5/6
     **/
    class DBUpdate implements Callable {

        private String tableName;

        private Map<String, String> updateMap;

        public DBUpdate(String tableName, Map<String, String> updateMap) {
            this.tableName = tableName;
            this.updateMap = updateMap;
        }

        @Override
        public Integer call() throws Exception {
            return dbConfig.update(this.tableName, this.updateMap);
        }
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO
     * @Date 2019/5/6
     **/
    class DBFind implements Callable {
        private String tableName;
        private String searchColumn;
        private String searchValue;

        public DBFind(String tableName, String searchColumn, String searchValue) {
            this.tableName = tableName;
            this.searchColumn = searchColumn;
            this.searchValue = searchValue;
        }

        @Override
        public List call() throws Exception {
            return dbConfig.find(this.tableName, this.searchColumn, this.searchValue);
        }
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 模糊查询
     * @Date 2019/5/6
     **/
    class DBFindLike implements Callable {
        private String tableName;
        private String searchColumn;
        private String searchValueLike;

        public DBFindLike(String tableName, String searchColumn, String searchValueLike) {
            this.tableName = tableName;
            this.searchColumn = searchColumn;
            this.searchValueLike = searchValueLike;
        }

        @Override
        public List call() throws Exception {
            return dbConfig.findLike(this.tableName, this.searchColumn, this.searchValueLike);
        }
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 设置主键
     * @Date 2019/5/6
     **/
    class DBSetPrimaryKeyColumnName implements Runnable {
        private String tableName;
        private String primaryKeyColumnName;

        public DBSetPrimaryKeyColumnName(String tableName, String primaryKeyColumnName) {
            this.tableName = tableName;
            this.primaryKeyColumnName = primaryKeyColumnName;
        }

        @Override
        public void run() {
            dbConfig.setPrimaryKeyColumnName(this.tableName,this.primaryKeyColumnName);
        }
    }

}
