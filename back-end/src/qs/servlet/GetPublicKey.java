package qs.servlet;


import qs.common.RSAUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用于接受公钥请求的Servlet
 */
@WebServlet("/rsaPassword")
public class GetPublicKey extends HttpServlet {

    /**
     * 接受公钥信息请求，利用qs.common包中的RSAUtil类获取本地公钥信息，并发送给请求方
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            if (!RSAUtil.hasKey()) {
                RSAUtil.generateKey();
            }
            RSAUtil.loadKey();
            String publicKey = RSAUtil.getKeyString(RSAUtil.getKeys().get("publicKey"));
            out.print(publicKey);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
