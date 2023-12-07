package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeKfWelcome;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 客服欢迎语表(WeKfWelcome)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
@Repository()
@Mapper
public interface WeKfWelcomeMapper extends BaseMapper<WeKfWelcome> {


}

