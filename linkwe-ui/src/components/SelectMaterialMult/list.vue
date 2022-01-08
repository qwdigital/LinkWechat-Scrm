<script>
import { getTree, getList } from '@/api/material'
import { getMaterialList } from '@/api/appTool/chatBar'
import Video from 'video.js'
export default {
  components: {},
  props: {
    // 0: '图片', 1: '语音', 2: '视频', 3: '普通文件', 4: '文本', 5: '海报',
    type: {
      type: String,
      default: '4'
    },
    multiple: {
      type: Boolean,
      default: false
    },
    selected: {
      type: Array,
      default: () => []
    },
    // 是否使用素材抓取回显选中
    isCheck: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: true, // 遮罩层
      // 查询条件
      query: {
        pageNum: 1,
        pageSize: 10,
        categoryId: '',
        search: '',
        mediaType: '4'
      },
      list: [], // 列表
      total: 0, // 总条数
      treeData: [], // 树
      // 树props
      treeProps: {
        children: 'children',
        label: 'name'
      },
      // 分组props
      groupProps: {
        // expandTrigger: 'hover',
        checkStrictly: true,
        children: 'children',
        label: 'name',
        value: 'id',
        emitPath: false
      },
      radio: '',

      // 树props
      treeProps: {
        children: 'children',
        label: 'name'
      },
      selectedx: []
    }
  },
  watch: {
    radio(val) {
      this.$emit('change', val)
    }
  },
  computed: {},
  created() {
    this.query.mediaType = this.type
    this.getTree()
    this.getList()
  },
  mounted() {},
  methods: {
    // 获取类目树
    getTree() {
      getTree(this.type).then(({ data }) => {
        this.treeData = data
      })
    },
    // 获取素材列表
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      this.isCheck && (this.query.pageSize = 9999)
      ;(this.isCheck ? getMaterialList : getList)(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false

          if (this.isCheck) {
            let selected = this.list.filter((d) => d.isCheck)
            this.$emit('update:selected', selected)

            // 选中回显
            if ([1, 3, 4].includes(+this.type)) {
              this.$nextTick(() => {
                selected.forEach((row) => {
                  this.$refs.table.toggleRowSelection(row, true)
                })
              })
            } else {
              this.selectedx = selected
            }
          }

          this.$emit('listChange', this.list)
        })
        .catch(() => {
          this.loading = false
        })
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.query.categoryId = data.id
      this.getList(1)
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.$emit('update:selected', selection)
    }
  }
}
</script>

<template>
  <div>
    <!-- <template v-if="false">
      <el-form ref="form" :model="query" label-width="80px">
        <el-form-item label="选择分组">
          <el-cascader v-model="query.categoryId" :options="treeData" :props="groupProps"></el-cascader>
          <el-input
            v-model="query.search"
            class="ml10 mr10"
            style="width: 150px"
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

      文本
      <el-table v-if="type == 4" v-loading="loading" :data="list" :show-header="false">
        <el-table-column width="30">
          <template slot-scope="scope">
            <el-radio v-model="radio" :label="scope.row">'</el-radio>
          </template>
        </el-table-column>
        <el-table-column prop="content"> </el-table-column>
      </el-table>

      图片
      <el-radio-group v-if="type == 0" v-loading="loading" class="img-wrap" v-model="radio">
        <el-radio v-for="(item, index) in list" :label="item" :key="index">
          <img class="img-li" :src="item.materialUrl" alt />
          <div class="ac mt5">{{ item.materialName }}</div>
        </el-radio>
      </el-radio-group>
    </template> -->

    <el-row :gutter="10">
      <el-col :span="6">
        <el-tree
          class="bfc-o"
          ref="tree"
          :data="treeData"
          :props="treeProps"
          :expand-on-click-node="false"
          highlight-current
          default-expand-all
          @node-click="handleNodeClick"
        >
        </el-tree>
      </el-col>
      <el-col :span="18">
        <div class="fxbw">
          素材库更新本分类素材后，自动同步到聊天工具栏
          <el-pagination
            class="fr"
            @current-change="getList"
            :current-page="query.pageNum"
            :page-size="query.pageSize"
            layout="prev, pager, next"
            :total="total"
          ></el-pagination>
        </div>
        <el-table
          ref="table"
          v-if="[1, 3, 4].includes(+type)"
          :data="list"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column v-if="type == 4" prop="content" label="文本内容"></el-table-column>
          <el-table-column v-else prop="materialName" label="素材名称"></el-table-column>
          <el-table-column prop="createTime" label="时间"></el-table-column>
        </el-table>

        <el-row v-else :gutter="20">
          <el-checkbox-group v-model="selectedx" @change="handleSelectionChange">
            <el-col
              :span="6"
              style="margin-bottom: 24px; min-width: 220px"
              v-for="(item, index) in list"
              :key="index"
            >
              <el-card shadow="hover" body-style="padding: 0px;">
                <div class="img-wrap">
                  <el-image
                    v-if="[0, 5].includes(+type)"
                    :src="item.materialUrl"
                    fit="contain"
                  ></el-image>
                  <video
                    v-else-if="type == 2"
                    id="video"
                    class="video-js vjs-default-skin vjs-big-play-centered"
                    controls
                    webkit-playsinline="true"
                    playsinline="true"
                    :autoplay="false"
                    preload="auto"
                    :poster="item.coverUrl"
                  >
                    <source :src="item.materialUrl" type="video/mp4" />
                  </video>
                </div>
                <div style="padding: 14px">
                  <el-checkbox :label="item">
                    <span class="label">
                      {{ item.materialName }}
                    </span>
                  </el-checkbox>
                </div>
              </el-card>
            </el-col>
          </el-checkbox-group>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
// .img-wrap {
//   ::v-deep.el-radio__input {
//     position: absolute;
//     left: 50%;
//     top: 50%;
//     transform: translate(-50%, -50%);
//   }
// }
.img-li {
  width: 160px;
  height: 160px;
  border-radius: 8px;
  border: 1px solid #e6ebf5;
}

.img-wrap {
  position: relative;
  height: 0;
  padding: 70% 0 0 0;
  border-bottom: 1px solid #e6ebf5;
  .el-image,
  #video {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
  }
}
::v-deep .el-checkbox__label {
  vertical-align: text-top;
}
.label {
  white-space: normal;
}
</style>
