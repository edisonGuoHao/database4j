package com.edison.db.config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: DbConfig
 * @description:
 * @author: edison_Kwok
 * @Date: create in 2019/5/6 16:24
 * @Version: 1.0
 */
public class DbConfig {

    /**
     * @Author edison_Kwok
     * @Description //TODO 主键列表
     * @Date 2019/5/6
     **/
    private volatile ConcurrentHashMap<String,String> primaryKeyColumnNameMap=new ConcurrentHashMap<>();


    /**
     * @Author edison_Kwok
     * @Description //TODO 数据库主存储结构
     * @Date 2019/5/6
     **/
    private  volatile ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<String,String>>> tables=new ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<String,String>>>();


    /**
     * @Author edison_Kwok
     * @Description //TODO 设置主键
     * @Date 2019/5/6
     * @Param []
     * @Return void
     **/
    public void setPrimaryKeyColumnName(String tableName,String primaryKeyColumnName){
        //先得到数据表
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> table = tables.get(tableName);
        if(table==null){
            throw new RuntimeException("数据库不存在");
        }
        for (String columnName : table.keySet()) {
            if(!primaryKeyColumnName.equals(columnName)){
                throw new RuntimeException("该主键字段不存在！");
            }
        }
        this.primaryKeyColumnNameMap.put(tableName,primaryKeyColumnName);
    }


    /**
     * @Author edison_Kwok
     * @Description //TODO 新建数据库
     * @Date 2019/5/6
     * @Param 
     * tableName : 数据库表明
     * columnNameSet ：数据库类名set集合
     * @Return boolean
     **/
    public Boolean createTable(String tableName, Set<String> columnNameSet){
        //新建数据表
        ConcurrentHashMap<String,ConcurrentHashMap<String,String>> table=new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
        //将类名加入进去
        for (String columnName : columnNameSet) {
            ConcurrentHashMap<String,String> columnMap=new ConcurrentHashMap<String,String>();
            table.put(columnName,columnMap);
        }
        return true;
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO
     * @Date 2019/5/6
     * @Param
     * tableName :表名
     * insertMap ：插入数据map
     * @Return java.lang.Integer
     **/
    public Integer insert(String tableName,Map<String,String> insertMap){
        //获取主键字段
        String primaryKeyColumnName= this.primaryKeyColumnNameMap.get(tableName);
        //先获取主键值
        String primaryKey = insertMap.get(primaryKeyColumnName);
        //先得到数据表
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> table = tables.get(tableName);
        //根据列名添加数据到数据表
        for (String columnName : table.keySet()) {
            ConcurrentHashMap<String, String> columnMap = table.get(columnName);
            if(primaryKeyColumnName.equals(columnName)){
                columnMap.put(primaryKeyColumnName,primaryKey);
            }
            String columnValue = insertMap.get(columnName);
            if(columnValue!=null){
                columnMap.put(primaryKey,columnValue);
            }
        }
        return 1;
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 删除数据
     * @Date 2019/5/6
     * @Param
     * tableName : 表名
     * primaryKeyColumnValue ：主键值
     * @Return java.lang.Integer
     **/
    public Integer delete(String tableName,String primaryKeyColumnValue){
        //得到数据库表
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> table = tables.get(tableName);
        //删除结构中所有key为primaryKeyColumnValue的entity
        for (String columnName : table.keySet()) {
            ConcurrentHashMap<String, String> columnMap = table.get(columnName);
            if(!columnMap.isEmpty()){
                columnMap.remove(primaryKeyColumnValue);
            }
        }
        return 1;
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 修改数据
     * @Date 2019/5/6
     * @Param
     * tableName :表名
     * updateMap ：修改数据Map
     * @Return java.lang.Integer
     **/
    public Integer update(String tableName,Map<String,String> updateMap){
        //获取主键字段
        String primaryKeyColumnName= this.primaryKeyColumnNameMap.get(tableName);
        //先获取主键值
        String primaryKey = updateMap.get(primaryKeyColumnName);
        if(primaryKey==null){
            throw new RuntimeException("修改主键不能为空");
        }
        //得到数据库表
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> table = tables.get(tableName);
        //根据updateMap获取修改数据表中字段
        for (String columnName : updateMap.keySet()) {
            ConcurrentHashMap<String, String> columnMap = table.get(columnName);
            if(columnMap==null){
                //修改语句包含不存在的字段
                return 0;
            }
            String updateValue = updateMap.get(columnName);
            columnMap.put(primaryKey,updateValue);
        }
        return 1;
    }


    /**
     * @Author edison_Kwok
     * @Description //TODO 精准查询
     * @Date 2019/5/6
     * @Param
     * tableName : 表名
     * searchColumn ：搜索列名
     * searchValue ：搜索值
     **/
    public List<Map<String,String>> find(String tableName,String searchColumn,String searchValue){
        //返回list表
        List<Map<String,String>> result=new ArrayList<>();
        //满足条件的主键
        Set<String> primaryKeyResult =new HashSet<>();
        //获取数据表
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> table = tables.get(tableName);
        //根据列名查找主键值
        ConcurrentHashMap<String, String> searchColumnMap = table.get(searchColumn);
        for (String primaryKey : searchColumnMap.keySet()) {
            String columnValue = searchColumnMap.get(primaryKey);
            if(searchValue.equals(columnValue)){
                primaryKeyResult.add(primaryKey);
            }
        }
        //将相关值包装成为List
        for (String primaryKey : primaryKeyResult) {
            Map<String, String> map = getEntityByPrimaryKey(table, primaryKey);
            result.add(map);
        }
        return result;
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 模糊查询
     * @Date 2019/5/6
     * @Param [tableName, columnName, columnValueLike]
     * @Return java.util.List
     **/
    public List<Map<String,String>> findLike(String tableName,String searchColumn,String searchValueLike){
        //返回list表
        List<Map<String,String>> result=new ArrayList<>();
        //满足条件的主键
        Set<String> primaryKeyResult =new HashSet<>();
        //获得数据表
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> table = tables.get(tableName);
        //查找符合条件的主键
        ConcurrentHashMap<String, String> searchColumnMap = table.get(searchColumn);
        //获取模糊查询方式
        Integer likeWay = this.tellTheLike(searchValueLike);
        if(likeWay==4){
            return null;
        }
        //获取模糊查询字段
        String searchValue = this.trimTheLike(searchValueLike);

        for (String primaryKey : searchColumnMap.keySet()) {
            String columnValue = searchColumnMap.get(primaryKey);
            if(likeWay==1){
                //以这个结尾
                if (columnValue.endsWith(searchValue)) {
                    primaryKeyResult.add(primaryKey);
                }
            }else if(likeWay==2){
                //以这个开头
                if(columnValue.startsWith(searchValue)){
                    primaryKeyResult.add(primaryKey);
                }
            }else if(likeWay==3){
                //包含
                if(columnValue.contains(searchValue)){
                    primaryKeyResult.add(primaryKey);
                }
            }
        }
        //包装成为一个list
        for (String primaryKey : primaryKeyResult) {
            Map<String, String> map = getEntityByPrimaryKey(table, primaryKey);
            result.add(map);
        }
        return result;
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 在表中根据主键查询整行信息
     * @Date 2019/5/6
     * @Param [table, primaryKey]
     * @Return java.util.Map<java.lang.String,java.lang.String>
     **/
    private Map<String, String> getEntityByPrimaryKey(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> table, String primaryKey) {
        Map<String,String> map=new HashMap<>();
        for (String columnName : table.keySet()) {
            ConcurrentHashMap<String, String> columnMap = table.get(columnName);
            String columnValue = columnMap.get(primaryKey);
            map.put(columnName,columnValue);
        }
        return map;
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 分辨是什么类型模糊查询
     * @Date 2019/5/6
     * @Param [searchValueLike]
     * @Return java.lang.Integer
     **/
    private Integer tellTheLike(String searchValueLike){
        boolean startsWith = searchValueLike.startsWith("%");
        boolean endsWith = searchValueLike.endsWith("%");
        if(!endsWith&&startsWith){
            //开头有
            return 1;
        }else if (!startsWith&&endsWith){
            //结尾有
            return 2;
        }else if(startsWith&&endsWith){
            //前后都有
            return 3;
        }
        return 4;
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 抽离出模糊查询词
     * @Date 2019/5/6
     * @Param [searchValueLike]
     * @Return java.lang.String
     **/
    private String trimTheLike(String searchValueLike){
        Integer likeFlag = this.tellTheLike(searchValueLike);
        if(likeFlag==3){
            return searchValueLike.substring(1,searchValueLike.length()-1);
        }else if(likeFlag==2){
            return searchValueLike.substring(0,searchValueLike.length()-1);
        }else if(likeFlag==1){
            return searchValueLike.substring(1);
        }else {
            return searchValueLike;
        }
    }


    private DbConfig() {
    }

    private static DbConfig dbConfig;

    public static DbConfig createDB(){
        if(dbConfig==null){
            dbConfig=new DbConfig();
        }
        return dbConfig;
    }
}
