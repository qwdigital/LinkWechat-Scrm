package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.community.WeEmpleCode;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;


public interface IWeEmpleCodeService extends IService<WeEmpleCode> {

    /**
     * 企微接口参数生成方法
     * @param weEmpleCode 员工活码实体类
     * @return 企微接口参数实体类
     */
    WeAddWayVo getWeContactWay(WeEmpleCode weEmpleCode);


    /**
     * 查询员工活码
     *
     * @param id 员工活码ID
     * @return 员工活码
     */
    WeEmpleCode selectWeEmpleCodeById(Long id);



//    /**
//     * 修改员工活码
//     *
//     * @param weEmpleCode 员工活码
//     * @return 结果
//     */
//    void updateWeEmpleCode(WeEmpleCode weEmpleCode);

    /**
     * 更新员工活码
     * @param weEmpleCode
     */
    void updateWeContactWay(WeEmpleCode weEmpleCode);







}
