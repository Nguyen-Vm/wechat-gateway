package com.nguyen.wechat.dto.request;

import com.google.common.collect.Lists;
import com.nguyen.wechat.dto.MessageCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.linker.foundation.dto.IRequest;
import org.linker.foundation.dto.exception.AppException;
import org.linker.foundation.utils.CollectionUtils;

import java.util.List;

/**
 * @author RWM
 * @date 2018/9/13
 */
@ApiModel("微信自定义菜单创建接口请求体")
public class WechatMenuCreateRequest implements IRequest {

    @ApiModelProperty("微信公众号")
    public String appId;

    @ApiModelProperty("微信凭证密钥")
    public String appSecret;

    @ApiModelProperty("菜单列表")
    public List<WechatMenu> wechatMenuList;

    public static class WechatMenu {

        @ApiModelProperty("类型 view网页类型，click点击类型")
        public String type;

        @ApiModelProperty("名称")
        public String name;

        @ApiModelProperty("click类型时必须，不超过128字节")
        public String key;

        @ApiModelProperty("view类型时必须，不超过1024字节")
        public String url;

        @ApiModelProperty("子菜单")
        public List<WechatMenu> subMenus = Lists.newArrayList();
    }


    @Override
    public void verify() {
        if (StringUtils.isBlank(appId)) {
            throw new AppException(MessageCode.AppIdNotNull);
        }
        if (StringUtils.isBlank(appSecret)) {
            throw new AppException(MessageCode.AppSecretNotNull);
        }
        if (CollectionUtils.isNullOrEmpty(wechatMenuList) || wechatMenuList.size() > 3) {
            throw new AppException(MessageCode.MainManuOversize);
        }
    }
}
