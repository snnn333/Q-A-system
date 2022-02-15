package qs.common;


import java.sql.*;
import java.util.ArrayList;

/**
 * DAO的子类，对QuestionType进行操作操作
 */
public class QuestionTypeDao extends DAO{

    public QuestionTypeDao(){
        super();
    }

    /**
     * 数据库自动生成id
     * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException {
        String sql = "select max(id) from QuestionType";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }

    /**
     * 建立一个新问题类型
     * @param name
     * @return
     * @throws SQLException
     */
    public synchronized Long createQuestionType(String name) throws SQLException{
        String sql = "insert into QuestionType(id,name) values (?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        Long id = nextId();
        ptmt.setLong(1,id);
        ptmt.setString(2,name);
        if(ptmt.executeUpdate() > 0)
            return id;
        else throw new SQLException();
    }

    /**
     * 通过id删除问题类型
     * @param id
     * @return
     * @throws SQLException
     */
    public Boolean delete(Long id) throws SQLException{
        String sql = "delete from QuestionType where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     * 通过id查找问题类型信息
     * @param id
     * @return
     * @throws SQLException
     */
    public QuestionType selectById(Long id) throws SQLException{
        String sql = "select * from QuestionType where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        ResultSet rs = ptmt.executeQuery();
        QuestionType questionType = null;
        while(rs.next()){
            questionType = new QuestionType();
            questionType.setId(rs.getLong("id"));
            questionType.setName(rs.getString("name"));
            questionType.setIcon(rs.getString("icon"));
        }
        return questionType;
    }

    /**
     * 通过类型名查找问题类型信息
     * @param name
     * @return
     * @throws SQLException
     */
    public QuestionType selectByName(String name) throws SQLException{
        String sql = "select * from QuestionType where name = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, name);
        ResultSet rs = ptmt.executeQuery();
        QuestionType questionType = null;
        while(rs.next()){
            questionType = new QuestionType();
            questionType.setId(rs.getLong("id"));
            questionType.setName(rs.getString("name"));
            questionType.setIcon(rs.getString("icon"));
        }
        return questionType;
    }

    /**
     * 更新问题类型的某一属性
     * @param id
     * @param attribute
     * @param value
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Boolean update(Long id,String attribute,T value) throws SQLException{
        String sql = "update QuestionType set " + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }
}
