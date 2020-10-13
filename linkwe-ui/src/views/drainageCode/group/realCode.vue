<style lang="scss" scoped>
</style>

<template>
  <div>
    <div class="mid-action">
        <div class="total">实际群码</div>
        <div>
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="dialogVisible = true">添加实际群码</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实际群码" align="center" prop="operId" />
      <el-table-column label="群名称" align="center" prop="title" />
      <el-table-column label="有效期" align="center" prop="businessType" />
      <el-table-column label="扫码次数" align="center" prop="businessType" />
      <el-table-column label="扫码次数限制" align="center" prop="businessType" />
      <el-table-column label="状态" align="center" prop="businessType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div>
      <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">上一步</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">下一步</el-button>
    </div>

    <el-dialog
      title="添加实际群码"
      :visible.sync="dialogVisible"
      width="width"
      :before-close="dialogBeforeClose">
      <el-form :model="form" :label-width="100">
        <el-form-item label="实际群码" prop="title">
            <el-upload
              class="avatar-uploader"
              action
              :show-file-list="false"
              :on-success="uploadSuccess"
              :before-upload="beforeUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>注：只能上传jpg/png格式图片，且不超过2M
          </el-form-item>
        <el-form-item label="群名称">
          <el-input v-model="a"></el-input>
        </el-form-item>
        <el-form-item label="有效期">
          <el-date-picker v-model="time" type="date" placeholder="date"></el-date-picker>
        </el-form-item>
        <el-form-item label="扫码次数限制">
          <el-input type="number" v-model="a"></el-input>
          <div>注：微信群建议设为300；企业微信群建议设为130</div>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  components:{},
  props:{},
  data(){
    return {
       dialogVisible: false
    }
  },
  watch:{},
  computed:{},
  created(){},
  mounted(){},
  methods:{},
}
</script>
