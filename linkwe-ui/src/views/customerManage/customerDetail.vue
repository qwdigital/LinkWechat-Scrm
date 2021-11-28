<script>
import { updateBirthday, getDetail, getSummary } from '@/api/customer'
import record from './customer/record'
import ctrack from './customer/track'
import { dictAddType, dictJoinGroupType, dictTrackState } from '@/utils/dictionary'
export default {
  name: 'CustomerDetail',
  components: { record, ctrack },
  data() {
    return {
      datePickerVisible: false,
      customer: {
        // weFlowerCustomerRels: [{}]
      },
      portrayalSum: { companyTags: [], personTags: [], trackStates: [] }, // 客户画像汇总
      birthday: '',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      },
      dictAddType,
      dictJoinGroupType,
      dictTrackState
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    updateBirthday() {
      if (!this.birthday || this.birthday == this.customer.birthday) {
        return
      }
      let data = {
        externalUserid: this.customer.externalUserid,
        birthday: this.birthday,
        firstUserId: this.customer.firstUserId
      }
      updateBirthday(data).then((response) => {
        this.msgSuccess('操作成功')
        // this.getDetail()
        this.$set(this.customer, 'birthday', this.birthday)
        // this.datePickerVisible = false
      })
    },
    /**
     *客户详情基础(基础信息+社交关系)
     * @param {*}
     * externalUserid	是	当前客户id
     * userId	是	当前跟进人id
     */
    getDetail() {
      getDetail(this.$route.query).then(({ data }) => {
        data.companyTags && (data.companyTags = data.companyTags.split(','))
        this.customer = data
        this.birthday = data.birthday
      })
    },
    /**
     *客户画像汇总
     * @param {*}
     * externalUserid	是	当前客户id
     */
    getSummary() {
      getSummary(this.$route.query.externalUserid).then(({ data }) => {
        data.companyTags && (data.companyTags = data.companyTags.split(','))
        //          {
        //   'companyTags':[{ //企业标签
        //      'userName':'',//添加人
        //      'tagsNames':'',//标签名多个标签使用逗号隔开
        //      'tagIds':''//多个标签id使用逗号隔开
        // }],
        // 'personTags':[{ //个人标签
        //      'userName':'',//添加人
        //      'tagsNames':'',//标签名多个标签使用逗号隔开
        //      'tagIds':''//多个标签id使用逗号隔开
        // }],
        // 'trackStates':[{
        //       'userName':'',//跟进人
        //       'trackStateList':[{ //跟进状态列表
        //       'trackState':'',//跟进状态
        //       'trackTime':''//跟进时间
        // }]
        // }]
        // }
        this.portrayalSum = data
        this.birthday = data.birthday
      })
    }

    // remark(item) {
    //   return (
    //     item.remark ||
    //     this.customer.customerName + (item.remarkCorpName ? '-' + item.remarkCorpName : '')
    //   )
    // }
  }
}
</script>

<template>
  <div>
    <!-- <el-button slot="append" circle icon="el-icon-back" @click="$router.back()"></el-button>返回 -->
    <div class=" flex aic mb20" @click="goRoute(customer)">
      <el-image style="width: 80px; height: 80px;" :src="customer.avatar" fit="fit"></el-image>
      <div class="ml10">
        <div>
          {{ customer.customerName + (customer.corpName ? '-' + customer.corpName : '') }}
          <span :style="{ color: customer.customerType === 1 ? '#4bde03' : '#f9a90b' }">{{
            { 1: '@微信', 2: '@企业微信' }[customer.customerType]
          }}</span>
          <i :class="['el-icon-s-custom', { 1: 'man', 2: 'woman' }[customer.gender]]"></i>
        </div>
        <div class="mt10">
          <template v-if="customer.companyTags">
            <el-tag type="info" v-for="(unit, unique) in customer.companyTags" :key="unique">{{
              unit
            }}</el-tag>
          </template>
          <div>
            暂无标签
          </div>
        </div>
      </div>
    </div>

    <el-card shadow="never">
      <div slot="header" class="card-title">社交关系</div>
      <el-tabs value="1">
        <el-tab-pane
          :label="`跟进员工(${customer.trackUsers ? customer.trackUsers.length : 0})`"
          name="1"
        >
          <el-table :data="customer.trackUsers">
            <el-table-column label="员工" align="center" prop="userName" />
            <el-table-column prop="addMethod" label="添加方式" align="center">
              <template slot-scope="{ row }">{{ dictAddType[row.addMethod + ''] }}</template>
            </el-table-column>
            <el-table-column label="添加时间" align="center" prop="firstAddTime" />
            <el-table-column prop="trackState" label="跟进状态" align="center">
              <template slot-scope="{ row }">
                <el-tag v-if="row.trackState" :type="dictTrackState[~~row.trackState + ''].color">{{
                  dictTrackState[~~row.trackState + ''].name
                }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane :label="`所在客群(${customer.groups ? customer.groups.length : 0})`" name="2">
          <el-table :data="customer.groups">
            <el-table-column label="群名" align="center" prop="groupName" />
            <el-table-column label="群主" align="center" prop="leaderName" />
            <el-table-column label="入群时间" align="center" prop="joinTime" />
            <el-table-column prop="joinScene" label="入群方式" align="center">
              <template slot-scope="{ row }">{{ dictJoinGroupType[row.joinScene + ''] }}</template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-tabs value="1" @tab-click="handleClick">
      <el-tab-pane label="客户画像汇总" name="1">
        <el-row :gutter="10">
          <el-col :span="14">
            <div class="left">
              <el-card class="mb10" shadow="never">
                <div slot="header" class="card-title">企业标签</div>
                <div
                  class="flex mb20"
                  v-for="(item, index) of portrayalSum.companyTags"
                  :key="index"
                >
                  <div style="width:60px;flex: none;">{{ item.userName }}：</div>
                  <template v-if="item.tagNames">
                    <el-tag
                      type="info"
                      v-for="(unit, unique) in item.tagNames.split(',')"
                      :key="unique"
                      >{{ unit }}</el-tag
                    >
                  </template>
                  <div>
                    暂无标签
                  </div>
                </div>
              </el-card>

              <el-card class="mb10" shadow="never">
                <div slot="header" class="card-title">个人标签</div>
                <div
                  class="flex mb20"
                  v-for="(item, index) of portrayalSum.personTags"
                  :key="index"
                >
                  <div style="width:60px;flex: none;">{{ item.userName }}：</div>
                  <template v-if="item.tagNames">
                    <el-tag
                      type="info"
                      v-for="(unit, unique) in item.tagNames.split(',')"
                      :key="unique"
                      >{{ unit }}</el-tag
                    >
                  </template>
                  <div>
                    暂无标签
                  </div>
                </div>
              </el-card>

              <el-card class="mb10" shadow="never">
                <div slot="header" class="card-title">跟进状态</div>
                <div
                  class="fxbw mb20"
                  v-for="(item, index) of portrayalSum.trackStates"
                  :key="index"
                >
                  <div style="width:60px;flex: none;">{{ item.userName }}：</div>
                  <el-steps
                    style="flex:auto;"
                    :active="item.trackStateList.length"
                    finish-status="success"
                  >
                    <el-step
                      v-for="(unit, unique) of item.trackStateList"
                      :key="unique"
                      :title="dictTrackState[~~unit.trackState + ''].name"
                      :description="unit.trackTime"
                    ></el-step>
                  </el-steps>
                </div>
              </el-card>

              <el-card shadow="never">
                <div slot="header" class="card-title">跟进记录</div>
                <el-tabs value="0">
                  <el-tab-pane
                    v-for="(item, index) in customer.trackUsers"
                    :key="index"
                    :label="item.userName"
                    :name="index + ''"
                  >
                    <record
                      :userId="item.userId"
                      :externalUserid="$route.query.externalUserid"
                    ></record>
                  </el-tab-pane>
                </el-tabs>
              </el-card>
            </div>
          </el-col>
          <el-col :span="10">
            <div class="right">
              <el-card>
                <div slot="header">
                  <span class="card-title">客户轨迹</span>
                  <span style=" color: #13a2e8;" class="fr">同步</span>
                </div>
                <div class="flex track-tab-wrap">
                  <div class="track-tab active">
                    全部
                  </div>
                  <div class="track-tab">
                    全部
                  </div>
                  <div class="track-tab">
                    全部
                  </div>
                  <div class="track-tab">
                    全部
                  </div>
                </div>
                <ctrack></ctrack>
              </el-card>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <!-- <el-card shadow="never" :body-style="{ width: '410px', lineHeight: '30px' }">
      <div class="flex aic mt20">
        <el-avatar style="flex: none;" :size="50" :src="customer.avatar"></el-avatar>
        <div class="info-wrap">
          <div class="mb10">
            {{ customer.name }}
            <span :style="{ color: customer.type == 1 ? '#4bde03' : '#f9a90b' }">{{
              { 1: '@微信', 2: '@企业微信' }[customer.type]
            }}</span>
            <i :class="['el-icon-s-custom', { 1: 'man', 2: 'woman' }[customer.gender]]"></i>
          </div>
          <div class="info">
            出生日期：{{ customer.birthday }}
            <div class="bfc-d">
              <el-date-picker
                v-if="datePickerVisible"
                v-model="birthday"
                type="date"
                :picker-options="pickerOptions"
                value-format="yyyy-MM-dd"
                placeholder="选择日期便于以后客情维护"
                @blur="datePickerVisible = false"
                @change="updateBirthday"
              ></el-date-picker>
              <i
                v-else
                v-hasPermi="['customerManage:customer:edit']"
                class="el-icon-edit"
                @click="datePickerVisible = true"
              ></i>
            </div>
          </div>
        </div>
      </div>
      <el-divider></el-divider>

      <el-button v-if="customer.status == 1" class="fr" type="danger" plain size="mini">该员工已被客户删除</el-button>
      <el-row :gutter="10">
        <el-col :span="10">备注名：</el-col>
        <el-col :span="10">
          <el-tooltip class="item" effect="dark" :content="remark(customer)" placement="top-start">
            <div class="toe al">
              {{ remark(customer) }}
            </div>
          </el-tooltip>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">标签：</el-col>
        <el-col :span="14">
          <el-tag type="info" v-for="(unit, unique) in customer.tagNames" :key="unique">{{ unit }}</el-tag>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">个人标签：</el-col>
        <el-col :span="12">{{ '--' }}</el-col>
      </el-row>
      <el-divider></el-divider>

      <el-row :gutter="10">
        <el-col :span="10">添加人：</el-col>
        <el-col :span="12">{{ customer.userName }}</el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">所在部门：</el-col>
        <el-col :span="12">{{ customer.department }}</el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">添加时间：</el-col>
        <el-col :span="12">{{ customer.firstAddTime }}</el-col>
      </el-row>
    </el-card> -->
  </div>
</template>

<style lang="scss" scoped>
.info-wrap {
  margin-left: 20px;
  .info {
    color: #aaa;
    line-height: 32px;
  }
}

.el-card {
  .el-row {
    color: #666;
    margin-bottom: 10px;
  }
  .el-col-10 {
    width: 100px;
    text-align: right;
  }
  .el-tag {
    margin-bottom: 5px;
  }
  /deep/.el-card__header {
    border: 0;
  }
}
.el-icon-s-custom {
  font-size: 16px;
  margin-left: 4px;
  color: #999;
  &.man {
    color: #13a2e8;
  }
  &.woman {
    color: #f753b2;
  }
}

.card-title {
  font-weight: 600;
}

.track-tab-wrap {
  .track-tab {
    background: #ddd;
    border-radius: 50px;
    padding: 5px 20px;
    cursor: pointer;
    & + .track-tab {
      margin-left: 10px;
    }
    &.active {
      background: $blue;
      color: #fff;
    }
  }
}
</style>
