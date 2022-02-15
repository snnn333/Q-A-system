package qs.common;


import java.sql.Time;
import java.sql.Timestamp;

/**
 * 数据库中Comment实体
 */
public class Comment {

    private Long id;
    private Long userId;
    private Long answerId;
    private String content;
    private Timestamp time;

    public Long getId() { return id; }
    public Long getUserId() {return userId; }
    public Long getAnswerId() { return answerId; }
    public String getContent() { return content; }
    public Timestamp getTime() { return time; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setAnswerId(Long answerId) { this.answerId = answerId; }
    public void setContent(String content) {this.content = content; }
    public void setTime(Timestamp time) { this.time = time; }
}
