package com.edison.db.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @ClassName: DBService
 * @description:
 * @author: edison_Kwok
 * @Date: create in 2019/5/6 18:24
 * @Version: 1.0
 */
public class DBService {

    /**
     * @Author edison_Kwok
     * @Description //TODO 达到多线程操作
     * @Date 2019/5/6
     **/
    private ExecutorService executorService=Executors.newCachedThreadPool();

    private DBExecutor dbExecutor=new DBExecutor(DbConfig.createDB());

    public Boolean createTable(String tableName, Set<String> columnNameSet){
        Future submit = executorService.submit(dbExecutor.new DBCreateTable(tableName, columnNameSet));
        try {
            return (Boolean) submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setPrimaryKeyColumnName(String tableName,String primaryKeyColumnName){
        executorService.submit(dbExecutor.new DBSetPrimaryKeyColumnName(tableName,primaryKeyColumnName));
    }

    public Integer insert(String tableName,Map<String,String> insertMap){
        Future future = executorService.submit(dbExecutor.new DBInsert(tableName, insertMap));
        try {
            return (Integer) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer delete(String tableName,String primaryKeyColumnValue){
        Future future = executorService.submit(dbExecutor.new DBDelete(tableName, primaryKeyColumnValue));
        try {
            return (Integer) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer update(String tableName,Map<String,String> updateMap){
        Future future = executorService.submit(dbExecutor.new DBUpdate(tableName, updateMap));
        try {
            return (Integer) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String,String>> find(String tableName, String searchColumn, String searchValue){
        Future future = executorService.submit(dbExecutor.new DBFind(tableName, searchColumn, searchValue));
        try {
            return (List<Map<String, String>>) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String,String>> findLike(String tableName,String searchColumn,String searchValueLike){
        Future future = executorService.submit(dbExecutor.new DBFindLike(tableName, searchColumn, searchValueLike));
        try {
            return (List<Map<String, String>>) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
