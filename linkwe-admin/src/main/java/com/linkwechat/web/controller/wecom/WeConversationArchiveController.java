package com.linkwechat.web.controller.wecom;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.service.IWeConversationArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sxw
 * @description 会话存档controller
 * @date 2020/12/19 13:51
 **/
@Slf4j
@RestController
@RequestMapping("/wecom/finance")
public class WeConversationArchiveController {
    @Autowired
    private IWeConversationArchiveService weConversationArchiveService;

    /**
     * 获取内部联系人列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('conversationArchive:InternalContact:list')")
    @Log(title = "获取内部联系人列表", businessType = BusinessType.SELECT)
    @GetMapping("/getInternalContactList")
    public R<List<JSONObject>> getInternalContactList(String userId, int pageSize, int pageNum) {
        return R.ok(weConversationArchiveService.getInternalContactList(userId, pageSize, pageNum));
    }
}
