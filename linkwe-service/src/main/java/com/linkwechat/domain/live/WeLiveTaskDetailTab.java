package com.linkwechat.domain.live;

import lombok.Data;

@Data
public class WeLiveTaskDetailTab {
    //应执行员工数|预计触达客户(客群)数
    private int estimateSendNumber;
    //实际发送人数|已送达客户(客群)数
    private int actualSendNumber;
    //未发送人数|未触达客户(客群)数
    private int noSendNumber;

    

}
