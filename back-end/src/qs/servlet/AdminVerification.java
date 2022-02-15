package qs.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import com.alibaba.fastjson.*;
import com.mysql.cj.xdevapi.Session;
import qs.common.UserDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Set;

public class AdminVerification extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * 处理验证请求，验证用户是否是管理员
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        // 验证用户是否是管理员，如果是，则允许接下来的操作。
        HttpSession session = req.getSession();
        Long userId = (Long)session.getAttribute("userId");
        try {
            UserDao udao = new UserDao();
            if(udao.selectById(userId).getRole() != 1)
                out.print("success");
            else out.print("failure");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }


}
