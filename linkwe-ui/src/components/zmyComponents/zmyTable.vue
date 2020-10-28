<template>
    <section class="ces-table-page">
        <section class="ces-search">
          <!-- 搜索内容 -->
           <el-form :size="size" inline label-width="80px"  ref="form"  v-if='isSearch'>
                   <el-form-item v-for='item in searchForm'  :key='item.prop' :prop="item.prop">
                     
                    <el-input v-if="item.type==='Input'" :disabled="item.disabled || false"  v-model="searchData[item.prop]" :placeholder='item.label' size="mini" ></el-input>
                    <!-- 下拉框 -->
                    <el-select v-if="item.type==='Select'" :disabled="item.disabled || false" :placeholder='item.label' v-model="searchData[item.prop]" size="mini" @change="item.change(searchData[item.prop])">
                        <el-option v-for="op in item.options" :label="op.label" :value="op.value" :key="op.value"></el-option>
                    </el-select>
                    <!-- 单选 -->
                    <el-radio-group v-if="item.type==='Radio'" v-model="searchData[item.prop]">
                        <el-radio v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio>
                    </el-radio-group>
                    <!-- 单选按钮 -->
                    <el-radio-group v-if="item.type==='RadioButton'" v-model="searchData[item.prop]" @change="item.change && item.change(searchData[item.prop])">
                        <el-radio-button v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio-button>
                    </el-radio-group>
                    <!-- 复选框 -->
                    <el-checkbox-group v-if="item.type==='Checkbox'" v-model="searchData[item.prop]" >
                        <el-checkbox v-for="ch in item.checkboxs" :label="ch.value" :key="ch.value">{{ch.label}}</el-checkbox>
                    </el-checkbox-group>
                    <!-- 日期valueFormat对于周  不能用 -->
                    <el-date-picker v-if="item.type==='Date'" :type="item.dateType || 'date'" :placeholder='item.label'  :format="item.format|| 'yyyy-MM-dd'" :value-format="item.valueFormat|| 'yyyy-MM-dd'"  v-model="searchData[item.prop]" ></el-date-picker>
                    <!-- 时间 -->
                    <el-time-select v-if="item.type==='Time'"  v-model="searchData[item.prop]" 
                        :picker-options="{ start: '08:30',  step: '00:15', end: '18:30' }"></el-time-select>
                    <!-- 日期时间 -->
                    <el-date-picker v-if="item.type==='DateTime'" type='datetime' v-model="searchData[item.prop]" :disabled="item.disable && item.disable(searchData[item.prop])"></el-date-picker>
                    <!-- 滑块 -->
                    <el-slider v-if="item.type==='Slider'" v-model="searchData[item.prop]"></el-slider>
                    <!-- 开关 -->
                    <el-switch v-if="item.type==='Switch'" v-model="searchData[item.prop]" ></el-switch>
                    <!-- 搜索按钮 -->
                    <el-button v-if="item.type==='search'" :size="size || item.size" type="danger"  @click="item.handle(that)">查询
                    </el-button>
                </el-form-item>
            </el-form>
          <!-- 表格操作按钮 -->
            <div class="ces-handle" v-if='isButton'>
                <el-button v-for='item in searchBtns' :key='item.label'
                    :size="size || item.size"
                    :type="item.type"
                    :icon='item.icon'
                    @click="item.handle(that)">{{item.label}}
                </el-button>
                <!-- 导入 -->
                <el-upload  v-if='isImport'
                    class="improt"
                    ref="upload"
                    :on-success="importSuccess"
                    :action="importUrl"
                    :show-file-list="false"
                    :auto-upload="true"
                    :data='importData'
                  >
                  <el-button type="warning" slot="trigger">导入</el-button>
                  </el-upload>
            </div>
        </section>
        
         
        
        <!-- 数据表格 -->
        <section class="ces-table"  v-if='isTable'>
            <el-table
                :header-cell-style="{height: '60px',padding: '0','font-size': '12px',color: '#8590a6'}"
                :row-style="{'font-size': '12px',color: '#212121'}"
                 align='center'
                 stripe  fit
                 :data='tableData'
                 :size='size'
                  height="100%"
                 style="width: 100%"
                 :border='isBorder'
                 @select='select'
                @select-all='selectAll'
                v-loading='loading'
                :defaultSelections='defaultSelections'
                ref="cesTable">
               
                <el-table-column v-if="isSelection" type="selection" align="center" ></el-table-column>
              
                <el-table-column v-if="isIndex" type="index" :label="indexLabel" align="center" width="80"></el-table-column>
                <!-- 数据栏 -->
                <el-table-column
                     v-for="(item) in tableCols"
                     :key="item.id"
                     :prop="item.prop"
                     :label="item.label"
                     :width="item.width"
                     :align="'center'||item.align"
                     :render-header="item.require?renderHeader:null"
                      
                >
                    <template slot-scope="scope" >
                        <!-- 按钮-->

                        <template v-if="item.type === 'Button'">
                            <el-button v-for="btnItem in item.btnList" :key="btnItem.label"
                             type="text" @click="btnItem.handle(that,scope.row)">
                              <i :class="btnItem.class">{{btnItem.label || btnItem.textEvent(that,scope.row)}}</i>
                            </el-button>
                          
                            <!-- <el-switch v-if="isSwitch"  v-model="scope.row.switch" @change='item.switchHandle'> </el-switch> -->
                        </template>

                         <el-input v-if="item.type==='input'" v-model="scope.row[item.prop]" :type="item.inputType||'text'" @blur='($event)=> item.submit($event,scope.row)' @input='($event)=> item.handle($event,scope.row)'></el-input>
                        <!-- html -->
                        
                        <span v-if="item.type==='html'" v-html="item.html(scope.row)"></span>
                        <!-- 下拉框 -->
                        <el-select v-if="item.type==='select'" v-model="scope.row[item.prop]" :size="size || btn.size"  :props="item.props"
                                   :disabled="item.isDisabled && item.isDisabled(scope.row)"
                                   @change='item.change && item.change(that,scope.row)'>
                        <el-option v-for="op in item.options" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                        <!-- 单选 -->
                        <el-radio-group v-if="item.type==='radio'" v-model="scope.row[item.prop]"
                                        :disabled="item.isDisabled && item.isDisabled(scope.row)" :size="size || btn.size"
                                        @change='item.change && item.change(that,scope.row)'>
                            <el-radio v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio>
                        </el-radio-group>
                        <!-- 复选框 -->
                        <el-checkbox-group v-if="item.type==='checkbox'" v-model="scope.row[item.prop]"
                                           :disabled="item.isDisabled && item.isDisabled(scope.row)" :size="size || btn.size"
                                           @change='item.change && item.change(that,scope.row)'>
                            <el-checkbox v-for="ra in item.checkboxs" :label="ra.value" :key='ra.value'>{{ra.label}}</el-checkbox>
                        </el-checkbox-group>
                        <!-- 评价 -->
                        <el-rate v-if="item.type==='rate'" v-model="scope.row[item.prop]"
                                 :disabled="item.isDisabled && item.isDisabled(scope.row)" :size="size || btn.size"
                                 @change='item.change && item.change(scope.row)'></el-rate>
                        <!-- 开关 -->
                        <el-switch v-if="item.type==='switch'" v-model="scope.row[item.prop]" :size="size || btn.size"
                                   :active-value='item.values&&item.values[0]'
                                   :inactive-value='item.values&&item.values[1]'
                                   :disabled="item.isDisabled && item.isDisabled(scope.row)"
                                   @change='item.change && item.change(scope.row)'></el-switch>
                        <!-- 图像 -->
                        <img v-if="item.type==='image'" :src="scope.row[item.prop]" @click="item.handle && item.handle(scope.row)"/>
                        <!-- 滑块 -->
                        <el-slider v-if="item.type==='slider'" v-model="scope.row[item.prop]"
                                   :disabled="item.isDisabled && item.isDisabled(scope.row)" :size="size || btn.size"
                                   @change='item.change && item.change(scope.row)'></el-slider>

                        <!-- <el-input v-if="item.type='input'" v-model="scope.row[item.prop]" :type="item.inputType||'text'" @blur='item.blurEvent(row)'></el-input> -->
                    
                        <!-- 默认 -->
                        <span v-if="!item.type"  @click="item.handle(scope.row)" 
                              :style="item.itemStyle && item.itemStyle(scope.row)" :size="size || btn.size"
                              :class="item.itemClass && item.item.itemClass(scope.row)">{{(item.formatter && item.formatter(scope.row)) || scope.row[item.prop]}}</span>
                        <a v-if="item.type == 'a'" style="color:blue" :href='scope.row[item.prop]' target="_blank">{{scope.row[item.prop] ==null ?'未上传附件':'附件下载'}}</a>
                    </template>
                </el-table-column>
            </el-table>
        </section>
        <!-- 分页 -->
        <slot name="mid"></slot>
        <section class="ces-pagination"  v-if='isPagination'>
            <el-pagination style='display: flex;justify-content: center;height: 100%;align-items: center;padding: 20px 0 20px 0;'
                   @current-change="handleCurrentChange"
                   @size-change="handleSizeChange"
                   background
                   layout="total,sizes ,prev, pager, next, jumper"
                   :page-size="tablePage.offset"
                   :current-page.sync="tablePage.page" 
                   :total="tablePage.total"
            ></el-pagination>
        </section>
        <!--弹窗 -->

    </section>
</template>

<script>

  export default {
    props:{
      that: { type: Object, default: this },
      // 表格型号：mini,medium,small
      size:{type:String,default:'medium'},
      isBorder:{type:Boolean,default:true},
      loading:{type:Boolean,default:false},
      // 表格操作
      isButton:{type:Boolean,default:false},
      isImport:{type:Boolean,default:false},
      importUrl:{type:String,default:''},
      importData:{type:Object,default:()=>({})},
      isSearch:{type:Boolean,default:false},
      searchBtns:{type:Array,default:()=>[]},
      // 表格数据
      tableData:{ type:Array,default:()=>[]},
      // 表格列配置
      tableCols:{ type:Array,default:()=>[]},
      // 是否显示表格复选框
      isSelection:{type:Boolean,default:false},
      defaultSelections:{ type:[Array,Object], default:()=>null},
      // 是否显示表格索引
      isIndex:{type:Boolean,default:false},
      indexLabel: {type:String,default:'序号'},
      // 是否显示分页
      isPagination:{type:Boolean,default:true},
      // 分页数据
      tablePage:{ type:Object,default:()=>({offset:10,page:1,total:0})},
      searchForm:{  type:Array,  default:()=>[]},
      // searchHandle:{  type:Array, default:()=>[] },
      searchData:{type:Object,default:()=>{}},
      labelWidth:{type:String,default:'100px'},
      isTable:{type:Boolean,default:true},
    },
    data(){
      return {
        options:{nianciOption:[]}
      }
    },
    watch:{
      'defaultSelections'(val) {
        this.$nextTick(function(){
          if(Array.isArray(val)){
            val.forEach(row=>{
              this.$refs.cesTable.toggleRowSelection(row)
            })
          }else{
            this.$refs.cesTable.toggleRowSelection(val)
          }
        })
      }
    },
    methods:{
      niancichange(value){
         console.log(this.searchData)
      },
      // 表格勾选
      select(rows,row){
        this.$emit('select',rows,row);
      },
      // 全选
      selectAll(rows){
        this.$emit('select-all',rows)
      },
      //

      handleCurrentChange(val){
        console.log(val)
        this.tablePage.page = val;
        this.$emit('currentchange',val);
      },
      handleSizeChange(val) {
        console.log(val)
        this.tablePage.offset = val;
        this.$emit('sizechange',val);
      },
      importSuccess(res, listData) {
       
        console.log(res,listData, "打印上传后的结果"); 
        const h = this.$createElement;
        const newDatas = []
        if(res.code==1){
          res.data.forEach(el=>{
            newDatas.push(h('p', null, el.name +' - '+ el.msg))
          })
        }
        return res.code == 1
          ? this.$notify(
              {
                title: res.msg,
                dangerouslyUseHTMLString: true,
                message: h('div', null, newDatas),
                duration:5000
              },
              setTimeout(()=>{
                  location.reload()
              },2000)
            )
          : this.$message.error('上传失败,原因:' + res.msg );
        

    },
      renderHeader(h,obj) {
        return h('span',{class:'ces-table-require'},obj.column.label)
      },
    },
    
    created(){
              

        
    }
  }
</script>
<style  scoped lang="scss">
    .ces-search{
        padding: 10px 0 20px 0;
        display: flex;
        justify-content: space-between;
        button{
          // margin:2px 0;
          color: #FFFFFF;
          background-color: #1890ff;
          border-color: #1890ff;
          border:none;
        }
        .ces-handle{
          display: flex;
          justify-content: flex-end;
          button{
              margin:0 10px 0 0;
               background-color: #1890ff;
              border-color: #1890ff;
              border:none;
            }
        }
         /deep/.el-input__inner{
          height: 35px;
        }
    }
    .ces-table-require::before{
        content:'*';
        color:F4483E;
    }
    .el-table__row td {
        text-align: center!important;
    }
    .has-gutter tr th {
        text-align: center!important;
    }
    .el-table__body-wrapper {
        height: 500px !important;
    }
    /deep/.el-table__body, .el-table__footer, .el-table__header{
      table-layout: unset!important;
    }
    /deep/.el-table__header{
      table-layout: unset!important;
    }
   
    /deep/.el-form-item{
      margin: 0 10px 0 0;
      width: 120px!important;

    }
    /deep/.el-date-editor.el-input{
      width: 120px!important;
    }
   /deep/.el-input--suffix .el-input__inner{
     padding-left: 10px;
   }
   /deep/.el-input__prefix{
     display: none;
   }
    /deep/.el-table__body-wrapper{
      height: auto!important;
    }
    .improt{button{padding: 9px 15px!important;}}
    
</style>