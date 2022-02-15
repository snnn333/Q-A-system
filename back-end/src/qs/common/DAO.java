package qs.common;


import java.sql.*;
import java.util.*;
import com.alibaba.fastjson.*;
import com.mysql.cj.xdevapi.SqlDataResult;


public class DAO {

    protected Connection conn;
    private String url = "jdbc:mysql://47.101.210.170:3306/questionsystem"+"?useUnicode=true&characterEncoding=utf8&useSSL=true";
    private String user = "qs";
    private String password = "123456";
    private ResultSet resultSet = null;

    public String getUrl() { return url;}
    public String getUser() { return user;}
    public String getPassword() { return password;}
    public ResultSet getResultSet() { return resultSet;}

    public void setUrl(String url) { this.url = url; }
    public void setUser(String user) { this.user = user; }
    public void setPassword(String password) {this.password = password; }


    public JSONArray toJsonArray(int size) throws NullPointerException, SQLException {
        int count = 0;
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next() && count < size) {
            JSONObject json = new JSONObject();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if(resultSet.getObject(i) == null)
                    json.put(metaData.getColumnLabel(i),"null");
                else
                    json.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            jsonArray.add(json);
            count++;
        }
        return jsonArray;
    }

    public JSONArray toJsonArray(int start, int size) throws NullPointerException, SQLException {
        int count = 0;
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next() && count < size + start) {

            if(count < start) continue;
            JSONObject json = new JSONObject();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if(resultSet.getObject(i) == null)
                    json.put(metaData.getColumnLabel(i),"null");
                else
                    json.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            jsonArray.add(json);
            count++;
        }
        return jsonArray;
    }

    public JSONArray toJsonArray() throws NullPointerException, SQLException {
        // 默认最多一次给出500行
        return toJsonArray(500);
    }

    public DAO(){
        connect();
    }

    public boolean excuteUpdate(String sql) throws SQLException{
        if(conn == null || conn.isClosed())
            connect();
        PreparedStatement ptmt = conn.prepareStatement(sql);

        return ptmt.executeUpdate() > 0;
    }

    public boolean excuteQuery(String sql) throws SQLException{
        if(conn == null || conn.isClosed())
            connect();
        PreparedStatement ptmt = conn.prepareStatement(sql);
        resultSet = ptmt.executeQuery();
        return resultSet != null;
    }



    public  void connect(){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to connect to database!");
        }
    }

    public Boolean insert(User user) throws Exception{
        //拼接sql语句
        String  sql = "insert into " +
                "User(id,name,age,phone,mail,password,role,score,photo,numOfQuery,numOfAnswer,cannotSpeak,cannotLogin)" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行

        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, user.getId());
            ptmt.setString(2, user.getName());
            ptmt.setLong(3, user.getAge());
            ptmt.setString(4,user.getPhone());
            ptmt.setString(5, user.getmail());
            ptmt.setString(6, user.getPassword());
            ptmt.setLong(7, user.getRole());
            ptmt.setLong(8,user.getScore());
            ptmt.setString(9, user.getPhoto());
            ptmt.setLong(10, user.getNumOfQuery());
            ptmt.setLong(11, user.getNumOfAnswer());
            ptmt.setLong(12, user.getCannotSpeak());
            ptmt.setLong(13, user.getCannotLogin());
        }catch (Exception e){
            System.err.println(e.getStackTrace());
            System.err.println("Failed to create a PreparedStatement for User!");
        }
        // 开始执行sql语
        return ptmt.executeUpdate()>0;
    }

    public Boolean update(User user) throws Exception{
        //拼接sql语句
        String sql = "update User set name = ?, age = ?, phone = ?, mail = ?,password = ?,role = ?,photo = ?," +
                "numOfQuery = ?, numOfAnswer = ?, cannotSpeak = ?,cannotLogin = ?, score = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setString(1, user.getName());
            ptmt.setLong(2, user.getAge());
            ptmt.setString(3,user.getPhone());
            ptmt.setString(4, user.getmail());
            ptmt.setString(5, user.getPassword());
            ptmt.setLong(6, user.getRole());
            ptmt.setString(7, user.getPhoto());
            ptmt.setLong(8, user.getNumOfQuery());
            ptmt.setLong(9, user.getNumOfAnswer());
            ptmt.setLong(10, user.getCannotSpeak());
            ptmt.setLong(11, user.getCannotLogin());
            ptmt.setLong(12, user.getScore());
            ptmt.setLong(13, user.getId());
        }catch (Exception e){
            System.err.println(e.getStackTrace());
            System.err.println("Failed to create a PreparedStatement for User!");
        }
        return ptmt.executeUpdate() > 0;
    }

    public Boolean delete(User user)throws Exception {
        String deleteSql = "delete from User where id =?";
        PreparedStatement ptmt = conn.prepareStatement(deleteSql); // 预编译SQL，减少sql执行
        ptmt.setLong(1,user.getId());
        return ptmt.executeUpdate() > 0;
    }

    public Boolean insert(Question question) throws Exception{
        //拼接sql语句
        String  sql = "insert into Question(id,userId,type,content,time,label,frequency,title,alreadyAward)" +
                "values(?,?,?,?,?,?,?,?,?)";

        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行

        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, question.getId());
            ptmt.setLong(2, question.getUserID());
            ptmt.setLong(3, question.getType());
            ptmt.setString(4, question.getContent());
            ptmt.setTimestamp(5, question.getTime());
            ptmt.setString(6, question.getLabel());
            ptmt.setLong(7, question.getFrequency());
            ptmt.setString(8, question.getTitle());
            ptmt.setLong(9, question.getAlreadyAward());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for User!");
        }

        // 开始执行sql语
        return ptmt.execute();
    }

    //修改数据
    public Boolean update(Question question) throws Exception{
        //拼接sql语句
        String sql = "update Question set userId = ?,type = ?, content = ?,time = ?,label = ?,frequency = ?,title = ?,alreadyAward = ? "+
                "where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, question.getUserID());
            ptmt.setLong(2, question.getType());
            ptmt.setString(3, question.getContent());
            ptmt.setTimestamp(4, question.getTime());
            ptmt.setString(5, question.getLabel());
            ptmt.setLong(6, question.getFrequency());
            ptmt.setString(7, question.getTitle());

            ptmt.setLong(8, question.getAlreadyAward());
            ptmt.setLong(9, question.getId());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for Question!");
        }
        //开始执行sql语句
        return ptmt.executeUpdate() > 0;

    }

    public Boolean delete(Question question)throws Exception{
        String deleteSql = "delete from Question where id=?";
        PreparedStatement ptmt = conn.prepareStatement(deleteSql); // 预编译SQL，减少sql执行
        ptmt.setLong(1,question.getId());
        return ptmt.executeUpdate() > 0;
    }

    public Boolean insert(QuestionType questionType) throws Exception{

        //拼接sql语句
        String  sql = "insert into " + "QuestionType(id,name,icon)" +
                "values (?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行

        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, questionType.getId());
            ptmt.setString(2, questionType.getName());
            ptmt.setString(3, questionType.getIcon());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for QuestionType!");
        }

        //开始执行sql语
        return ptmt.executeUpdate() > 0;
    }

    public Boolean update(QuestionType questionType) throws Exception{
        //拼接sql语句
        String sql = "update QuestionType set" + "name = ?, icon = ? where id = ?" ;
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setString(1, questionType.getName());
            ptmt.setString(2, questionType.getIcon());
            ptmt.setLong(3, questionType.getId());


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for QuestionType!");
        }
        return ptmt.executeUpdate() > 0;
    }

    public Boolean delete(QuestionType questionType) throws Exception {
        String deleteSql="delete from QuestionType where id= ? ";
        PreparedStatement ptmt = conn.prepareStatement(deleteSql); // 预编译SQL，减少sql执行
        ptmt.setLong(1,questionType.getId());
        return ptmt.executeUpdate() > 0;
    }

    public Boolean insert(Answer answer) throws Exception{

        //拼接sql语句
        String  sql = "insert into " + "Answer(id,userId,questionId,content,time,numOfAgree)" +
                "values (?,?,?,?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, answer.getId());
            ptmt.setLong(2, answer.getUserId());
            ptmt.setLong(3, answer.getQuestionId());
            ptmt.setString(4, answer.getContent());
            ptmt.setTimestamp(5, answer.getTime());
            ptmt.setLong(6, answer.getNumOfAgree());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for Answer!");
        }
        //开始执行sql语
        return ptmt.executeUpdate() > 0;

    }

    public Boolean update(Answer answer) throws Exception{
        //拼接sql语句
        String sql = "update Answer set userId = ?, questionId = ?,  content = ?,time = ?, numOfAgree = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, answer.getUserId());
            ptmt.setLong(2, answer.getQuestionId());
            ptmt.setString(3, answer.getContent());
            ptmt.setTimestamp(4, answer.getTime());
            ptmt.setLong(5, answer.getNumOfAgree());
            ptmt.setLong(6, answer.getId());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for Answer!");
        }
        return ptmt.executeUpdate() > 0;
    }

    public Boolean delete(Answer answer) throws Exception {
        String deleteSql="delete from Answer where id= ? ";
        PreparedStatement ptmt = conn.prepareStatement(deleteSql);
        ptmt.setLong(1,answer.getId());
        return ptmt.executeUpdate() > 0;
    }

    public Boolean insert(Comment comment) throws Exception{

        //拼接sql语句
        String  sql = "insert into " + "Comment(id,userId,answerId,content,time)" +
                "values(?,?,?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行

        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, comment.getId());
            ptmt.setLong(2, comment.getUserId());
            ptmt.setLong(3, comment.getAnswerId());
            ptmt.setString(4, comment.getContent());
            ptmt.setTimestamp(5, comment.getTime());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for Comment!");
        }

        return ptmt.executeUpdate() > 0;
    }

    public Boolean update(Comment comment) throws Exception{

        //拼接sql语句
        String sql = "update Comment set userId = ?,answerId = ?,content = ?, time = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, comment.getUserId());
            ptmt.setLong(2, comment.getAnswerId());
            ptmt.setString(3, comment.getContent());
            ptmt.setTimestamp(4, comment.getTime());
            ptmt.setLong(5, comment.getId());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for Comment!");
        }
        return ptmt.executeUpdate() > 0;
    }

    public Boolean delete(Comment comment) throws Exception {

        String deleteSql="delete from Comment where id = ? ";

        PreparedStatement ptmt = conn.prepareStatement(deleteSql); // 预编译SQL，减少sql执行

        ptmt.setLong(1,comment.getId());

        return ptmt.executeUpdate() > 0;

    }

    public Boolean insert(SubComment subComment) throws Exception{

        //拼接sql语句
        String  sql = "insert into " + "SubComment(id,userId,commentId,content,time)" +
                "values(?,?,?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, subComment.getId());
            ptmt.setLong(2, subComment.getUserId());
            ptmt.setLong(3, subComment.getCommentId());
            ptmt.setString(4, subComment.getContent());
            ptmt.setTimestamp(5, subComment.getTime());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for SubComment!");
        }

        return ptmt.executeUpdate() > 0;
    }

    public Boolean update(SubComment subComment) throws Exception{
        //拼接sql语句
        String sql = "update SubComment set userId = ?,commentId = ?,content = ?, time = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, subComment.getUserId());
            ptmt.setLong(2, subComment.getCommentId());
            ptmt.setString(3, subComment.getContent());
            ptmt.setTimestamp(4, subComment.getTime());
            ptmt.setLong(5, subComment.getId());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for SubComment!");
        }
        return ptmt.executeUpdate() > 0;
    }

    public Boolean delete(SubComment subComment) throws Exception {
        String deleteSql = "delete from SubComment where id= ?";
        PreparedStatement ptmt = conn.prepareStatement(deleteSql); // 预编译SQL，减少sql执行
        ptmt.setLong(1,subComment.getId());
        return ptmt.executeUpdate() > 0;
    }

    public <T> int getSqlType(T value){
        if(value instanceof String)
            return Types.VARCHAR;
        else if(value instanceof Long)
            return Types.INTEGER;
        else if(value instanceof byte [])
            return Types.BLOB;
        else if(value instanceof Timestamp)
            return Types.TIMESTAMP;
        else return -1;
    }

    public <T> Boolean update(String table,Long id,String attribute,T value) throws SQLException{
        String sql = "update "+ table +"set" + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        return ptmt.executeUpdate() > 0;
    }

    public Boolean insert(Agree agree) throws Exception{

        //拼接sql语句
        String  sql = "insert into " + "Agree(answerId,userId)" + "values (?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql); // 预编译SQL，减少sql执行
        // 向sql语句传参
        try {
            //传参
            ptmt.setLong(1, agree.getAnswerId());
            ptmt.setLong(2, agree.getUserId());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to create a PreparedStatement for Answer!");
        }
        //开始执行sql语
        return ptmt.executeUpdate() > 0;

    }

    public Boolean delete(Agree agree) throws Exception {
        String deleteSql = "delete from Agree where answerId= ?,userId= ?";
        PreparedStatement ptmt = conn.prepareStatement(deleteSql); // 预编译SQL，减少sql执行
        ptmt.setLong(1,agree.getAnswerId());
        ptmt.setLong(2,agree.getUserId());
        return ptmt.executeUpdate() > 0;
    }
}

