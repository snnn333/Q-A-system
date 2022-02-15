package qs.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import qs.common.*;

/**
 * 用户修改密码
 */
@WebServlet("/AlterPsw")
public class AlterPsw extends HttpServlet

{
    UserDao userDao = new UserDao();

    /**
     * 用户修改密码,并加密存入数据库
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void changePassword(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
        response.setContentType("text");
        PrintWriter out = response.getWriter();

        String  mail=request.getParameter("email");

        try {

            User user = userDao.selectByMail(mail);
            User userNow = user;

            String oldpsw  = request.getParameter("oldpsw");//旧密码
            String psw = user.getPassword();

            //判断是否生成公钥
            if (RSAUtil.generateKey()) {
                System.out.println("-----成功生成公私钥-------");

            }
            RSAUtil.loadKey();

            //解密公钥，加密私钥
            oldpsw = RSAUtil.decrypt(oldpsw);
            String oldone = MD5Util.encrypt(oldpsw);

            //判断新旧密码是否一致
            if(oldone.equals(psw))
            {
                String newpsw = request.getParameter("newpsw");//新密码

                newpsw = RSAUtil.decrypt(newpsw);
                String newone = MD5Util.encrypt(newpsw);

                UserDao userdao = new UserDao();
                userdao.connect();
                userNow.setPassword(newone);

                try {
                    //数据库更新
                    userdao.update(userNow);
                } catch (Exception e) {
                    System.out.println("error1");
                }

                System.out.println("修改成功");
                out.println("successfully");
            }
            else
            {
                System.out.println("旧密码输入错误");//Application
                out.println("failed");
            }

        }catch (Exception e) {
            System.out.println("error2");
        }
    }

    /**
     * 调用修改密码函数
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        try{
            changePassword(request,response);
        }
        catch(Exception e) {
            System.out.println("error");
        }
    }
}
