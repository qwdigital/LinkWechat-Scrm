package com.linkwechat.web.controller.system;

import com.linkwechat.LinkWeAuthApplication;
import com.linkwechat.web.service.ISysAreaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = LinkWeAuthApplication.class)
@AutoConfigureMockMvc
class SysAreaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ISysAreaService sysAreaService;

    @Test
    void list() {
    }

}