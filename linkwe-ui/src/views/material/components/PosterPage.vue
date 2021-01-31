<template>
    <div>
        <div ref="tuiImageEditor" style="width: 100%;height: 100%;"></div>
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
    cssMaxWidth: 640,
    cssMaxHeight: 1136
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
            visible: false
        }
    },
    mounted() {
        let options = editorDefaultOptions;
        if (this.includeUi) {
            options = Object.assign(includeUIOptions, this.options);
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
        this.editorInstance.loadImageFromURL('https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2980445260,41238050&fm=26&gp=0.jpg', 'imagePoster').then(async result => {
            console.log('old : ' + result.oldWidth + ', ' + result.oldHeight);
            console.log('new : ' + result.newWidth + ', ' + result.newHeight);
            // await this.addImage('https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2980445260,41238050&fm=26&gp=0.jpg')
            // await this.addText('aloha');
        });

        // hack UI
        this.editorInstance.events.addText = []
        this.editorInstance._handlers.addText = null;
        this.editorInstance.ui._actions.text.modeChange = function () {}
        document.getElementsByClassName('tui-image-editor-submenu')[0].style.display = 'none';
        document.getElementsByClassName('tie-btn-text')[0].style.display = 'none';

        this.initBtn();
        this.addEventListener();
    },
    destroyed() {
        // Object.keys(this.$listeners).forEach(eventName => {
        //     this.editorInstance.off(eventName);
        // });
        this.editorInstance.destroy();
        this.editorInstance = null;
    },
    methods: {
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
                this.records(objectProps)
                let id = this.GenNonDuplicateID() + '_' + objectProps.id;
                let target = this.$parent.records[objectProps.id];
                target && (target.randomId = id);
            }.bind(this));
        },
        //移动
        onObjectMoved(obj) {
            console.log('onObjectMoved');
            this.getRecord(obj);
        },
        //新增/选中
        objectActivated(obj) {
            console.log('objectActivated');
            this.activeObjectId = obj.id;
            this.getRecord(obj);
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
                this.inputFontSizeRange.setAttribute('value',obj.fontSize);        
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

            this.inputFontSizeRange.setAttribute('value', fontSize);
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
            let value = this.inputFontSizeRange.getAttribute('value');
            if (this.activeObjectId) {
                let size = parseInt(value, 10);
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
            // this.activateImageMode();   
            // this.addImage('https://images.gitee.com/uploads/images/2020/1231/234016_20fdd151_1480777.png',2);
        },
        // 提交选择的图片
        getMaterial(text, image, file) {
            // console.log(image.materialUrl)
            this.activateImageMode();
            this.addImage(image.materialUrl, 2);
            this.visible = false;
        },
        addImage (imgPath, type) {
            if (!imgPath || !imgPath.length){
                throw Error("imgPath can't null")
            }
            this.editorInstance.addImageObject(imgPath).then(objectProps => {
                let id = this.GenNonDuplicateID() + '_' + objectProps.id;
                // this.$parent.imgList[id] = imgPath;  // FIXME
                // console.log(objectProps)
                // console.log(this.$parent.records)
                // let target = this.$parent.records[objectProps.id];
                
                let target = {}
                // if (target) {
                    target.url = imgPath;
                    target.randomId = objectProps.id;
                    target.objType = type;
                // }
                this.$emit('getImageData', target); // 将图片数据传给父组件保存
            });
        },
        addText (text, style = null, position = null) {
            // return new Promise(function(resolve, reject) {
                // !style && (style = {
                //     fill: '#000',
                //     fontSize: 20,
                //     fontWeight: 'bold'
                // })
                // !position && (position = {
                //     x: 10,
                //     y: 10
                // })
                // this.activateTextMode();
                // this.editorInstance.addText( text, {
                //     style: style,
                //     position: position
                // }).then(objectProps => {
                //     console.log(objectProps.id);
                //     resolve(objectProps);
                // });
            // }.bind(this));
            this.editorInstance
            .addText(text || '双击输入文字', {
                position: position,
            })
            .then(function (objectProps) {
                console.log(objectProps);
                let id = this.GenNonDuplicateID() + '_' + objectProps.id;
                let target = this.$parent.records[objectProps.id];
                target && (target.randomId = id);
            });
        },
        activateImageMode () {
            let imageEditor = this.editorInstance;
            imageEditor.stopDrawingMode();
        },
        resizeEditor () {
            let editor = document.getElementsByClassName('tui-image-editor')[0];
            let container = document.getElementsByClassName('tui-image-editor-canvas-container')[0];
            
            editor.style.height = window.getComputedStyle(container).maxHeight;
        },
        getRecord(obj){
            if (!this.records) this.records = {};
            let vo = this.records[obj.id];
            if (vo) {
                vo.left = obj.left;
                vo.top = obj.top;
                vo.width = obj.width;
                vo.height = obj.height;
                vo.angle = obj.angle;
                if (obj.type === 'text' || obj.type === 'i-text') {
                   vo.text = obj.text;
                   vo.fontSize = obj.fontSize;
                }
                this.records[obj.id] = vo;
            } else {
                this.records[obj.id] = obj;
            }
            
        },
        GenNonDuplicateID(randomLength){
            return parseFloat(Math.random().toString().substr(3,randomLength) + Date.now()).toString(36)
        }   
    }
};
</script>
