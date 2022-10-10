package com.linkwechat.domain;


import lombok.Data;

import java.io.Serializable;

/**
 * 相关参数是否配置
 */
@Data
public class WeConfigParamInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    //是否首次注册:true是;false不是
    private boolean firstRegister=true;

    //公众号参数是否填写: true填写;false未填写;
    private boolean weAppParamFill=false;

    //会话存档相关参数是否填写: true填写;false未填写
    private boolean chatParamFill=false;

    //红包参数是否填写: true填写;false未填写;
    private boolean redEnvelopesParamFile=false;
}
