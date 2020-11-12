<script>
import { add } from '@/api/groupMessage'
import PhoneDialog from '@/components/PhoneDialog'

export default {
  components: { PhoneDialog },
  props: {},
  data() {
    return {
      // 表单参数
      form: {},
      statusOptions: [
        { label: '发送给客户', value: '0' },
        { label: '发送给客户群', value: '1' },
      ],
      activeName: 'second',
    }
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    },
  },
}
</script>

<template>
  <div class="app-container">
    <el-card shadow="never" :body-style="{ padding: '40px 20px 20px' }">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="群发类型" prop="postName">
          <el-radio-group v-model="form.pushType">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.value"
              >{{ dict.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="发送范围" prop="postCode">
          <el-button type="primary" size="mini">选择发送客户</el-button>
        </el-form-item>
        <el-form-item label="发送时间" prop="postSort">
          <el-date-picker
            v-model="form.settingTime"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="datetime"
            placeholder="选择日期时间"
          ></el-date-picker>
          <span>设置时间定时发送，不设置立即发送</span>
        </el-form-item>
        <el-form-item label=" ">
          <div class="flex content-wrap">
            <div class="content-left">
              <el-button class="create" size="mini">新建素材</el-button>
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane name="first">
                  <span slot="label"> <i class="el-icon-date"></i> 文本 </span>
                  <el-input
                    v-model="form.messageJson"
                    type="textarea"
                    :autosize="{ minRows: 2, maxRows: 50 }"
                    placeholder="请输入"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="角色管理" name="third">
                  <span slot="label"> <i class="el-icon-date"></i> 图片 </span>
                  角色管理
                </el-tab-pane>
                <el-tab-pane label="定时任务补偿" name="fourth">
                  <span slot="label"> <i class="el-icon-date"></i> 文件 </span>
                  定时任务补偿
                </el-tab-pane>
                <el-button type="primary" class="mt20">从素材库选择</el-button>
                <!-- <el-tab-pane label="配置管理" name="second">
                <span slot="label"> <i class="el-icon-date"></i> 网页 </span>
                配置管理
              </el-tab-pane> -->

                <!-- <el-tab-pane label="定时任务补偿" name="fourth">
                <span slot="label"> <i class="el-icon-date"></i> 海报 </span>
                定时任务补偿
              </el-tab-pane> -->
              </el-tabs>

              <el-button type="primary">通知成员发送</el-button>
              <span>通知成员，向选中的客户发送以上企业消息</span>
              <i class="el-icon-edit"></i>
            </div>

            <PhoneDialog :message="form.messageJson"></PhoneDialog>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.content-wrap {
  height: 500px;
  align-items: stretch;
  justify-content: space-between;
  padding-right: 6%;
  .content-left {
    position: relative;
    width: 60%;
    .create {
      position: absolute;
      right: 10px;
      top: 16px;
    }
  }

  .el-tabs {
    height: 90%;
    background: #eee;
    border-radius: 8px;
    padding: 10px;
  }
  .select {
  }
}
</style>
