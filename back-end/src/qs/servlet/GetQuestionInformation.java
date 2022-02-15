package qs.servlet;

import com.alibaba.fastjson.JSONArray;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.annotation.WebServlet;

import qs.common.*;

/**
 * 用户发布问题
 */
@WebServlet("/QL")
public class GetQuestionInformation extends HttpServlet {
    public void judge(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //找到保存到session中的登录用户
        User user =  (User) request.getSession().getAttribute("user");
        //发布问题时，扣除10积分
        user.setScore(user.getScore()-10);

        //发布者设置悬赏积分，并扣除积分
        Long awardingScore=Long.valueOf(request.getParameter("awardingScore"));
        user.setScore(user.getScore()-awardingScore);

        String content = request.getParameter("content");
        String label = request.getParameter("lable");
        String title = request.getParameter("title ");
        Long userid=user.getId();
        String type1=request.getParameter("QuestionType");
        Long type=Long.valueOf(request.getParameter("QuestionType"));

        try {
            System.out.println(type);
            QuestionDao qdao = new QuestionDao();

            qdao.connect();

            Long id = qdao.createQuestion(userid);

            Question q=new Question();

            q.setContent(content);
            q.setId(id);
            q.setType(type);
            q.setUserId(userid);
            q.setTitle(title);
            String []L=label.split(",");

         response.setCharacterEncoding("utf-8");
         response.setContentType("text");
         PrintWriter out = response.getWriter();

         qdao.update(id,"content", content );
         qdao.update(id, "type", type);
         qdao.update(id, "title", title);


         Label labled=new Label();
         LabelDao ldao=new LabelDao();
         ldao.connect();
         DAO dao=new DAO();


            for(int i=0;i<L.length;i++)
            {
                String sql =  "select count(*) from Label where name = " + String.format(" '%s'", L[i]);
                dao.excuteQuery(sql);

                ResultSet rs = dao.getResultSet();
                Long cnt = 0L;
                while(rs.next()){
                    cnt = rs.getLong(1);

                    break;
                }

                //判断标签是否存在
                Long labelId = null;
                if(cnt == 0)
                    labelId = ldao.createLabel(L[i]);
                else
                {
                    labled=ldao.selectByName(L[i]);
                    labelId=labled.getId();
                }
                String sql2="insert into Q_L(questionId, labelId) values "+"( "+ id     +" , " + labelId +   ")";
                dao.excuteUpdate(sql2);

            }
           out.println("success");

        }catch(Exception e)
        {
            System.out.println("error2");
        }
    }

    /**
     * 返回已有的问题类别
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void ReturnType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text");
        PrintWriter out = response.getWriter();
        try
        {
            JSONArray json = new JSONArray();
            QuestionDao qdao=new QuestionDao();
            Question q=new Question();


            qdao.connect();
            String sql="select QuestionType.*"+"From QuestionType";
            qdao.excuteQuery(sql);

            out.println(qdao.toJsonArray().toJSONString());


        } catch (Exception e)
        {
            System.out.println("error1");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        try{
            ReturnType(request,response);
        }
        catch(Exception e) {
            System.out.println("error12");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text");
        PrintWriter out = response.getWriter();
        try{
            judge(request,response);
        }
        catch(Exception e) {
            out.println("error");
        }
    }
}