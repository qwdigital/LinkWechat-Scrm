<template>
  <div>
    <welcome-content v-if="show" :isSingle="form.welcomeMsgTplType === '3'" :showTemplate="false" :showMember="form.welcomeMsgTplType === '2'" :showMorMaterial="true" :baseData="materialData" @submit="getWelData"></welcome-content>
  </div>
</template>

<script>
  import {
    addOrUpdate, getPreview
  } from '@/api/drainageCode/welcome'
  import SelectMaterial from '@/components/SelectMaterial'
  import WelcomeContent from '@/components/WelcomeContent.vue'
  export default {
    components: {
      SelectMaterial,
      WelcomeContent
    },
    props: {},
    data () {
      return {
        name: '活码',
        type: '新建',
        materialData: {
          welcomeMsg: '',
          materialMsgList: []
        },
        dialogVisible: false,
        dialogVisibleSelectMaterial: false,
        form: {
          id: '',
          welcomeMsgTplType: '',
          welcomeMsg: '',
          materialMsgList: []
        },
        // 遮罩层
        loading: false,
        show: false
      }
    },
    watch: {},
    computed: {},
    created () {
      this.form.id = this.$route.query.id
      if (this.form.id) {
        let data = JSON.parse(localStorage.getItem('obj'))
        this.form = Object.assign(this.form, data)
        this.form.welcomeMsgTplType = this.form.welcomeMsgTplType.toString()
      } else {
        this.form.welcomeMsgTplType = this.$route.query.welcomeMsgTplType
      }
      this.name = this.form.welcomeMsgTplType === '1' ? '活码' : this.form.welcomeMsgTplType === '2' ? '员工' : '入群'
      this.type = this.form.id ? '编辑' : '新建'
      this.$route.meta.title = (this.form.id ? '编辑' : '新建') + '欢迎语'
      this.form.id && this.getData()
    },
    mounted () {
      this.show = true
    },
    methods: {
      getWelData (data) {
        this.form.welcomeMsg = data.welcomeMsg
        let img = []
        let imgText = []
        let applet = []
        data.materialMsgList.forEach((unit, key) => {
          if (unit.msgType === '0') {
            img.push(unit.materialUrl)
          }
          if (unit.msgType === '7') {
            let obj = {
              imageTextTile: unit.materialName,
              imageTextUrl: unit.content
            }
            imgText.push(obj)
          }
          if (unit.msgType === '8') {
            let oob = {
              appTile: unit.materialName,
              appId: unit.materialUrl,
              appPath: unit.content,
              appPic: unit.coverUrl
            }
            applet.push(oob)
          }
        })
        if (applet.length) {
          this.form.applet = applet
        } else {
          this.form.applet = []
        }
        if (imgText.length) {
          this.form.imageText = imgText
        } else {
          this.form.imageText = []
        }
        if (img.length) {
          this.form.picUrl = img.join(',')
        } else {
          this.form.picUrl = ''
        }
        if (this.form.welcomeMsgTplType === '2') {
          this.form.userIds = data.users.map(dd => {
            return dd.userId
          }).join(',')
        }
        this.submit()
      },
      getData () {
        // getPreview(this.form.id).then(({ data }) => {
        this.materialData = this.form
        console.log(this.materialData)
        this.materialData.users = []
        if (this.materialData.userIds) {
          let name = this.materialData.userNames.split(',')
          this.materialData.userIds.split(',').forEach((ddd, index) => {
            let obj = {
              useUserId: ddd,
              userName: name[index]
            }
            this.materialData.users.push(obj)
          })
        }
        console.log(this.materialData.users)
        this.materialData.materialMsgList = []
        let img = []
        let imgText = []
        let applet = []
        if (this.form.picUrl) {
          this.form.picUrl.split(',').forEach(dd => {
            let obj = {
              materialUrl: dd,
              msgType: '0',
            }
            img.push(obj)
          })
        }
        if (this.form.imageText && this.form.imageText.length) {
          this.form.imageText.forEach(dd => {
            let obj = {
              materialName: dd.imageTextTile,
              content: dd.imageTextUrl,
              msgType: '7'
            }
            imgText.push(obj)
          })
        }
        if (this.form.applet && this.form.applet.length) {
          this.form.applet.forEach(cc => {
            let obj = {
              materialName: cc.appTile,
              materialUrl: cc.appId,
              content: cc.appPath,
              coverUrl: cc.appPic,
              msgType: '8'
            }
            applet.push(obj)
          })
        }
        this.materialData.materialMsgList.push(...img)
        this.materialData.materialMsgList.push(...imgText)
        this.materialData.materialMsgList.push(...applet)
        this.$forceUpdate()
        // })
      },
      submit () {
        addOrUpdate(this.form)
          .then(({
            data
          }) => {
            this.msgSuccess('操作成功')
            this.loading = false
            // this.$router.back()
            console.log(this.form.welcomeMsgTplType === '1')
            this.$router.push({
              path: '/drainageCode/welcome/',
              query: {
                type: this.form.welcomeMsgTplType.toString()
              }
            })
          })
          .catch(() => {
            this.loading = false
          })
      }
    },
  }
</script>

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

  .crumb- {
    // 一级 页面标题
    &title {
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 90px; // line-height: 90px;
      font-size: 18px;
      font-weight: 500;
      color: #333;
      padding: 0 20px;
      background: #fff;
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;
    }
  }

  .crumb {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    display: flex;
  }
</style>
