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
import static jdk.nashorn.internal.objects.Global.exit;
import javax.servlet.annotation.WebServlet;

import qs.common.*;

/**
 * 上传头像
 */
@WebServlet("/uploadphoto")
public class UploadPhoto extends HttpServlet {

    /**
     * 获取图片的路径并插入数据库
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取网页上输入的各项信息
        String  mail=request.getParameter("email");
        String  photo=request.getParameter("uri");
        PrintWriter out = response.getWriter();
        try {
            UserDao userDao = new UserDao();
            User user = userDao.selectByMail(mail);

            //数据库进行修改
            user.setPhoto(photo);
            UserDao userdao = new UserDao();
            userdao.connect();
            if (userdao.update(user)) {
                out.println("success");
            }
            else {
                out.println("error");
            }

        }
        catch (Exception e) {
            System.out.println("error");
        }
    }

    /**
     * 调用获取图片的函数
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String  mail=request.getParameter("email");

        PrintWriter out = response.getWriter();
        try {
            //给网页传输photo
            UserDao userDao = new UserDao();
            User user = userDao.selectByMail(mail);

            out.println(user.getPhoto());
        }
        catch (Exception e) {
            System.out.println("error");
        }
    }

}
