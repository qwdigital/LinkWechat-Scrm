<template>
  <div class="app-container">
    <div class="left-tree">
      <zmy-tree :isfilter='true' :treeData='treeData' :treeDefaultProps='treeDefaultProps' :treeEvent='treeEvent'>

      </zmy-tree>
    </div>
    <div class="right">
        <div class="ces-main">
            <zmy-table
                :that='that'
                size='small'
                :isSelection='true'
                :isIndex='true'
                :isButton='true'
                :isSearch='true'
                :tableData='tableData'
                :tableCols='tableCols'
                :isPagination='true'
                :tablePage='searchValue'
                :loading="loading"
                :searchBtns="searchBtns"
                :searchData="searchValue" 
                :searchForm="searchForm" 
                @currentchange="fetchData"
                @sizechange="fetchData"
                @select='select'
                @select-all='selectAll'
                :isImport='false'
                :importUrl='importUrl'
                :importData='importData' 
            >
            </zmy-table>   
        </div>
        <div class='ces-dialog'>
            <zmy-dialog :title="dialog.title" :dialogFormVisible="dialog.visible" :dialogData="dialog.dialogData"
            :dialogForm="dialogForms" :rules="dialog.rules" :dialogBtns="dialogBtns" :dialogWidth="dialog.dialogWidth">
            </zmy-dialog>
        </div>
         <div class='ces-dialog'>
            <zmy-dialog :title="dialog2.title" :dialogFormVisible="dialog2.visible" :dialogData="dialog2.dialogData"
            :dialogForm="dialogForms2" :rules="dialog2.rules" :dialogBtns="dialogBtns2" :dialogWidth="dialog2.dialogWidth">
            </zmy-dialog>
        </div>
    </div>
  </div>
</template>

<script>

import { getToken } from '@/utils/auth'
const modules = {}
const path = require('path')
const files = require.context('@/components/zmyComponents', false, /\.vue$/)
files.keys().forEach(key => { const name = path.basename(key, '.vue'); modules[name] = files(key).default || files(key)})
import {getList,info,add,edit,del,getTreeList,infoTree,addTree,editTree,delTree} from '@/api/zmyApi/sucai-guanli/all.js'
import {isEmpty,objCopy,initObj} from '@/utils/zmyUtils'
export default {
  components: modules,
  data() {
    return {
      treeData: [{
          id: 1,
          label: '一级 1',
          children: [{
            id: 4,
            label: '二级 1-1',
            children: [{
              id: 9,
              label: '三级 1-1-1'
            }, {
              id: 10,
              label: '三级 1-1-2'
            }]
          }]
        }, {
          id: 2,
          label: '一级 2',
          children: [{
            id: 5,
            label: '二级 2-1'
          }, {
            id: 6,
            label: '二级 2-2'
          }]
        }, {
          id: 3,
          label: '一级 3',
          children: [{
            id: 7,
            label: '二级 3-1'
          }, {
            id: 8,
            label: '二级 3-2'
          }]
        }],
      treeDefaultProps:{
        children: 'children',
        label: 'label'
      },
        importUrl:'',
        importData:{},
        that : this,
        loading:true,
        dialogtip:'add',
        selectArr:[],
        tableData : [],
        searchValue:{categoryId:4,search:'', pageNum: 1, pageSize: 10,},
        options:{},
        dialog:{
            title:'',
            dialogWidth:'50%',
            dialogRef:'form1',
            visible:false,
            dialogData:{id:'',parentId:'',name:'',mediaType:4},
            rules:{}
        },
        dialog2:{
            title:'',
            dialogWidth:'60%',
            dialogRef:'form2',
            visible:false,
            dialogData:{categoryId:'',id:'',content:'',mediaType:4},
            rules:{}
        },
    };
  },
  computed: {
    // 树事件
    treeEvent(){
      return {
        addParent:()=>{
              this.dialogtip = 'add';this.dialog.title='新增父节点';
              this.dialog.dialogData ={parentId:'',id:'',name:'',mediaType:4};
              this.dialog.visible = true;
        },
        edit:(node,data)=>{this.$message.error('123')},
        append:(node,data)=>{this.$message.error('123')},
        remove:(node,data)=>{this.$message.error('123')},
        
        }
    },
    // 搜索框
    searchForm(){
      return [
          {type:'Input',label:'文本素材',prop:'search'},
          {type:'search',handle:(that,row)=>{this.fetchData(); }},
      ]
    },
    searchBtns(){
           return  [
                {label:'移动分组',type:'danger',handle:()=>{
                      
                }},
                {label:'添加文本',type:'danger',handle:()=>{
                       this.dialogtip = 'add';this.dialog2.title = '新增';
                       this.dialog2.dialogData=initObj(this.dialog2.dialogData,[]);
                       this.dialog2.visible = true;
                }},
                {label:'删除',type:'danger',handle:()=>{
                    let that  = this;
                    if(this.selectArr.length==0){this.$message.error('请勾选待删除信息！');  return;}
                    if(this.selectArr.length>=1){
                       this.$confirm("确认删除吗？", "提示", { type: "warning" }).then(() => {
                            del({id:this.selectArr}).then(res=>{  that.$message.success('删除成功'); that.fetchData(); })})
                    }
                }}
            ]
      },
    tableCols(){
            return [
                {label:'文本名称',prop:'grade_name'}, 
                {label:'时间',prop:'grade_name'}, 
                {label:'操作',type:'Button',width:'240px',btnList:[
                    {type:"primary",label:'编辑',class:'el-icon-edit',handle:(that,row)=>{
                        this.dialogtip = 'edit';this.dialog2.title = '编辑';
                        this.dialog2.dialogData = objCopy(this.dialog2.dialogData,row);
                        this.dialog2.visible = true;
                    }},
                  
                ]},
            ]
      },
       dialogForms(){
          return [ 
                {type:'Input',label:'节点名称',prop:'name',placeholder:'请输入'},
                ] 
      },
       dialogForms2(){
          return [ 
                {type:'Input',label:'文本分类',prop:'name',placeholder:'请输入'},
                {type:'TextArea',label:'文本内容',prop:'content',placeholder:'请输入',width:'100%'},

                ] 
      },
      dialogBtns(){
          return  [
                {label:'保存',type:'primary',handle:(value)=>{
                    console.log(this.dialog.dialogData)
                       if(!isEmpty(this.dialog.dialogData,['parentId','id'])){ this.$message.error('请先填写所有信息再进行保存！'); return; }
                       if(this.dialogtip == 'add'){
                         let params = {
                           mediaType:this.dialog.dialogData.mediaType,
                           name:this.dialog.dialogData.name,
                           parentId:''
                         }
                            addTree(params).then(res=>{ 
                                if(res.data.code==1){
                                    this.$message.success('新增成功'); 
                                    this.dialog.dialogData = initObj(this.dialog.dialogData,['school_id']);
                                    this.dialog.visible  = false;this.fetchData(); 
                                } 
                            }) 
                       }else{
                            edit(this.dialog.dialogData).then(res=>{  
                                if(res.data.code==1){
                                 this.$message.success('编辑成功'); 
                                 this.dialog.dialogData = initObj(this.dialog.dialogData,['school_id']);
                                 this.dialog.visible  = false;this.fetchData();
                                }
                             })     
                       }
                }},
                {label:'返回',type:'info',handle:(value)=>{
                    this.dialog.dialogData = initObj(this.dialog.dialogData,['school_id']);
                    this.dialog.visible  = false;
                }}
            ]
      },
      dialogBtns2(){
        return  [
                {label:'保存',type:'primary',handle:(value)=>{
                    console.log(this.dialog.dialogData)
                       if(!isEmpty(this.dialog.dialogData,['parentId','id'])){ this.$message.error('请先填写所有信息再进行保存！'); return; }
                       if(this.dialogtip == 'add'){
                         let params = {
                           mediaType:this.dialog.dialogData.mediaType,
                           name:this.dialog.dialogData.name,
                           parentId:''
                         }
                            addTree(params).then(res=>{ 
                                if(res.data.code==1){
                                    this.$message.success('新增成功'); 
                                    this.dialog.dialogData = initObj(this.dialog.dialogData,['school_id']);
                                    this.dialog.visible  = false;this.fetchData(); 
                                } 
                            }) 
                       }else{
                            edit(this.dialog.dialogData).then(res=>{  
                                if(res.data.code==1){
                                 this.$message.success('编辑成功'); 
                                 this.dialog.dialogData = initObj(this.dialog.dialogData,['school_id']);
                                 this.dialog.visible  = false;this.fetchData();
                                }
                             })     
                       }
                }},
                {label:'返回',type:'info',handle:(value)=>{
                    this.dialog.dialogData = initObj(this.dialog.dialogData,['school_id']);
                    this.dialog.visible  = false;
                }}
            ]
      }
    
  },
  methods: {
    fetchData(){
          this.loading = true;
          this.tableData = [];
         
          getList(this.searchValue).then(res=>{
            console.log(res)
              if(res.code == 200){
                this.searchValue.total =parseInt(res.total) ;
                this.tableData = res.rows;
                setTimeout(()=>{  this.loading = false;},500)
              }
          })
      },
      getTreeDate(){
        let params ={
          mediaType:2
        }
         getTreeList(params).then(res=>{
           
         })
      },
      select(all,current){ this.selectArr =[];  all.forEach(item=>{ this.selectArr.push(item) }) },
      selectAll(all){  this.selectArr =[];  all.forEach(item=>{ this.selectArr.push(item) }) },
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getTreeDate()
    this.fetchData();
  },
  
};
</script>
<style scoped lang='scss'>
.app-container{
  display: flex;
  width: 100%;
  .left-tree{
      width: 25%;
  }
  .right{
    width: 75%;
  }
}

</style>