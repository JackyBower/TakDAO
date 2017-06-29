package com.tak.dao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tak.dao.dao.IUserDao;
import com.tak.dao.dao.UserDaoImpl;
import com.tak.dao.entity.User;
import com.tak.daocore.dao.impl.BaseDaoImpl;
import com.tak.daocore.dao.impl.query.QueryCondition;
import com.tak.daocore.dao.impl.query.QueryOperator;
import com.tak.daocore.dao.impl.query.param.QueryParam;
import com.tak.daocore.dao.impl.result.QueryResult;
import com.tak.daocore.db.DBHelper;
import com.tak.daocore.model.Paging;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private IUserDao userDao;

    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = DBHelper.getInstance();

        //获取BaseDaoImpl对象
        BaseDaoImpl baseDao = dbHelper.getBaseDao(this, User.class);

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
    }
}
