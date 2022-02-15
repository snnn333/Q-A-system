package qs.servlet;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

import qs.common.*;

/**
 * 同一用户再次登录，返回悬赏信息
 */
@WebServlet("/G-A")
public class ReturnAwarding extends HttpServlet
{
    public void getInformationAwarding(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        Long questionId= Long.valueOf(req.getParameter("questionId"));
        String mail = req.getParameter("email");

        try {
            QuestionDao qdao=new QuestionDao();
            qdao.connect();
            Question question=qdao.selectById(questionId);
            out.println(question.getAlreadyAward());
        }catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * 调用获取信息函数
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{

            this.getInformationAwarding(request,response);
        }
        catch(Exception e) {
            System.out.println("error");
        }
    }
}
