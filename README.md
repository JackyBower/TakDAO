# TakDAO

TakDAO是一个快速&轻量级的Android SQLite ORM映射框架，尽可能的简化数据库操作。
宗旨在使其尽可能容易地与SQLite数据库一起工作，同时仍然能够实现原始SQL的强大
功能和灵活性，可以轻松地读取和写入数据，而不会有一堆凌乱的SQL字符串。


## 特征
1. TakDAO简化了数据库的增删改查操作.
2. 通过注解的方式进行数据库对象映射.
3. 也可以自定义实现复杂的业务逻辑处理.


## 使用示例
  添加依赖
  Gradle：
   ```java
  compile 'com.tak.daocore:TakDAO:1.0.0'
  ```
  Maven：
  ```java
  <dependency>
    <groupId>com.tak.daocore</groupId>
    <artifactId>TakDAO</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
  </dependency>
   ```
1. 基本操作

   ```java
   String id = UUID.randomUUID().toString();
   
   User user = new User();
   user.setId(id);
   user.setStatus("1");
   user.setName("test");

   //保存
   baseDao.save(user, false);

   //查看
   User user1 = (User) baseDao.get(id);

   //查询所有
   List<User> userList = baseDao.findAll();

   //组装查询参数
   QueryParam param = new QueryParam("status", QueryOperator.EQ, "1");
   //构建查询条件
   QueryCondition condition = new QueryCondition();
   //默认分页大小
   condition.setPaging(new Paging());
   //排序字段
   condition.setOrderBy("name", QueryCondition.DIRECTION.DESC);
   condition.setParam(param);
   //获取当前数据
   QueryResult<User> result = baseDao.find(condition);
   result.getRows();
   result.getTotal();

   //自定义dao实现复杂的sql
   userDao = new UserDaoImpl(this);

   List<User> list = userDao.findUserByList(id, "test");
   ```
   
2. 自定义DAO，
   ```java
   //继承IBaseDao
   public interface IUserDao extends IBaseDao<User>{
   
       List<User> findUserByList(String id,String name);
   }

   //继承BaseDaoImpl,实现当前IUserDao接口
   public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {
 
     public UserDaoImpl(Context context) {
         //通过DBHelper.getInstance()获取getSQLiteDBHelper()对象
         super(DBHelper.getInstance().getSQLiteDBHelper(), User.class);
     }
 
     @Override
     public List<User> findUserByList(String id, String name) {
         //实现复杂的业务逻辑
         return null;
     }
   }

   ```

## 使用方式

1. 实体的创建,通过注解来创建表名或字段,对于Date或Boolean，在数据库内部表示用整形（INTEGER）来表示，
   ```java
   @Table(name = "hw_user")
   public class User extends DefaultEntity{
   
       /**
        * 名称
        */
       @Column(name = "name", length = 100)
       private String name;
   
       /**
        * 状态(0代表未激活,1代表激活)
        */
       @Column(name = "status", length = 20)
       private String status;
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   
       public String getStatus() {
           return status;
       }
   
       public void setStatus(String status) {
           this.status = status;
       }
   }
   ```

2. 获取DBHelper单例对象
   ```java
   DBHelper dbHelper = DBHelper.getInstance();
   ```
   通过使用下面这个方法获取BaseDaoImpl对象，如果表不存在则自动创建该表；
   ```java
    BaseDaoImpl baseDao = dbHelper.getBaseDao(this, User.class); 
   ```
   
3. IBaseDao提供了以下方法进行数据库的增删改查等操作
   ```java
   public interface IBaseDao<T> {
   
       /**
        * 获取数据库操作对象
        */
       SQLiteOpenHelper getDbHelper();
   
       /**
        * 默认主键自增,调用insert(T,true);
        *
        * @param entity
        * @return
        */
       long save(T entity);
   
       /**
        * 插入实体类
        *
        * @param entity
        * @param flag   flag为true是自动生成主键,flag为false时需手工指定主键.
        * @return
        */
       long save(T entity, boolean flag);
   
       /**
        * 根据ID删除
        *
        * @param id
        */
       void delete(String id);
   
       /**
        * 删除多条数据
        *
        * @param ids
        */
       void delete(String... ids);
   
       /**
        * 删除所有数据
        */
       void deleteAll();
   
       /**
        * 更新实体类来
        *
        * @param entity
        */
       void update(T entity);
   
       /**
        * 根据ID来查找
        *
        * @param id
        * @return
        */
       T get(String id);
   
       /**
        * 查询当前数据
        *
        * @param sql
        * @param selectionArgs
        * @return
        */
       List<T> rawQuery(String sql, String[] selectionArgs);
   
       /**
        * 查询所有数据
        *
        * @return
        */
       List<T> findAll();
   
       /**
        * 根据条件来查询
        *
        * @param columns
        * @param selection
        * @param selectionArgs
        * @param groupBy
        * @param having
        * @param orderBy
        * @param limit
        * @return
        */
       List<T> find(String[] columns, String selection,
                    String[] selectionArgs, String groupBy, String having,
                    String orderBy, String limit);
   
       /**
        * 判断当前表是否存在
        * @return
        */
       boolean isTableExist();
   
       /**
        * 判断表是否存在
        *
        * @param sql
        * @param selectionArgs
        * @return
        */
       boolean isExist(String sql, String[] selectionArgs);
   
       /**
        * 将查询的结果保存为名值对map.
        *
        * @param sql           查询sql
        * @param selectionArgs 参数值
        * @return 返回的Map中的key全部是小写形式.
        */
       List<Map<String, String>> query2MapList(String sql, String[] selectionArgs);
   
       /**
        * 封装执行sql代码.
        *
        * @param sql
        * @param selectionArgs
        */
       void executeJPQL(String sql, Object[] selectionArgs);
   
       /**
        * 根据条件查询数据
        *
        * @param condition
        * @return
        * @throws DAOException
        */
       QueryResult<T> find(QueryCondition condition);
   
       /**
        * 根据条件查询数据
        *
        * @param namedWhereClause
        * @param nameOrderbyClause
        * @param paging
        * @param params
        * @return
        */
       QueryResult<T> find(String namedWhereClause, String nameOrderbyClause, Paging paging, Map<String, Object> params) throws DAOException;
   
       /**
        * 统一关闭数据库连接
        */
       void closeDB();
   }
   ```
   
## Thanks
   [https://github.com/smuyyh/easyDAO](easyDao)
   


## License
Copyright 2014 KitTak
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.