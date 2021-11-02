<script>
import { getPosterInfo, addPoster, updatePoster, removePoster } from '@/api/material/poster.js'
import MaPage from '@/views/material/components/MaPage'
import SelectMaterial from '@/components/SelectMaterial'
import { fabric } from 'fabric'

import qrCodeImage from '@/assets/poster/img/qrCodeImage.png'

export default {
  name: 'Poster',
  components: {
    MaPage,
    SelectMaterial
  },
  data() {
    return {
      imgList: {},
      posterSubassemblyList: [],
      dialogVisibleSelectMaterial: false,
      dialog: {
        preview: false, // 预览弹出显示隐藏
        edit: false // 编辑弹出显示隐藏
      },
      rangeErrorMsg: '',
      form: {
        title: '', // 海报名称
        categoryId: '', // 所属分类
        type: '1', // 海报类型
        // content: '', // 内容
        // count: '', // 虚拟次数
        // sort: '', // 海报排序
        // jump: [], // 跳转页面
        delFlag: 0, // 是否启用
        backgroundImgPath: '', // 图片url
        mediaId: '' // 图片id
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
      activeObject: {},
      fontColor: ''
    }
  },
  watch: {
    fontColor(hex) {
      this.setAttr('fill', hex)
    }
  },
  created() {},
  mounted() {},
  methods: {
    listChange(data) {
      this.srcList = data.map((item) => item.materialUrl)
    },
    preview(url) {
      this.previewImg = url || ''
      this.dialog.preview = true
    },
    async edit(item) {
      try {
        if (item) {
          // 编辑
          const res = await getPosterInfo(item.id)
          const data = res.data || {}
          // console.log('getPosterInfo', data)
          this.form = {
            id: data.id,
            title: data.title,
            categoryId: data.categoryId,
            type: data.type,
            delFlag: data.delFlag,
            backgroundImgPath: data.backgroundImgPath,
            posterJSON: JSON.parse(data.otherField)
          }
          this.posterSubassemblyList = data.posterSubassemblyList || []
        } else {
          // 新增，清除编辑的数据
          this.form = {}
          this.posterSubassemblyList = []
        }
        this.dialog.edit = true
        this.initPoster()
      } catch (error) {
        console.log(error)
      }
    },
    // 初始化海报画布
    initPoster() {
      this.$nextTick(() => {
        let canvas = (this.canvas = new fabric.Canvas('canvas'))

        if (this.form.posterJSON) {
          // 加载画布信息
          this.setPosterBackgroundImage()
          canvas.loadFromJSON(this.form.posterJSON, () => {
            canvas.renderAll()
          })
        } else {
          this.setPosterBackgroundImage()
        }

        // 删除某个图层
        var deleteBtn = document.getElementById('deleteBtn')
        function addDeleteBtn(x, y) {
          deleteBtn.style.display = 'none'
          deleteBtn.style.left = x - 10 + 'px'
          deleteBtn.style.top = y - 30 + 'px'
          deleteBtn.style.display = 'block'
        }

        canvas.on('selection:created', function(e) {
          addDeleteBtn(e.target.lineCoords.tr.x, e.target.lineCoords.tr.y)
        })
        canvas.on('selection:updated', function(e) {
          addDeleteBtn(e.target.lineCoords.tr.x, e.target.lineCoords.tr.y)
        })

        //通用事件另外写法
        canvas.on({
          'mouse:down': function(e) {
            if (e.target != undefined) {
              var ob = canvas.getActiveObject()
              if (ob) {
                var i = e.transform.corner
                if (i == 'tr') {
                  this.del()
                }
                ob.set({
                  transparentCorners: false,
                  cornerColor: 'white',
                  cornerStrokeColor: 'blue',
                  borderColor: 'blue',
                  cornerSize: 12,
                  // rotatingPointOffset: 15,
                  padding: 10,
                  cornerStyle: 'circle',
                  borderDashArray: [3, 3],
                  snapAngle: 45, //在45度时自动保持到45的倍数
                  snapThreshold: 5,
                  centeredRotation: true
                })
              }
            }
          }
        })

        //是否拖动
        // let panning = false
        // canvas.on('mouse:down', (e) => {
        //   this.activeObject = canvas.getActiveObject()
        //   if (!this.activeObject) {
        //     deleteBtn.style.display = 'none'
        //   }
        //   //按住alt键可拖动画布
        //   if (e.e.altKey) {
        //     panning = true
        //     canvas.selection = false
        //   }
        // })

        // //鼠标抬起
        // canvas.on('mouse:up', function(e) {
        //   panning = false
        //   canvas.selection = true
        // })

        // //鼠标移动
        // canvas.on('mouse:move', function(e) {
        //   if (panning && e && e.e) {
        //     let delta = new fabric.Point(e.e.movementX, e.e.movementY)
        //     canvas.relativePan(delta)
        //     console.log(e)
        //   }
        // })

        canvas.on('object:modified', function(e) {
          addDeleteBtn(e.target.lineCoords.tr.x, e.target.lineCoords.tr.y)
        })
        canvas.on('object:scaling', function(e) {
          deleteBtn.style.display = 'none'
        })
        canvas.on('object:moving', function(e) {
          deleteBtn.style.display = 'none'
        })
        canvas.on('object:rotating', function(e) {
          deleteBtn.style.display = 'none'
        })
        canvas.on('mouse:wheel', function(e) {
          deleteBtn.style.display = 'none'
        })

        // alt键缩放
        document.getElementById('canvas-wrap').addEventListener('wheel', (e) => {
          // e.stopPropagation()
          if (e.altKey) {
            e.preventDefault()
            console.log(e)
            let zoom = (e.deltaY > 0 ? 0.1 : -0.1) + canvas.getZoom()
            zoom = Math.max(0.1, zoom) //最小为原来的1/10
            zoom = Math.min(5, zoom) //最大是原来的5倍
            let zoomPoint = new fabric.Point(e.offsetX, e.offsetY)
            canvas.zoomToPoint(zoomPoint, zoom)
          }
        })

        deleteBtn.addEventListener('click', () => {
          let activeObject = canvas.getActiveObject()
          if (activeObject) {
            this.$confirm('是否确认删除吗?', '警告', {
              type: 'warning'
            }).then(() => {
              canvas.remove(activeObject)
              this.activeObject = null
              deleteBtn.style.display = 'none'
            })
          }
        })
      })
    },
    // 设置海报背景
    setPosterBackgroundImage() {
      if (!this.form.backgroundImgPath) {
        return
      }
      let canvas = this.canvas
      new fabric.Image.fromURL(this.form.backgroundImgPath, (img) => {
        // img.set({
        //   // 通过scale来设置图片大小，这里设置和画布一样大
        //   // scaleX: canvas.width / img.width,
        //   // scaleY: canvas.height / img.height
        // })
        canvas.setWidth(img.width)
        canvas.setHeight(img.height)
        canvas.setBackgroundImage(img, canvas.renderAll.bind(canvas))
        canvas.renderAll()
      })
    },
    // 设置文本对齐方式
    setAttr(attr, type) {
      this.activeObject.set(attr, type)
      this.canvas.requestRenderAll()
      // this.activeObject.set({
      //   textAlign: type
      // })
    },
    addObj(type, obj) {
      let options = {
        left: 100,
        top: 100,
        borderDashArray: [3, 3],
        customType: { text: 1, image: 2, qrcode: 3 }[type] // 自定义类型 1 固定文本 2 固定图片 3 二维码图片
      }
      switch (type) {
        case 'text':
          let text = new fabric.IText('请输入', options)
          this.canvas.add(text).setActiveObject(text)
          break
        case 'imageShow':
          this.dialogVisibleSelectMaterial = true
          this.isBackgroundImage = false
          break

        case 'image':
        case 'qrcode':
          new fabric.Image.fromURL(type == 'image' ? obj : qrCodeImage, (img) => {
            img.set(options)
            img.scale(this.canvas.width / img.width / 2)
            // img.scaleToWidth(200)
            this.canvas.add(img).setActiveObject(img)
          })
          break

        default:
          break
      }
      this.activeObject = this.canvas.getActiveObject()
    },
    beforeCloseDialog() {
      this.$refs.form.resetFields()
      this.form.id = undefined
      this.form.posterJSON = undefined
      this.dialog.edit = false
    },
    remove(id) {
      this.$confirm('是否确认删除吗?', '警告', {
        type: 'warning'
      })
        .then(function() {
          return removePoster(id)
        })
        .then(() => {
          this.$refs.page.getList(1)
          this.msgSuccess('删除成功')
        })
    },
    submitSelectMaterial(text, image, file) {
      if (this.isBackgroundImage) {
        this.form.mediaId = image.id
        this.form.backgroundImgPath = image.materialUrl
        this.setPosterBackgroundImage()
        this.dialogVisibleSelectMaterial = false
      } else {
        this.addObj('image', image.materialUrl)
      }
    },
    //
    save(isBack) {
      if (!this.form.backgroundImgPath) {
        this.rangeErrorMsg = '请选择背景图片'
        return
      } else {
        this.rangeErrorMsg = ''
      }
      this.$refs.form.validate((valid) => {
        if (valid) {
          let laoding = this.$loading()
          const form = this.form
          form.posterJSON = this.canvas.toJSON(['customType'])
          let list = form.posterJSON.objects
          form.otherField = JSON.stringify(form.posterJSON)
          delete form.posterJSON
          let posterSubList = []
          let vo = null
          let i = 0
          let len = list.length
          while (i < len) {
            vo = list[i]

            let isText = vo.type == 'i-text'

            let align = (vo.textAlign && (vo.textAlign === 'left' ? 1 : vo.textAlign === 'center' ? 2 : 3)) || 1

            let posData = {
              id: null, // 修改的时候后端默认没增删，沟通后让先传null
              content: vo.text || '', // 文本内容
              delFlag: 0, // 1 启动  0 删除       暂时写死
              fontColor: vo.fill || '#000000',
              fontId: null, // TODO 后端让传NULL  isText ? i : null,   // 字体ID   与imgPath互斥
              fontSize: parseInt(vo.fontSize),
              fontTextAlign: align, // 1 2 3  left center right
              left: parseInt(vo.left), // - (isText ? 0 : vo.width >> 1), // 显示偏移了
              top: parseInt(vo.top), // - (isText ? 0 : vo.height >> 1),
              width: parseInt(vo.width + (isText ? vo.fontSize / 2 : 0)),
              height: parseInt(vo.height),
              imgPath: vo.src || '',
              posterId: null,
              type: vo.customType, // 1 固定文本 2 固定图片 3 二维码图片
              // alpha: vo.opacity,                   // 后端暂时不支持
              fontStyle: vo.italic && vo.bold ? 3 : vo.italic ? 2 : vo.bold ? 1 : 0, // 0 通常 1 粗体 2 斜体 3 粗 + 斜
              rotate: vo.angle,
              order: i, // 层级
              // categoryId: 0,                       // 分类ID (不需要传了)
              verticalType: 2 // 居中方式后端让写死2
            }
            posterSubList.push(posData)
            i++
          }

          ;(form.id ? updatePoster : addPoster)(
            Object.assign(
              {
                // backgroundImgPath: 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2980445260,41238050&fm=26&gp=0.jpg',
                posterSubassemblyList: posterSubList
              },
              form
            )
          )
            .then((res) => {
              laoding.close()
              this.msgSuccess(res.msg)
              if (isBack) {
                this.$refs.page.getList(1)
                this.beforeCloseDialog()
              }
            })
            .catch(() => {
              laoding.close()
            })
        }
      })
    }
  }
}
</script>

<template>
  <div>
    <MaPage ref="page" type="5" @listChange="listChange" :selected="ids" v-slot="{ list }">
      <el-row :gutter="20">
        <el-col :span="6" style="margin-bottom: 24px; min-width: 220px" v-for="(item, index) in list" :key="index">
          <el-card shadow="hover" body-style="padding: 0px;">
            <div class="img-wrap">
              <el-image class="poster-img" :src="item.materialUrl" fit="contain"></el-image>
              <div class="actions">
                <el-tag class="actions-btn" type="success" size="mini" effect="dark" @click="preview(item.materialUrl)"
                  >预览</el-tag
                >
                <el-tag class="actions-btn" type="success" size="mini" effect="dark" @click="edit(item)">编辑</el-tag>
                <el-tag class="actions-btn" type="success" size="mini" effect="dark" @click="remove(item.id)"
                  >删除</el-tag
                >
              </div>
            </div>
            <div style="padding: 14px">
              <el-checkbox v-model="ids" :label="item.id">{{ item.materialName }}</el-checkbox>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-dialog title="海报预览" width="50%" :visible.sync="dialog.preview">
        <img class="preview-img" :src="previewImg" />
      </el-dialog>
      <el-dialog
        title="海报编辑"
        width="1180px"
        v-if="dialog.edit"
        :visible.sync="dialog.edit"
        :before-close="beforeCloseDialog"
        :close-on-click-modal="false"
      >
        <div class="poster-edit-dialog fxbw">
          <div style="width: 30%;">
            <el-form ref="form" :rules="rules" :model="form" label-width="120px">
              <el-form-item label="海报名称" prop="title">
                <el-input v-model="form.title" maxlength="10" show-word-limit></el-input>
              </el-form-item>
              <el-form-item label="所属分类" prop="categoryId">
                <el-cascader
                  v-if="$refs.page"
                  v-model="form.categoryId"
                  :options="$refs.page.treeData[0].children"
                  :props="$refs.page.groupProps"
                  style="width: 100%;"
                ></el-cascader>
                <!-- <el-select
                v-model="form.categoryId"
                placeholder="请选择分类"
              >
                <el-option label="海报一" value="1"></el-option>
                <el-option label="海报二" value="2"></el-option>
              </el-select> -->
              </el-form-item>
              <!-- <el-form-item label="所属二级分类">
              <el-select
                v-model="form.classifySecond"
                placeholder="请选择分类"
              >
                <el-option label="海报一" value="poster1"></el-option>
                <el-option label="海报二" value="poster2"></el-option>
              </el-select>
            </el-form-item> -->
              <el-form-item label="海报类型" prop="type">
                <el-radio-group v-model="form.type">
                  <el-radio label="1">通用海报</el-radio>
                  <!-- <el-radio label="1">名片海报</el-radio>
                <el-radio label="2">专属海报</el-radio> -->
                  <!-- <el-radio label="4">案例海报</el-radio>
                <el-radio label="5">产品海报</el-radio> -->
                </el-radio-group>
              </el-form-item>
              <!-- <el-form-item
              :label="`${form.type === '4' ? '案例' : '产品'}内容`"
              v-if="form.type === '4' || form.type === '5'"
            >
              <el-input v-model="form.content"></el-input>
            </el-form-item> -->
              <!-- <el-form-item label="虚拟次数">
              <el-input v-model="form.count" type="number"></el-input>
            </el-form-item>
            <el-form-item label="海报排序">
              <el-input v-model="form.sort" type="number"></el-input>
            </el-form-item>
            <el-form-item label="跳转页面" v-if="form.type === '3'">
              <el-checkbox-group v-model="form.jump">
                <el-checkbox label="首页" value="1"></el-checkbox>
                <el-checkbox label="名片" value="2"></el-checkbox>
              </el-checkbox-group>
            </el-form-item> -->
              <el-form-item label="是否启用" prop="delFlag">
                <el-radio-group v-model="form.delFlag">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="背景图片" :required="true" :error="rangeErrorMsg">
                <div v-if="form.backgroundImgPath">
                  <el-image
                    style="width: 100px; height: 100px; cursor: pointer;border-radius: 6px;"
                    :src="form.backgroundImgPath"
                    fit="fit"
                  >
                  </el-image>
                </div>
                <el-button
                  icon="el-icon-plus"
                  size="mini"
                  @click="isBackgroundImage = dialogVisibleSelectMaterial = true"
                  >选择图片</el-button
                >
              </el-form-item>
              <el-form-item label="">
                <el-button type="success" @click="save()">保存</el-button>
                <el-button type="success" @click="save(1)">保存并关闭</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div style="">
            <div id="canvas-wrap">
              <i class="el-icon-error" id="deleteBtn"></i>
              <canvas id="canvas" width="300" height="500" style="border: 1px solid #ddd;"> </canvas>
            </div>
            <div id="tbody-containerui-image-editor-controls">
              <ul class="menu">
                <li class="menu-item" id="btn-text" @click="addObj('text')">添加自定义文本</li>
                <li class="menu-item" id="btn-image" @click="addObj('imageShow')">添加图片</li>
                <li class="menu-item" id="btn-qrCode" @click="addObj('qrcode')">添加二维码</li>
                <!-- <li class="menu-item" id="btn-nickName">添加客户昵称</li> -->
              </ul>
              <div class="sub-menu-container">
                <ul class="menu" v-show="activeObject && activeObject.type == 'i-text'">
                  <li class="menu-item">
                    <!-- <div>
                      <button class="btn-text-style" data-style-type="b">Bold</button>
                      <button class="btn-text-style" data-style-type="i">Italic</button>
                      <button class="btn-text-style" data-style-type="u">Underline</button>
                    </div> -->
                    <!-- <div> -->
                    <div>
                      <button class="btn-text-style" @click="setAttr('textAlign', 'left')">左对齐</button>
                      <button class="btn-text-style" @click="setAttr('textAlign', 'center')">居中</button>
                      <button class="btn-text-style" @click="setAttr('textAlign', 'right')">右对齐</button>
                    </div>
                  </li>
                  <li class="menu-item">
                    <el-color-picker v-model="fontColor" color-format="hex"></el-color-picker>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </el-dialog>
    </MaPage>
    <!-- 选择素材弹窗 -->
    <SelectMaterial :visible.sync="dialogVisibleSelectMaterial" type="1" :showArr="[1]" @success="submitSelectMaterial">
    </SelectMaterial>
  </div>
</template>

<style lang="scss" scoped>
.el-icon-error {
  position: absolute;
  top: 0px;
  left: 0px;
  cursor: pointer;
  z-index: 9;
  font-size: 24px;
  color: #fff;
  display: none;
}
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
    margin: 10px 0 0 0;
    // & + .actions-btn {
    //   margin: 10px;
    // }
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
#canvas-wrap {
  margin-right: 20px;
  display: inline-block;
  position: relative;
  vertical-align: top;
  overflow: auto;
  max-width: 450px;
  max-height: 700px;
}
#tbody-containerui-image-editor-controls {
  display: inline-block;
  position: relative;
}

.border {
  border: 1px solid black;
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
.sub-menu-container {
  font-size: 14px;
  margin-bottom: 1em;
}

.btn-text-style {
  padding: 5px;
  margin: 3px 1px;
  border: 1px dashed #bfbebe;
  outline: 0;
  background-color: #eee;
  cursor: pointer;
}
</style>
