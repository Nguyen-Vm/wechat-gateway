package com.nguyen.wechat.dto;

/**
 * @author RWM
 * @date 2018/3/20
 * @description:
 */
public enum MessageCode implements IMessageCode {

    ValidateSignatureError("-40001","签名验证错误"),
    ParseXmlError("-40002","xml解析失败"),
    ComputeSignatureError("-40003","sha加密生成签名失败"),
    IllegalAesKey("-40004", "SymmetricKey非法"),
    ValidateAppidError("-40005", "appid校验失败"),
    EncryptAESError("-40006", "aes加密失败"),
    DecryptAESError("-40007", "aes解密失败"),
    IllegalBuffer("-40008", "解密后得到的buffer非法"),
    ;

    private final String code;
    private final String desc;

    MessageCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String msg() {
        return desc;
    }
}
