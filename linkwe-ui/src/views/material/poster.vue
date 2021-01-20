<script>
import {
  getList,
  getPosterInfo,
  addPoster,
  updatePoster,
  removePoster,
} from '@/api/material/poster.js'
import MaPage from '@/views/material/components/MaPage'

// Load Style Code
import "tui-image-editor/dist/tui-image-editor.css";
import "tui-color-picker/dist/tui-color-picker.css";

import PosterPage from "./components/PosterPage.vue";
import bgpng from "@/assets/poster/img/bg.png";

var locale_ru_RU = {
  "DeleteAll": "全部清空",
  "Delete": "删除元素",
  "Undo": "后退",
  "Redo": "前进"
};

export default {
  name: 'Poster',
  components: { 
    MaPage,
    "tui-image-editor": PosterPage
  },
  data () {
    return {
      imgList: {},
      dialog: {
        preview: false, // 预览弹出显示隐藏
        edit: false // 编辑弹出显示隐藏
      },
      posterEdit: {
        step: 0
      },
      posterForm: {
        title: '', // 海报名称
        categoryId: '', // 所属分类
        type: '1', // 海报类型
        // content: '', // 内容
        // count: '', // 虚拟次数
        // sort: '', // 海报排序
        // jump: [], // 跳转页面
        delFlag: 0 // 是否启用
      },
      rules: {
        title: {
          required: true,
          message: '请输入海报名称',
          trigger: 'blur'
        },
        categoryId: {
          required: true,
          message: '请选择分类'
        },
        type: {
          required: true,
          message: '请选择海报类型'
        },
        delFlag: {
          required: true,
          message: '请选择是否启用'
        }
      },
      srcList: [],
      ids: [], // 选中数组
      previewImg: '', // 预览图片地址
      useDefaultUI: true,
      options: {
        includeUI: {
          // initMenu: "text",
          locale: locale_ru_RU,
          // loadImage: {
          //   path: 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.gtimg.com%2Fsports%2Fpics%2Fhv1%2F171%2F106%2F1472%2F95744001.jpg&refer=http%3A%2F%2Fimg1.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612444990&t=6589254fe9669cc6a45fd3688f269612',
          //   name: "posterImage"
          // },
          usageStatistics: false,
          menuBarPosition: "right",
          menu: ['text'],  // FIXME 因为借用了CANVAS的UI  所以需要使用TEXT，需要额外注释,后面创建自己的UI在去掉
          theme: {
            "common.bi.image": "",
            "common.bisize.width": "251px",
            "common.bisize.height": "21px",
            "common.backgroundImage": bgpng,
            "common.backgroundColor": "#fff",
            "common.border": "1px solid #c1c1c1",

            // header
            'header.backgroundImage': 'none',
            'header.backgroundColor': 'transparent',
            'header.border': '0px',

            // load button
            'loadButton.backgroundColor': '#fff',
            'loadButton.border': '1px solid #ddd',
            'loadButton.color': '#222',
            'loadButton.fontFamily': "'Noto Sans', sans-serif",
            'loadButton.fontSize': '12px',

            // download button
            'downloadButton.backgroundColor': '#fdba3b',
            'downloadButton.border': '1px solid #fdba3b',
            'downloadButton.color': '#fff',
            'downloadButton.fontFamily': "'Noto Sans', sans-serif",
            'downloadButton.fontSize': '12px',

            // main icons
            'menu.normalIcon.color': '#8a8a8a',
            'menu.activeIcon.color': '#555555',
            'menu.disabledIcon.color': '#434343',
            'menu.hoverIcon.color': '#e9e9e9',
            'menu.iconSize.width': '24px',
            'menu.iconSize.height': '24px',

            // colorpicker style
            'colorpicker.button.border': '1px solid #1e1e1e',
            'colorpicker.title.color': '#fff',
          }
          // {
          //   "common.bi.image": "",
          //   "common.bisize.width": "251px",
          //   "common.bisize.height": "21px",
          //   "common.backgroundImage": bgpng,
          //   "common.backgroundColor": "#fff",
          //   "common.border": "1px solid #c1c1c1",

          //   // header
          //   "header.backgroundImage": "none",
          //   "header.backgroundColor": "transparent",
          //   "header.border": "0px",

          //   // main icons
          //   "menu.iconSize.width": "34px",
          //   "menu.iconSize.height": "34px",
          // }
        }
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
      console.log(url)
      console.log('preview', url)
      this.previewImg = url || ''
      this.dialog.preview = true
    },
    async edit (item) {
      try {
        const res = await getPosterInfo(item.id)
        const data = res.data || {}
        console.log('getPosterInfo',data)
        this.posterForm = {
          id: data.id,
          title: data.title,
          categoryId: data.categoryId,
          type: data.type,
          delFlag: data.delFlag
        }
        this.posterEdit.step = 0
        this.dialog.edit = true
      } catch (error) {
        console.log(error)
      }
    },
    ready () {
      console.log('ready')
    },
    onAddText(pos) {
      this.$refs.tuiImageEditor.editorInstance
      .addText('双击输入文字', {
        position: pos.originPosition,
      })
      .then(function (objectProps) {
        console.log(objectProps);
      });
    },
    //移动
    onObjectMoved(res) {
      console.log('onObjectMoved')
      this.getRecord(res)
    },
    //新增/选中
    objectActivated(obj) {
      console.log('objectActivated')
      var imageEditor = this.$refs.tuiImageEditor;
      imageEditor.activeObjectId = obj.id;
      if (obj.type === 'text') {
        imageEditor.showSubMenu('text');
        imageEditor.setTextToolbar(obj);
        imageEditor.activateTextMode();
      }
    },
    //缩放
    onObjectScaled(obj) {
      console.log('onObjectScaled')
      // this.getRecord(res);
      // this.checkState();
      if (obj.type === 'text') {
        this.$refs.tuiImageEditor.inputFontSizeRange.setAttribute('value',obj.fontSize);        
      }
    },
    //重做
    onRedoStackChanged(length) {
      if (length) {
        this.$refs.tuiImageEditor.btn_redo.remove('disabled');
      } else {
        this.$refs.tuiImageEditor.btn_redo.add('disabled');
      }
      this.$refs.tuiImageEditor.resizeEditor();
    },
    onUndoStackChanged(length) {
      if (length) {
        this.$refs.tuiImageEditor.btn_undo.classList.remove('disabled')
      } else {
        this.$refs.tuiImageEditor.btn_undo.classList.add('disabled')
      }
      this.$refs.tuiImageEditor.resizeEditor();
    },
    showSubMenu (type) {
      switch (type) {
        case 'text':
          document.getElementsByClassName('tui-image-editor-submenu')[0].display = 'block';
          break;
        default:
          document.getElementsByClassName('tui-image-editor-submenu')[0].display = 'none';
      }
    },
    activateTextMode () {
      let imageEditor = this.$refs.tuiImageEditor.editorInstance;
      if (imageEditor.getDrawingMode() !== 'TEXT') {
        imageEditor.stopDrawingMode();
        imageEditor.startDrawingMode('TEXT');
      }
    },
    checkState (obj) {
      switch (obj.type) {
        case 'text':
          this.showSubMenu('text');
          this.activateTextMode();
        break;
        default:
          this.activateImageMode();
        break;
      }
    },
    activateImageMode () {
      let imageEditor = this.$refs.tuiImageEditor.editorInstance;
      imageEditor.stopDrawingMode();
    },
    toNextStep () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.posterEdit.step = 1
        } else {
          return false;
        }
      })
    },
    toPrevStep () {
      this.posterEdit.step = 0
    },
    beforeCloseDialog () {
      this.$refs.form.resetFields()
      this.posterForm.id = undefined
      this.dialog.edit = false
    },
    remove (id) {
      this.$confirm('是否确认删除吗?', '警告', {
        type: 'warning',
      })
        .then(function() {
          return removePoster(id)
        })
        .then(() => {
          this.$refs.page.getList(1)
          this.msgSuccess('删除成功')
        })
    },
    inputFontSizeRangeChange (e) {
      this.$refs.tuiImageEditor.inputFontSizeRangeChange(e);
    },
    getRecord(res){
      var flag = false;
      if (this.records && this.records.length) {
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
      } else {
        this.records = [];
      }
      if(!flag){
        this.records.push(res)
      }
    },
    // FIXME 没写完
    async reShow (list) {
      list = JSON.parse('[{"id":2,"type":"i-text","left":55.26947693665858,"top":180.32266601562497,"width":200,"height":45.199999999999996,"fill":"#000000","stroke":null,"strokeWidth":1,"opacity":1,"angle":0,"text":"请输入文字","fontFamily":"Times New Roman","fontSize":40,"fontStyle":"normal","textAlign":"left","fontWeight":"normal"},{"id":3,"type":"image","left":195.71660545226203,"top":506.3232314730081,"width":500,"height":500,"fill":"rgb(0,0,0)","stroke":null,"strokeWidth":0,"opacity":1,"angle":0},{"id":4,"type":"image","left":373.102002853498,"top":190.44302690999768,"width":792,"height":792,"fill":"rgb(0,0,0)","stroke":null,"strokeWidth":0,"opacity":1,"angle":0}]');
      let i = 0, len = list && list.length || 0
      for ( ; i<len; i++) {
        await this.loadRes(list[i]);
      }
    },
    async loadRes (obj) {
      return new Promise((resolve) => {
        let imageEditor = this.$refs.tuiImageEditor.editorInstance;
        switch (obj.type) {
          case 'i-text':
          case 'text':
            imageEditor.
            addText(obj.text, {
                position: {
                  x: obj.left,
                  y: obj.right
                },
            })
            .then(function (objectProps) {
                console.log(objectProps);
            });
          break;
          case 'image':
            this.editorInstance.addImageObject(imgPath).then(objectProps => {
                console.log(objectProps.id);
            }).then(function (objectProps) {
                console.log(objectProps);
            });
          break;
        }
      });
    },
    //
    async save() {
      let list =[];
      try {
        let imageEditor = this.$refs.tuiImageEditor;
        
        let deleteId = [];
        imageEditor.editorInstance._invoker._undoStack.forEach(element => {
          if(element.name == "removeObject"){
              deleteId.push(element.args[1])
          }
        })
        Object.values(imageEditor.records).forEach(item => {
          if(deleteId.indexOf((item.id).toString())>=0){
              console.log(" ")
          }else{
            list.push(item)
          }
        });
      
        // const image = this.$refs.tuiImageEditor.editorInstance.toDataURL();
        // res.url = image;
      } catch (error) {
        console.log(error)
      }
      
      // TODO 塞新建海报的数据 
      let posterSubList = [];
      let i = 0, len = list.length;
      while (i < len) {
        let vo = list[i];
        let type = list[i].type;
        let align = vo.textAlign && 
                    (vo.textAlign === 'left' ? 1 : vo.textAlign === 'center' ? 2 : 3) 
                    || null; 
        let isText = type === 'i-text' || type === 'text';
        let posData = {
          id: vo.randomId || i,
          content: vo.text || '',   // 文本内容 
          delFlag: 1,   // 1 启动  0 删除      FIXME 暂时写死
          fontColor: vo.fill || '#000000',
          fontId: isText ? i : null,   // 字体ID   与imgPath互斥
          fontSize: vo.fontSize,
          fontTextAlign: align, // 1 2 3  left center right
          left: vo.left,
          top: vo.top,
          width: vo.width,
          height: vo.height,
          imgPath: vo.url || '',
          posterId: null,
          type: isText ? 1 : vo.objType,   //  1 固定文本 2 固定图片 3 二维码图片
          alpha: vo.opacity,
          fontStyle: (vo.italic && vo.bold) ? 3 : vo.italic ? 2 : vo.bold ? 1 : 0, // 0 通常 1 粗体 2 斜体 3 粗 + 斜
          rotate: vo.angle,
          order: i,  // 层级
          categoryId: 0, // 分类ID
          verticalType: 2 // 居中方式 写死2
        };
        posterSubList.push(posData);
        i++;
      }

      const posterForm = this.posterForm
      console.log('save', posterForm.id)
      let res = {}
      if (posterForm.id) {
        // 编辑海报
        res = await updatePoster(Object.assign({}, {
          backgroundImgPath: 'http://106.13.236.58:8080/profile/2020/12/31/5ca3fa0b-55d7-433c-ba02-61d4502e45e1.jpg',
          posterSubassemblyList: []
        }, this.posterForm))
      } else {
        // 新建海报
        res = await addPoster(Object.assign({}, {
          backgroundImgPath: 'http://106.13.236.58:8080/profile/2020/12/31/5ca3fa0b-55d7-433c-ba02-61d4502e45e1.jpg',
          posterSubassemblyList: posterSubList
        }, this.posterForm))
      }
      if (res.code === 200) {
        this.msgSuccess(res.msg)
        this.$refs.page.getList(1)
        this.beforeCloseDialog()
      }
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
                @click="remove(item.id)"
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
       <img class="preview-img" :src="previewImg" />
    </el-dialog>
    <el-dialog title="海报编辑" width="80%" :visible.sync="dialog.edit" :before-close="beforeCloseDialog">
      <div class="poster-edit-dialog">
        <el-steps :active="posterEdit.step" simple finish-status="success">
          <el-step title="基本信息编辑"></el-step>
          <el-step title="界面元素及内容编辑"></el-step>
        </el-steps>
        <br />
        <div v-show="posterEdit.step === 0">
          <el-form ref="form" :rules="rules" :model="posterForm" label-width="120px">
            <el-form-item label="海报名称" prop="title">
              <el-input
                v-model="posterForm.title"
                maxlength="10"
                show-word-limit
              ></el-input>
            </el-form-item>
            <el-form-item label="所属分类" prop="categoryId">
              <el-cascader
                v-model="posterForm.categoryId"
                :options="$refs.page.treeData[0].children"
                :props="$refs.page.groupProps"
              ></el-cascader>
              <!-- <el-select
                v-model="posterForm.categoryId"
                placeholder="请选择分类"
              >
                <el-option label="海报一" value="1"></el-option>
                <el-option label="海报二" value="2"></el-option>
              </el-select> -->
            </el-form-item>
            <!-- <el-form-item label="所属二级分类">
              <el-select
                v-model="posterForm.classifySecond"
                placeholder="请选择分类"
              >
                <el-option label="海报一" value="poster1"></el-option>
                <el-option label="海报二" value="poster2"></el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="海报类型" prop="type">
              <el-radio-group v-model="posterForm.type">
                <el-radio label="1">通用海报</el-radio>
                <!-- <el-radio label="1">名片海报</el-radio>
                <el-radio label="2">专属海报</el-radio> -->
                <!-- <el-radio label="4">案例海报</el-radio>
                <el-radio label="5">产品海报</el-radio> -->
              </el-radio-group>
            </el-form-item>
            <!-- <el-form-item
              :label="`${posterForm.type === '4' ? '案例' : '产品'}内容`"
              v-if="posterForm.type === '4' || posterForm.type === '5'"
            >
              <el-input v-model="posterForm.content"></el-input>
            </el-form-item> -->
            <!-- <el-form-item label="虚拟次数">
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
            </el-form-item> -->
            <el-form-item label="是否启用" prop="delFlag">
              <el-radio-group v-model="posterForm.delFlag">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="toNextStep"
                >前往设计海报</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-show="posterEdit.step === 1">
          <div class="imageEditorApp">
            <tui-image-editor
              ref="tuiImageEditor"
              :include-ui="useDefaultUI"
              :options="options"
            ></tui-image-editor>
          </div>
          <div id="tbody-containerui-image-editor-controls">
            <ul class="menu">
              <li class="menu-item" id="btn-text">添加自定义文本</li>
              <li class="menu-item" id="btn-image">添加图片</li>
              <li class="menu-item" id="btn-qrCode">添加二维码</li>
              <!-- <li class="menu-item" id="btn-nickName">添加客户昵称</li> -->
            </ul>
            <div class="sub-menu-container" id="text-sub-menu">
              <ul class="menu">
                <li class="menu-item">
                  <div>
                    <button class="btn-text-style" data-style-type="b">Bold</button>
                    <button class="btn-text-style" data-style-type="i">Italic</button>
                    <button class="btn-text-style" data-style-type="u">Underline</button>
                  </div>
                  <div>
                    <button class="btn-text-style" data-style-type="l">Left</button>
                    <button class="btn-text-style" data-style-type="c">Center</button>
                    <button class="btn-text-style" data-style-type="r">Right</button>
                  </div>
                </li>
                <li class="menu-item">
                  <label class="no-pointer">
                    <input id="input-font-size-range" @change="inputFontSizeRangeChange($event)" type="range" min="10" max="100" value="10" />
                  </label>
                </li>
                <li class="menu-item">
                  <div id="tui-text-color-picker">Text color</div>
                </li>
                <li class="menu-item close">Close</li>
              </ul>
            </div>
          </div>
          <el-button type="success" @click="save"
            >保存</el-button>
          <el-button @click="toPrevStep">返回上一步</el-button>
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
  width: 50%;
  height: 700px;
  // float: left;
  position: relative;
}
#tbody-containerui-image-editor-controls {
  float: right;
  width: 50%;
  position: relative;
  margin-top: -700px;
}
.tui-image-editor-header-logo {
  display: none !important;
  border: 1px solid red;
}
.tui-image-editor-download-btn {
  display: none !important;
}

.border {
  border: 1px solid black;
}
.body-container {
  width: 1000px;
}
.tui-image-editor-controls {
  min-height: 250px;
}
.menu {
  padding: 0;
  margin-bottom: 5px;
  text-align: center;
  color: #544b61;
  font-weight: 400;
  list-style-type: none;
  user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  -webkit-user-select: none;
}
.menu-item {
  padding: 10px;
  display: inline-block;
  cursor: pointer;
  vertical-align: middle;
}
.menu-item a {
  text-decoration: none;
}
.menu-item.no-pointer {
  cursor: default;
}
.menu-item.active,
.menu-item:hover {
  background-color: #f3f3f3;
}
.menu-item.disabled {
  cursor: default;
  color: #bfbebe;
}
.align-left-top {
  text-align: left;
  vertical-align: top;
}
.range-narrow {
  width: 80px;
}
.sub-menu-container {
  font-size: 14px;
  margin-bottom: 1em;
  display: none;
}
/* .tui-image-editor {
  height: 500px;
}
.tui-image-editor-canvas-container {
  margin: 0 auto;
  top: 50%;
  transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  -moz-transform: translateY(-50%);
  -webkit-transform: translateY(-50%);
  border: 1px dashed black;
  overflow: hidden;
}
.tui-colorpicker-container {
  margin: 5px auto 0;
}
.tui-colorpicker-palette-toggle-slider {
  display: none;
} */
.input-wrapper {
  position: relative;
}
.input-wrapper input {
  cursor: pointer;
  position: absolute;
  font-size: 999px;
  left: 0;
  top: 0;
  opacity: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.btn-text-style {
  padding: 5px;
  margin: 3px 1px;
  border: 1px dashed #bfbebe;
  outline: 0;
  background-color: #eee;
  cursor: pointer;
}
.icon-text {
  font-size: 20px;
}
.select-line-type {
  outline: 0;
  vertical-align: middle;
}
#tui-color-picker {
  display: inline-block;
  vertical-align: middle;
}
#tui-text-palette {
  display: none;
  position: absolute;
  padding: 10px;
  border: 1px solid #bfbebe;
  background-color: #fff;
  z-index: 9999;
}
</style>
