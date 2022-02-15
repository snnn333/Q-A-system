package qs.common;

import java.sql.Timestamp;

/**
 * 数据库中Label实体
 */
public class Label {
    private Long id;
    private String name;

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setId(Long id) { this.id = id; }
    public void setName(String Name) { this.name = name; }

}
