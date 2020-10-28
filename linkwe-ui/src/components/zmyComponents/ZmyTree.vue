<template>
   <section class="tree-container">
    <el-row v-if='isfilter' class="search_header">
        <el-col :span="20">
            <el-input placeholder="输入关键字进行过滤" size="small"  v-model="filterText"></el-input>
        </el-col>
        <el-col :span="4" class="plus">
            <i class="el-icon-plus" @click="treeEvent.addParent"></i>
        </el-col>
    </el-row>
        <el-tree
            class="filter-tree"
            :data="treeData"
            :props="treeDefaultProps"
            default-expand-all
            :filter-node-method="filterNode"
            ref="tree">
                <!-- style="display:flex;justify-content:space-between;" -->
                 <span class="custom-tree-node" slot-scope="{ node, data }" >
                    <span>{{ node.label }}</span>
                    <el-tooltip  placement="bottom"  popper-class="atooltip" effect="light">
                        <i class="el-icon-setting"></i>
                        <div slot="content" class='tipContent'>
                            <span>
                                <el-button  type="text"  size="mini" >
                                    <i class="el-icon-edit" @click="treeEvent.edit(node,data)"></i>
                                </el-button>
                                <el-button  type="text"  size="mini" >
                                    <i class="el-icon-plus" @click="treeEvent.append(node,data)"></i>
                                </el-button>
                                <el-button  type="text" size="mini" >
                                    <i class="el-icon-delete" @click="treeEvent.remove(node,data)"></i>
                                </el-button>
                            </span>
                        </div>
                    </el-tooltip>
                    
                    
                </span>





        </el-tree>
   </section>
</template>

<script>

  export default {
    props:{
     isfilter:{type:Boolean,default:false}, //是否显示过滤
     treeEvent:{type:Object,default:()=>{}},//tree组件的所有函数事件
     treeData:{type:Array,default:()=>[]}, //tree数据
     treeDefaultProps:{type:Object,default:()=>({children:'children',label:'label'}) },//tree的父子节点变量定义
    },
    data(){
      return {
        filterText: '',
      }
    },
    watch:{
       filterText(val) {
        this.$refs.tree.filter(val);
       }
    },
    methods:{
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      }
    },
    
    created(){
              

        
    }
  }
</script>
<style  scoped lang="scss">
.tree-container{
  .search_header{
      padding: 10px 0;
      /deep/.el-input__inner{
        height: 35px;
        line-height: 35px;
      }
  }
  
}
.plus{
    color: #606266;
    text-align: center;
    font-size: 25px;
    height: 35px;
    line-height: 35px;
}
.filter-tree{
  font-size:20px;
}
/deep/.el-tree-node__content{
  position: relative;
}
/deep/ .el-tooltip{
  position: absolute;
  top: 0;
  right: 30px;
}
/deep/.el-tree-node__content{
  height: 36px;
  line-height:36px;
}
.atooltip {
  background: #fff !important;
  border: none!important;
}
.tipContent{
  width:100%;
  height: 100%;
  background: #fff !important;
}
</style>