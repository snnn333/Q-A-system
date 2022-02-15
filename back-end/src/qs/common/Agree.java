package qs.common;

/**
 * 数据库中Agree实体
 */
public class Agree {
    private Long answerId;
    private Long userId;

    public Long getAnswerId() { return answerId; }
    public Long getUserId() { return userId; }

    public void setAnswerId(Long answerId) { this.answerId = answerId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
