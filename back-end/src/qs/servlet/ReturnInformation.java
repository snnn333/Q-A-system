package qs.servlet;


import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;

import qs.common.User;
import qs.common.UserDao;

/**
 * 返回用户与问题相关信息
 */
@WebServlet("/ReturnInformation")
public class ReturnInformation extends HttpServlet
{
    /**
     * 从数据库获取点赞数等信息，返回给前端，显示在页面上
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        response.setContentType("text");
        PrintWriter out = response.getWriter();
        try{
            UserDao userDao=new UserDao();
            String  mail=request.getParameter("email");
            User user= userDao.selectByMail(mail);

            Long numOfAnswer=user.getNumOfAnswer();
            Long numOfQuery=user.getNumOfQuery();
            Long score=user.getScore();

            JSONObject json = new JSONObject();
            json.put("numOfAnswer",numOfAnswer);
            json.put("numOfQuery",numOfQuery);
            json.put("score",score);

            out.println(json.toJSONString());

        }
        catch(Exception e) {
            System.out.println("error");
            out.println("error");
        }
    }
}
