<template>
    <div ref="tuiImageEditor" style="width: 100%;height: 100%;"></div>
</template>
<script>
import ImageEditor from 'tui-image-editor';
import { setTimeout } from 'timers';

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
        document.getElementsByClassName('tui-image-editor-header')[0].innerHTML = '';
        setTimeout(()=>{
            if(window.localStorage.getItem('record')){
                var list = JSON.parse(window.localStorage.getItem('record'));
                list.forEach(element => {
                    setTimeout(()=>{
                        this.editorInstance.addIcon(element.type, element).then(objectProps=>{
                            console.log(objectProps)
                        }).catch((res)=>{console.log(res)})
                    },500)
                });
            }
            this.editorInstance.addImageObject('https://pics7.baidu.com/feed/3801213fb80e7bec2314d2a77c58683f9a506b2c.jpeg?token=ff070cf4be817f18bbe26476f682d614&s=BD84CB14520A434F063CA4C30300E0B5')
        },2000)
        this.addEventListener();
    },
    destroyed() {
        Object.keys(this.$listeners).forEach(eventName => {
            this.editorInstance.off(eventName);
        });
        this.editorInstance.destroy();
        this.editorInstance = null;
    },
    methods: {
        addEventListener() {
            Object.keys(this.$listeners).forEach(eventName => {
                this.editorInstance.on(eventName, (...args) => this.$emit(eventName, ...args));
            });
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
        }
    }
};
</script>
