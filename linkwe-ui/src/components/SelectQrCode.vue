<script>
import { getList } from '@/api/drainageCode/group'

export default {
  components: {},
  props: {
    // 添加标签显隐
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '选择群活码'
    },
    selected: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      loading: true, // 遮罩层
      query: {
        pageNum: 1,
        pageSize: 10,
        activityName: '',
        createBy: '',
        beginTime: '',
        endTime: ''
      },
      list: [], // 列表
      total: 0, // 总条数
      radio: ''
    }
  },
  watch: {
    selected(val) {
      this.setSelected()
    },
    list(val) {
      this.setSelected()
    }
  },
  computed: {
    Pvisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  created() {
    this.getList()
  },
  mounted() {},
  methods: {
    // 获取列表
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    submit() {
      this.Pvisible = false
      this.$emit('success', this.radio)
    },
    setSelected() {
      if (!this.selected.length) return

      this.list.forEach((code) => {
        if (code.id == this.selected[0].id) {
          this.radio = code
        }
      })
    }
  }
}
</script>

<template>
  <el-dialog
    :title="title"
    :visible.sync="Pvisible"
    width="650px"
    append-to-body
    :close-on-click-modal="false"
  >
    <div>
      <el-form ref="form" :model="query" label-width="">
        <el-form-item label="">
          <el-input
            v-model="query.activityName"
            class="ml10 mr10"
            style="width: 150px;"
            placeholder="请输入名称"
            @keydown.enter="getList(1)"
          ></el-input>
          <el-button
            v-hasPermi="['contacts:organization:query']"
            icon="el-icon-search"
            circle
            @click="getList(1)"
          ></el-button>

          <el-pagination
            class="fr"
            @current-change="getList"
            :current-page="query.pageNum"
            :page-size="query.pageSize"
            layout="prev, pager, next"
            :total="total"
          ></el-pagination>
        </el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading">
        <el-table-column width="30">
          <template slot-scope="scope">
            <el-radio v-model="radio" :label="scope.row">'</el-radio>
          </template>
        </el-table-column>

        <el-table-column prop="activityName" label="活码名称" align="center"></el-table-column>

        <el-table-column prop="activityDesc" label="活码描述" align="center" width="160">
          <template #default="{ row }">
            <el-popover placement="bottom" width="200" trigger="hover" :content="row.activityDesc">
              <div slot="reference" class="table-desc overflow-ellipsis">
                {{ row.activityDesc }}
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column prop="codeUrl" label="活码样式" align="center" width="130">
          <template #default="{ row }">
            <el-popover placement="bottom" trigger="hover">
              <el-image slot="reference" :src="row.codeUrl" class="code-image--small"></el-image>
              <el-image :src="row.codeUrl" class="code-image"> </el-image>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column label="实际群码总数" align="center">
          <template #default="{ row }">
            {{ (row.actualList && row.actualList.length) || 0 }}
          </template>
        </el-table-column>

        <el-table-column prop="availableCodes" label="可用实际群码数" align="center">
          <template #default="{ row }">
            <el-popover
              v-if="row.aboutToExpireCodes > 0"
              placement="bottom"
              width="200"
              trigger="hover"
              :content="'有' + row.aboutToExpireCodes + '个实际群码即将过期。'"
            >
              <i slot="reference" class="el-icon-warning expire-icon"></i>
            </el-popover>

            {{ row.availableCodes }}
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div slot="footer">
      <el-button @click="Pvisible = false">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
.code-image {
  width: 200px;
  height: 200px;
}

.code-image--small {
  width: 50px;
  height: 50px;
}

.expire-icon {
  color: red;
}
</style>
