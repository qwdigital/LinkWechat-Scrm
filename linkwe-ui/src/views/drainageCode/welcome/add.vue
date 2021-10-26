<script>
import { addOrUpdate, getPreview } from '@/api/drainageCode/welcome'
import SelectMaterial from '@/components/SelectMaterial'
import WelcomeContent from '@/components/WelcomeContent.vue'
export default {
  components: {
    SelectMaterial,
    WelcomeContent
  },
  props: {},
  data() {
    return {
      materialData: {
        welcomeMsg: '',
        materialMsgList: []
      },
      dialogVisible: false,
      dialogVisibleSelectMaterial: false,
      form: {
        // id: '',
        welcomeMsgTplType: '',
        welcomeMsg: '',
        materialUrl: ''
      },
      // 遮罩层
      loading: false
    }
  },
  watch: {},
  computed: {},
  created() {
    this.form = Object.assign(this.form, this.$route.query)
    this.$route.meta.title = (this.form.id ? '编辑' : '新建') + '欢迎语'
    this.form.id && this.getData()
  },
  mounted() {},
  methods: {
    getWelData(data) {
      let form = this.form
      form.welcomeMsg = data.welcomeMsg
      // this.form.mediaId = data.mediaId
      // this.form.materialMsgList = data.materialMsgList
      data.materialMsgList.forEach((element) => {
        // 0 图片；4文本；7图文；8小程序
        if (element.msgType == 0) {
          ;(form.picUrl || (form.picUrl = [])).push(element.materialUrl)
        } else if (element.msgType == 7) {
          ;(form.imageText || (form.imageText = [])).push({
            imageTextTile: element.materialName, //图文标题
            imageTextUrl: element.content //图文路径
          })
        } else if (element.msgType == 8) {
          ;(form.applet || (form.applet = [])).push({
            appTile: element.materialName, //小程序标题
            appId: element.materialUrl, //小程序id
            appPath: element.content, //小程序路径
            appPic: element.coverUrl //小程序封面
          })
        }
      })
      if (form.welcomeMsgTplType === '1') {
        form.userIds = data.users.map((e) => e.userId) + ''
      }
      form.picUrl += ''
      this.submit()
    },
    getData() {
      getPreview(this.form.id).then(({ data }) => {
        this.materialData = {}
        this.materialData = data
        this.materialData.materialMsgList.forEach((item) => {
          item.msgType = Number(item.msgType)
        })
      })
    },
    submit() {
      debugger
      addOrUpdate(this.form)
        .then(({ data }) => {
          this.msgSuccess('操作成功')
          this.loading = false
          // this.$router.back()
          // console.log(form.welcomeMsgTplType === '1')
          this.$router.push({
            path: '/wechat/drainageCode/welcome/',
            query: {
              type: this.form.welcomeMsgTplType
            }
          })
        })
        .catch(() => {
          this.loading = false
        })
    },
    insertName() {
      this.form.welcomeMsg += '#客户昵称#'
    },
    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {
      this.form.mediaId = image.id
      this.form.materialUrl = image.materialUrl
      this.dialogVisibleSelectMaterial = false
    },
    removeMaterial() {
      this.form.mediaId = ''
      this.form.materialUrl = ''
    }
  }
}
</script>

<template>
  <welcome-content
    :showTemplate="false"
    :showMember="form.welcomeMsgTplType == 2"
    :showMorMaterial="true"
    :baseData="materialData"
    @submit="getWelData"
  ></welcome-content>
</template>

<style lang="scss" scoped>
.page {
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
}
.form {
  width: 50%;
  max-width: 500px;
}

.filter-wrap {
  justify-content: space-between;
}
.list-wrap {
  flex-wrap: wrap;
}
</style>
