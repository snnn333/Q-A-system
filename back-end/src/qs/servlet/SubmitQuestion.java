package qs.servlet;


import com.alibaba.fastjson.JSONArray;
import qs.common.*;

import javax.print.DocFlavor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * 接受前端请求，插入新增问题或修改已保存问题
 */
@WebServlet("/submitQuestion")
public class SubmitQuestion extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        String entity = req.getParameter("entity");
        if(action.equals("update") && entity.equals("Question")){
            doUpdateQuestion(req, resp);
        }else if(action.equals("select") && entity.equals("Question")){
            doSelectQuestion(req, resp);
        }else{
            out.print("Params error!");
        }

    }

    /**
     * 查询现有的问题，从而获取上次保存的信息
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    void doSelectQuestion(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        PrintWriter out = resp.getWriter();

        Long qid = Long.valueOf(req.getParameter("questionId"));
        String sql = "select * from Question where id = " + qid;
        try {
            DAO dao = new DAO();
            dao.excuteQuery(sql);
            JSONArray jsonArray = dao.toJsonArray();
            sql = "select *, Label.name from Q_L, Label where questionId = " + qid + " and Label.id = labelId";
            dao.excuteQuery(sql);
            jsonArray.add(dao.toJsonArray());
            out.print(jsonArray.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新或插入新的问题
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    void doUpdateQuestion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        User user = (User)req.getSession().getAttribute("user");
        PrintWriter out = resp.getWriter();
//        // for debug
//        user = new User();
//        user.setId(2l);
        if(user == null) {
            out.print("请登录！");
            return;
        }

        String title = URLDecoder.decode(req.getParameter("title"),"UTF-8");
        String content = URLDecoder.decode(req.getParameter("content"),"UTF-8");
        String labels = URLDecoder.decode(req.getParameter("labels"), "UTF-8");
        Long type  = Long.valueOf(req.getParameter("questionType"));
        Long qid = Long.valueOf(req.getParameter("questionId"));
        Long isReleased = Long.valueOf(req.getParameter("isReleased"));
        Long score = Long.valueOf(req.getParameter("score"));

        try {
            QuestionDao qdao = new QuestionDao();
            QuestionTypeDao qtdao = new QuestionTypeDao();

            Question question = null;
            if(qid >= 0)
                question = qdao.selectById(qid);

            // 如果是新的问题,则数据库新插入一列
            if(question == null){
                qid = qdao.createQuestion(user.getId());
            }
            qdao.update(qid, "content", content);
            qdao.update(qid, "title", title);
            qdao.update(qid, "type", type);
            qdao.update(qid, "content", content);
            qdao.update(qid, "reward", score);
            qdao.update(qid, "isReleased", isReleased==1?1:question.getIsReleased());

            // 更新问题的标签
            String labelList[] = labels.split("\\s+");
            LabelDao ldao = new LabelDao();


            for (String name:labelList){
                if(name.equals("")) continue;;
                Label label = ldao.selectByName(name);
                if(label == null){
                    ldao.createLabel(name);
                    label = ldao.selectByName(name);
                }
                String sql = String.format("insert into Q_L(questionId,labelId) values(%s,%s)",qid, label.getId());
                try{
                    if(ldao.excuteUpdate(sql) == false){
                        System.out.println("该问题已经具有该标签");
                    }
                }catch (Exception e){
                    System.out.println("该问题已经具有该标签");
                }
            }
            out.print("更新成功！");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
