<script>
import { updateBirthday, getDetail } from '@/api/customer'

export default {
  // name: 'CustomerDetail',
  data() {
    return {
      datePickerVisible: false,
      customer: {
        weFlowerCustomerRels: [{}],
      },
      birthday: '',
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
      }
      updateBirthday(data).then((response) => {
        this.msgSuccess('操作成功')
        this.getDetail()
        this.datePickerVisible = false
      })
    },
    getDetail() {
      getDetail(this.$route.query.id).then(({ data }) => {
        this.customer = data[0]
        this.birthday = this.customer.birthday
      })
    },
  },
}
</script>

<template>
  <div>
    <!-- <el-button slot="append" circle icon="el-icon-back" @click="$router.back()"></el-button>返回 -->
    <div class="flex aic">
      <el-avatar :size="100" :src="customer.avatar"></el-avatar>
      <div class="info-wrap">
        <div class="mb10">
          {{ customer.name }}
          <span
            :style="{ color: customer.type == 1 ? '#4bde03' : '#f9a90b' }"
            >{{ { 1: '@微信', 2: '@企业微信' }[customer.type] }}</span
          >
          <i
            :class="[
              'el-icon-s-custom',
              { 1: 'man', 2: 'woman' }[customer.gender],
            ]"
          ></i>
        </div>
        <div class="info">
          出生日期：{{ customer.birthday || '--' }}
          <div class="bfc-d ml20">
            <el-date-picker
              v-if="datePickerVisible"
              v-model="birthday"
              type="date"
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

    <el-card shadow="never" :body-style="{ width: '400px' }">
      <div>
        <el-button
          v-if="customer.weFlowerCustomerRels[0].status == 1"
          class="fr"
          type="danger"
          plain
          size="mini"
          >该员工已被客户删除</el-button
        >
        <el-row :gutter="10">
          <el-col :span="10">备注名：</el-col>
          <el-col :span="10">
            <el-tooltip
              class="item"
              effect="dark"
              :content="customer.name"
              placement="top-start"
            >
              <div class="toe al">{{ customer.name }}</div>
            </el-tooltip>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">标签：</el-col>
          <el-col :span="12">
            <div
              v-for="(item, index) in customer.weFlowerCustomerRels"
              :key="index"
            >
              <el-tag
                type="info"
                v-for="(unit, unique) in item.weFlowerCustomerTagRels"
                :key="unique"
                >{{ unit.tagName }}</el-tag
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">个人标签：</el-col>
          <el-col :span="12">{{ '--' }}</el-col>
        </el-row>
        <el-divider></el-divider>
        <el-row :gutter="10">
          <el-col :span="10">添加人：</el-col>
          <el-col :span="12">{{
            customer.weFlowerCustomerRels[0].userName
          }}</el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">所在部门：</el-col>
          <el-col :span="12">{{
            customer.weFlowerCustomerRels[0].department
          }}</el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">添加时间：</el-col>
          <el-col :span="12">{{
            customer.weFlowerCustomerRels[0].createTime
          }}</el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.mid-action {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  align-items: center;
  .total {
    background-color: rgba(65, 133, 244, 0.1);
    border: 1px solid rgba(65, 133, 244, 0.2);
    border-radius: 3px;
    font-size: 14px;
    min-height: 32px;
    line-height: 32px;
    padding: 0 12px;
    color: #606266;
  }
  .num {
    color: #00f;
  }
}

.info-wrap {
  margin-left: 20px;
  .info {
    color: #aaa;
    line-height: 32px;
  }
}

.el-card {
  display: inline-block;
  margin-top: 20px;
  .el-row {
    color: #666;
    margin-bottom: 10px;
  }
  .el-col-10 {
    width: 100px;
    text-align: right;
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
