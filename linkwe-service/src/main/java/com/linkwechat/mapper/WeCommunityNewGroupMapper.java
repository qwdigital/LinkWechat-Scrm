package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.vo.WeCommunityNewGroupVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface WeCommunityNewGroupMapper extends BaseMapper<WeCommunityNewGroup> {


//   /**
//    * 根据用户id，获取用户名
//    * @param userIds
//    * @return
//    */
//   //这种方法写到auth里面用fegin调用接口
//   List<Map<String,String>> findUserNameByUserIds(@Param("userIds") List<String> userIds);
//

//   /**
//    * 查询新客自动拉群列表
//    *
//    * @param /emplCodeName 员工名称
//    * @param /createBy     创建人
//    * @param /beginTime    开始时间
//    * @param /endTime      结束时间
//    * @return {WeCommunityNewGroupVo}s 列表
//    */
//   @DataScope(type = "2", value = @DataColumn(name = "create_by_id", userid = "user_id"))
//   List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup);



}
