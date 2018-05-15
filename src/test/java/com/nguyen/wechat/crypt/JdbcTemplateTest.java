package com.nguyen.wechat.crypt;

import com.nguyen.wechat.model.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author RWM
 * @date 2018/5/14
 * @description:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JdbcTemplateTest {

    private final RowMapper<AccessToken> mapper = new AccessTokenMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void selectAccessToken() {
        AccessToken accessToken = jdbcTemplate.queryForObject("select * from t_access_token where app_id = ?", new Object[]{"wx09a57365be7f2c53"}, mapper);
        log.info("app_id: " + accessToken.appId + ", access_token: " + accessToken.accessToken);
    }

    private static final class AccessTokenMapper implements RowMapper<AccessToken> {

        @Override
        public AccessToken mapRow(ResultSet resultSet, int i) throws SQLException {
            AccessToken token = new AccessToken();
            token.appId = resultSet.getString("app_id");
            token.accessToken = resultSet.getString("access_token");
            token.expiresIn = resultSet.getLong("expires_in");
            return token;
        }
    }
}
