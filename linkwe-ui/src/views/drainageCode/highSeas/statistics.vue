<template>
  <div>
    <div style="margin-top: 0; background-color: #ffffff">
      <div class="g-card g-pad20" style="margin-top: 0">
        <el-row :gutter="20">
          <el-col :span="4">
            <div class="total-item">
              <div class="name">导入客户总数</div>
              <div class="value">
                {{ totalData.importCustomerTotalNum || 0 }}
              </div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="total-item">
              <div class="name">已添加客户数</div>
              <div class="value">
                {{ totalData.addCustomerNum || 0 }}
              </div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="total-item">
              <div class="name">添加完成率</div>
              <div class="value">{{ totalData.completionRate }}%</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="total-item">
              <div class="name">待添加客户数</div>
              <div class="value">
                {{ totalData.waitAddCustomerNum || 0 }}
              </div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="total-item">
              <div class="name">待通过客户数</div>
              <div class="value">
                {{ totalData.waitPassCustomerNum || 0 }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="divider-content"></div>
      <div class="g-card g-m20">
        <div class="table-header">导入记录</div>
        <el-table v-loading="importLoading" :data="importList" max-height="600">
          <el-table-column
            label="导入表格名称"
            align="center"
            prop="tableExcelName"
            min-width="180px"
            show-overflow-tooltip
          />
          <el-table-column
            label="导入客户总数"
            align="center"
            prop="cutomerTotalNum"
            show-overflow-tooltip
          />
          <el-table-column
            label="导入时间"
            align="center"
            prop="createTime"
            show-overflow-tooltip
            width="180"
          ></el-table-column>
          <el-table-column
            label="分配员工"
            align="center"
            prop="addUserName"
            show-overflow-tooltip
          />
          <el-table-column
            label="待添加客户数"
            align="center"
            prop="waitAddCustomerNum"
          ></el-table-column>
          <el-table-column
            label="待通过客户数"
            align="center"
            prop="waitPassCustomerNum"
          ></el-table-column>
          <el-table-column
            label="已添加客户数"
            align="center"
            prop="addCustomerNum"
            show-overflow-tooltip
          />
          <el-table-column
            label="添加完成率"
            align="center"
            prop="completionRate"
            show-overflow-tooltip
          >
            <template slot-scope="{ row }">
              <span> {{ row.completionRate }}% </span>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="importTotal > 0"
          :total="importTotal"
          :page.sync="importPage.pageNum"
          :limit.sync="importPage.pageSize"
          @pagination="getImportListFn()"
        />
      </div>
      <div class="divider-content"></div>
      <div class="g-card g-m20">
        <div class="table-header">员工添加统计</div>
        <el-table v-loading="addLoading" :data="addList" max-height="600">
          <el-table-column
            label="员工名称"
            align="center"
            prop="addUserName"
            show-overflow-tooltip
          />
          <el-table-column
            label="分配客户总数"
            align="center"
            prop="cutomerTotalNum"
            show-overflow-tooltip
          />
          <el-table-column
            label="待添加客户数"
            align="center"
            prop="waitAddCustomerNum"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            label="待通过客户数"
            align="center"
            prop="waitPassCustomerNum"
            width="180"
          >
          </el-table-column>
          <el-table-column
            label="已添加客户数"
            align="center"
            prop="addCustomerNum"
            show-overflow-tooltip
          />
          <el-table-column label="添加完成率" align="center" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <span> {{ row.completionRate }}% </span>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="addTotal > 0"
          :total="addTotal"
          :page.sync="addPage.pageNum"
          :limit.sync="addPage.pageSize"
          @pagination="getAddList()"
        />
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import { getTotal, getImportAndStaffList } from '@/api/drainageCode/seas'
export default {
  name: 'CodeStaffStatistics',
  data() {
    return {
      loading: false,
      importList: [],
      importLoading: false,
      importPage: {
        groupByType: 1,
        pageSize: 10,
        pageNum: 1
      },
      importTotal: 0,
      addList: [],
      addLoading: false,
      addPage: {
        groupByType: 2,
        pageSize: 10,
        pageNum: 1
      },
      addTotal: 0,
      totalData: {
        addCustomerNum: 0,
        completionRate: 0,
        importCustomerTotalNum: 0,
        waitAddCustomerNum: 0,
        waitPassCustomerNum: 0
      }
    }
  },
  created() {
    this.getTotalFn()
    this.getImportListFn()
    this.getAddList()
    // this.$store.dispatch(
    // 	'app/setBusininessDesc',
    // 	`
    //      <div>支持单人、批量单人及多人方式新建员工活码，客户可以通过扫描员工活码添加员工为好友，并支持自动给客户打标签和发送欢迎语。</div>
    //    `
    // )
  },
  mounted() {},
  methods: {
    getTotalFn() {
      console.log(222)
      getTotal().then((res) => {
        if (res.code === 200) {
          this.totalData = res.data
        }
      })
    },
    getImportListFn() {
      this.importLoading = true
      getImportAndStaffList(this.importPage)
        .then(({ rows, total }) => {
          this.importList = rows
          this.importList.forEach((ddd) => {
            ddd.createTime = moment(ddd.createTime).format('YYYY-MM-DD HH:mm:SS')
          })
          this.importTotal = +total
          this.importLoading = false
        })
        .catch(() => {
          this.importLoading = false
        })
    },
    getAddList() {
      this.addLoading = true
      getImportAndStaffList(this.addPage)
        .then(({ rows, total }) => {
          this.addList = rows
          this.addTotal = +total
          this.addLoading = false
        })
        .catch(() => {
          this.addLoading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__header {
  margin-bottom: 0;
}
.divider-content {
  width: 100%;
  height: 10px;
  background-color: #f5f7fb;
}

.my-divider {
  display: block;
  height: 1px;
  width: 100%;
  background-color: #dcdfe6;
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sub-des {
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
}

.total-item {
  // width: 200px;
  padding: 20px;
  background: linear-gradient(90deg, #3c89f0 0%, #1364f4 100%);
  border-radius: 4px;

  .name {
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #ffffff;
  }

  .value {
    margin-top: 5px;
    font-size: 18px;
    font-family: JDZhengHT-EN-Regular, JDZhengHT-EN;
    font-weight: 400;
    color: #ffffff;
  }
}

.table-header {
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #333333;
  margin-bottom: 20px;
}
</style>
