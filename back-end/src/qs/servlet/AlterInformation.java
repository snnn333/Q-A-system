package qs.servlet;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import qs.common.*;

/**
 * 修改个人信息
 */
@WebServlet("/AlterInformation")
public class AlterInformation extends HttpServlet {
    UserDao userDao = new UserDao();

    /**
     * 获取前端用户更改的信息，并插入数据库
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void updatePersonal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String  mail=request.getParameter("email");
        try {
            User user = userDao.selectByMail(mail);

            //获取网页上输入的各项信息
            String phone = request.getParameter("phone");
            String username = URLDecoder.decode(request.getParameter("username"),"UTF-8");
            Long age = Long.parseLong(request.getParameter("age"));
            String email = request.getParameter("email");

            boolean isOk = true;


                if (isOk) {
                    User userNow =new User();
                    System.out.println("信息修改");
                    System.out.println(username);

                    userNow.setPhone(phone);
                    userNow.setAge(age);
                    userNow.setMail(email);
                    userNow.setName(username);

                    //插入数据库
                    UserDao userdao = new UserDao();
                    userdao.connect();
                    if (userdao.update(userNow)) {
                        JSONObject json = new JSONObject();
                        json.put("phone", phone);
                        json.put("username", username);
                        json.put("age", age);
                        json.put("mail", email);
                        out.println(json.toJSONString());
                    }
                }
        }
        catch (Exception e) {
            out.println("error");
        }

    }


    /**
     * 给前端提供用户原有信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        try {

            String mail = request.getParameter("email");
            User user = userDao.selectByMail(mail);
            String phone = user.getPhone();
            String username = URLEncoder.encode(user.getName(), "UTF-8");
            Long age = user.getAge();

            response.setContentType("text");
            PrintWriter out = response.getWriter();

            JSONObject json = new JSONObject();
            json.put("phone", phone);
            json.put("username", username);
            json.put("age", age);
            json.put("mail", mail);

            out.println(json.toJSONString());

        } catch (Exception e) {
            System.out.println("error2");
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        try{
            updatePersonal(request,response);
        }
        catch(Exception e) {
            System.out.println("error1");
        }
    }
}
