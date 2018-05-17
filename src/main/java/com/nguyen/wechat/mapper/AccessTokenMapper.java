package com.nguyen.wechat.mapper;

import com.nguyen.wechat.dao.AccessTokenDao;
import com.nguyen.wechat.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author RWM
 * @date 2018/3/25
 */
public interface AccessTokenMapper extends JpaRepository<AccessToken, String>, AccessTokenDao {

    @Query("FROM AccessToken at WHERE at.appId = :appId")
    AccessToken findByAppId(@Param("appId") String appId);
}
