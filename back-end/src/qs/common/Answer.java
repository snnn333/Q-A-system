package qs.common;

import java.sql.*;

/**
 * 数据库中Answer实体
 */
public class Answer {
    private Long id;
    private Long userId;
    private Long questionId;
    private String content;
    private Timestamp time;
    private Long numOfAgree;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getQuestionId() { return questionId; }
    public String getContent() { return content; }
    public Timestamp getTime() {return  time; }
    public Long getNumOfAgree() { return numOfAgree; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public void setContent(String content) { this.content = content; }
    public void setTime(Timestamp time) { this.time = time; }
    public void setNumOfAgree(Long numOfAgree) { this.numOfAgree = numOfAgree; }

}
