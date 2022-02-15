package qs.common;

import java.sql.*;
import java.util.ArrayList;

/**
 * DAO的子类，对SubComment进行操作操作
 */
public class SubCommentDao extends DAO{

    public SubCommentDao(){
        super();
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException{
        String sql = "select max(id) from SubComment";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }

    /**
     *
     * @param userId
     * @param commentId
     * @return
     * @throws SQLException
     */
    public synchronized Long createSubComment(Long userId,Long commentId) throws SQLException{
        String sql = "insert into SubComment(id,userId,commentId) values (?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        Long id = nextId();
        ptmt.setLong(1,id);
        ptmt.setLong(2,userId);
        ptmt.setLong(3,commentId);
        if(ptmt.executeUpdate() > 0)
            return id;
        else throw new SQLException();
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Boolean delete(Long id) throws SQLException{
        String sql = "delete from SubComment where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *
     * @param id
     * @param attribute
     * @param value
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Boolean update(Long id,String attribute,T value) throws SQLException{
        String sql = "update SubComment set " + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public SubComment selectById(Long id) throws SQLException{
        String sql = "select * from SubComment where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        ResultSet rs = ptmt.executeQuery();
        SubComment subComment = null;
        while(rs.next()){
            subComment = new SubComment();
            subComment.setId(rs.getLong("id"));
            subComment.setUserId(rs.getLong("userId"));
            subComment.setCommentId(rs.getLong("commentId"));
            subComment.setContent(rs.getString("content"));
            subComment.setTime(rs.getTimestamp("time"));
        }
        return subComment;
    }

    /**
     * 返回某一评论的全部子评论信息
     * @param commentId
     * @return
     * @throws SQLException
     */
    public ArrayList<SubComment> selectByCommentId(Long commentId) throws SQLException{
        ArrayList<SubComment> subComments = new ArrayList<SubComment>();
        String sql = "select * from SubComment where  commentId = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,commentId);
        ResultSet rs = ptmt.executeQuery();
        SubComment subComment = null;
        while(rs.next()){
            subComment = new SubComment();
            subComment.setId(rs.getLong("id"));
            subComment.setUserId(rs.getLong("userId"));
            subComment.setCommentId(rs.getLong("commentId"));
            subComment.setContent(rs.getString("content"));
            subComment.setTime(rs.getTimestamp("time"));
            subComments.add(subComment);
        }
        return subComments;
    }
}
