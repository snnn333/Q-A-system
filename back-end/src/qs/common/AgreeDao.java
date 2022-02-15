package qs.common;

 **/
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO的子类，对Agree进行操作操作
 */
public class AgreeDao extends DAO {
    public AgreeDao(){ super(); }

    /**
     * 数据库自动生成id
      * @return
     * @throws SQLException
     */
    public synchronized Long nextId() throws SQLException {
        String sql = "select max(id) from Agree";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return rs.getLong(1) + 1;
        }else throw new SQLException();
    }
}
