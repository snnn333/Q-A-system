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
 * 忘记密码后重置密码
 */
@WebServlet("/ForgetPassword")
public class ForgetPassword extends HttpServlet
{
    /**
     *重置新密码后插入数据库
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void changePassword(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
        PrintWriter pw = response.getWriter();

        String  mail=request.getParameter("email");
        String password = request.getParameter("password");//新密码

        try {
            UserDao userDao = new UserDao();
            User user = userDao.selectByMail(mail);

            response.setContentType("text");
            PrintWriter out = response.getWriter();
            try
            {
                //判断是否生成公钥
                if (RSAUtil.generateKey()) {
                    System.out.println("-----成功生成公私钥-------");

                }
                RSAUtil.loadKey();

                //解密公钥，加密私钥
                password = RSAUtil.decrypt(password);
                String psw = MD5Util.encrypt(password);

                //更新用户信息
                user.setPassword(psw);
                userDao.update(user);
                out.println("successfully");

            }catch(Exception e){
                out.println("error");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

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
