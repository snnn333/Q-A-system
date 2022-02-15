package qs.servlet;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import qs.common.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 问题索引页面业务逻辑实现
 */
@WebServlet("/index")
public class Index extends HttpServlet {

    /**
     * 接受Get请求，实现问题的关键字搜索、索引、查询
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        /* 读取并解析各个参数 */

        String action = req.getParameter("action");
        String entity = req.getParameter("entity");
        try {
            if (!(action.equals("select")) || !(entity.equals("Question"))){
                out.print("'action' and 'entity' Paramters error!");
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String keywords =  null;
        String questionType = null;
        String labels = null;
        String labelSet = null;
        String questionTypeSet = null;
        try {
            keywords = URLDecoder.decode(req.getParameter("keywords"), "UTF-8");
            questionType = URLDecoder.decode(req.getParameter("questionType"), "UTF-8");
            labels = URLDecoder.decode(req.getParameter("labels"), "UTF-8");
            if(req.getParameter("debug") != null) {
                keywords = new String(req.getParameter("keywords").getBytes("ISO-8859-1"), "UTF-8");
                questionType = new String(req.getParameter("questionType").getBytes("ISO-8859-1"), "UTF-8");
                labels = new String(req.getParameter("labels").getBytes("ISO-8859-1"), "UTF-8");
            }

            String arr[] = labels.split("\\s+");
            labelSet = process(labels);
            questionTypeSet = process(questionType);

        }catch (Exception e){
            out.print("Paramters error!");
            e.printStackTrace();
        }

        int index = 0, num = 0;
        try {
            index = Integer.valueOf(req.getParameter("index"));
            num = Integer.valueOf(req.getParameter("num"));
        }catch (NullPointerException e){
            index = 0;
            num = 20;
        }catch (Exception e){
            e.printStackTrace();
        }

        /* 首先按照组合关键字搜索问题 */
        String sql = null;
        if(keywords.equals("")){
            sql = "select * from Question where true ";
        }else {
            String searchMode = QuestionDao.getSearchMode(keywords);
            sql = "select * " +
                    String.format(" ,match(title) against ('%s' in boolean mode) * 10 as res1 ", searchMode) +
                    String.format(" ,match(content) against ('%s' in boolean mode) * 3 as res2 ",searchMode) +
                    " from Question " +
                    String.format(" where match(title, content) against('%s' in boolean mode) ",searchMode);

        }
        /* 其次筛选特定类别的问题 */
        if(questionType.indexOf("全部") == -1){
            sql += String.format(" and type in (select id from QuestionType where name in %s ) ",questionTypeSet);
        }

        /* 最后筛选特定标签的问题 */
        if(labels.indexOf("全部") == -1){
            sql += String.format(" and exists (select * from Q_L where questionId = Question.id and labelId in (select id from Label where name in %s ) ) ", labelSet);
        }

        if(!keywords.equals("")){
            sql += " order by (res1) + (res2) desc ";
        }else {
            sql += " order by time desc ";
        }

        /* for debug */
        System.out.println(sql);

        JSONArray queryResult = null;
        JSONArray result = new JSONArray();

        try {
            DAO dao = new DAO();
            dao.excuteQuery(sql);
            queryResult = dao.toJsonArray(index, num);
        }catch (Exception e){
            e.printStackTrace();
            out.print("sql error!");
        }

        /* 查询每个问题的浏览量和提问者 */
        try {
            UserDao udao = new UserDao();
            QuestionDao qdao = new QuestionDao();

            for (int i = 0; i < queryResult.size(); i++) {
                JSONObject json = queryResult.getJSONObject(i);
                if(json.getLong("isReleased") == 0) continue;
                Long userId = json.getLong("userId");
                User user = udao.selectById(userId);
                json.put("name", user.getName());
                json.put("numOfAnswer", qdao.getNumOfAnswer(json.getLong("id")));
                result.add(json);
            }
            out.print(result.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }



    }


    /**
     * 解析空格隔开的参数值，并组装成sql语句中set的形式
     * @param words
     * @return
     */
    private String process(String words){
        String arr[] = words.split("\\s+");
        String rs = "";
        for (int i = 0; i < arr.length; i++) {
            if(i == 0) rs += String.format(" '%s' ",arr[i]);
            else rs += String.format(" , '%s' ",arr[i]);
        }
        return  String.format("(%s)",rs);
    }

}







