package qs.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import qs.common.*;

@WebServlet("/sign")
public class Sign extends HttpServlet {

    /**
     * 用户注册，部分属性设置默认值
     * @param request
     * @param response
     * @throws SQLException
     * @throws ServletException
     * @throws IOException
     */
    public void save(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        //从前端获取信息，并设置部分属性的默认值
        UserDao userDao = new UserDao();
        Long id = userDao.nextId();
        Long role = 0L;//普通用户
        Long score = 100L;
        Long age = Long.parseLong(request.getParameter("age"));
        Long numOfQuery = 0L;
        Long numOfAnswer = 0L;
        Long cannotSpeak = 0L;
        Long cannotLogin = 1L;
        String name = request.getParameter("username");
        name = URLDecoder.decode(name,"UTF-8");
        String phone = request.getParameter("phone");
        String mail = request.getParameter("email");
        String password = request.getParameter("password");
        String Photo = "http://query.liublack.cn/qs/contents/647062810.jpg";


        try {

            UserDao userdao = new UserDao();

            User userd =  userdao.selectByMail(mail);
            if(userd==null) {

                User user=new  User();
                if (RSAUtil.generateKey()) {
                    System.out.println("-----成功生成公私钥-------");

                }
                RSAUtil.loadKey();
                password = RSAUtil.decrypt(password);

                String psw = MD5Util.encrypt(password);

                user.setId(id);
                user.setRole(role);
                user.setScore(score);
                user.setAge(age);
                user.setNumOfQuery(numOfQuery);
                user.setNumOfAnswer(numOfAnswer);
                user.setCannotSpeak(cannotSpeak);
                user.setCannotLogin(cannotLogin);
                user.setName(name);
                user.setPhone(phone);
                user.setMail(mail);
                user.setPassword(psw);
                user.setPhoto(Photo);


                userdao.connect();
                userdao.insert(user);
                out.println("successfully");

            }
            else
                out.println("repeated");
        } catch (Exception e) {
            out.println("unsuccessfully");
            System.out.println("error1");
        }
    }

    /**
     * 调用注册函数
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
            try{
                this.save(request,response);
        }
        catch(Exception e) {
            System.out.println("error2");
        }
    }

}


