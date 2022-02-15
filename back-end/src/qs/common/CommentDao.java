package qs.common;


import java.sql.*;
import java.util.ArrayList;

/**
 * DAO的子类，对Comment进行操作操作
 */
public class CommentDao extends DAO{

    public CommentDao(){
        super();
    }

    /**
     *数据库自动生成id
     * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException{
        String sql = "select max(id) from Comment";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }

    /**
     *建立一个新评论
     * @param userId
     * @param answerId
     * @return
     * @throws SQLException
     */
    public synchronized Long createComment(Long userId,Long answerId) throws SQLException{
        String sql = "insert into Comment(id,userId,answerId) values (?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        Long id = nextId();
        ptmt.setLong(1,id);
        ptmt.setLong(2,userId);
        ptmt.setLong(3,answerId);
        if(ptmt.executeUpdate() > 0)
            return id;
        else throw new SQLException();
    }

    /**
     *通过id删除评论1
     * @param id
     * @return
     * @throws SQLException
     */
    public Boolean delete(Long id) throws SQLException{
        String sql = "delete from Comment where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *更新某一属性
     * @param id
     * @param attribute
     * @param value
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Boolean update(Long id,String attribute,T value) throws SQLException{
        String sql = "update Comment set " + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *通过id查找
     * @param id
     * @return
     * @throws SQLException
     */
    public Comment selectById(Long id) throws SQLException{
        String sql = "select * from Comment where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        ResultSet rs = ptmt.executeQuery();
        Comment comment = null;
        while(rs.next()){
            comment = new Comment();
            comment.setId(rs.getLong("id"));
            comment.setUserId(rs.getLong("userId"));
            comment.setAnswerId(rs.getLong("answerId"));
            comment.setContent(rs.getString("content"));
            comment.setTime(rs.getTimestamp("time"));
        }
        return comment;
    }

    /**
     *查找某一回复的所有评论信息
     * @param answerId
     * @return
     * @throws SQLException
     */
    public ArrayList<Comment> selectByAnswerId(Long answerId) throws SQLException{
        ArrayList<Comment> comments = new ArrayList<Comment>();
        String sql = "select * from Comment where  answerId = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,answerId);
        ResultSet rs = ptmt.executeQuery();
        Comment comment = null;
        while(rs.next()){
            comment = new Comment();
            comment.setId(rs.getLong("id"));
            comment.setUserId(rs.getLong("userId"));
            comment.setAnswerId(rs.getLong("answerId"));
            comment.setContent(rs.getString("content"));
            comment.setTime(rs.getTimestamp("time"));
            comments.add(comment);
        }
        return comments;
    }
}
