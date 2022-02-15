package qs.servlet;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import qs.common.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLDecoder;
import java.sql.ResultSet;


/**
 * 接受前端请求，反问有关问题的详细信息
 */
@WebServlet("/detail")
public class QuestionDetail extends HttpServlet {

    /**
     * 接受Get请求，判断业务逻辑
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String entity = req.getParameter("entity");


        if(action.equals("select") && entity.equals("Question"))
            doSelectQuestion(req, resp);
        else if(action.equals("select") && entity.equals("Answer"))
            doSelectAnswer(req, resp);
        else if(action.equals("select") && entity.equals("Comment"))
            doSelectComment(req, resp);
        else if(action.equals("addNumOfAgree")){
            doAddNumOfAgree(req, resp);
        }

    }

    /**
     * 接受Post请求，进行插入回答或评论逻辑
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String entity = req.getParameter("entity");

        if(action.equals("insert") && entity.equals("Answer"))
            doInsertAnswer(req, resp);
        else if(action.equals("insert") && entity.equals("Comment"))
            doInsertComment(req, resp);




    }

    /**
     * 实现查询有关问题详细信息
     * @param req
     * @param resp
     * @throws IOException
     */
    public synchronized void doSelectQuestion(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        Long qid = Long.valueOf(req.getParameter("questionId"));
        try {
            // 查询对应问题id的问题内容
            QuestionDao qdao = new QuestionDao();
            Question question = qdao.selectById(qid);

            // 查询提问该问题的用户
            UserDao udao = new UserDao();
            User user = udao.selectById(question.getUserID());

            JSONObject qDetail = new JSONObject(); //存储问题的内容
            JSONObject uDetail = new JSONObject(); //存储用户的信息

            // 设置提问该问题的用户信息
            uDetail.put("name", user.getName());
            uDetail.put("photo",user.getPhoto());
            uDetail.put("score",user.getScore());
            uDetail.put("mail", user.getmail());
            uDetail.put("numOfQuery", udao.getNumOfQuery(user.getId()));
            uDetail.put("numOfAnswer", udao.getNumOfAnswer(user.getId()));
            qDetail.put("user",uDetail.toJSONString());

            qDetail.put("title", question.getTitle());
            qDetail.put("content", question.getContent());
            qDetail.put("numOfAnswer", qdao.getNumOfAnswer(qid));
            qDetail.put("date", question.getTime());
            qDetail.put("reward", question.getReward());
            // 展示问题的标签
            DAO dao = new DAO();
            String sql = "select Label.name from Q_L, Label where questionId = " + qid + " and Label.id = Q_L.labelId ";
            dao.excuteQuery(sql);

            qDetail.put("label", dao.toJsonArray().toJSONString());

            // 将浏览量加一
            qdao.update(question.getId(), "frequency", question.getFrequency() + 1);

            out.print(qDetail.toJSONString());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * 实现查询有关问题的回答信息
     * @param req
     * @param resp
     * @throws IOException
     */
    public void doSelectAnswer(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        User user = (User)req.getSession().getAttribute("user");
        PrintWriter out = resp.getWriter();
        if(user == null){
            out.print("请登录！");
            return;
        }
//        // for debug
//        user = new User();
//        user.setId(2l);


        Long qid = Long.valueOf(req.getParameter("questionId"));
        int index = Integer.parseInt(req.getParameter("index"));
        int num = Integer.parseInt(req.getParameter("num"));
        String orderBy = req.getParameter("orderBy");
        if (orderBy == null)
            orderBy = "time";
        if(orderBy.equals("numOfAgree")) orderBy = " numOfAgree desc ";
        String sql = " select Answer.*, User.name, User.photo from Answer, User where questionId = " + qid +
                " and Answer.userId = User.id  and Answer.isReleased = 1 order by " + orderBy;

        // for debug
        System.out.println(sql);

        try {
            DAO dao = new DAO();
            dao.excuteQuery(sql);
            JSONArray jsonArray = dao.toJsonArray(index, num);
            // 查询每个
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject json = (JSONObject) jsonArray.remove(i);
                Long answerId = json.getLong("id");
                String sql2 = String.format("select count(*) hasAgreed from Agree where answerId = %d and userId = %d",answerId, user.getId());
                dao.excuteQuery(sql2);
                json.put("hasAgreed", ((JSONObject)dao.toJsonArray().get(0)).getLong("hasAgreed"));
                jsonArray.add(i, json);
            }
            out.print(jsonArray.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * 实现点赞功能
     * @param req
     * @param resp
     * @throws IOException
     */
    public void doAddNumOfAgree(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        User user = (User)req.getSession().getAttribute("user");
        // for Debug
        user = new User();
        user.setId(2l);

        Long aid = Long.valueOf(req.getParameter("answerId"));
        PrintWriter out = resp.getWriter();
        try {
            AnswerDao adao = new AnswerDao();
            Answer answer = adao.selectById(aid);
            String sql = String.format("insert into Agree(answerId, userId) values(%d,%d)",aid,user.getId());
            if(adao.excuteUpdate(sql)){
                adao.update(aid, "numOfAgree", answer.getNumOfAgree() + 1);
                out.print("success");
            }else{
                out.print("failure");
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    /**
     * 实现查询对应回答的评论信息
     * @param req
     * @param resp
     * @throws IOException
     */
    public void doSelectComment(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        String temp = req.getParameter("answerId");
        Long aid = Long.valueOf(req.getParameter("answerId"));
        int index = Integer.parseInt(req.getParameter("index"));
        int num = Integer.parseInt(req.getParameter("num"));

        String sql = "select Comment.*, User.id, User.name,User.photo from Comment, User " +
                " where Comment.answerId = " + aid + " and User.id = Comment.userId " +
                " order by Comment.time ";

        // for debug
        System.out.println(sql);

        try {
            DAO dao = new DAO();
            dao.excuteQuery(sql);
            out.print(dao.toJsonArray(index, num).toJSONString());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * 实现插入对应回复信息
     * @param req
     * @param resp
     * @throws IOException
     */
    public void doInsertAnswer(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Long qid = Long.valueOf(req.getParameter("questionId"));
        String mail = req.getParameter("mail");
        String content = URLDecoder.decode(req.getParameter("content"),"UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            AnswerDao adao = new AnswerDao();
            UserDao udao = new UserDao();
            User user = udao.selectByMail(mail);
            Long aid = adao.createAnswer(qid, user.getId());
            adao.update(aid, "content", content);
            adao.update(aid, "isReleased", 1);
            out.print("success");
        }catch (Exception e){
            out.print("failure");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * 实现插入对应回复的评论信息
     * @param req
     * @param resp
     * @throws IOException
     */
    public void doInsertComment(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Long aid = Long.valueOf(req.getParameter("answerId"));
        String mail = req.getParameter("mail");
        String content = URLDecoder.decode(req.getParameter("content"),"UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            CommentDao cdao = new CommentDao();
            UserDao udao = new UserDao();
            User user = udao.selectByMail(mail);
            Long cid = cdao.createComment(user.getId(), aid);
            cdao.update(cid, "content", content);
            out.print("success");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            out.print(e.getMessage());
        }
    }

}
