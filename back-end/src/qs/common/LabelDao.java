package qs.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO的子类，对Label进行操作操作
 */
public class LabelDao extends DAO {

    public LabelDao(){
        super();
    }

    /**
     *数据库自动生成id
     * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException {
        String sql = "select max(id) from Label";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }

    /**
     *建立一个新标签
     * @param name
     * @return
     * @throws SQLException
     */
    public synchronized Long createLabel(String name) throws SQLException{
        String sql = "insert into Label(id, name) values (?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        Long id = nextId();
        ptmt.setLong(1,id);
        ptmt.setString(2,name);
        if(ptmt.executeUpdate() > 0)
            return id;
        else throw new SQLException();
    }

    /**
     *更新某一属性
     * @param id
     * @param attribute
     * @param value
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Boolean update(Long id,String attribute,T value) throws SQLException{
        String sql = "update Label set " + attribute + " = ? where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setObject(1,value, getSqlType(value));
        ptmt.setLong(2,id);
        return ptmt.executeUpdate() > 0;
    }

    /**
     *通过id查找标签
     * @param id
     * @return
     * @throws SQLException
     */
    public Label selectById(Long id) throws SQLException{
        String sql = "select * from Label where id = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setLong(1,id);
        ResultSet rs = ptmt.executeQuery();
        Label label = null;
        while(rs.next()){
            label = new Label();
            label.setId(rs.getLong("id"));
            label.setName(rs.getString("name"));

        }
        return label;
    }

    /**
     *通过标签名查找标签
     * @param name
     * @return
     * @throws SQLException
     */
    public Label selectByName(String name) throws SQLException{
        String sql = "select * from Label where name = ?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1,name);
        ResultSet rs = ptmt.executeQuery();
        Label label = null;
        while(rs.next()){
            label = new Label();
            label.setId(rs.getLong("id"));
            label.setName(rs.getString("name"));

        }
        return label;
    }
}
