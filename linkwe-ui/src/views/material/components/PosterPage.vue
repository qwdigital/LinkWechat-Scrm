<template>
    <div>
        <div ref="tuiImageEditor" style="width: 100%;height: 700px;"></div>
        <select-material ref="selectMaterial" type="1" :showArr="[1]" :visible.sync="visible" @success="getMaterial" />
    </div>
</template>
<script>
import ImageEditor from 'tui-image-editor';
import selectMaterial from '@/components/SelectMaterial'
import { setTimeout } from 'timers';
import colorPicker from 'tui-color-picker';
import qrCodeImage from "@/assets/poster/img/qrCodeImage.png";

const includeUIOptions = {
    includeUI: {
        initMenu: 'filter',
    }
};
const editorDefaultOptions = {
    cssMaxHeight: 700
};
export default {
    name: 'PosterPage',
    components: {
        selectMaterial
    },
    props: {
        btnContainer: null, // 自带按钮容器
        includeUi: {
            type: Boolean,
            default: true
        },
        options: {
            type: Object,
            default() {
                return editorDefaultOptions;
            }
        }
    },
    data() {
        return {
            qrCodeUrl: 'http://www.linkwechat.cn/static/img/qrCodeImage.a6d01316.png',
            visible: false
        }
    },
    mounted() {
        let options = editorDefaultOptions;
        if (this.includeUi) {
            options = Object.assign(includeUIOptions, this.options);
        }
        if (this.editorInstance) {
            console.log('this.editorInstance')
        }
        this.editorInstance = new ImageEditor(this.$refs.tuiImageEditor, options);
        document.getElementsByClassName('tui-image-editor-header')[0].innerHTML = '';
       
        if(window.localStorage.getItem('record')){
            let list = JSON.parse(window.localStorage.getItem('record'));
            list.forEach(element => {
                setTimeout(()=>{
                    this.editorInstance.addIcon(element.type, element).then(objectProps=>{
                        console.log(objectProps)
                    }).catch((res)=>{console.log(res)})
                },500)
            });
        }

        // hack UI
        this.editorInstance.events.addText = []
        this.editorInstance._handlers.addText = null;
        this.editorInstance.ui._actions.text.modeChange = function () {}
        document.getElementsByClassName('tui-image-editor-submenu')[0].style.display = 'none';
        document.getElementsByClassName('tie-btn-text')[0].style.display = 'none';
        document.getElementsByClassName('tui-image-editor-main')[0].style.top = '0';
        document.getElementsByClassName('tui-image-editor-align-wrap')[0].style.verticalAlign = 'top';

        this.initBtn();
        this.addEventListener();
    },
    destroyed() {
        // Object.keys(this.$listeners).forEach(eventName => {
        //     this.editorInstance.off(eventName);
        // });
        console.log('deatory poster')
        this.editorInstance.destroy();
        this.editorInstance = null;
    },
    methods: {
        // 设置背景图片
        getBackgroundUrl(bacUrl, posterSubassemblyList) {
            this.editorInstance.loadImageFromURL(bacUrl, 'imagePoster').then(async result => {
                console.log('old : ' + result.oldWidth + ', ' + result.oldHeight);
                console.log('new : ' + result.newWidth + ', ' + result.newHeight);

                // 由于篡改了tuiimage,第一次使用addText会报错，错误不影响流程，但是会导致promise中断，提前触发报错。
                try {
                    this.editorInstance.
                    addText('', {
                        position: {
                            x: -100,
                            y: -100
                        },
                    })
                    .then(function (objectProps) {
                        console.log('dd');
                    }).catch(()=>{});
                } catch (e) {
                    console.log('addText 内部状态错误')
                }
                // FIXME 暂时延迟回显
                setTimeout(()=>{
                    posterSubassemblyList.length && this.reShowDisplay(posterSubassemblyList);
                }, 500);
            });
        },
        // 设置修改回显
        async reShowDisplay (reShowDisplays) {
            let i = 0,len = reShowDisplays.length;
            let _data = null;
            while (i < len) {
                _data = reShowDisplays[i];
                if (_data) {
                    await this.createReShowDisplay(_data);
                }
                i ++;
            }
            // fixme 有个bug，暂时在添加空的文本重制下状态
            this.editorInstance.
            addText('', {
                position: {
                    x: -100,
                    y: -100
                },
            })

            // 清楚状态
            this.activateImageMode();
        },
        // 1 固定文本 2 固定图片 3 二维码图片
        createReShowDisplay(data) {
            return new Promise((resolve)=>{
                switch (data.type) {
                    case 1:
                        this.showSubMenu('text');
                        this.activateTextMode();
                        
                        this.editorInstance.
                        addText(data.content, {
                            position: {
                                x: data.left,
                                y: data.top
                            },
                        })
                        .then(function (objectProps) {
                            this.activeObjectId = objectProps.id;
                            objectProps.left =      data.left;
                            objectProps.top =       data.top;
                            objectProps.fontSize =  data.fontSize;
                            objectProps.fill =      data.fontColor;
                            objectProps.textAlign = data.textAlign;
                            this.getRecord(objectProps);

                            // 重置 TextStyle
                            this.editorInstance.changeTextStyle(objectProps.id, {
                                fontSize:   data.fontSize,
                                fill:       data.fontColor,
                                textAlign:  data.fontTextAlign === 1 ? 'left' : 
                                            data.fontTextAlign === 2 ? 'center' : 'right'
                            }).then(()=>{
                                resolve();
                            })                    
                        }.bind(this));
                    break;
                    case 2:
                    case 3:
                        this.activateImageMode();
                        
                        this.editorInstance.addImageObject(
                            data.type === 2 ? data.imgPath : 
                            data.imgPath === this.qrCodeUrl ? qrCodeImage : data.imgPath
                        ).then(objectProps => {
                            console.log(objectProps.id);
                            this.activeObjectId = objectProps.id;

                            let target = {}
                            data.displayId =    objectProps.id;
                            target.url =        data.imgPath;
                            target.randomId =   objectProps.id;
                            target.objType =    data.type;

                            this.editorInstance.setObjectProperties(objectProps.id, {
                                left:       data.left + data.width / 2,
                                top:        data.top + data.height / 2
                            }).then(()=>{
                                let obj = {
                                    left:   data.left + data.width / 2,
                                    top:    data.top + data.height / 2,
                                    width:  data.width,
                                    height: data.height,
                                    angle:  data.rotate,
                                    id:     data.displayId
                                }
                                this.getRecord(obj);
                                resolve()
                            })
                            // 将图片数据传给父组件保存
                            this.$emit('getImageData', target);
                            console.log(objectProps);
                        });
                    break;
                }
            });
        },
        initBtn () {

            this.activeObjectId = null;

            // btns leftBtn
            this.btn_deleteAll = document.getElementsByClassName('tie-btn-deleteAll')[0];
            this.btn_deleteAll.name = 'deleteAll';
            this.btn_delete = document.getElementsByClassName('tie-btn-delete')[0];
            this.btn_delete.name = 'delete';
            this.btn_reset = document.getElementsByClassName('tie-btn-reset')[0];
            this.btn_reset.style.display = 'none'
            this.btn_redo = document.getElementsByClassName('tie-btn-redo')[0];
            this.btn_redo.name = 'redo';
            this.btn_undo = document.getElementsByClassName('tie-btn-undo')[0];
            this.btn_undo.name = 'undo';

            // btns rightBtn
            this.btnText = document.getElementById('btn-text');
            this.btnText.name = 'btnText';
            this.btnImage = document.getElementById('btn-image');
            this.btnImage.name = 'btnImage';
            this.btnQrCode = document.getElementById('btn-qrCode');
            this.btnQrCode.name = 'btnQrCode';

            // Sub menus
            this.displayingSubMenu = null;
            this.textSubMenu = document.getElementById('text-sub-menu');

            // input
            this.btnTextStyle = document.getElementsByClassName('btn-text-style');
            this.btnClose = document.getElementsByClassName('close')[0];
            this.btnClose.name = 'btnClose'

            // input etc
            this.inputFontSizeRange = document.getElementById('input-font-size-range');
            this.inputStrokeWidthRange = document.getElementById('input-stroke-width-range');
            this.inputCheckTransparent = document.getElementById('input-check-transparent');
            this.inputCheckGrayscale = document.getElementById('input-check-grayscale');
            this.inputCheckInvert = document.getElementById('input-check-invert');
            this.inputCheckSepia = document.getElementById('input-check-sepia');
            this.inputCheckSepia2 = document.getElementById('input-check-sepia2');
            this.inputCheckSharpen = document.getElementById('input-check-sharpen');
            this.inputCheckEmboss = document.getElementById('input-check-emboss');
            this.inputCheckRemoveWhite = document.getElementById('input-check-remove-white');
            this.inputRangeRemoveWhiteThreshold = document.getElementById('input-range-remove-white-threshold');
            this.inputRangeRemoveWhiteDistance = document.getElementById('input-range-remove-white-distance');

            // Color picker
            this.textColorpicker = colorPicker.create({
                container: document.getElementById('tui-text-color-picker'),
                color: '#000000'
            });
            this.textColorpicker.on('selectColor', function (event) {
                if (this.activeObjectId) {
                    this.editorInstance.changeTextStyle(this.activeObjectId, {
                        fill: event.color,
                    });
                    let vo = this.records[this.activeObjectId];
                    if (vo) {
                        vo.fill = event.color;
                        this.records[this.activeObjectId] = vo;
                    }
                }
            }.bind(this));
        },
        addEventListener() {
            this.btn_deleteAll.addEventListener('click', this.mouseClickHandler.bind(this));
            this.btn_delete.addEventListener('click', this.mouseClickHandler.bind(this));
            this.btn_redo.addEventListener('click', this.mouseClickHandler.bind(this));
            this.btn_undo.addEventListener('click', this.mouseClickHandler.bind(this));

            this.btnText.addEventListener('click', this.mouseClickHandler.bind(this));
            this.btnImage.addEventListener('click', this.mouseClickHandler.bind(this));
            this.btnQrCode.addEventListener('click', this.mouseClickHandler.bind(this));

            // text
            this.btnClose.addEventListener('click', this.mouseClickHandler.bind(this));
            let i = 0,len = this.btnTextStyle.length;
            while (i < len) {
                this.btnTextStyle[i].name = 'textStyle';
                this.btnTextStyle[i].addEventListener('click', this.mouseClickHandler.bind(this));
                i++;
            }
            this.editorInstance.on({
                objectAdded: function (objectProps) {
                    console.info(objectProps);
                },
                undoStackChanged: this.onUndoStackChanged.bind(this),
                redoStackChanged: this.onRedoStackChanged.bind(this),
                objectScaled: this.onObjectScaled.bind(this),
                addText: this.onAddText.bind(this),
                textEditing: this.textEditing.bind(this),
                objectActivated: this.objectActivated.bind(this),
                objectMoved: this.onObjectMoved.bind(this)
            });
            this.btn_deleteAll.addEventListener('click', this.mouseClickHandler.bind(this))
            this.btn_delete.addEventListener('click', this.mouseClickHandler.bind(this))
            this.btn_reset.addEventListener('click', this.mouseClickHandler.bind(this))
            this.btn_redo.addEventListener('click', this.mouseClickHandler.bind(this))
            this.btn_undo.addEventListener('click', this.mouseClickHandler.bind(this))

            this.btnText.addEventListener('click', this.mouseClickHandler.bind(this))
            this.btnImage.addEventListener('click', this.mouseClickHandler.bind(this))

            // text
            this.btnClose.addEventListener('click', this.mouseClickHandler.bind(this))
            // this.inputBrushWidthRange.addEventListener('onchange', this.mouseClickHandler.bind(this))
            i = 0;
            len = this.btnTextStyle.length;
            while (i < len) {
                this.btnTextStyle[i].name = 'textStyle'
                this.btnTextStyle[i].addEventListener('click', this.mouseClickHandler.bind(this))
                i++
            }
        },
        redoHandler () {
            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            if (!this.btn_redo.matches(".disabled")) {
                this.editorInstance.redo();
            }
        },
        undoHandler () {
            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            if (!this.btn_undo.matches(".disabled")) {
                this.editorInstance.undo();
            }
        },
        onAddText(pos) {
            this.editorInstance
            .addText('请输入文字', {
                position: pos.originPosition,
            })
            .then(function (objectProps) {
                console.log(objectProps);
                this.getRecord(objectProps);
            }.bind(this));
        },
        // 文本改变事件
        textEditing (e) {
            console.log('textEditing', e)
        },
        //移动
        onObjectMoved(obj) {
            console.log('onObjectMoved', JSON.stringify(obj));
            this.getRecord(obj);
        },
        //新增/选中
        objectActivated(obj) {
            console.log('objectActivated');
            // 第一次选中tui-image传过来的位置有问题，这里处理一下，如果之前有数据不使用选中的数据
            this.activeObjectId = obj.id;
            this.getRecord(obj, true);
            if (obj.type === 'text' || obj.type === 'i-text') {
                this.showSubMenu('text');
                this.setTextToolbar(obj);
                this.activateTextMode();
            } else {
                // 目前就一个TEXT SUB
                this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            }
        },
        //缩放
        onObjectScaled(obj) {
            console.log('onObjectScaled')
            this.getRecord(obj);
            if (obj.type === 'text' || obj.type === 'i-text') {
                let size = obj.fontSize;
                size = size > 100 ? 100 : size < 10 ? 10 : size;
                // this.editorInstance.changeTextStyle(this.activeObjectId, {
                //     fontSize: size
                // });        
                this.inputFontSizeRange.value = size;
            }
        },
        onRedoStackChanged(length) {
            if (length) {
                this.btn_redo.classList.remove('disabled');
            } else {
                this.btn_redo.classList.add('disabled');
            }
            this.resizeEditor();
        },
        onUndoStackChanged(length) {
            if (length) {
                this.btn_undo.classList.remove('disabled')
            } else {
                this.btn_undo.classList.add('disabled')
            }
            this.resizeEditor();
        },
        mouseClickHandler (event) {
            let name = event.currentTarget.name;
            console.log(name);
            switch (name) {
                case 'btnText':
                    this.showSubMenu('text');
                    this.activateTextMode();
                break;
                case 'btnImage':
                    this.getImages();
                break;
                case 'deleteAll':
                    this.clearObjects();
                break;
                case 'delete':
                    this.clearActivtyObject();
                break;
                case 'redo':
                    this.redoHandler();
                break;
                case 'undo':
                    this.undoHandler();
                break;
                case 'textStyle':
                    this.checkTextStyle(event.currentTarget);
                break;
                case 'btnClose':
                    this.closeHandler();            
                break;
                case 'btnQrCode':
                    this.qrCodeHandler();
                break;  
            }
        },
        qrCodeHandler () {
            // let codeUrl = 'https://images.gitee.com/uploads/images/2020/1231/234016_20fdd151_1480777.png';
            this.activateImageMode();
            // 暂时写死二维码
            this.addImage(qrCodeImage, 3);
        },
        clearObjects () {
            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            this.editorInstance.clearObjects();
        },
        clearActivtyObject () {
            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            this.activeObjectId && this.editorInstance.removeObject(this.activeObjectId);
        },
        closeHandler () {
            this.editorInstance.stopDrawingMode();
            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
        },
        checkTextStyle (target) {
            if (!target) throw Error('textStyle is null')
            if (this.activeObjectId) {
                let styleType = target.getAttribute('data-style-type');
                let styleObj;
                switch (styleType) {
                    case 'b':
                        styleObj = { fontWeight: 'bold' };
                        this.records[this.activeObjectId] && (this.records[this.activeObjectId].bold = true)
                    break;
                    case 'i':
                        styleObj = { fontStyle: 'italic' };
                        this.records[this.activeObjectId] && (this.records[this.activeObjectId].italic = true)
                    break;
                    case 'u':
                        styleObj = { underline: true };     // 后端暂时不支持 
                    break;
                    case 'l':
                        styleObj = { textAlign: 'left' }; 
                        this.records[this.activeObjectId] && (this.records[this.activeObjectId].textAlign = 'left')
                    break;
                    case 'c':
                        styleObj = { textAlign: 'center' };
                        this.records[this.activeObjectId] && (this.records[this.activeObjectId].textAlign = 'center')
                    break;
                    case 'r':
                        styleObj = { textAlign: 'right' };
                        this.records[this.activeObjectId] && (this.records[this.activeObjectId].textAlign = 'right')
                    break;
                    default:
                        styleObj = {};
                }
                
                this.editorInstance.changeTextStyle(this.activeObjectId, styleObj);
            }
        },
        setTextToolbar (obj) {
            let fontSize = obj.fontSize;
            let fontColor = obj.fill;

            this.inputFontSizeRange.value = fontSize;
            this.textColorpicker.setColor(fontColor);
        },
        showSubMenu (type) {
            let submenu = null;
            switch (type) {
                case 'text':
                    submenu = this.textSubMenu;
                break;
                default:
                    submenu = 0;
            }

            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            submenu.style.display = 'block';
            this.displayingSubMenu = submenu;
        },
        inputFontSizeRangeChange () {
            console.log('inputFontSizeRangeChange')
            let value = this.inputFontSizeRange.value;
            if (this.activeObjectId) {
                let size = parseInt(value, 10);
                size = size > 100 ? 100 : size < 10 ? 10 : size;
                this.editorInstance.changeTextStyle(this.activeObjectId, {
                    fontSize: size
                });
                this.records[this.activeObjectId] && (this.records[this.activeObjectId].fontSize = size);
            }
        },
        activateTextMode() {
            if (this.editorInstance.getDrawingMode() !== 'TEXT') {
                this.editorInstance.stopDrawingMode();
                this.editorInstance.startDrawingMode('TEXT');
            }
        },
        getRootElement() {
            return this.$refs.tuiImageEditor;
        },
        invoke(methodName, ...args) {
            console.log('invoke', methodName)
            console.log('invoke', ...args)
            let result = null;
            if (this.editorInstance[methodName]) {
                result = this.editorInstance[methodName](...args);
            } else if (methodName.indexOf('.') > -1) {
                const func = this.getMethod(this.editorInstance, methodName);

                if (typeof func === 'function') {
                    result = func(...args);
                }
            }
            return result;
        },
        getMethod(instance, methodName) {
            const {first, rest} = this.parseDotMethodName(methodName);
            const isInstance = (instance.constructor.name !== 'Object');
            const type = typeof instance[first];
            let obj;

            if (isInstance && type === 'function') {
                obj = instance[first].bind(instance);
            } else {
                obj = instance[first];
            }

            if (rest.length > 0) {
                return this.getMethod(obj, rest);
            }
            return obj;
        },
        parseDotMethodName(methodName) {
            console.log('parseDotMethodName', methodName)
            const firstDotIdx = methodName.indexOf('.');
            let firstMethodName = methodName;
            let restMethodName = '';

            if (firstDotIdx > -1) {
                firstMethodName = methodName.substring(0, firstDotIdx);
                restMethodName = methodName.substring(firstDotIdx + 1, methodName.length);
            }

            return {
                first: firstMethodName,
                rest: restMethodName
            };
        },
        getImages () {
            this.visible = true; // 显示图片列表dialog
        },
        // 提交选择的图片
        getMaterial(text, image, file) {
            this.activateImageMode();
            this.addImage(image.materialUrl, 2);
            this.visible = false;
        },
        addImage (imgPath, type) {
            if (!imgPath || !imgPath.length){
                throw Error("imgPath can't null")
            }
            this.editorInstance.addImageObject(imgPath).then(objectProps => {
                
                let target = {}
                // 二维码是占位符，所以可以写死 
                target.url = type === 3 ? this.qrCodeUrl : imgPath;
                target.randomId = objectProps.id;
                target.objType = type;
                // 将图片数据传给父组件保存
                this.$emit('getImageData', target);
            });
        },
        addText (text, style = null, position = null) {
            this.editorInstance
            .addText(text || '双击输入文字', {
                position: position,
            })
            .then(function (objectProps) {
                console.log(objectProps);
            });
        },
        activateImageMode () {
            this.editorInstance.stopDrawingMode();
        },
        resizeEditor () {
            let editor = document.getElementsByClassName('tui-image-editor')[0];
            let container = document.getElementsByClassName('tui-image-editor-canvas-container')[0];
            
            editor.style.height = window.getComputedStyle(container).maxHeight;
        },
        getRecord(obj, isSelect){
            if (!this.records) this.records = {};
            let vo = this.records[obj.id];

            // 如果之前有数据且是选中传过来的数据，不接收
            if (vo && isSelect) return;

            if (vo) {
                vo.left = obj.left;
                vo.top = obj.top;
                vo.width = obj.width;
                vo.height = obj.height;
                vo.angle = obj.angle;
                if (obj.type === 'text' || obj.type === 'i-text') {
                   vo.text = obj.text;
                   vo.fontSize = obj.fontSize;
                   vo.fill = obj.fontColor,
                   vo.textAlign = obj.textAlign
                }
                this.records[obj.id] = vo;
            } else {
                this.records[obj.id] = obj;
            }
            
        }
        // GenNonDuplicateID(randomLength){
        //     return parseFloat(Math.random().toString().substr(3,randomLength) + Date.now()).toString(36)
        // }   
    }
};
</script>
