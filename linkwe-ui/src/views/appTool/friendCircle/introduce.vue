<style lang="scss" scoped>
.section-box {
  margin-top: 32px;
  &:first-of-type {
    margin-top: 0;
  }
  .section-step {
    height: 22px;
    line-height: 22px;
    padding: 0 7px;
    text-align: center;
    color: #fff;
    background-color: #faad14;
    display: inline-block;
    font-size: 14px;
    margin-bottom: 12px;
  }
  .message {
    font-size: 14px;
    color: #606266;
    line-height: 22px;
    font-weight: 400;
  }
}
</style>

<template>
  <div>
    <div class="el-card__body">
      <div class="section-box">
        <div class="section-step">第一步</div>
        <div class="message">管理员登录企业微信管理后台，在我的企业——通讯录管理——成员资料显示——对外资料显示，点击修改；</div>
        <img
        
          src="https://wscos-1253767630.cos.ap-nanjing.myqcloud.com/wework/assets/moment/1.png"
          class="image"
          style="width: 882px; height: 277px;"
        />
        <div class="message">点击添加自定义信息，选择网页，名称填写朋友圈，确定保存后，员工对外资料页就会出现朋友圈这一项信息。</div>
        <img
          src="https://wscos-1253767630.cos.ap-nanjing.myqcloud.com/wework/assets/moment/2.png"
          class="image"
          style="width: 750px; height: 817px;"
        />
      </div>
      <div class="section-box">
        <div class="section-step">第二步</div>
        <div
          class="message"
        >完成修改后，返回企业微信手机端，打开对外信息显示，填写朋友圈网页信息；该栏需要输入网页名称，建议填写“查看朋友圈”；</div>
        <img
          src="https://wscos-1253767630.cos.ap-nanjing.myqcloud.com/wework/assets/moment/3.png"
          class="image"
          style="width: 439px; height: 526px;"
        />
      </div>
      <div class="section-box">
        <div class="section-step">第三步</div>
        <div class="message">
          获取朋友圈地址。后台管理员可一键发送、导出、复制员工的朋友圈地址。
          <br />员工将朋友圈链接粘贴到对外信息显示的朋友圈列表内，这样客户打开员工的对外资料，就能看到员工发布的朋友圈。
        </div>
        <img
          src="https://wscos-1253767630.cos.ap-nanjing.myqcloud.com/wework/assets/moment/4.png"
          class="image"
          style="width: 561px; height: 577px;"
        />
      </div>
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
