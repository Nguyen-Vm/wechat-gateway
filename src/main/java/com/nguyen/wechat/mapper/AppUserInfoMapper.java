package com.nguyen.wechat.mapper;

import com.nguyen.wechat.model.AppUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author RWM
 * @date 2018/4/3
 */
public interface AppUserInfoMapper extends JpaRepository<AppUserInfo, String> {
    @Query("FROM AppUserInfo ui WHERE ui.openId = :openId")
    AppUserInfo findByOpenId(@Param("openId") String openId);
}
