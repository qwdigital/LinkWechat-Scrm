<template>
    <div class="list" v-loading="loading">
        <div v-if="personList.length>=1">         
            <ul>             
            <li v-for="(item,index) in personList" :key="index" @click="liClick(item)">
               <el-row style="padding:10px" v-if="item.finalChatContext.msgtype=='text'">
                <span class="fl"> 
                    <div class="ninebox">
                        <ul v-if="item.roomInfo">
                            <li v-for="(a,i) in item.roomInfo.avatar.split(',')" :key="i"><img :src="a" alt=""></li>
                             
                        </ul>
                    </div>
                
                </span>
                 <span class="fl" style="margin-left:8px;line-height:60px">
                    <p>{{item.finalChatContext.roomInfo.name}} <span class="fr gray">{{parseTime(item.finalChatContext.fromInfo.updateTime)}}</span></p>
                   <p class="gray" v-if="item.finalChatContext.fromInfo">{{item.finalChatContext.fromInfo.name}}:{{item.finalChatContext.text.content}}</p>     
                </span> 
                </el-row>     
                   <!-- <el-row style="padding:10px" v-if="item.finalChatContext.msgtype=='file'">
                <el-col :span="3">&nbsp;</el-col>
                <el-col :span="21">
                   <p><span class="fr gray">{{parseTime(item.finalChatContext.msgtime)}}</span></p>
                   <p class="gray padt10" >{{item.finalChatContext.from}}:
                       <span v-if="item.finalChatContext.file.fileext=='mp4'">[视频]</span>
                       </p>     
                </el-col>
                </el-row>   -->
            </li>
        </ul>
        </div>
        <div v-else></div>
    </div>
</template>
<script>
    import {parseTime} from '@/utils/common.js'
    export default {
        props: {
            personList: {
                type: Array,
                defluat: () => []
            },
            loading: {
                type: Boolean,
                defluat: false
            },
        },
     
        data() {
            return{
                loadings:true
            }
        },
        methods:{
            liClick(e){
                this.$emit('groupFn',e)
            }
        }
    }
</script>
<style lang="scss" scoped>
*{ padding: 0;
.fl{float: left;}
      margin: 0;}
      .ninebox{ width: 62px; height: 60px; border: 1px solid #199ed8;;
      ul li{
          float: left;
          width: 17px;
          height: 17px;
          padding: 0!important;
          margin:1px 2px 2px 1px;
      }}
    .list {
        overflow-y:scroll;
      
      ::-webkit-scrollbar {
                display: none;
            }

        /deep/ .el-loading-spinner{margin-top: 20px;}
       .fr{float:right;}
       .gray{color: #999;}
       .padt10{padding-top: 10px;}
        ul li {
            padding: 10px;
            overflow: hidden;
            border-bottom: 1px solid #efefef;
            cursor: pointer;
            p{white-space:nowrap;
            overflow:hidden;
            text-overflow:ellipsis;}
            :hover{ background: #efefef;}
            img {
                width: 40px;
                height: 40px;
                float: left
            }
        }
    }
</style>