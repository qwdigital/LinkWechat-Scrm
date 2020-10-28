<!-- 搜索表单 -->
<template>
<div class="ces-search">
    <el-dialog :title="title" :visible.sync="dialogFormVisible"  :close-on-click-modal="false" :show-close="false" :width="dialogWidth">
    <el-form :size="size" inline :model='dialogData' :label-width="dialogLabelWidth" :rules="rules" :ref="dialogRef" style="">
        <el-form-item v-for='item in dialogForm' :label="item.label" :key='item.prop' :prop="item.prop" :style="{width:(item.width?item.width:'40%')}">
            <!-- 输入框 -->
            <el-input v-if="item.type==='Input'" :placeholder='item.placeholder'  :type="item.inputType || 'text'" v-model="dialogData[item.prop]" size="mini" :disabled="item.disabled || false"></el-input>
             <!-- 输入框 -->
            <el-input v-if="item.type==='TextArea'" :placeholder='item.placeholder'   type='textarea' :rows="2" v-model="dialogData[item.prop]" size="mini" :disabled="item.disabled || false"></el-input>
            <!-- 下拉框 -->
            <el-select v-if="item.type==='Select'" :placeholder='item.placeholder'  v-model="dialogData[item.prop]" size="mini" @change="item.change(dialogData[item.prop])"  :multiple="item.multiple || false"  :disabled="item.disabled || false">
                <el-option v-for="op in item.options" :label="op.label" :value="op.value" :key="op.value"></el-option>
            </el-select>
            <!-- 单选 -->
            <el-radio-group v-if="item.type==='Radio'" v-model="dialogData[item.prop]" :disabled="item.disabled || false">
                <el-radio v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio>
            </el-radio-group>
            <!-- 单选按钮 -->
            <el-radio-group v-if="item.type==='RadioButton'" v-model="dialogData[item.prop]" @change="item.change && item.change(dialogData[item.prop])" :disabled="item.disabled || false">
                <el-radio-button v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio-button>
            </el-radio-group>
            <!-- 复选框 -->
            <el-checkbox-group v-if="item.type==='Checkbox'" v-model="dialogData[item.prop]" :disabled="item.disabled || false">
                <el-checkbox v-for="ch in item.options" :label="ch.value" :key="ch.value"  @change="item.change(dialogData[item.prop])" >{{ch.label}}</el-checkbox>
            </el-checkbox-group>
            <!-- 日期 -->
            <el-date-picker v-if="item.type==='Date'" v-model="dialogData[item.prop]" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :disabled="item.disabled || false"></el-date-picker>
            <!-- 时间 -->
            <el-time-select v-if="item.type==='Time'"  arrow-control value-format='HH:mm' :picker-options="{ start: '05:00', step: '00:05', end: '23:30'}" 
            v-model="dialogData[item.prop]"  :disabled="item.disabled || false"></el-time-select>
            <!-- 日期时间 -->
            <el-date-picker v-if="item.type==='DateTime'" type='datetime' format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" v-model="dialogData[item.prop]" :disabled="item.disable && item.disable(dialogData[item.prop])"></el-date-picker>
            <!-- 滑块 -->
            <!-- <el-slider v-if="item.type==='Slider'" v-model="dialogData[item.prop]"></el-slider> -->
            <!-- 开关 -->
            <el-switch v-if="item.type==='Switch'" v-model="dialogData[item.prop]" :disabled="item.disabled || false"></el-switch>
            <!-- 文件 -->
            <el-upload  v-if="item.type==='UploadFile'" 
                class="upload-demo"
                :action="item.action"
                :on-preview="handlePreview"
                :on-success="item.onSuccess"
                :on-remove="handleRemove"
                :before-remove="beforeRemove"
                 multiple
                :limit="1"
                :on-exceed="handleExceed"
                :file-list="item.fileList">
                <el-button size="small" type="primary">点击上传</el-button>
                <span v-if="dialogData[item.prop]">{{dialogData[item.prop].slice(dialogData[item.prop].indexOf('/uploads/')+18)|| ''}}</span>
            </el-upload>
            <!-- 图片展示上传 -->
            <el-upload  v-if="item.type==='UploadImg'"
                class="avatar-uploader"
                :action="item.action"
                :show-file-list="false"
                :on-success="item.onSuccess"
                :before-upload="beforeAvatarUpload"
            >
            <img v-if="dialogData[item.prop]" :src="dialogData[item.prop]" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-button  v-if="item.type==='Button'" type="text"  @click="item.change(dialogData)"> <i :class="item.class">{{item.text}}</i></el-button>
        </el-form-item>
        <slot name="content"></slot>
        <el-form-item class="dialog-footer">
            <el-button v-for="btnItem in dialogBtns" :key="btnItem.label"
                :type="btnItem.type || 'primary'"
                style="background-color:#2EA39D;border-color:#fff"
                :size="btnItem.size || 'medium'"
                @click="btnItem.handle(dialogData)">{{btnItem.label}}
            </el-button>
        </el-form-item>
    </el-form>
   
   
   </el-dialog>
</div>
</template>

<script>
export default {
    props:{
        dialogFormVisible:{
            type:Boolean,
            default:true
        },
        dialogLabelWidth:{
            type:String,
            default:'100px'
        },
        dialogRef:{
            type:String,
            default:'ruleForm'
        },
        dialogWidth:{
            type:String,
            default:'60%'
        },
        size:{
            type:String,
            default:'medium'
        },
        rules:{   
            type:Object,
            default:{}
        },
        dialogForm:{  //表单框类型数组
            type:Array,
            default:[]
        },
        dialogData:{     //表单数据
            type:Object,
            default:{}
        },
        title:{     //表单数据
            type:String,
            default:'操作'
        },
        dialogBtns:{
            type:Array,
            default:[]
        }
    },
    data () {
        return {
        };
    },
    methods:{
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file,111);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
    //  图片上传限制
       beforeAvatarUpload(file) {
        // const isJPG = file.type === 'image/jpeg';
        // const isLt2M = file.size / 1024 / 1024 < 2;

        // if (!isJPG) {
        //   this.$message.error('上传头像图片只能是 JPG 格式!');
        // }
        // if (!isLt2M) {
        //   this.$message.error('上传头像图片大小不能超过 2MB!');
        // }
        // return isJPG && isLt2M;
      }
    }

}

</script>
<style scoped lang='scss'>
.dialog-footer{
    padding: 40px 0 0 0;
    width: 100%;
    text-align: center;
}
/deep/.el-form-item__content{
    width:70%!important;
}
/deep/.el-date-editor.el-input, .el-date-editor.el-input__inner{
    width: 100%!important;
}
/deep/.el-dialog__header{
    background-color: #2EA39D;
    text-align: center;
}
/deep/.el-dialog__title{
    color: #fff!important;
    font-weight: 550;
    font-size: 22px;
}
 /deep/.el-select{
      width: 100%!important;
    }

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
  }
  .avatar {
    width: 100px;
    height: 100px;
    display: block;
  }
</style>
