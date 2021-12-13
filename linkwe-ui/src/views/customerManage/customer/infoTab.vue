<script>
import { getSummary, getFollowUpRecord, getCustomerInfoByUserId } from '@/api/customer'
import record from './record'
export default {
  name: '',
  props: {
    // 当前跟进人id
    userId: {
      type: String,
      default: ''
    }
  },
  components: {
    record
  },
  data() {
    return {
      portrayalSum: { companyTags: [], personTags: [], trackStates: [] }, // 客户画像汇总
      trajectoryType: {
        0: '全部',
        1: '信息动态',
        2: '社交动态',
        3: '跟进动态',
        4: '待办动态'
      },
      recod: [],
      active: '0',
      openTrack: ['0'],
      lastSyncTime: 0
    }
  },
  computed: {
    isSync() {
      return (+new Date() - +new Date(this.lastSyncTime)) / 3600000 < 2
    }
  },
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
      getSummary(this.$route.query.externalUserid).then(({ data }) => {
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
      getCustomerInfoByUserId(this.$route.query.externalUserid, this.userId).then(({ data }) => {
        this.portrayalSum = data
      })
    },
    changeTrack(type) {
      this.openTrack.includes(type) || this.openTrack.push(type)
      this.active = type
    },
    sync() {
      if (this.isSync) {
        this.msgError('由于企业微信开放平台的限制，两小时内不得重复同步操作')
        return
      }
      this.openTrack = ['0']
      this.active = '0'
      this.$refs['record'][0].$forceUpdate()
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
            <div
              v-for="(item, index) of portrayalSum.companyTags"
              :key="index"
              :class="['flex', index && 'mt20']"
            >
              <div class="name oe">{{ item.userName }}</div>
              ：
              <template v-if="item.tagNames">
                <el-tag
                  type="info"
                  v-for="(unit, unique) in item.tagNames.split(',')"
                  :key="unique"
                  >{{ unit }}</el-tag
                >
              </template>
              <div v-else class="sub-text-color">
                暂无标签
              </div>
            </div>
          </el-card>

          <el-card class="mb10" shadow="never">
            <div slot="header" class="card-title">个人标签</div>
            <div
              v-for="(item, index) of portrayalSum.personTags"
              :key="index"
              :class="['flex', index && 'mt20']"
            >
              <div class="name oe">{{ item.userName }}</div>
              ：
              <template v-if="item.tagNames">
                <el-tag
                  type="info"
                  v-for="(unit, unique) in item.tagNames.split(',')"
                  :key="unique"
                  >{{ unit }}</el-tag
                >
              </template>
              <div v-else class="sub-text-color">
                暂无标签
              </div>
            </div>
          </el-card>

          <el-card class="mb10" shadow="never">
            <div slot="header" class="card-title">跟进状态</div>
            <template v-if="portrayalSum.trackStates && portrayalSum.trackStates.length">
              <div class="fxbw mb20" v-for="(item, index) of portrayalSum.trackStates" :key="index">
                <div class="name oe">{{ item.userName }}</div>
                ：
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
            </template>
            <div v-else class="ac sub-text-color">暂无数据</div>
          </el-card>

          <el-card shadow="never">
            <div slot="header" class="card-title">跟进记录</div>
            <el-tabs v-if="portrayalSum.trackStates && portrayalSum.trackStates.length" value="0">
              <el-tab-pane
                v-for="(item, index) in portrayalSum.trackStates"
                :key="index"
                :label="item.userName"
              >
                <record :userId="item.userId" viewType="1"></record>
              </el-tab-pane>
            </el-tabs>
            <div v-else class="ac sub-text-color">暂无数据</div>
          </el-card>
        </div>
      </el-col>

      <el-col :span="10">
        <div class="right">
          <el-card>
            <div slot="header">
              <span class="card-title">客户轨迹</span>
              <el-tooltip
                effect="light"
                :disabled="!isSync"
                class="item"
                content="由于企业微信开放平台的限制，两小时内不得重复同步操作"
                placement="top-start"
              >
                <el-button
                  v-preventReClick
                  style="color: #13a2e8;padding: 0;"
                  class="fr cp"
                  type="text"
                  @click="sync"
                  >同步</el-button
                >
              </el-tooltip>
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
</style>
