package qs.servlet;


import qs.common.DAO;
import qs.common.QuestionType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;

/**
 * 处理主页页面逻辑
 */
@WebServlet("/main")
public class Main extends HttpServlet {

    /**
     * 接受Get请求，获取热帖和最新问题
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // for debug
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");
        String entity = req.getParameter("entity");
        String orderBy = req.getParameter("orderBy");
        // 默认按热度排序
        if(orderBy == null) orderBy = new String("frequency");
        String questionType = req.getParameter("QuestionType");
        if(questionType != null)
            questionType = URLDecoder.decode(questionType,"UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            DAO dao = new DAO();

            // 查看用户
            if(action.equals("select") && entity.equals("User")){
                Long id = (Long) req.getSession().getAttribute("userId");

                String sql = "select id, role, name, photo, score from User where id = " + id;
                dao.excuteQuery(sql);
                out.print(dao.toJsonArray().toJSONString());
            }

            else if (action.equals("select") && entity.equals("Question")) {
                int index = Integer.parseInt(req.getParameter("index"));   // 查找的开始
                int num = Integer.parseInt(req.getParameter("num"));       // 查找的数目

                // 查询热帖功能
                if (questionType.equals("热帖") && orderBy.equals("frequency")) {
                    String sql = "select Question.*,(select name from User where User.id = Question.userId) name,(select count(*) " +
                            " from Answer where questionId = Question.id) numOfAnswer from Question  where isReleased = 1 order by frequency";
                    dao.excuteQuery(sql);
                    out.print(dao.toJsonArray(index,num).toJSONString());
                }else if(questionType.equals("热帖") && orderBy.equals("time")){
                    String sql = "select Question.*,(select name from User where User.id = Question.userId) name,(select count(*) " +
                            " from Answer where questionId = Question.id) numOfAnswer from Question  where isReleased = 1 order by time";
                    dao.excuteQuery(sql);
                    out.print(dao.toJsonArray(index,num).toJSONString());
                }

                // 查询单个问题类别
                else if(orderBy.equals("frequency") || orderBy.equals("time")){
                    String sql = "select Question.* ,(select count(*) from Answer where Answer.questionId = Question.id) numOfAnswer,(select name from User where User.id = Question.userId) name from Question, QuestionType where QuestionType.id = Question.type" +
                            " and isReleased = 1 and QuestionType.name = " + String.format(" '%s' ", questionType) +
                            "order by " + orderBy;

                    dao.excuteQuery(sql);
                    out.print(dao.toJsonArray(index,num).toJSONString());
                }
            }   // 查看所有的问题类别
            else if (action.equals("select") && entity.equals("QuestionType")) {
                String sql = "select * from QuestionType";
                dao.excuteQuery(sql);
                out.print(dao.toJsonArray().toJSONString());


                // for debug
                dao.excuteQuery(sql);
                System.out.println(dao.toJsonArray().toJSONString());
            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
