package com.nguyen.wechat.mapper;

import com.nguyen.wechat.model.AppAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author RWM
 * @date 2018/4/3
 */
public interface AppAuthTokenMapper extends JpaRepository<AppAuthToken, String> {

    @Query("FROM AppAuthToken at WHERE at.openId = :openId")
    AppAuthToken findByOpenId(@Param("openId") String openId);
}
