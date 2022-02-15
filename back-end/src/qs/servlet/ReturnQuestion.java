package qs.servlet;


import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;

import qs.common.Question;
import qs.common.QuestionDao;
import qs.common.User;
import qs.common.UserDao;

/**
 * 给用户个人页面返回本人发布的所有问题
 */
@WebServlet("/ReturnQuestion")
public class ReturnQuestion extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            try{
            String mail=request.getParameter("mail");
            UserDao userdao=new UserDao();
            userdao.connect();
            User user= new User();
            user=userdao.selectByMail(mail);
            QuestionDao qdao=new QuestionDao();
            qdao.connect();

            //查找某一用户发布的所有信息
            ArrayList<Question> questions = new ArrayList<Question>();
            questions=qdao.selectByUserId(user.getId());
            ArrayList<Timestamp> times=new ArrayList<Timestamp>();

           JSONObject json = new JSONObject();
           json.put("question",questions);

            response.setContentType("text");
            PrintWriter out = response.getWriter();
            out.println(json.toJSONString());

            }catch(Exception e)
            {
                System.out.println("error!!!!");
            }
        }
    }
