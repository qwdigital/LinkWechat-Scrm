package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.query.*;
import com.linkwechat.domain.leads.leads.vo.WeLeadsImportResultVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 线索 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:43
 */
public interface IWeLeadsService extends IService<WeLeads> {

    /**
     * 领取线索
     *
     * @param leadsId 线索Id
     * @author WangYX
     * @date 2023/07/11 16:15
     */
    void receiveLeads(Long leadsId);


    /**
     * 批量分配
     *
     * @param request 分配参数
     * @return {@link String}
     * @author WangYX
     * @date 2023/05/18 15:00
     */
    String allocation(WeLeadsAllocationRequest request);

    /**
     * 线索导出
     *
     * @param request 导出请求参数
     * @author WangYX
     * @date 2023/07/13 11:33
     */
    void export(WeLeadsExportRequest request);

    /**
     * 导入模板
     *
     * @author WangYX
     * @date 2023/07/13 15:31
     */
    void importTemplate();

    /**
     * excel批量导入
     *
     * @param file  excel导入文件
     * @param seaId 公海Id
     * @return {@link WeLeadsImportResultVO}
     * @throws IOException
     * @author WangYX
     * @date 2023/07/14 11:22
     */
    WeLeadsImportResultVO batchImportFromExcel(MultipartFile file, Long seaId) throws IOException;


    /**
     * 移动端-我的跟进
     *
     * @param name   客户名称
     * @param status 线索状态
     * @return {@link List <WeLeads>}
     * @author WangYX
     * @date 2023/07/17 18:20
     */
    List<WeLeads> myFollowList(String name, Integer status);

    /**
     * 移动端-公海线索列表
     *
     * @param name  线索名称
     * @param seaId 公海Id
     * @return {@link List< WeLeads>}
     * @author WangYX
     * @date 2023/07/18 9:59
     */
    List<WeLeadsVO> seaLeadsList(String name, Long seaId);

    /**
     * 员工主动退回线索
     *
     * @param request 退回请求参数
     * @author WangYX
     * @date 2023/07/18 14:07
     */
    void userReturn(WeLeadsUserReturnRequest request);

    /**
     * 更新线索信息
     *
     * @param request 更新请求参数
     * @author WangYX
     * @date 2023/07/18 14:33
     */
    void update(WeLeadsUpdateRequest request);

    /**
     * 绑定客户
     *
     * @param request 绑定请求参数
     * @author WangYX
     * @date 2023/07/18 18:05
     */
    void bindCustomer(WeLeadsBindCustomerRequest request);

    /**
     * 线索手动新增
     *
     * @param request 请求参数
     * @return {@link Long} 线索Id
     * @author WangYX
     * @date 2023/07/25 17:34
     */
    Long manualAdd(WeLeadsAddRequest request);

    /**
     * 线索列表
     *
     * @param request 请求参数
     * @return {@link List<WeLeadsVO>}
     * @author WangYX
     * @date 2023/08/16 17:37
     */
    List<WeLeadsVO> selectList(WeLeadsBaseRequest request);


}
