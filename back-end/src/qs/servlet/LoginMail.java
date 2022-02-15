package qs.servlet;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import qs.common.*;

/**
 * 邮箱登录
 */
@WebServlet("/loginMail")
public class LoginMail extends HttpServlet
{
    public void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        response.setCharacterEncoding("UTF-8");
        String email=request.getParameter("mail");
        String password = request.getParameter("password");

        UserDao userdao = new UserDao();
        userdao.connect();
        try
        {
            //公钥解密，私钥加密后，与数据库匹配
            if (RSAUtil.generateKey()) {
                System.out.println("-----成功生成公私钥-------");

            }
            RSAUtil.loadKey();
            password = RSAUtil.decrypt(password);
            String psw = MD5Util.encrypt(password);


            User user =  userdao.selectByMail(email);

            //判断是否允许登录
            if (user != null && user.getPassword().equals(psw))
            {
                if(user.getCannotLogin()==0)
                //登录成功
                {
                    response.setContentType("text");
                    PrintWriter out = response.getWriter();

                    //判断登录者角色
                    if(user.getRole()==0) {

                        String u="user";
                        Long id=user.getId();
                        String name=  URLDecoder.decode(user.getName(),"UTF-8");
                        String photo=user.getPhoto();
                        JSONObject json = new JSONObject();
                        json.put("role", u);
                        json.put("userId", id);
                        json.put("name",name);
                        json.put("photo",photo);

                        out.println(json.toJSONString());

                        HttpSession Session = request.getSession();
                        Session.setAttribute("user", user);//将登陆成功的用户保存于session
                    }
                    else if(user.getRole()==1)
                    {
                        String u="manager";
                        Long id=user.getId();
                        String name=  URLDecoder.decode(user.getName(),"UTF-8");
                        String photo=user.getPhoto();
                        JSONObject json = new JSONObject();
                        json.put("role", u);
                        json.put("userId", id);
                        json.put("name",name);
                        json.put("photo",photo);

                        out.println(json.toJSONString());

                        HttpSession Session = request.getSession();
                        Session.setAttribute("user", user);//将登陆成功的用户保存于session
                    }
                }

                else
                {
                    String u="forbidden";
                    Long id=user.getId();
                    JSONObject json = new JSONObject();
                    json.put("role", u);
                    json.put("userId", id);

                    response.setContentType("text");
                    PrintWriter out = response.getWriter();
                    out.println(json.toJSONString());
                }
            } else
            {
                response.setContentType("text");
                PrintWriter out = response.getWriter();
                String u="error";
                Long id=user.getId();
                JSONObject json = new JSONObject();
                json.put("role", u);
                json.put("userId", id);
                out.println(json.toJSONString());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
    {
        this.save(request,response);
    }
}
