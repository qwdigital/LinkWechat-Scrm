package com.linkwechat.domain.live;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.converter.SexConverter;
import com.linkwechat.common.converter.WhetherConverter;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.converter.LiveStateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 观看成员
 * @TableName we_live_watch_user
 */
@TableName(value ="we_live_watch_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLiveWatchUser extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    @ExcelIgnore
    private Long id;


    /**
     * 观看成员名
     */
    @ExcelProperty("成员名称")
    private String watchUserName;


    /**
     * 外部联系人性别 0-未知 1-男性 2-女性
     */
    @ExcelProperty(value="性别",converter = SexConverter.class)
    private Integer gender;


    /**
     * 光看人员类型@仟域或部门
     */
    @ExcelProperty("所属")
    private String remarks;


    /**
     * 1是,0否
     */
    @ExcelProperty(value = "是否为企业客户",converter = WhetherConverter.class)
    private Integer isCompanyCustomer;


    /**
     * 观看时长
     */
    @ExcelProperty("观看时长")
    private String watchTime;



    /**
     * 是否评论。0-否；1-是
     */
    @ExcelProperty(value = "是否评论",converter = WhetherConverter.class)
    private Integer isComment;

    /**
     * 是否连麦发言。0-否；1-是
     */
    @ExcelProperty(value = "是否连麦发言",converter = WhetherConverter.class)
    private Integer isMic;





    /**
     * 观看成员id
     */
    @ExcelIgnore
    private String watchUserId;


    /**
     * 观看成员头像
     */
    @ExcelIgnore
    private String watchAvatar;



    /**
     * 1:员工;2:客户
     */
    @ExcelIgnore
    private Integer watchUserType;







    /**
     * 观看时长，n小时k分
     */
    @ExcelIgnore
    @TableField(exist = false)
    private String watchTimeDesc;



    /**
     * 直播主表id
     */
    @ExcelIgnore
    private Long liveId;



    /**
     * 删除标识 0 有效 1 删除
     */
    @ExcelIgnore
    private Integer delFlag;

}