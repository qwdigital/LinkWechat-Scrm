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
      activeName: 'text',
      list: [
        { label: '文本', name: 'text', component: Mtext },
        { label: '图片', name: 'image', component: Mimage },
        { label: '图文', name: 'imageText', component: MimageText },
        { label: '链接', name: 'link', component: Mlink },
        { label: '语音', name: 'audio', component: Maudio },
        { label: '视频', name: 'video', component: Mvideo },
        { label: '文件', name: 'file', component: Mfile },
        { label: '小程序', name: 'applet', component: Mapplet },
        { label: '海报', name: 'poster', component: Mposter }
      ],
      opened: ['text']
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
    <el-tabs v-model="activeName" @tab-click="(v) => opened.includes(v.name) || opened.push(v.name)">
      <el-tab-pane v-for="(item, index) in list" :key="index" :label="item.label" :name="item.name">
        <component v-if="opened.includes(item.name)" :is="item.component"></component>
      </el-tab-pane>
      <!-- <el-tab-pane label="图片" name="image">
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
      </el-tab-pane> -->
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped></style>
