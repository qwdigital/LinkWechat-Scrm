<template>
    <div ref="tuiImageEditor" style="width: 100%;height: 100%;"></div>
</template>
<script>
import ImageEditor from 'tui-image-editor';
import { setTimeout } from 'timers';
import colorPicker from 'tui-color-picker';

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
    props: {
        btnContainer: null, // �Դ���ť����
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
    mounted() {
        let options = editorDefaultOptions;
        if (this.includeUi) {
            options = Object.assign(includeUIOptions, this.options);
        }
        this.editorInstance = new ImageEditor(this.$refs.tuiImageEditor, options);
        // document.getElementsByClassName('tui-image-editor-header')[0].innerHTML = '';
       
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
        this.editorInstance.loadImageFromURL('https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.gtimg.com%2Fsports%2Fpics%2Fhv1%2F171%2F106%2F1472%2F95744001.jpg&refer=http%3A%2F%2Fimg1.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612444990&t=6589254fe9669cc6a45fd3688f269612', 'imagePoster').then(async result => {
            console.log('old : ' + result.oldWidth + ', ' + result.oldHeight);
            console.log('new : ' + result.newWidth + ', ' + result.newHeight);
            // await this.addImage('https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2980445260,41238050&fm=26&gp=0.jpg')
            // await this.addText('aloha');
        });

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
            // this.btn_deleteAll = document.getElementsByClassName('tie-btn-deleteAll')[0];
            // this.btn_deleteAll.name = 'deleteAll';
            // this.btn_delete = document.getElementsByClassName('tie-btn-delete')[0];
            // this.btn_delete.name = 'delete';
            // this.btn_reset = document.getElementsByClassName('tie-btn-reset')[0];
            // this.btn_reset.style.display = 'none'
            // this.btn_redo = document.getElementsByClassName('tie-btn-redo')[0];
            // this.btn_redo.name = 'redo';
            // this.btn_undo = document.getElementsByClassName('tie-btn-undo')[0];
            // this.btn_undo.name = 'undo';

            // btns rightBtn
            this.btnText = document.getElementById('btn-text');
            this.btnText.name = 'btnText';
            this.btnImage = document.getElementById('btn-image');
            this.btnImage.name = 'btnImage';

            // Sub menus
            this.displayingSubMenu = null;
            this.textSubMenu = document.getElementById('text-sub-menu');

            // input
            this.btnTextStyle = document.getElementsByClassName('btn-text-style');
            this.btnClose = document.getElementsByClassName('close')[0];
            this.btnClose.name = 'btnClose'

            // input etc
            this.inputFontSizeRange = document.getElementById('input-font-size-range');
            // this.inputBrushWidthRange = document.getElementById('input-brush-width-range');
            // this.inputBrushWidthRange.name = 'inputBrushWidthRange';
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
                this.activeObjectId && this.editorInstance.changeTextStyle(this.activeObjectId, {
                    fill: event.color,
                });
            }.bind(this));
        },
        addEventListener() {
            // this.btn_deleteAll.addEventListener('click', this.mouseClickHandler.bind(this))
            // this.btn_delete.addEventListener('click', this.mouseClickHandler.bind(this))
            // this.btn_redo.addEventListener('click', this.mouseClickHandler.bind(this))
            // this.btn_undo.addEventListener('click', this.mouseClickHandler.bind(this))

            this.btnText.addEventListener('click', this.mouseClickHandler.bind(this))
            this.btnImage.addEventListener('click', this.mouseClickHandler.bind(this))

            // text
            this.btnClose.addEventListener('click', this.mouseClickHandler.bind(this))
            // this.inputBrushWidthRange.addEventListener('onchange', this.mouseClickHandler.bind(this))
            let i = 0,len = this.btnTextStyle.length;
            while (i < len) {
                this.btnTextStyle[i].name = 'textStyle'
                this.btnTextStyle[i].addEventListener('click', this.mouseClickHandler.bind(this))
                i++
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
        },
        onAddText(pos) {
            this.editorInstance
            .addText('双击输入文字', {
                position: pos.originPosition,
            })
            .then(function (objectProps) {
                console.log(objectProps);
            });
        },
        //移动
        onObjectMoved(obj) {
            console.log('onObjectMoved')
            this.getRecord(obj)
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
            } else {
                // 目前就一个TEXT SUB
                this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            }
        },
        //缩放
        onObjectScaled(obj) {
            console.log('onObjectScaled')
            if (obj.type === 'text') {
                this.inputFontSizeRange.setAttribute('value',obj.fontSize);        
            }
        },
        onRedoStackChanged(length) {
            // if (length) {
            //     this.btn_redo.classList.remove('disabled');
            // } else {
            //     this.btn_redo.classList.add('disabled');
            // }
            // this.resizeEditor();
        },
        onUndoStackChanged(length) {
            // if (length) {
            //     this.btn_undo.classList.remove('disabled')
            // } else {
            //     this.btn_undo.classList.add('disabled')
            // }
            // this.resizeEditor();
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
                // case 'inputBrushWidthRange':
                //     this.editorInstance.setBrush({ width: parseInt(this.value, 10) });
                // break;
            }
        },
        clearObjects () {
            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            this.editorInstance.clearObjects();
        },
        clearActivtyObject () {
            this.displayingSubMenu && (this.displayingSubMenu.style.display = 'none');
            this.activeObjectId && this.editorInstance.removeObject(this.activeObjectId);
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
                    break;
                    case 'i':
                        styleObj = { fontStyle: 'italic' };
                    break;
                    case 'u':
                        styleObj = { underline: true };
                    break;
                    case 'l':
                        styleObj = { textAlign: 'left' };
                    break;
                    case 'c':
                        styleObj = { textAlign: 'center' };
                    break;
                    case 'r':
                        styleObj = { textAlign: 'right' };
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
            this.activeObjectId && this.editorInstance.changeTextStyle(this.activeObjectId, {
                fontSize: parseInt(value, 10),
            });
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
            this.activateImageMode();
            this.addImage('https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2980445260,41238050&fm=26&gp=0.jpg');
        },
        addImage (imgPath) {
            if (!imgPath || !imgPath.length){
                throw Error("imgPath can't null")
            }
            // return new Promise(function(resolve, reject) {
                this.editorInstance.addImageObject(imgPath).then(objectProps => {
                    console.log(objectProps.id);
                    // resolve(objectProps);
                });
            // }.bind(this));
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
                if(element.id == obj.id){
                    this.records[index] = obj;
                    flag = true;
                }
                }
            } else {
                this.records = [];
            }
            if(!flag){
                this.records.push(obj)
            }
        }
    }
};
</script>
