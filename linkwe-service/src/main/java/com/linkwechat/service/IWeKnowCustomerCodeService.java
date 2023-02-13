package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.know.WeKnowCustomerCode;
import java.io.IOException;
import java.util.List;

/**
* @author robin
* @description 针对表【we_know_customer_code(识客码)】的数据库操作Service
* @createDate 2023-01-09 17:13:50
*/
public interface IWeKnowCustomerCodeService extends IService<WeKnowCustomerCode> {


  /**
   * 新增或编辑识客码
   * @param weKnowCustomerCode
   * @param isUpdate true更新 false新增
   */
  void  addOrUpdateKnowCustomer(WeKnowCustomerCode weKnowCustomerCode,boolean isUpdate) throws IOException;


  /**
   * 获取识客码列表
   * @param knowCustomerName 识客码名称
   * @return
   */
  List<WeKnowCustomerCode> findWeKnowCustomerCodes(String knowCustomerName);


  /**
   * 根据id获取编辑详情
   * @param id
   * @return
   */
  WeKnowCustomerCode findWeKnowCustomerCodeDetailById(Long id);

}
