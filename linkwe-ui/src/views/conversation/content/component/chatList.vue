<template>
  <div class="takecontent">
    <ul>
      <li v-for="(item, index) in data" :key="index">
        <!-- <span v-if="item.fromInfo.name">{{item.fromInfo.name}}</span> -->
        <div class="mb15">
          <span class="mr20">{{ item.name }}</span>
          <span :style="{ color: item.action == 'send' ? '#199ed8' : '#999' }">{{
            item.msgTime
          }}</span>
        </div>

        <ChatContent :message="item"></ChatContent>
      </li>
    </ul>
  </div>
</template>

<script>
import ChatContent from '@/components/ChatContent'
export default {
  components: {
    ChatContent
  },
  props: {
    data: {
      type: Array,
      defluat: () => []
    }
  },
  mounted() {},
  data() {
    return {}
  },
  methods: {}
}
</script>
<style lang="scss" scoped>
#videoPlayer ::v-deep .vjs-tech {
  height: 450px;
}

.shabowbox {
  position: fixed;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  left: 0;
  top: 0;
  z-index: 2000;
}

.shabowboxvidoe {
  position: fixed;
  width: 800px;
  height: 475px;
  left: 50%;
  margin-left: -400px;
  top: 50%;
  margin-top: -235px;
  z-index: 2001;
  background: #fff;
}

.shabowboxaudio {
  height: 125px;
  padding: 12px;
}

.close {
  position: fixed;
  width: 50px;
  height: 50px;
  right: 10px;
  z-index: 2012;
  top: 10px;
  text-align: center;
  line-height: 50px;
  font-size: 20px;
  color: #fff;
  cursor: pointer;
  font-size: 43px;
}

.takecontent {
  text-align: left;
  width: 100%;
  overflow-y: scroll;

  ::-webkit-scrollbar {
    display: none;
  }

  ul li {
    padding: 10px;
  }
}
</style>
