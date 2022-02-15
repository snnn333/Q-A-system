package qs.servlet;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import qs.common.UserDao;

@WebServlet("/SendEmailtwo")
public class ForgetPasswordEmail extends HttpServlet{

    /**
     * 邮件发送的相关设置，与发送邮件
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void send(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String myEmailAccount = "2081672516@qq.com";
        String myEmailPassword ="kdksknsmlwdscjcf";

        // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
        String myEmailSMTPHost = "smtp.qq.com";
        String receiveMailAccount=request.getParameter("email");
        try {
            // 创建参数配置, 用于连接邮件服务器的参数配置
            Properties props = new Properties();                    // 参数配置
            props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
            props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

            final String smtpPort = "465";
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", smtpPort);

            // 根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(props);
            session.setDebug(true);

            MimeMessage message = createMimeMessage(session, myEmailAccount, myEmailPassword, request, response, receiveMailAccount);

            // 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();
            transport.connect(myEmailAccount, myEmailPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch(Exception e)
        {
            System.out.println("error");
        }
    }

    /**
     * 创建一封邮件
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */

    public MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, HttpServletRequest request, HttpServletResponse response,String receiveMailAccount) throws Exception {

        UserDao userdao = new UserDao();
        String username = userdao.selectByMail(receiveMailAccount).getName();

        // 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sendMail, ":D 答", "UTF-8"));//(MimeUtility.encodeText("发送人：") + "问答系统"));//.setFrom(new InternetAddress(sendMail, "问答系统", "UTF-8"));

            InternetAddress address = new InternetAddress(receiveMailAccount);
            message.setRecipient(MimeMessage.RecipientType.TO, address);//new InternetAddress(receiveMail, username+"用户", "UTF-8"));


            String link = "http://localhost:8080/#/resetPsw";
            message.setSubject("重置密码", "UTF-8");

            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("尊敬的" + username + "：");
            multipart.addBodyPart(messageBodyPart1);

            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setText("你好,");
            multipart.addBodyPart(messageBodyPart2);

            BodyPart messageBodyPart3 = new MimeBodyPart();
            messageBodyPart3.setText("请点击"); //+ link + "重置密码");
            multipart.addBodyPart(messageBodyPart3);

            BodyPart messageBodyPart4 = new MimeBodyPart();
            messageBodyPart4.setText(link);
            multipart.addBodyPart(messageBodyPart4);

            BodyPart messageBodyPart5 = new MimeBodyPart();
            messageBodyPart5.setText("重置密码");
            multipart.addBodyPart(messageBodyPart5);
            message.setContent(multipart, "text/html;charset=UTF-8");

            message.setSentDate(new Date());
            message.saveChanges();
        }catch(Exception e)
        {
            System.out.println("error!!!!");;
        }

        return message;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        try{
            this.send(request,response);
        }
        catch(Exception e) {
            System.out.println("error2");
        }
    }
}

