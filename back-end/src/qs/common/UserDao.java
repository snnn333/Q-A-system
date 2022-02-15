package qs.common;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javax.crypto.Cipher;
import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.security.KeyPairGenerator;

/**
 * DAO的子类，对User进行操作操作
 */
public class UserDao extends DAO{
    public UserDao(){
        super();
    }

    /**
     *数据库自动生成Id
     * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException{
        String sql = "select max(id) from User";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }

    /**
     *建立一个User对象
     * @param mail
     * @return
     * @throws SQLException
     */
    public synchronized Long createUser(String mail) throws SQLException{
        String sql = "insert into User(id,mail) values (?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        Long id = nextId();
        ptmt.setLong(1,id);
        ptmt.setString(2,mail);
        if(ptmt.executeUpdate() > 0)
            return id;
        else throw new SQLException();
    }

    /**
     *删除一个User对象
     * @param id
     * @return
     * @throws SQLException
     */
    public Boolean delete(Long id) throws SQLException{
        String sql = "delete from User where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *根据id查找一个User对象
     * @param id
     * @return
     * @throws SQLException
     */
    public  User selectById(Long id) throws SQLException{
        String sql = "select * from User where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        ResultSet rs = ptmt.executeQuery();
        User user = null;
        while(rs.next()){
            user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setMail(rs.getString("mail"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getLong("role"));
            user.setScore(rs.getLong("score"));
            user.setPhoto(rs.getString("photo"));
            user.setNumOfQuery(rs.getLong("numOfQuery"));
            user.setNumOfAnswer(rs.getLong("numOfAnswer"));
            user.setCannotSpeak(rs.getLong("cannotSpeak"));
            user.setCannotLogin(rs.getLong("cannotLogin"));
            user.setAge(rs.getLong("age"));
        }
        return user;
    }

    /**
     *根据mail一个User对象
     * @param mail
     * @return
     * @throws Exception
     */
    public  User selectByMail(String mail) throws Exception{
        String sql = "select * from User where mail = ?;";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1,mail);
        User user = null;
        ResultSet rs = ptmt.executeQuery();
        while(rs.next()){
            user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setMail(rs.getString("mail"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getLong("role"));
            user.setScore(rs.getLong("score"));
            user.setPhoto(rs.getString("photo"));
            user.setNumOfQuery(rs.getLong("numOfQuery"));
            user.setNumOfAnswer(rs.getLong("numOfAnswer"));
            user.setCannotSpeak(rs.getLong("cannotSpeak"));
            user.setCannotLogin(rs.getLong("cannotLogin"));
            user.setAge(rs.getLong("age"));
        }
        return user;
    }

    /**
     *更新User某一信息
     * @param id
     * @param attribute
     * @param value
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Boolean update(Long id,String attribute,T value) throws SQLException{
        String sql = "update User set " + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *更新用户名
     * @param id
     * @param name
     * @return
     * @throws SQLException
     */
    public Boolean updateName(Long id,String name) throws SQLException{
        String sql = "update User set name = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1,name);
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *更新用户电话
     * @param id
     * @param phone
     * @return
     * @throws SQLException
     */
    public Boolean updatePhone(Long id,String phone) throws SQLException{
        String sql = "update User set phone = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1,phone);
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *更新用户邮箱
     * @param id
     * @param mail
     * @return
     * @throws SQLException
     */
    public Boolean updateMail(Long id,String mail) throws SQLException{
        String sql = "update User set mail = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1,mail);
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *返回用户提问的个数
     * @param userId
     * @return
     * @throws SQLException
     */
    public Long getNumOfQuery(Long userId) throws SQLException{
        String sql = "select count(*) from Question where userId = " +
                userId + " and isReleased = 1";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        while(rs.next()){
            return rs.getLong(1);
        }
        return new Long(0);
    }

    /**
     *返回用户回复的个数
     * @param userId
     * @return
     * @throws SQLException
     */
    public Long getNumOfAnswer(Long userId) throws SQLException{
        String sql = "select count(*)  from Answer where userId = " +
                userId + " and isReleased = 1";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        while(rs.next()){
            return rs.getLong(1);
        }
        return new Long(0);
    }


}
