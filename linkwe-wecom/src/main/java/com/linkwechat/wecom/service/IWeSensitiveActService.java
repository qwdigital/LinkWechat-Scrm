package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeSensitiveAct;

import java.util.List;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/12 17:38
 */
public interface IWeSensitiveActService extends IService<WeSensitiveAct> {
    /**
     * 查询敏感行为
     *
     * @param id 敏感行为ID
     * @return 敏感行为
     */
    public WeSensitiveAct selectWeSensitiveActById(Long id);

    /**
     * 查询敏感行为列表
     *
     * @param weSensitiveAct 敏感行为
     * @return 敏感行为
     */
    public List<WeSensitiveAct> selectWeSensitiveActList(WeSensitiveAct weSensitiveAct);

    /**
     * 新增敏感行为
     *
     * @param weSensitiveAct 敏感行为
     * @return 结果
     */
    public boolean insertWeSensitiveAct(WeSensitiveAct weSensitiveAct);

    /**
     * 修改敏感行为
     *
     * @param weSensitiveAct 敏感行为
     * @return 结果
     */
    public boolean updateWeSensitiveAct(WeSensitiveAct weSensitiveAct);

    /**
     * 批量删除敏感行为
     *
     * @param ids 需要删除的敏感行为ID
     * @return 结果
     */
    public boolean deleteWeSensitiveActByIds(Long[] ids);
}
