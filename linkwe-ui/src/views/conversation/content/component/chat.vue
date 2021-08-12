<template>
  <div class="takecontent">
    <ul>
      <li v-for="(item, index) in allChat" :key="index">
        <!-- <span v-if="item.fromInfo.name">{{item.fromInfo.name}}</span> -->
        <div class="mb15">
          <span class="mr20">{{ item.fromId }}</span>
          <span
            :style="{ color: item.action == 'send' ? '#199ed8' : '#999' }"
            >{{ item.msgTime }}</span
          >
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
    allChat: {
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
#videoPlayer /deep/ .vjs-tech {
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
  height: 600px;
  overflow-y: scroll;

  ::-webkit-scrollbar {
    display: none;
  }

  ul li {
    padding: 10px;
  }

  .msgtypetext {
    padding: 10px 0;
  }

  .msgtypevoice {
    margin: 10px;
  }

  .msgtypefile {
    margin: 10px;
    width: 200px;
    height: 40px;
    line-height: 40px;
    cursor: pointer;
    color: #199ed8;
    text-indent: 10px;
    box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.2), 0 1px 4px 0 rgba(0, 0, 0, 0.19);
  }

  .card_name {
    width: 320px;
    height: 105px;
    line-height: 80px;
    text-indent: 10px;
  }

  .msgtypevideo {
    margin: 10px;

    cursor: pointer;

    border-radius: 8px;
  }

  .msgtypeimg {
    width: 100px;
    height: 80px;
    margin: 10px;

    img {
      width: 100px;
      height: 80px;
    }
  }

  .msgtypecard {
    width: 320px;
    height: 140px;
    margin: 10px;
    border-radius: 8px;
    -webkit-box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2),
      0 6px 8px 0 rgba(0, 0, 0, 0.19);
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 8px 0 rgba(0, 0, 0, 0.19);
    position: relative;

    .card_foot {
      position: absolute;
      height: 20px;
      border-top: 1px solid #efefef;
      text-align: left;
      bottom: 15px;
      padding: 10px;
      color: #333;
      font-weight: bold;
      width: 100%;
    }
  }
}
</style>
