package qs.common;

import java.sql.*;

/**
 * 数据库中QuestionType实体
 */
public class QuestionType {
    private Long id;
    private String name;
    private String icon;

    public Long getId() { return id; }
    public String getName() {return name; }
    public String getIcon() { return icon; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setIcon(String icon) { this.icon = icon; }

}
