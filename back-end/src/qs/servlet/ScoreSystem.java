package qs.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.ArrayList;

import qs.common.*;

@WebServlet("/ScoreSystem")
public class ScoreSystem extends HttpServlet
{
    /**
     * 发布者为某一回复悬赏积分
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void giveScore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Long questionId = Long.valueOf(request.getParameter("questionId"));
        Long answerId=Long.valueOf(request.getParameter("answerId"));

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            AnswerDao aDao = new AnswerDao();
            Answer answer = aDao.selectById(answerId);

            UserDao udao = new UserDao();
            User user = udao.selectById(answer.getUserId());

            QuestionDao qdao = new QuestionDao();
            Question question = qdao.selectById(questionId);

            if(question.getAlreadyAward() == 0){
                udao.update(user.getId(), "score", user.getScore() + question.getReward());
                qdao.update(question.getId(), "alreadyAward", 1);
                aDao.update(answer.getId(), "givenScore", 1);
                out.print("ok");
            }else {
                out.print("repeat");
            }
        }catch(Exception e) {
           e.printStackTrace();
        }
    }

    /**
     * 浏览者为回复点赞
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void giveLike(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Long questionId = Long.valueOf(request.getParameter("questionId"));
        Long answerId=Long.valueOf(request.getParameter("answerId"));
        String mail=request.getParameter("email");

        try {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            //查找发布回复的用户
            String sql=" select userId from Answer where id = " + answerId  ;
            AnswerDao answerdao = new AnswerDao();
            answerdao.connect();
            answerdao.excuteQuery(sql);
            ResultSet rs = answerdao.getResultSet();
            Long userId=0L;
            while(rs.next()) {
                userId = rs.getLong(1);

            }

            Answer answer=answerdao.selectById(answerId);
            answer.setNumOfAgree(answer.getNumOfAgree()+1);

            //判断用户是否点过赞
            UserDao LoginUserdao=new UserDao();
            User LoginUser=LoginUserdao.selectByMail(mail);
            String sql2=" select count(*) from Agree where answerId = " + answerId  + " and userId = " + LoginUser.getId();
            AgreeDao agreedao = new AgreeDao();
            agreedao.connect();
            agreedao.excuteQuery(sql2);
            ResultSet rs2 = agreedao.getResultSet();
            Long cnt=0L;
            while(rs2.next()){
                cnt = rs2.getLong(1);
                break;
            }
            if(cnt!=0)
            {
                out.println("already givelike");
            }
            else {
                //若没有点过赞，则点赞，并更新信息
                UserDao userdao = new UserDao();
                userdao.connect();
                User user = userdao.selectById(userId);
                user.setScore(user.getScore() + 1);

                Agree agree=new Agree();
                agree.setAnswerId(answerId);
                agree.setUserId(LoginUser.getId());
                //插入Agree实体，建立对应关系
                agreedao.insert(agree);

                userdao.connect();
                userdao.update(user);
                out.println("successfully");

            }
        }catch(Exception e)
        {
            System.out.println("error");
            e.getMessage() ;
        }
    }

    /**
     * 调用点赞函数
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try
        {
            giveLike(request,response);
        }catch(Exception e)
        {
            e.getMessage();
            System.out.println("error-post");
        }
    }

    /**
     * 调用悬赏函数
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try
        {
            giveScore(request,response);
        }catch(Exception e)
        {
            e.getMessage();
            System.out.println("error-get");
        }
    }
}
