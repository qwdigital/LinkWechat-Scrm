package com.linkwechat.scheduler.service.impl.crowd;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.linkwechat.common.enums.TrackState;
import com.linkwechat.common.enums.strategiccrowd.CustomerAttributesEnum;
import com.linkwechat.common.enums.strategiccrowd.RelationEnum;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeCustomerTrajectory;
import com.linkwechat.domain.WeStrategicCrowdCustomerRel;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import com.linkwechat.scheduler.service.AbstractCrowdService;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeCustomerTrajectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 客户属性人群计算
 *
 * @author danmo
 */

@Service
@Slf4j
public class CrowdAttributesServiceImpl extends AbstractCrowdService {

    @Autowired
    public IWeCustomerService weCustomerService;

    @Resource
    public IWeCustomerTrajectoryService weCustomerTrajectoryService;

    private final static List<CustomerAttributesEnum> filterFieldNullList;
    private final static List<RelationEnum> filterCptList;
    private final static List<CustomerAttributesEnum> customerTrajectorySpecial;

    static {
        //多个字段
        filterFieldNullList = new ArrayList<>(Arrays.asList(
                CustomerAttributesEnum.ADD_TIME,
                CustomerAttributesEnum.LOST_TIME,
                CustomerAttributesEnum.LAST_ACTIVE_TIME));
        //特殊处理运算
        filterCptList = new ArrayList<>(Arrays.asList(
                RelationEnum.NOT_INCLUDE,
                RelationEnum.NOT_EQUAL
        ));
        //通过轨迹获取user_id
        customerTrajectorySpecial = new ArrayList<>(Arrays.asList(
                CustomerAttributesEnum.LOST_TIME,
                CustomerAttributesEnum.LAST_ACTIVE_TIME
        ));
    }

    @Override
    public List<WeCustomer> calculate(WeStrategicCrowdSwipe crowdSwipe) {
        LambdaQueryWrapper<WeCustomer> wrapper = new LambdaQueryWrapper<>();
        CustomerAttributesEnum customerAttributesEnum = CustomerAttributesEnum.parseEnum(Integer.valueOf(crowdSwipe.getCode()));
        StringBuilder sqlStr = new StringBuilder();

        //前置条件
        if(ObjectUtil.isEmpty(crowdSwipe.getValue()) && ObjectUtil.isNotEmpty(crowdSwipe.getStartTime()) && ObjectUtil.isEmpty(crowdSwipe.getEndTime()) ){
            crowdSwipe.setValue(crowdSwipe.getStartTime());
        }

        switch (customerAttributesEnum){
            case QQ:
                sqlStr.append("qq ");
                break;
            case AGE:
                String value = crowdSwipe.getValue();
                if(StringUtils.isNotEmpty(value)){
                    if(value.contains("-")){
                        String[] values = value.split("-");
                        DateTime endTime = DateUtil.offset(new Date(), DateField.YEAR, -Integer.parseInt(values[0]));
                        DateTime startTime = DateUtil.offset(new Date(), DateField.YEAR, -Integer.parseInt(values[1]));
                        String s1 = DateUtils.parseDateToStr(DateUtils.YYYY, startTime);
                        String s2 = DateUtils.parseDateToStr(DateUtils.YYYY, endTime);
                        crowdSwipe.setStartTime(s1);
                        crowdSwipe.setEndTime(s2);
                    }else {
                        DateTime dateTime = DateUtil.offset(new Date(), DateField.YEAR, -Integer.parseInt(value));
                        String s = DateUtils.parseDateToStr(DateUtils.YYYY, dateTime);
                        crowdSwipe.setValue(s);
                        setCptReverse(crowdSwipe);
                    }
                }
                sqlStr.append("date_format(birthday, '%Y' ) ");
                break;
            case SEX:
                sqlStr.append("gender ");
                break;
            case AREA:
                sqlStr.append("concat_ws(',',province_id,city_id,area_id) ");
                break;
            case NAME:
                sqlStr.append("customer_name ");
                break;
            case EMAIL:
                sqlStr.append("email ");
                break;
            case PHONE:
                sqlStr.append("phone ");
                break;
            case COMPANY:
                sqlStr.append("corp_name ");
                break;
            case ADD_TIME:
                sqlStr.append("date_format(add_time, '%Y-%m-%d' ) ");
                break;
            case BIRTHDAY:
                sqlStr.append("date_format(birthday, '%Y-%m-%d' ) ");
                break;
            case LOST_TIME:
                sqlStr.append("action = '删除员工' and date_format(create_time, '%Y-%m-%d' ) ");
                break;
            case PROFESSION:
                sqlStr.append("position ");
                break;
            case FOLLOW_USER:
                sqlStr.append("add_user_id ");
                break;
            case CUSTOMER_TYPE:
                sqlStr.append("customer_type ");
                break;
            case FOLLOW_DEPART:
                sqlStr.append("add_user_id ");
                break;
            case FOLLOW_STATUS:
                sqlStr.append("track_state ");
                break;
            case LAST_ACTIVE_TIME:
                sqlStr.append("date_format(create_time, '%Y-%m-%d' )  ");
                break;
            default:
                break;
        }

        if(StringUtils.isEmpty(sqlStr.toString())){
            return null;
        }
        cptAndJoinSql(sqlStr,customerAttributesEnum,crowdSwipe);

        wrapper.apply(sqlStr.toString());
        log.info("客户属性 sql执行Where:{}",sqlStr.toString());

        //个性化需求
        if(customerTrajectorySpecial.contains(customerAttributesEnum)){
            List<WeCustomer> result = new LinkedList<>();
            QueryWrapper<WeCustomerTrajectory> wrapperT = new QueryWrapper<>();
            wrapperT.apply(sqlStr.toString());
            List<WeCustomerTrajectory> weCustomerTrajectoryList;
            weCustomerTrajectoryList = weCustomerTrajectoryService.list(wrapperT
                    .eq("operator_type", 1)
            );
            if(customerAttributesEnum.equals(CustomerAttributesEnum.LAST_ACTIVE_TIME)) {
                List<WeCustomerTrajectory> weCustomerTrajectoryListT  = weCustomerTrajectoryService.list(
                        new LambdaQueryWrapper<WeCustomerTrajectory>().eq(WeCustomerTrajectory::getOperatorType, 1));
                Map<String, List<WeCustomerTrajectory>> map = weCustomerTrajectoryListT.parallelStream()
                        .collect(Collectors.groupingBy(WeCustomerTrajectory::getOperatorId));
                List<WeCustomerTrajectory> weCustomerTrajectoryListTone = weCustomerTrajectoryList;
                List<WeCustomerTrajectory> weCustomerTrajectoryListResult = new LinkedList<>();
                map.forEach((key,list)->{
                    Date date = list.stream().map(WeCustomerTrajectory::getCreateTime).max(Date::compareTo).get();
                    weCustomerTrajectoryListTone.forEach(weCustomerTrajectory -> {
                        if(Objects.equals(weCustomerTrajectory.getOperatorId(),key) && Objects.equals(weCustomerTrajectory.getCreateTime(),date)){
                            weCustomerTrajectoryListResult.add(weCustomerTrajectory);
                        }
                    });
                });
                weCustomerTrajectoryList = weCustomerTrajectoryListResult;
            }
            List<String>ids = weCustomerTrajectoryList.stream()
                    .map(WeCustomerTrajectory::getOperatorId).distinct().collect(Collectors.toList());

            if(ObjectUtil.isNotEmpty(ids)){
                List<WeCustomer> weCustomerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                        .in(WeCustomer::getExternalUserid, ids)
                        .eq(WeCustomer::getDelFlag, 0)
                );
                if(customerAttributesEnum.equals(CustomerAttributesEnum.LOST_TIME)){
                    List<Long> weCustomerListT =  new LinkedList<>();
                    for (WeCustomer weCustomer : weCustomerList){
                        for (WeCustomerTrajectory weCustomerTrajectory : weCustomerTrajectoryList){
                            String externalUserid = weCustomer.getExternalUserid();
                            String addUserId = weCustomer.getAddUserId();
                            Integer trackState = weCustomer.getTrackState();
                            if(Objects.equals(externalUserid,weCustomerTrajectory.getOperatorId()) &&
                                    Objects.equals(addUserId,weCustomerTrajectory.getOperatoredObjectId())
                            ){
                                result.add(weCustomer);
                                if(!Objects.equals(TrackState.STATE_YLS.getType(),trackState)){
                                    weCustomerListT.add(weCustomer.getId());
                                }
                            }
                        }
                    }
                    result  = result.stream().filter(bean->!weCustomerListT.contains(bean.getId())).collect(Collectors.toList());
                    log.info("流失员工去重id：{}",result.stream().map(WeCustomer::getId).distinct().collect(Collectors.toList()).toString());
                    return result;
                }
                return weCustomerList;
            }
            return result;
        }
        return weCustomerService.list(wrapper);
    }

    public void cptAndJoinSql(StringBuilder sqlStr,CustomerAttributesEnum customerAttributesEnum,WeStrategicCrowdSwipe crowdSwipe){
        RelationEnum relationEnum = RelationEnum.parseEnum(Integer.valueOf(crowdSwipe.getRelation()));
        if(filterCptList.contains(relationEnum) && !filterFieldNullList.contains(customerAttributesEnum)){
            String strNoNull = sqlStr.toString();
            sqlStr.append(" is not null").append(" and ").append(strNoNull).append(" != '' ").append(" and ").append(strNoNull);
        }
        switch (relationEnum){
            case NULL:
                String str = sqlStr.toString();
                sqlStr.append(" is null ").append(" or ").append(str).append(" = ''");
                break;
            case EQUAL:
                if(sqlStr.toString().contains("add_user_id")){
                    AddUserIdSpecial(sqlStr,crowdSwipe.getValue(),relationEnum);
                }else {
                    sqlStr.append(" = ").append("'").append(crowdSwipe.getValue()).append("'");
                }
                break;
            case INCLUDE:
                sqlStr.append(" like ").append("'%").append(crowdSwipe.getValue()).append("%'");
                break;
            case INTERVAL:
                sqlStr.append(" between '").append(crowdSwipe.getStartTime()).append("' and '").append(crowdSwipe.getEndTime()).append("'");
                break;
            case NOT_NULL:
                String strNoNull = sqlStr.toString();
                sqlStr.append(" is not null ").append(" and ").append(strNoNull).append(" != '' ");
                break;
            case LESS_THAN:
                sqlStr.append(" < ").append("'").append(crowdSwipe.getValue()).append("'");
                break;
            case MORE_THAN:
                sqlStr.append(" > ").append("'").append(crowdSwipe.getValue()).append("'");
                break;
            case NOT_EQUAL:
                if(sqlStr.toString().contains("add_user_id")){
                    AddUserIdSpecial(sqlStr,crowdSwipe.getValue(),relationEnum);
                }else {
                    sqlStr.append(" <> ").append("'").append(crowdSwipe.getValue()).append("'");
                }
                break;
            case LESS_EQUAL:
                sqlStr.append(" <= ").append("'").append(crowdSwipe.getValue()).append("'");
                break;
            case GREATER_EQUAL:
                sqlStr.append(" >= ").append("'").append(crowdSwipe.getValue()).append("'");
                break;
            case NOT_INCLUDE:
                sqlStr.append(" not like ").append("'%").append(crowdSwipe.getValue()).append("%'");
                break;
            default:
                break;
        }
    }




    private static void AddUserIdSpecial(StringBuilder sqlStr,String value,RelationEnum relationEnum){
        switch (relationEnum){
            case EQUAL:
                sqlStr.append(" in ");
                break;
            case NOT_EQUAL:
                sqlStr.append(" not in ");
                break;
        }
        String[] addUserIds = value.split(",");
        for (int i = 0; i < addUserIds.length; i++) {
            if(i==0){
                sqlStr.append("( ");
            }
            sqlStr.append("'").append(addUserIds[i]).append("',");
            if(addUserIds.length - 1 == i){
                sqlStr.delete(sqlStr.length()-1,sqlStr.length());
                sqlStr.append(" )");
            }
        }
    }


    //计算取反
    private void setCptReverse(WeStrategicCrowdSwipe crowdSwipe ){
        RelationEnum relationEnum = RelationEnum.parseEnum(Integer.valueOf(crowdSwipe.getRelation()));
        switch (relationEnum){
            case LESS_THAN:
                crowdSwipe.setRelation(RelationEnum.MORE_THAN.getCode().toString());
                break;
            case MORE_THAN:
                crowdSwipe.setRelation(RelationEnum.LESS_THAN.getCode().toString());
                break;
            case GREATER_EQUAL:
                crowdSwipe.setRelation(RelationEnum.LESS_EQUAL.getCode().toString());
                break;
            case LESS_EQUAL:
                crowdSwipe.setRelation(RelationEnum.GREATER_EQUAL.getCode().toString());
                break;
        }
    }



}
