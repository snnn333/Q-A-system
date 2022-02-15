package qs.common;

import com.mysql.cj.xdevapi.SqlDataResult;

import java.sql.*;
import java.util.ArrayList;

/**
 * DAO的子类，对Question进行操作操作
 */
public class QuestionDao extends DAO{

    public QuestionDao(){
        super();
    }

    /**
     * 数据库自动生成Id
     * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException{
        String sql = "select max(id) from Question";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }

    /**
     * 建立一个新问题
     * @param userId
     * @return
     * @throws SQLException
     */
    public synchronized Long createQuestion(Long userId) throws SQLException{
        String sql = "insert into Question(id,userId) values (?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        Long id = nextId();
        ptmt.setLong(1,id);
        ptmt.setLong(2,userId);
        if(ptmt.executeUpdate() > 0)
            return id;
        else throw new SQLException();
    }

    /**
     * 通过id删除问题
     * @param id
     * @return
     * @throws SQLException
     */
    public Boolean delete(Long id) throws SQLException{
        String sql = "delete from Question where id = ?";
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
        String sql = "update Question set " + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     * 通过id获得问题信息
     * @param id
     * @return
     * @throws SQLException
     */
    public Question selectById(Long id) throws SQLException{
        String sql = "select * from Question where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        ResultSet rs = ptmt.executeQuery();
        Question question = null;
        while(rs.next()){
            question = new Question();
            question.setId(rs.getLong("id"));
            question.setUserId(rs.getLong("userId"));
            question.setType(rs.getLong("type"));
            question.setContent(rs.getString("content"));
            question.setTime(rs.getTimestamp("time"));
            question.setLabel(rs.getString("label"));
            question.setFrequency(rs.getLong("frequency"));
            question.setTitle(rs.getString("title"));
            question.setIsReleased(rs.getLong("isReleased"));
            question.setReward(rs.getLong("reward"));
            question.setAlreadyAward(rs.getLong("alreadyAward"));
        }
        return question;
    }

    /**
     * 获得某一用户发布的全部问题
     * @param userId
     * @return
     * @throws SQLException
     */
    public ArrayList<Question> selectByUserId(Long userId) throws SQLException{
        ArrayList<Question> questions = new ArrayList<Question>();
        String sql = "select * from Question where  userId = ? order by frequency desc";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,userId);
        ResultSet rs = ptmt.executeQuery();
        Question question = null;
        while(rs.next()){
            question = new Question();
            question.setId(rs.getLong("id"));
            question.setUserId(rs.getLong("userId"));
            question.setType(rs.getLong("type"));
            question.setContent(rs.getString("content"));
            question.setTime(rs.getTimestamp("time"));
            question.setLabel(rs.getString("label"));
            question.setFrequency(rs.getLong("frequency"));
            question.setTitle(rs.getString("title"));
            question.setIsReleased(rs.getLong("isReleased"));
            question.setReward(rs.getLong("reward"));
            questions.add(question);
        }
        return questions;
    }

    /**
     * 获得某一类型的全部问题
     * @param typeId
     * @return
     * @throws SQLException
     */
    public ArrayList<Question> selectByTypeId(Long typeId) throws SQLException{
        ArrayList<Question> questions = new ArrayList<Question>();
        String sql = "select * from Question where  type = ? order by frequency desc";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,typeId);
        ResultSet rs = ptmt.executeQuery();
        Question question = null;
        while(rs.next()){
            question = new Question();
            question.setId(rs.getLong("id"));
            question.setUserId(rs.getLong("userId"));
            question.setType(rs.getLong("type"));
            question.setContent(rs.getString("content"));
            question.setTime(rs.getTimestamp("time"));
            question.setLabel(rs.getString("label"));
            question.setFrequency(rs.getLong("frequency"));
            question.setTitle(rs.getString("title"));
            question.setIsReleased(rs.getLong("isReleased"));
            question.setReward(rs.getLong("reward"));
            questions.add(question);
        }
        return questions;
    }

    /**
     * 计算问题的回复数
     * @param questionId
     * @return
     * @throws SQLException
     */
    public Long getNumOfAnswer(Long questionId) throws SQLException{
        String sql = "select count(*)  from Answer where questionId = " +
                questionId + " and isReleased = 1";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        while(rs.next()){
            return rs.getLong(1);
        }
        return new Long(0);
    }

    /**
     * 分解拼接关键词
     * @param words
     * @return
     */
    public static String getSearchMode(String words){
        String arr[] = words.split("\\s+");
        String searchMode = "";
        for (String ss:arr) {
            searchMode += String.format("+%s ", ss);
        }
        return searchMode;
    }



}
