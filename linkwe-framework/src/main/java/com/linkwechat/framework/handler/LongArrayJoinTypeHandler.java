package com.linkwechat.framework.handler;

import com.linkwechat.common.utils.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: Mybatis数组转字符串a, b, c, d, e
 * @author: HaoN
 * @create: 2020-09-08 17:19
 **/
@MappedTypes(value = { Integer[].class,Short[].class,Long[].class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class LongArrayJoinTypeHandler extends BaseTypeHandler<Long[]> {

    String split = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType)
            throws SQLException {

        ps.setString(i, StringUtils.join(parameter, split));
    }

    @Override
    public Long[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String reString = rs.getString(columnName);
        if (reString != null && reString.length() > 0) {
            String[] arrs = reString.split(split);

            List<Long> idList = new ArrayList<>(arrs.length);
            for (String id : arrs) {
                if (id != null && id.length() > 0) {
                    idList.add(Long.valueOf(id));
                }
            }
            return idList.toArray(new Long[idList.size()]);
        }
        return null;
    }

    @Override
    public Long[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String reString = rs.getString(columnIndex);
        if (reString != null && reString.length() > 0) {
            String[] arrs = reString.split(split);

            List<Integer> idList = new ArrayList<>(arrs.length);
            for (String id : arrs) {
                if (id != null && id.length() > 0) {
                    idList.add(Integer.valueOf(id));
                }
            }
            return idList.toArray(new Long[idList.size()]);
        }
        return null;
    }

    @Override
    public Long[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String reString = cs.getString(columnIndex);
        if (reString != null && reString.length() > 0) {
            String[] arrs = reString.split(split);

            List<Integer> idList = new ArrayList<>(arrs.length);
            for (String id : arrs) {
                if (id != null && id.length() > 0) {
                    idList.add(Integer.valueOf(id));
                }
            }
            return idList.toArray(new Long[idList.size()]);
        }
        return null;
    }

}

