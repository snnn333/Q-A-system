package qs.common;


import java.sql.*;

/**
 * 数据库中SubComment实体
 */
public class SubComment {

    private Long id;
    private Long userId;
    private Long commentId;
    private String content;
    private Timestamp time;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getCommentId() { return commentId; }
    public String getContent() { return content; }
    public Timestamp getTime() { return time; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = id; }
    public void setCommentId(Long commentId) { this.commentId = id; }
    public void setContent(String content) { this.content = content; }
    public void setTime(Timestamp time) { this.time = time; }

}
