<script>
import Mtext from './text'
import Mimage from './image'
import MimageText from './imageText'
import Maudio from './audio'
import Mvideo from './video'
import Mfile from './file'
import Mapplet from './applet'
import Mposter from './poster'
import Mlink from './link'
export default {
  name: '',
  components: {
    Mtext,
    Mimage,
    MimageText,
    Maudio,
    Mvideo,
    Mfile,
    Mapplet,
    Mposter,
    Mlink
  },
  data() {
    return {
      activeName: 'text'
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {}
}
</script>

<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="文本" name="text">
        <Mtext></Mtext>
      </el-tab-pane>
      <el-tab-pane label="图片" name="image">
        <Mimage></Mimage>
      </el-tab-pane>
      <el-tab-pane label="图文" name="imageText">
        <MimageText></MimageText>
      </el-tab-pane>
      <el-tab-pane label="链接" name="link">
        <Mlink></Mlink>
      </el-tab-pane>
      <el-tab-pane label="语音" name="audio">
        <Maudio></Maudio>
      </el-tab-pane>
      <el-tab-pane label="视频" name="video">
        <Mvideo></Mvideo>
      </el-tab-pane>
      <el-tab-pane label="文件" name="file">
        <Mfile></Mfile>
      </el-tab-pane>
      <el-tab-pane label="小程序" name="applet">
        <Mapplet></Mapplet>
      </el-tab-pane>
      <el-tab-pane label="海报" name="poster">
        <Mposter></Mposter>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped>
/deep/.el-tabs__header {
  margin-bottom: 0;
  background: #fff;
}
</style>
