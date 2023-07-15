package com.linkwechat.service;

import com.linkwechat.domain.WeCustomerLink;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author robin
* @description 针对表【we_customer_link(获客助手)】的数据库操作Service
* @createDate 2023-07-04 17:41:13
*/
public interface IWeCustomerLinkService extends IService<WeCustomerLink> {


    /**
     *  创建获客助手
     * @param customerLink
     * @param createOrUpdate true创建 false更新
     */
    void createOrUpdateCustomerLink(WeCustomerLink customerLink,boolean createOrUpdate);


    /**
     * 根据获客链接id获取详情
     * @param id
     * @return
     */
    WeCustomerLink findWeCustomerLinkById(Long id);







}
