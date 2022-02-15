package qs.servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import com.alibaba.fastjson.*;
import com.mysql.cj.xdevapi.Session;
import qs.common.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Set;

/**
 * 实现管理员增删改查用户、问题、标签、类别的Servlet
 */
@WebServlet("/admin")
public class Admin extends HttpServlet {


    /**
     * 接受处理Get请求，分情况进入业务逻辑代码
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        switch (action){
            case "select": doSelect(req, resp); break;
            case "insert": doInsert(req, resp); break;
            case "update": doUpdate(req, resp); break;
            case "delete": doDelete(req, resp); break;
        }

    }

    /**
     * 实现管理员查询各种实体功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doSelect(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        DAO dao = new DAO();
        PrintWriter out = resp.getWriter();
        String entity = req.getParameter("entity");
        try {
            // 如果是查询用户，直接给出全部用户信息  !注意规定管理员不可管理管理员的信息
            if (entity.equals("User"))
                dao.excuteQuery("select id,name,mail,cannotSpeak,cannotLogin from User where role = 0");

            // 如果是查询问题类型，直接给出全部问题类型
            if(entity.equals("QuestionType"))
                dao.excuteQuery("select * from QuestionType");

            // 如果是查询对应问题类型的问题
            // 如果没有传输问题类型，则默认给出全部问题列表，否则给出对应问题类型的问题
            if(entity.equals("Question")){
                String type = req.getParameter("type");
                String sql = new String("select id,title,label,time from Question");
                if(type != null) sql += "where type = " + type;
                dao.excuteQuery(sql);
            }

            if(entity.equals("Label")){
                dao.excuteQuery("select Label.*, (select count(*) from Q_L where labelId=Label.id) count from Label");

            }
            if(dao.getResultSet() != null)
                out.print(dao.toJsonArray().toJSONString());
            else out.print("error!");

        }
        catch (SQLException e){
            out.print("failure");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * 实现管理员插入各种实体功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doInsert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        PrintWriter out = resp.getWriter();
        String entity = req.getParameter("entity");
        try{
            // 插入新的问题类型
            if(entity.equals("QuestionType")){
                // 获取新的问题类型的名字
                String name = URLDecoder.decode(req.getParameter("name"),"UTF-8");
                QuestionTypeDao qtdao = new QuestionTypeDao();
                Long id = qtdao.createQuestionType(name);
                out.print("success");
            }
            if(entity.equals("Label")){
                // 获取新的问题类型的名字
                String name = URLDecoder.decode(req.getParameter("name"),"UTF-8");
                LabelDao ldao = new LabelDao();
                Long id = ldao.createLabel(name);
                out.print("success");
            }
        }
        catch (SQLException e){
            out.print("failure");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    /**
     * 实现管理员更新实体功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String entity = req.getParameter("entity");
        Long id = Long.valueOf(req.getParameter("id"));
        PrintWriter out = resp.getWriter();
        try{
            if(entity.equals("User")){

                UserDao udao = new UserDao();
                String []paramters = {"cannotSpeak", "cannotLogin"};
                for(int i = 0; i < paramters.length; i++){
                    if(req.getParameter(paramters[i]) != null) {
                        Long value = Long.valueOf(req.getParameter(paramters[i]));
                        udao.update(id,paramters[i],value);
                    }
                }

            }else if(entity.equals("QuestionType")){
                QuestionTypeDao qtdao = new QuestionTypeDao();
                String name = URLDecoder.decode(req.getParameter("name"),"UTF-8");
                qtdao.update(id, "name",name);
            }else if(entity.equals("Label")){
                LabelDao ldao = new LabelDao();
                String name = req.getParameter("name");
                ldao.update(id, "name", name);
            }
            out.print("success");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            out.print("failure");
        }

    }

    /**
     * 实现管理员删除各种实体功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String entity = req.getParameter("entity");
        String id = req.getParameter("id");
        String name = URLDecoder.decode(req.getParameter("name"), "UTF-8");
        name = String.format("\"%s\"", name);
        DAO dao = new DAO();
        try {
            if(entity.equals("User")) {
                if (dao.excuteUpdate("delete from User where id = " + id))
                    out.print("success");
                else out.print("failure");
            }
            if(entity.equals("QuestionType") && id != null) {
                if (dao.excuteUpdate("delete from QuestionType where id = " + id))
                    out.print("success");
                else out.print("failure");
            }
            if(entity.equals("QuestionType") && name != null) {
                if (dao.excuteUpdate("delete from QuestionType where name = " + name ))
                    out.print("success");
                else out.print("failure");
            }
            if(entity.equals("Label") && id != null) {
                if (dao.excuteUpdate("delete from Label where id = " + id))
                    out.print("success");
                else out.print("failure");
            }
            if(entity.equals("Label") && name != null) {
                if (dao.excuteUpdate("delete from Label where name = " + name))
                    out.print("success");
                else out.print("failure");
            }

        }
        catch (Exception e){
            out.print("failure");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }



    }
}
