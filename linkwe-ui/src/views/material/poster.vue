<script>
import MaPage from '@/views/material/components/MaPage'

// Load Style Code
import "tui-image-editor/dist/tui-image-editor.css";
import "tui-color-picker/dist/tui-color-picker.css";

import PosterPage from "./components/PosterPage.vue";
import bgpng from "@/assets/poster/img/bg.png";

var locale_ru_RU = {
  "Text": "文本",
  "Mask": "遮罩",
  "Delete-all": "全部清空"
};

export default {
  name: 'Poster',
  components: { 
    MaPage,
    "tui-image-editor": PosterPage
  },
  data () {
    return {
      dialog: {
        preview: false, // 预览弹出显示隐藏
        edit: false // 编辑弹出显示隐藏
      },
      posterEdit: {
        step: 0
      },
      posterForm: {
        name: '', // 海报名称
        classifyFirst: '', // 所属分类
        classifySecond: '', // 所属二级分类
        type: '', // 海报类型
        content: '', // 内容
        count: '', // 虚拟次数
        sort: '', // 海报排序
        jump: [], // 跳转页面
        switch: '0' // 是否启用
      },
      srcList: [],
      ids: [], // 选中数组
      previewImg: '', // 预览图片地址
      useDefaultUI: true,
      options: {
        includeUI: {
          // initMenu: "text",
          locale: locale_ru_RU,
          loadImage: {
            path: 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.gtimg.com%2Fsports%2Fpics%2Fhv1%2F171%2F106%2F1472%2F95744001.jpg&refer=http%3A%2F%2Fimg1.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612444990&t=6589254fe9669cc6a45fd3688f269612',
            name: "posterImage"
          },
          usageStatistics: false,
          menuBarPosition: "top",
          menu: ["text", "mask"],
          theme: {
            "common.bi.image": "",
            "common.bisize.width": "251px",
            "common.bisize.height": "21px",
            "common.backgroundImage": bgpng,
            "common.backgroundColor": "#fff",
            "common.border": "1px solid #c1c1c1",

            // header
            "header.backgroundImage": "none",
            "header.backgroundColor": "transparent",
            "header.border": "0px",

            // main icons
            "menu.iconSize.width": "24px",
            "menu.iconSize.height": "24px",

            // submenu primary color
            "submenu.backgroundColor": "#ffffffCC",
            "submenu.partition.color": "#858585",

            // submenu icons
            "submenu.iconSize.width": "32px",
            "submenu.iconSize.height": "32px",

            // submenu labels
            "submenu.normalLabel.color": "#858585",
            "submenu.normalLabel.fontWeight": "normal",
            "submenu.activeLabel.color": "#000",
            "submenu.activeLabel.fontWeight": "normal",

            // checkbox style
            "checkbox.border": "1px solid #ccc",
            "checkbox.backgroundColor": "#fff",

            // rango style
            "range.pointer.color": "#333",
            "range.bar.color": "#ccc",
            "range.subbar.color": "#606060",

            "range.disabledPointer.color": "#d3d3d3",
            "range.disabledBar.color": "rgba(85,85,85,0.06)",
            "range.disabledSubbar.color": "rgba(51,51,51,0.2)",

            "range.value.color": "#000",
            "range.value.fontWeight": "normal",
            "range.value.fontSize": "11px",
            "range.value.border": "0",
            "range.value.backgroundColor": "#f5f5f5",
            "range.title.color": "#000",
            "range.title.fontWeight": "lighter",

            // colorpicker style
            "colorpicker.button.border": "0px",
            "colorpicker.title.color": "#000"
          }
        },
        // cssMaxWidth: 640,
        // cssMaxHeight: 1136
      }
    }
  },
  watch: {},
  created () {
    console.log('created')
  },
  methods: {
    listChange (data) {
      console.log('listChange', JSON.stringify(data))
      this.srcList = data.map((item) => item.materialUrl)
    },
    preview (url) {
      console.log('preview', url)
      this.previewImg = url || ''
      this.dialog.preview = true
    },
    edit (item) {
      console.log('edit', JSON.stringify(item))
      this.posterEdit.step = 0
      this.dialog.edit = true
    },
    ready () {
      console.log('ready')
    },

    onAddText(res) {
      console.log("RES : ", res);
    },
    //移动
    onObjectMoved(res) {
      console.log('onObjectMoved')
      this.getRecord(res)
    },
    //新增/选中
    objectActivated(res) {
      console.log('objectActivated')
      this.getRecord(res)
    },
    //缩放
    onObjectScaled(res) {
      console.log('onObjectScaled')
      this.getRecord(res)
    },
    //重做
    onRedoStackChanged(res) {
      console.log("RES : ", res);
    },
    onUndoStackChanged(res) {
      if(res == 0){
        this.records = []
      }
    },
    getRecord(res){
    var flag = false;
      for (let index = 0; index < this.records.length; index++) {
        const element ={
          fill: this.records[index].fill,
          height: this.records[index].height,
          id: this.records[index].id,
          left: this.records[index].left,
          opacity: this.records[index].opacity,
          stroke: this.records[index].stroke,
          strokeWidth: this.records[index].strokeWidth,
          top: this.records[index].top,
          type: this.records[index].type,
          width: this.records[index].width,
        };
        if(element.id == res.id){
          // console.log(element)
          this.records[index] = res;
          flag = true;
        }
      }
      if(!flag){
        this.records.push(res)
      }
    },
    //
    save() {
      let res = {};
      var list =[];
      this.$refs.tuiImageEditor.editorInstance._invoker._undoStack.forEach(element => {
        this.records.forEach(item => {
          if(element.name =='addIcon' && element.undoData.object.__fe_id && item.id == element.undoData.object.__fe_id){
            item.type = element.args[1]
          }
        });
      });
      var deleteId = [];
      this.$refs.tuiImageEditor.editorInstance._invoker._undoStack.forEach(element => {
        if(element.name == "removeObject"){
            deleteId.push(element.args[1])
        }
      })
      this.records.forEach(item => {
        if(deleteId.indexOf((item.id).toString())>=0){
            console.log(" ")
        }else{
          list.push(item)
        }
      });
      //全清除
      this.$refs.tuiImageEditor.editorInstance._invoker._undoStack.forEach(element => {
        if(element.name == "loadImage" || element.name ==  "clearObjects"){
          list = []
        }
      })
      const image = this.$refs.tuiImageEditor.editorInstance.toDataURL();
      res.url = image;
      res.records = list;
      console.log("最后结果：")
      console.log(res)
      window.localStorage.setItem('record',JSON.stringify(list))
    }
  },
}
</script>

<template>
  <MaPage
    ref="page"
    type="5"
    @listChange="listChange"
    :selected="ids"
    v-slot="{ list }"
  >
    <el-row :gutter="20">
      <el-col
        :span="6"
        style="margin-bottom: 24px; min-width: 220px"
        v-for="(item, index) in list"
        :key="index"
      >
        <el-card shadow="hover" body-style="padding: 0px;">
          <div class="img-wrap">
            <el-image
              class="poster-img"
              :src="item.materialUrl"
              fit="contain"
            ></el-image>
            <div class="actions">
              <el-tag
                class="actions-btn"
                type="success"
                size="mini"
                effect="dark"
                @click="preview(item.materialUrl)"
                >预览</el-tag
              >
              <el-tag
                class="actions-btn"
                type="success"
                size="mini"
                effect="dark"
                @click="edit(item)"
                >编辑</el-tag
              >
              <el-tag
                class="actions-btn"
                type="success"
                size="mini"
                effect="dark"
                @click="$refs.page.remove(item.id)"
                >删除</el-tag
              >
            </div>
          </div>
          <div style="padding: 14px">
            <el-checkbox v-model="ids" :label="item.id">{{
              item.materialName
            }}</el-checkbox>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="海报预览" width="30%" :visible.sync="dialog.preview">
      <el-image class="preview-img" :src="previewImg" fit="contain"></el-image>
    </el-dialog>
    <el-dialog title="海报编辑" width="80%" :visible.sync="dialog.edit">
      <div class="poster-edit-dialog">
        <el-steps :active="posterEdit.step" simple finish-status="success">
          <el-step title="基本信息编辑"></el-step>
          <el-step title="界面元素及内容编辑"></el-step>
        </el-steps>
        <br />
        <div v-if="posterEdit.step === 0">
          <el-form ref="form" :model="posterForm" label-width="120px">
            <el-form-item label="海报名称">
              <el-input
                v-model="posterForm.name"
                maxlength="10"
                show-word-limit
              ></el-input>
            </el-form-item>
            <el-form-item label="所属分类">
              <el-select
                v-model="posterForm.classifyFirst"
                placeholder="请选择分类"
              >
                <el-option label="海报一" value="poster1"></el-option>
                <el-option label="海报二" value="poster2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="所属二级分类">
              <el-select
                v-model="posterForm.classifySecond"
                placeholder="请选择分类"
              >
                <el-option label="海报一" value="poster1"></el-option>
                <el-option label="海报二" value="poster2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="海报类型">
              <el-radio-group v-model="posterForm.type">
                <!-- <el-radio label="1">名片海报</el-radio>
                <el-radio label="2">专属海报</el-radio> -->
                <el-radio label="3">通用海报</el-radio>
                <!-- <el-radio label="4">案例海报</el-radio>
                <el-radio label="5">产品海报</el-radio> -->
              </el-radio-group>
            </el-form-item>
            <el-form-item
              :label="`${posterForm.type === '4' ? '案例' : '产品'}内容`"
              v-if="posterForm.type === '4' || posterForm.type === '5'"
            >
              <el-input v-model="posterForm.content"></el-input>
            </el-form-item>
            <el-form-item label="虚拟次数">
              <el-input v-model="posterForm.count" type="number"></el-input>
            </el-form-item>
            <el-form-item label="海报排序">
              <el-input v-model="posterForm.sort" type="number"></el-input>
            </el-form-item>
            <el-form-item label="跳转页面" v-if="posterForm.type === '3'">
              <el-checkbox-group v-model="posterForm.jump">
                <el-checkbox label="首页" value="1"></el-checkbox>
                <el-checkbox label="名片" value="2"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="是否启用">
              <el-radio-group v-model="posterForm.switch">
                <el-radio label="名片海报" value="1">是</el-radio>
                <el-radio label="专属海报" value="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="posterEdit.step = 1"
                >前往设计海报</el-button
              >
              <el-button>返回</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-else>
          <div class="imageEditorApp">
            <tui-image-editor
              ref="tuiImageEditor"
              :include-ui="useDefaultUI"
              :options="options"
              @addText="onAddText"
              @objectMoved="onObjectMoved"
              @objectScaled="onObjectScaled"
              @redoStackChanged="onRedoStackChanged"
              @undoStackChanged="onUndoStackChanged"
              @objectActivated="objectActivated"
            ></tui-image-editor>
          </div>
        </div>
      </div>
    </el-dialog>
  </MaPage>
</template>

<style lang="scss" scoped>
.img-wrap {
  position: relative;
  height: 0;
  padding: 70% 0 0 0;
  border-bottom: 1px solid #e6ebf5;
  &:hover .actions {
    opacity: 1;
  }
}
.actions {
  position: absolute;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-end;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  padding: 10px;
  opacity: 0;
  transition: opacity 0.3s;
  .actions-btn {
    display: inline-block;
    cursor: pointer;
    & + .actions-btn {
      margin-top: 10px;
    }
  }
}
.poster-img {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
}
.preview-img {
  width: 100%;
}
.imageEditorApp {
  width: 1000px;
  height: 800px;
}
.tui-image-editor-header-logo {
  display: none !important;
  border: 1px solid red;
}
.tui-image-editor-download-btn {
  display: none !important;
}
</style>
