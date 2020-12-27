<template>
    <div>
        <el-form :inline="true" :model="form" class="demo-form-inline">
            <el-row>
                <el-form-item label="员工名称">
                    <el-select v-model="form.Ename" placeholder="员工名称" style="width:300px">
                    </el-select>
                </el-form-item>
                <el-form-item label="客户名称">
                    <el-input v-model="form.Cname" placeholder="客户名称" style="width:300px"></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item label="查找内容">
                    <el-input v-model="form.Scontent" placeholder="查找内容" style="width:300px"></el-input>
                </el-form-item>
                <el-form-item label="时间范围">
                    <el-time-picker is-range v-model="form.Stime" range-separator="至" start-placeholder="开始时间"
                        end-placeholder="结束时间" placeholder="选择时间范围">
                    </el-time-picker>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item>
                    <el-button type="primary">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button>导出列表</el-button>
                </el-form-item>
            </el-row>
        </el-form>
        <div class="content">
            <el-table :data="fileData" stripe style="width: 100%" :header-cell-style="{background:'#fff'}">
                <el-table-column prop="date" label="发送者" width="200">
                </el-table-column>
                <el-table-column prop="name" label=" 内容">
                </el-table-column>

                <el-table-column label="消息状态" width="200">  
                       <template slot="header" >
                           {{floorRange}} 
                      <el-select size="mini" v-model="floorRange" class="noborder" @change="chechName(floorRange)">
                            <el-option v-for="item in displayOptions" :key="item.value" :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                         </template>
                   
                </el-table-column>
                <el-table-column prop="address" label="发送时间" width="200">
                </el-table-column>
            </el-table>
            <el-pagination
  :page-size="20"
  style="padding:10px"
  :pager-count="11"
  layout="prev, pager, next"
  :total="1000">
</el-pagination>
        </div>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                form: {
                    Ename: "",
                    Cname: '',
                    Scontent: '',
                    Stime: ''
                },
                fileData: [],
                floorRange:'全部',
                 displayOptions: [{
                        value: "0",
                        label: "全部"
                    },
                    {
                        value: "1",
                        label: "已发送"
                    },
                    {
                        value: "2",
                        label: "已撤回"
                    },
                    {
                        value: "3",
                        label: "切回企业日志"
                    }
                ]
            }
        },
        methods:{
            chechName(e){
                if(e==0){
                    this.floorRange='全部'
                }
                else if(e==1){
                    this.floorRange='已发送'
                }else if(e==2){
                    this.floorRange='已撤回'
                }else{
                      this.floorRange='切回企业日志'
                }
            }
        }
    }
</script>
<style lang="scss" scoped>
    .demo-form-inline {
        background: #efefef;
        padding: 18px 10px 0 10px;
    }

    .content {
        margin-top: 15px;
        padding: 10px;
    }
    .noborder {
        /deep/ .el-input--mini .el-input__inner{width: 2px;border:none}
    }
</style>