package qs.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import qs.common.User;
import qs.common.UserDao;

/**
 * 改变登录状态
 */
@WebServlet("/confirmRegister")
public class ConfirmRegister extends HttpServlet
 {
     /**
      * 邮箱验证后，用户才可以登录
      * @param request
      * @param response
      * @throws ServletException
      * @throws IOException
      */
     public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
     {
         PrintWriter out = response.getWriter();
         try{

             String mail=request.getParameter("email");
             UserDao userdao=new UserDao();
             userdao.connect();
             User user= new User();
             user=userdao.selectByMail(mail);
             user.setCannotLogin(0L);

             userdao.update(user);
             out.println("success");
         }
         catch(Exception e) {
             out.println("error");
         }
     }
}
