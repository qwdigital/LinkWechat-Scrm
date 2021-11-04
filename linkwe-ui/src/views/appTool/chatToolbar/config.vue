<style lang="scss" scoped>
.el-card-inner {
  h4 {
    margin: 12px 0 24px;
  }
  p {
    font-size: 14px;
    color: #606266 !important;
    margin-bottom: 13px;
    padding: 0;
    margin: 1em 0;
  }
  .upload-desc {
    align-items: start;
  }
}
</style>

<template>
  <div>
    <el-card shadow="never">
      <div class="el-card-inner">
        <h4>创建自建应用并设置应用主页</h4>
        <p>
          请在企业微信官方后台“应用管理-自建应用”（
          <el-link
            type="primary"
            href="https://work.weixin.qq.com/wework_admin/loginpage_wx?redirect_uri=https://work.weixin.qq.com/wework_admin/frame#apps"
            target="_blank"
          >官方后台链接</el-link>）创建自建应用用于聊天工具栏。
        </p>
        <p>
          然后在“客户联系-配置-聊天工具栏管理”（
          <el-link
            type="primary"
            href="https://work.weixin.qq.com/wework_admin/loginpage_wx?redirect_uri=https://work.weixin.qq.com/wework_admin/frame#apps"
            target="_blank"
          >官方后台链接</el-link>）的配置应用页面，选择该应用并将以下网址链接复制并设置为自定义应用主页。
          <el-button type="text">查看图示讲解</el-button>
        </p>
        <span>访问地址</span>
        <el-input v-model="jio" disabled style="width: 350px; margin: 0px 15px;" placeholder></el-input>
        <el-button type="primary">复制链接地址</el-button>
        <p>请将该链接设置为聊天工具栏应用主页，用于聊天工具栏页面访问。否则无法正常使用聊天工具栏</p>
      </div>
    </el-card>

    <el-card shadow="never">
      <div class="el-card-inner">
        <h4>设置应用可信域名</h4>
        <p>
          请在企业微信官方后台“应用管理-自建应用”（
          <el-link
            type="primary"
            href="https://work.weixin.qq.com/wework_admin/loginpage_wx?redirect_uri=https://work.weixin.qq.com/wework_admin/frame#apps"
            target="_blank"
          >官方后台链接</el-link>），打开用于聊天工具栏的应用设置页，将qw.wshoto.com设置为可信域名，并下载校验文件保存到电脑本地后，在此处上传。
          <button
            type="button"
            class="el-button el-button--text el-button--medium"
          >
            <!---->
            <!---->
            <span>查看图示讲解</span>
          </button>
        </p>
        <p class="upload-desc flex">
          <span style="float: left;">上传校验文件：</span>
        <el-upload
          action
          on-preview
          on-remove
          before-remove
          multiple
          limit="3"
          on-exceed
          :file-list="[{name: 'name', url: 'url'}]"
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">请上传txt格式的校验文件，用于完成域名归属验证。否则无法正常使用聊天工具栏</div>
        </el-upload>
        </p>
      </div>
    </el-card>

    <div class="ac">
      <el-checkbox v-model="d" >已配置好聊天工具栏应用主页和应用可信域名</el-checkbox>
      <div><el-button type="primary">完成配置，去抓取素材</el-button></div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {};
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {},
};
</script>
