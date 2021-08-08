package com.linkwechat.wecom.domain.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.XmlUtils;
import me.chanjar.weixin.common.util.xml.XStreamCDataConverter;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.util.xml.XStreamTransformer;

import java.io.Serializable;

/**
 * 微信推送过来的消息，也是同步回复给用户的消息，xml格式
 *
 * @author danmo
 * @description
 * @date 2020/11/10 10:29
 **/
@Data
@Slf4j
@XStreamAlias("xml")
public class WxCpXmlMessageVO extends WxCpXmlMessage {
    @XStreamAlias("Alias")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String alias;

    @XStreamAlias("BatchJob")
    private BatchJob batchJob = new BatchJob();

    @XStreamAlias("FailReason")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String failReason;

    @XStreamAlias("TagType")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String tagType;

    @XStreamAlias("Id")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String id;

    @XStreamAlias("UpdateDetail")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String updateDetail;

    @XStreamAlias("JoinScene")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String joinScene;

    @XStreamAlias("QuitScene")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String quitScene;

    @XStreamAlias("MemChangeCnt")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String memberChangeCnt;

    @Data
    public static class BatchJob implements Serializable {
        private static final long serialVersionUID = -3418685294606228837L;
        @XStreamAlias("JobId")
        @XStreamConverter(value = XStreamCDataConverter.class)
        private String jobId;

        @XStreamAlias("JobType")
        @XStreamConverter(value = XStreamCDataConverter.class)
        private String jobType;

        @XStreamAlias("ErrCode")
        private Integer errCode;

        @XStreamAlias("ErrMsg")
        @XStreamConverter(value = XStreamCDataConverter.class)
        private String errMsg;
    }

    public static WxCpXmlMessageVO fromXml(String xml) {
        //修改微信变态的消息内容格式，方便解析
        //xml = xml.replace("</PicList><PicList>", "");
        final WxCpXmlMessageVO xmlPackage = XStreamTransformer.fromXml(WxCpXmlMessageVO.class, xml);
        xmlPackage.setAllFieldsMap(XmlUtils.xml2Map(xml));
        return xmlPackage;
    }
}
