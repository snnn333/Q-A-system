package qs.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

/**
 * 文件上传
 */
@WebServlet("/upload")
public class Upload extends HttpServlet {

    private boolean isMultipart;
    private static int maxFileSize = 1024 * 1024 * 10;  // 最大允许上传10M的文件
    private static int maxMemSize = 100 * 1024;         // 最大缓存大小

    /**
     * 接受Post请求，读取其中的文件并保存至本地
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        // 检查是否有一个文件上传请求
        Boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            out.print("No file error!");
            return;
        }

        ArrayList<String> uris = saveFile(req, resp);
        JSONArray jsonArray = new JSONArray();
        for(String uri :uris ){
            JSONObject json = new JSONObject();
            json.put("uri",uri);
            jsonArray.add(json);
        }

        out.print(jsonArray.toJSONString());
    }


    /**
     * 接受文件信息，保存到本地硬盘，并返回uri路径信息
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public static ArrayList<String> saveFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ArrayList<String> uris = new ArrayList<>();
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // 设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        String path = req.getServletContext().getRealPath("/");
        factory.setRepository(new File(path));

        // 创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 允许上传的文件大小的最大值
        upload.setSizeMax(maxFileSize);

        try{
            // 解析请求，获取文件项
            List<FileItem> fileItems = upload.parseRequest(req);
            // 处理上传的文件项
            Iterator it = fileItems.iterator();

            for (FileItem fi:fileItems){
                //如果获取的表单信息是普通的文本信息,则跳过，本程序只处理文件项
                if(fi.isFormField()) {
                    continue;
                }

                // 获取表单属性的名字
                String name = fi.getFieldName();

                String fileName = fi.getName();
                String fileType = fileName.substring(fileName.lastIndexOf("."));

                Random random = new Random();

                // 找到一个没有用过的文件名字
                String saveName = Math.abs(random.nextInt()) + fileType;
                String uri = String.format("contents/%s", saveName);
                while(exits(path + uri)){
                    saveName = Math.abs(random.nextInt()) + fileType;
                    uri = String.format("contents/%s", saveName);
                }

                // 保存文件
                File file = new File(path + uri);
                fi.write(file);

                // 保存uri
                uris.add("/"+uri);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return uris;
    }

    public static boolean exits(String filePath){
        File file = new File(filePath);
        return  file.exists();
    }


}
