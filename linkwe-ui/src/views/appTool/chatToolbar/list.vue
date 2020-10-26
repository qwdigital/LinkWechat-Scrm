<style lang="scss" scoped>
.chat-preview {
  border-radius: 4px;
  position: relative;
  margin-left: 40px;
  display: inline-block;
  width: 150px;
  min-height: 263px;
  background-repeat: no-repeat;
  background-size: cover;
  top: 15px;
  .logo {
    width: 40px;
    height: 40px;
    position: absolute;
    top: 18px;
    left: 50%;
    transform: translateX(-50%);
    border-radius: 50%;
  }
  .company-name {
    font-size: 12px;
    position: absolute;
    top: 70px;
    text-align: center;
    color: #ffe7be;
    width: 100%;
  }
}
</style>

<template>
  <div>
    <div>
      配置聊天工具栏，方便成员在外部会话中查看和使用，提高服务效率。
      <el-button type="text" @click="$router.push('/appTool/explain')">图文详解</el-button>
      <el-button type="text" @click="$router.push('/appTool/config')">查看已配置信息</el-button>
    </div>
    <div>
      <span>抓取快捷回复素材</span> 素材抓取后，即可在聊天工具栏使用
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="素材类型" align="center" prop="operId" />
      <el-table-column label="聊天工具栏名称" align="center" prop="title" />
      <el-table-column label="已抓取素材数量" align="center" prop="businessType" :formatter="typeFormat" />
      <el-table-column label="是否启用" align="center" prop="operTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" prop="operId">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
          >抓取素材</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-card shadow="never" header="红包工具栏">
      <!-- <div slot="header"></div> -->
      <el-row :gutter="10">
        <el-col :span="10">
          <div>
            <div>
              发红包权限人
              <el-button type="text">修改</el-button>
            </div>
            <el-tag type="info" effect="plain">
              <i class="el-icon-user-solid"></i> 深肤色
            </el-tag>
            <el-tag type="info" effect="plain">
              <i class="el-icon-user-solid"></i>深肤色
            </el-tag>
            <el-tag type="info" effect="plain">
              <i class="el-icon-user-solid"></i>深肤色
            </el-tag>
          </div>
          <el-form ref="form" :model="form" label-width="100px">
            <el-form-item label="企业logo">
              <el-upload action :show-file-list="false" :on-success="d" :before-upload="d">
                <img v-if="imageUrl" :src="imageUrl" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div>上传企业logo，建议112*112</div>
            </el-form-item>
            <el-form-item label="企业名称">
              <el-input v-model="model"></el-input>
            </el-form-item>
            <el-form-item label="红包应用ID">
              <el-input v-model="model"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="10">
          <div
            class="chat-preview"
            style="background-image: url(&quot;https://wscos-1253767630.cos.ap-nanjing.myqcloud.com/wework/assets/red-packet.png&quot;);"
          >
            <div class="el-image logo" style>
              <div class="el-image__error">加载失败</div>
              <!---->
            </div>
            <el-image class="logo" :src="url" :fit="fit"></el-image>
            <div class="company-name">脑白金</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog
      title="抓取文本类型素材库"
      :visible.sync="dialogVisible"
      :before-close="dialogBeforeClose"
    >
      <el-row :gutter="10">
        <el-col :span="6">
          <el-tree :data="d" :props="d" @node-click="d"></el-tree>
        </el-col>
        <el-col :span="18">
          <div class="fxbw">
            素材库更新本分类素材后，自动同步到聊天工具栏
            <div class="filter-right">
              <i class="el-icon-arrow-left"></i> 1/1
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
          <el-table :data="data" style="width: 100%">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="prop" label="文本内容" width="width"></el-table-column>
            <el-table-column prop="prop" label="时间" width="width"></el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {
      dialogVisible: true,
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {},
};
</script>
