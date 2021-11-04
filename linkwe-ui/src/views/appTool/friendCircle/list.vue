<style lang="scss" scoped></style>

<template>
  <div>
    <el-tabs type="card" v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="朋友圈动态" name="1">
        <div class="mb20">
          * 该朋友圈为展示在企业成员对外资料显示中的企业朋友圈，
          <!-- <el-button type="primary">查看功能介绍</el-button> -->
          <el-link type="primary" href="/appTool/friendIntroduce"
            >查看功能介绍</el-link
          >
        </div>

        <el-form
          class="top-search"
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="100px"
        >
          <el-form-item label="关键词" prop="title">
            <el-input
              v-model="queryParams.title"
              placeholder="请输入"
              clearable
              size="small"
            />
          </el-form-item>
          <el-form-item label="发布时间">
            <el-date-picker
              v-model="dateRange"
              size="small"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label>
            <el-button type="primary" @click="handleQuery">查询</el-button>
          </el-form-item>
        </el-form>

        <div class="ar mb15">
          <el-button type="primary">发布动态</el-button>
        </div>
        <el-table
          v-loading="loading"
          :data="list"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="所选成员" align="center" prop="operId" />
          <el-table-column label="发布内容" align="center" prop="title" />
          <el-table-column label="创建人" align="center" prop="businessType" />
          <el-table-column
            label="发布时间"
            align="center"
            prop="operTime"
            width="180"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.operTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row, scope.index)"
                v-hasPermi="['monitor:operlog:query']"
                >下载</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row, scope.index)"
                v-hasPermi="['monitor:operlog:query']"
                >复制链接</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row, scope.index)"
                v-hasPermi="['monitor:operlog:query']"
                >查看详情</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row, scope.index)"
                v-hasPermi="['monitor:operlog:query']"
                >编辑</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row, scope.index)"
                v-hasPermi="['monitor:operlog:query']"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />

        <!-- 添加或修改参数配置对话框 -->
        <el-dialog
          title="编辑图片"
          :visible.sync="open"
          width="600px"
          append-to-body
        >
          <el-form ref="form" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="图片名称">
              <el-input v-model="sd" placeholder></el-input>
            </el-form-item>
            <el-form-item label="图片文案">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
            <el-form-item label="图片">
              <el-upload
                action
                :show-file-list="false"
                :on-success="d"
                :before-upload="d"
              >
                <img v-if="imageUrl" :src="imageUrl" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div>
                支持JPG,PNG格式，图片大小不超过2M，建议上传宽高1:1的图片
              </div>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane label="设置" name="2">
        <div class="ar mb15">
          <el-input
            placeholder="请输入内容"
            prefix-icon="el-icon-search"
            style="width: 240px;"
            v-model="input2"
          ></el-input>
          <el-button
            class="ml10"
            @click="$router.push('/appTool/friendBackground')"
            >设置默认背景图片</el-button
          >
          <el-button type="primary">一键设置朋友圈地址</el-button>
        </div>
        <el-table
          v-loading="loading"
          :data="list"
          @selection-change="handleSelectionChange"
        >
          <el-table-column label="员工" align="center" prop="operId" />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row, scope.index)"
                v-hasPermi="['monitor:operlog:query']"
                >编辑头像背景</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row, scope.index)"
                v-hasPermi="['monitor:operlog:query']"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {
      queryParams: {},
      form: {},
      activeName: "1"
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {}
};
</script>
