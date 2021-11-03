<script>
import { updateBirthday, getDetail } from '@/api/customer'

export default {
  // name: 'CustomerDetail',
  data() {
    return {
      datePickerVisible: false,
      customer: {
        // weFlowerCustomerRels: [{}]
      },
      birthday: '',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      }
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
    getDetail() {
      this.customer = JSON.parse(this.$route.query.data)
      // getDetail(this.$route.query.id).then(({ data }) => {
      //   this.customer = data[0]
      //   this.birthday = this.customer.birthday
      // })
    },
    remark(item) {
      return item.remark || this.customer.customerName + (item.remarkCorpName ? '-' + item.remarkCorpName : '')
    }
  }
}
</script>

<template>
  <div>
    <!-- <el-button slot="append" circle icon="el-icon-back" @click="$router.back()"></el-button>返回 -->
    <div class=" flex aic" @click="goRoute(customer)">
      <el-image style="width: 80px; height: 80px;" :src="customer.url" fit="fit"></el-image>
      <div>
        <div class="ml10">
          {{ customer.customerName }}
          <span :style="{ color: customer.type === 1 ? '#4bde03' : '#f9a90b' }">{{
            { 1: '@微信', 2: '@企业微信' }[customer.type]
          }}</span>
          <i :class="['el-icon-s-custom', { 1: 'man', 2: 'woman' }[customer.gender]]"></i>
        </div>
        <div>
          <el-tag type="info" v-for="(unit, unique) in customer.tagNames" :key="unique">{{ unit }}</el-tag>
        </div>
      </div>
    </div>

    <el-card style="width:100%;">
      <div slot="header">社交关系</div>
      <el-tabs value="1">
        <el-tab-pane :label="`跟进员工(${1})`" name="1">
          <el-table :data="list">
            <el-table-column label="员工" align="center" prop="welcomeMsg" />
            <el-table-column label="添加方式" align="center" prop="createBy" />
            <el-table-column label="添加时间" align="center" prop="createTime" />
            <el-table-column label="跟进状态" align="center" prop="createTime" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane :label="`所在客群(${2})`" name="2">
          <el-table :data="list">
            <el-table-column label="群名" align="center" prop="welcomeMsg" />
            <el-table-column label="群主" align="center" prop="createBy" />
            <el-table-column label="入群时间" align="center" prop="createTime" />
            <el-table-column label="入群方式" align="center" prop="createTime" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-tabs value="1" @tab-click="handleClick">
      <el-tab-pane label="客户画像汇总" name="1">
        <el-row :gutter="10">
          <el-col :span="14">
            <div class="left">
              <el-card style="width:100%;">
                <div slot="header">企业标签</div>
                <div>
                  张三：
                  <el-tag type="info" v-for="(unit, unique) in customer.tagNames" :key="unique">{{ unit }}</el-tag>
                </div>
                <div>
                  张三：
                  <el-tag type="info" v-for="(unit, unique) in customer.tagNames" :key="unique">{{ unit }}</el-tag>
                </div>
              </el-card>

              <el-card style="width:100%;">
                <div slot="header">个人标签</div>
                <div>
                  张三：
                  <el-tag type="info" v-for="(unit, unique) in customer.tagNames" :key="unique">{{ unit }}</el-tag>
                </div>
                <div>
                  张三：
                  <el-tag type="info" v-for="(unit, unique) in customer.tagNames" :key="unique">{{ unit }}</el-tag>
                </div>
              </el-card>

              <el-card style="width:100%;">
                <div slot="header">跟进状态</div>
                <div>
                  张三：
                  <el-steps :active="active" finish-status="success">
                    <el-step title="步骤 1"></el-step>
                    <el-step title="步骤 2"></el-step>
                    <el-step title="步骤 3"></el-step>
                  </el-steps>
                </div>
                <div>
                  张三：
                  <el-steps :active="active" finish-status="success">
                    <el-step title="步骤 1"></el-step>
                    <el-step title="步骤 2"></el-step>
                    <el-step title="步骤 3"></el-step>
                  </el-steps>
                </div>
              </el-card>

              <el-card style="width:100%;">
                <div slot="header">跟进记录</div>
                <el-tabs value="1">
                  <el-tab-pane :label="`张三(${1})`" name="1">
                    <el-table :data="list">
                      <el-table-column label="员工" align="center" prop="welcomeMsg" />
                      <el-table-column label="跟进记录" align="center" prop="createBy" />
                      <el-table-column label="跟进状态" align="center" prop="createTime" />
                      <el-table-column label="跟进记录时间" align="center" prop="createTime" />
                    </el-table>

                    <el-pagination
                      @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                      :current-page="pageNum"
                      :page-sizes="[10, 20, 50]"
                      :page-size="pageSize"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="totalCount"
                    >
                    </el-pagination>
                  </el-tab-pane>
                  <el-tab-pane :label="`张三(${2})`" name="2">
                    <el-table :data="list">
                      <el-table-column label="员工" align="center" prop="welcomeMsg" />
                      <el-table-column label="跟进记录" align="center" prop="createBy" />
                      <el-table-column label="跟进状态" align="center" prop="createTime" />
                      <el-table-column label="跟进记录时间" align="center" prop="createTime" />
                    </el-table>
                    <el-pagination
                      @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                      :current-page="pageNum"
                      :page-sizes="[10, 20, 50]"
                      :page-size="pageSize"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="totalCount"
                    >
                    </el-pagination>
                  </el-tab-pane>
                </el-tabs>
              </el-card>
            </div>
          </el-col>
          <el-col :span="10">
            <div class="right"></div>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <el-card shadow="never" :body-style="{ width: '410px', lineHeight: '30px' }">
      <div class="flex aic mt20">
        <el-avatar style="flex: none;" :size="50" :src="customer.avatar"></el-avatar>
        <div class="info-wrap">
          <div class="mb10">
            {{ customer.name }}
            <!-- <span :style="{ color: customer.type == 1 ? '#4bde03' : '#f9a90b' }">{{
              { 1: '@微信', 2: '@企业微信' }[customer.type]
            }}</span>
            <i :class="['el-icon-s-custom', { 1: 'man', 2: 'woman' }[customer.gender]]"></i> -->
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
    </el-card>
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
  display: inline-block;
  margin: 20px 20px 0 0;
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
</style>
