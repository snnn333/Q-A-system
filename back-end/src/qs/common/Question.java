package qs.common;

import java.sql.*;
/**
 * 数据库中Question实体
 */
public class Question {

    private Long id;
    private Long userId;
    private Long frequency;
    private Long type;
    private String content;
    private Timestamp time;
    private String label;
    private String title;
    private Long isReleased;
    private Long reward;
    private Long alreadyAward;

    public Long getId(){ return id; }
    public Long getUserID() { return userId; }
    public Long getFrequency() { return frequency; }
    public Long getType() { return type; }
    public String getContent() { return content; }
    public Timestamp getTime() { return time; }
    public String getLabel() { return label; }
    public String getTitle() { return title; }
    public Long getIsReleased() { return isReleased;}
    public Long getReward() { return reward; }
    public Long getAlreadyAward() { return alreadyAward; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setFrequency(Long frequency) { this.frequency = frequency; }
    public void setType(Long type) { this.type = type; }
    public void setContent(String content) {this.content = content; }
    public void setTime(Timestamp time) { this.time = time; }
    public void setLabel(String label) { this.label = label; }
    public void setTitle(String title) { this.title = title; }
    public void setIsReleased(Long isReleased) { this.isReleased = isReleased; }
    public void setReward(Long reward) { this.reward = reward; }
    public void setAlreadyAward(Long alreadyAward) { this.alreadyAward = alreadyAward; }
}
