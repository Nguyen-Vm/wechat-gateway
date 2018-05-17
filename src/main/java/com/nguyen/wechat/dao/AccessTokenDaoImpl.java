package com.nguyen.wechat.dao;

import com.nguyen.wechat.model.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author RWM
 * @date 2018/5/17
 */
public class AccessTokenDaoImpl implements AccessTokenDao{

    private final RowMapper<AccessToken> mapper = new AccessTokenMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public AccessToken selectByAppId(String appId) {
        return jdbcTemplate.queryForObject("select * from t_access_token where app_id = ?", new Object[]{"wx09a57365be7f2c53"}, mapper);
    }

    private static final class AccessTokenMapper implements RowMapper<AccessToken> {

        @Override
        public AccessToken mapRow(ResultSet resultSet, int i) throws SQLException {
            AccessToken token = new AccessToken();
            token.id = resultSet.getString("id");
            token.createTime = resultSet.getDate("create_time");
            token.updateTime = resultSet.getDate("update_time");
            token.appId = resultSet.getString("app_id");
            token.accessToken = resultSet.getString("access_token");
            token.expiresIn = resultSet.getLong("expires_in");
            return token;
        }
    }
}
