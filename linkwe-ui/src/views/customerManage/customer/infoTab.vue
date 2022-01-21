<script>
import { getSummary, getFollowUpRecord, getCustomerInfoByUserId } from '@/api/customer'
import { dictTrackState } from '@/utils/dictionary'
import record from './record'
export default {
  name: '',
  props: {
    // 当前跟进人id
    userId: {
      type: String,
      default: ''
    }
    // trackUsers: {
    //   type: Array,
    //   default: ''
    // }
  },
  components: {
    record
  },
  data() {
    return {
      portrayalSum: { companyTags: [], personTags: [], trackStates: [], trackUsers: [] }, // 客户画像汇总
      trajectoryType: {
        0: '全部',
        1: '客户动态',
        2: '员工动态',
        3: '互动动态',
        4: '跟进动态'
      },
      recod: [],
      active: '0',
      openedTabs: ['0'],
      openTrack: ['0'],
      lastSyncTime: null,
      dictTrackState
    }
  },
  computed: {},
  watch: {},
  created() {
    this.userId ? this.getCustomerInfoByUserId() : this.getSummary()
  },
  mounted() {},
  methods: {
    /**
     *客户画像汇总
     * @param {*}
     * externalUserid	是	当前客户id
     */
    getSummary() {
      getSummary(this.$route.query).then(({ data }) => {
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
      })
    },
    /**
     *客户画像单个跟进人
     */
    getCustomerInfoByUserId() {
      getCustomerInfoByUserId(this.$route.query).then(({ data }) => {
        this.portrayalSum = data
      })
    },
    changeTab(tab) {
      this.openedTabs.includes(tab.index) || this.openedTabs.push(tab.index)
    },
    changeTrack(type) {
      this.openTrack.includes(type) || this.openTrack.push(type)
      this.active = type
    },
    sync() {
      this.$refs['record'][this.active].sync()
    }
  }
}
</script>

<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="14">
        <div class="left">
          <el-card class="mb10" shadow="never">
            <div slot="header" class="card-title">企业标签</div>
            <template v-if="portrayalSum.companyTags && portrayalSum.companyTags.length">
              <div
                v-for="(item, index) of portrayalSum.companyTags"
                :key="index"
                :class="['flex', index && 'mt20']"
              >
                <!-- 汇总的场景显示名字 -->
                <template v-if="!userId">
                  <div class="name oe">{{ item.userName }}</div>
                  ：
                </template>
                <template v-if="item.tagNames">
                  <el-tag v-for="(unit, unique) in item.tagNames.split(',')" :key="unique">{{
                    unit
                  }}</el-tag>
                </template>
                <div v-else class="sub-text-color ac">
                  暂无标签
                </div>
              </div>
            </template>
            <div v-else class="sub-text-color ac">
              暂无标签
            </div>
          </el-card>

          <el-card class="mb10" shadow="never">
            <div slot="header" class="card-title">个人标签</div>
            <template v-if="portrayalSum.personTags && portrayalSum.personTags.length">
              <div
                v-for="(item, index) of portrayalSum.personTags"
                :key="index"
                :class="['flex', index && 'mt20']"
              >
                <!-- 汇总的场景显示名字 -->
                <template v-if="!userId">
                  <div class="name oe">{{ item.userName }}</div>
                  ：
                </template>
                <template v-if="item.tagNames">
                  <el-tag v-for="(unit, unique) in item.tagNames.split(',')" :key="unique">{{
                    unit
                  }}</el-tag>
                </template>
                <div v-else class="sub-text-color ac">
                  暂无标签
                </div>
              </div>
            </template>
            <div v-else class="sub-text-color ac">
              暂无标签
            </div>
          </el-card>

          <template v-if="userId">
            <el-card class="mb10" shadow="never">
              <div slot="header" class="card-title">共同群聊</div>
              <template v-if="portrayalSum.commonGroupChat">
                <el-tag
                  type="info"
                  v-for="(unit, unique) in portrayalSum.commonGroupChat.split(',')"
                  :key="unique"
                  >{{ unit }}</el-tag
                >
              </template>
              <div v-else class="sub-text-color ac">
                暂无标签
              </div>
            </el-card>

            <el-card class="mb10" shadow="never">
              <div slot="header" class="card-title">详细资料</div>
              <el-row :gutter="20" type="type" class="pad10" justify="space-between">
                <el-col :span="12">
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 手机号 </el-col>
                    <el-col :span="18">{{ portrayalSum.phone || '无' }}</el-col>
                  </el-row>
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 年龄 </el-col>
                    <el-col :span="18">{{ portrayalSum.age || '无' }}</el-col>
                  </el-row>
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 生日 </el-col>
                    <el-col :span="18">{{ portrayalSum.birthday || '无' }}</el-col>
                  </el-row>
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 邮箱 </el-col>
                    <el-col :span="18">{{ portrayalSum.email || '无' }}</el-col>
                  </el-row>
                </el-col>
                <el-col :span="12">
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 地址 </el-col>
                    <el-col :span="18">{{ portrayalSum.address || '无' }}</el-col>
                  </el-row>
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> QQ </el-col>
                    <el-col :span="18">{{ portrayalSum.qq || '无' }}</el-col>
                  </el-row>
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 职业 </el-col>
                    <el-col :span="18">{{ portrayalSum.position || '无' }}</el-col>
                  </el-row>
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 公司 </el-col>
                    <el-col :span="18">{{ portrayalSum.corpName || '无' }}</el-col>
                  </el-row>
                </el-col>
                <el-col>
                  <el-row class="baseinfo-row">
                    <el-col :span="6"> 其他描述</el-col>
                    <el-col :span="18">
                      {{ portrayalSum.otherDescr || '无' }}
                    </el-col>
                  </el-row>
                </el-col>
              </el-row>
            </el-card>
          </template>

          <el-card class="mb10" shadow="never">
            <div slot="header" class="card-title">跟进状态</div>
            <template v-if="portrayalSum.trackStates && portrayalSum.trackStates.length">
              <div
                v-for="(item, index) of portrayalSum.trackStates"
                :key="index"
                :class="['flex', index && 'mt20']"
              >
                <!-- 汇总的场景显示名字 -->
                <template v-if="!userId">
                  <div class="name oe">{{ item.userName }}</div>
                  ：
                </template>
                <template v-if="item.trackStateList.length">
                  <el-steps style="flex:auto;" :active="item.trackStateList.length">
                    <el-step
                      v-for="(unit, unique) of item.trackStateList"
                      :key="unique"
                      :title="
                        dictTrackState[+unit.trackState + ''] &&
                          dictTrackState[+unit.trackState + ''].name
                      "
                      :status="
                        dictTrackState[+unit.trackState + ''] &&
                          dictTrackState[+unit.trackState + ''].color
                      "
                      :description="unit.trackTime"
                    ></el-step>
                  </el-steps>
                </template>
                <div v-else class="sub-text-color ac">
                  暂无数据
                </div>
              </div>
            </template>
            <div v-else class="sub-text-color ac">暂无数据</div>
          </el-card>

          <el-card shadow="never">
            <div slot="header" class="card-title">跟进记录</div>
            <!-- 单个人的场景 -->
            <record v-if="userId" :userId="userId" viewType="1"></record>
            <!-- 汇总的场景 -->
            <el-tabs
              v-else-if="portrayalSum.trackUsers && portrayalSum.trackUsers.length"
              value="0"
              @tab-click="changeTab"
            >
              <el-tab-pane
                v-for="(item, index) in portrayalSum.trackUsers"
                :key="index"
                :label="item.userName"
              >
                <record
                  v-if="openedTabs.includes(index + '')"
                  :userId="item.trackUserId"
                  trajectoryType="4"
                  viewType="1"
                ></record>
              </el-tab-pane>
            </el-tabs>
            <div v-else class="sub-text-color ac">暂无数据</div>
          </el-card>
        </div>
      </el-col>

      <el-col :span="10">
        <div class="right">
          <el-card>
            <div slot="header">
              <span class="card-title" @click="sync">客户轨迹</span>

              <ButtonSync class="btn-sync" :lastSyncTime="lastSyncTime" @click="sync"
                >同步</ButtonSync
              >
            </div>
            <div class="flex track-tab-wrap mb15">
              <div
                v-for="(value, type) of trajectoryType"
                :key="type"
                :class="['track-tab', type === active && 'active']"
                @click="changeTrack(type)"
              >
                {{ value }}
              </div>
            </div>
            <template v-for="(item, index) in openTrack">
              <record
                ref="record"
                v-show="item === active"
                :key="index"
                :userId="userId"
                :userIdAll="portrayalSum.trackUsers.map((e) => e.trackUserId).join()"
                :lastSyncTime.sync="lastSyncTime"
                :trajectoryType="item == 0 ? null : item"
              ></record>
            </template>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
.name {
  width: 65px;
  flex: none;
  text-align: right;
}
.el-card {
  .el-tag {
    margin-bottom: 5px;
  }
}
.track-tab-wrap {
  .track-tab {
    background: #ddd;
    border-radius: 50px;
    padding: 5px 10px;
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
.btn-sync {
  position: relative;
  float: right;
  top: -11px;
}
.sub-text-color {
  flex: auto;
}

.baseinfo-row {
  position: relative;
  padding-left: 10px;
  line-height: 50px;
  .el-col {
    box-shadow: 0px 1px 0px 0px #f1f1f1;
    &:first-child {
      color: #999;
    }
  }
  &::before {
    content: '';
    position: absolute;
    top: 22.5px;
    left: 0;
    width: 5px;
    height: 5px;
    background: #76abf4;
    border-radius: 50%;
  }
  > .el-col:last-child {
    text-align: right;
  }
}
</style>
