package qs.common;


import com.alibaba.fastjson.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO的子类，对Answer进行操作操作
 */
public class AnswerDao extends DAO{

    public AnswerDao(){
        super();
    }

    /**
     *数据库自动生成id
     * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException{
        String sql = "select max(id) from Answer";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }

    /**
     *建立一个新回复
     * @param questionId
     * @param userId
     * @return
     * @throws SQLException
     */
    public synchronized Long createAnswer(Long questionId,Long userId) throws SQLException{
        String sql = "insert into Answer(id,questionId,userId) values (?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        Long id = nextId();
        ptmt.setLong(1,id);
        ptmt.setLong(2,questionId);
        ptmt.setLong(3,userId);
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
        String sql = "delete from Answer where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *通过id查找回复
     * @param id
     * @return
     * @throws SQLException
     */
    public Answer selectById(Long id) throws SQLException{
        String sql = "select * from Answer where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        ResultSet rs = ptmt.executeQuery();
        Answer ans = null;
        while(rs.next()){
            ans = new Answer();
            ans.setId(rs.getLong("id"));
            ans.setUserId(rs.getLong("userId"));
            ans.setQuestionId(rs.getLong("questionId"));
            ans.setContent(rs.getString("content"));
            ans.setTime(rs.getTimestamp("time"));
            ans.setNumOfAgree(rs.getLong("numOfAgree"));
        }
        return ans;
    }

    /**
     *查找某一用户的所有回复信息
     * @param userId
     * @return
     * @throws SQLException
     */
    public ArrayList<Answer> selectByUserId(Long userId) throws SQLException{
        ArrayList<Answer> answers = new ArrayList<Answer>();
        String sql = "select * from Answer where  userId = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,userId);
        ResultSet rs = ptmt.executeQuery();
        Answer ans = null;
        while(rs.next()){
            ans = new Answer();
            ans.setId(rs.getLong("id"));
            ans.setUserId(rs.getLong("userId"));
            ans.setQuestionId(rs.getLong("questionId"));
            ans.setContent(rs.getString("content"));
            ans.setTime(rs.getTimestamp("time"));
            ans.setNumOfAgree(rs.getLong("numOfAgree"));
            answers.add(ans);
        }
        return answers;
    }

    /**
     *查找某一问题的所有回复信息
     * @param questionId
     * @return
     * @throws SQLException
     */
    public ArrayList<Answer> selectByQuestionId(Long questionId) throws SQLException{
        ArrayList<Answer> answers = new ArrayList<Answer>();
        String sql = "select * from Answer where  questionId = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,questionId);
        ResultSet rs = ptmt.executeQuery();
        Answer ans = null;
        while(rs.next()){
            ans = new Answer();
            ans.setId(rs.getLong("id"));
            ans.setUserId(rs.getLong("userId"));
            ans.setQuestionId(rs.getLong("questionId"));
            ans.setContent(rs.getString("content"));
            ans.setTime(rs.getTimestamp("time"));
            ans.setNumOfAgree(rs.getLong("numOfAgree"));
            answers.add(ans);
        }
        return answers;
    }

    /**
     * 更新某一属性信息
     * @param id
     * @param attribute
     * @param value
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Boolean update(Long id,String attribute,T value) throws SQLException{
        String sql = "update Answer set " + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     * 计算某一问题的回复数
     * @param questionId
     * @return
     * @throws SQLException
     */
    public int countByQuestionId(Long questionId) throws SQLException{
        String sql = "select count(*) from Answer where questionId = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,questionId);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) return rs.getInt(1);
        else throw new SQLException();
    }

    /**
     * 关键词匹配
     * @param words
     * @return
     * @throws SQLException
     * @throws NullPointerException
     */
    public int search(String words) throws SQLException, NullPointerException{
        String searchMode = getSearchMode(words);
        String sql = String.format("select * from Answer where match(content) against ('%s', in boolean mode)", searchMode);
        excuteQuery(sql);
        return toJsonArray().size();
    }

    /**
     * 分解拼接关键词
     * @param words
     * @return
     */
    public String getSearchMode(String words){
        String arr[] = words.split("\\s+");
        String searchMode = "";
        for (String ss:arr) {
            searchMode += String.format("+%s ", ss);
        }
        return searchMode;
    }


}
