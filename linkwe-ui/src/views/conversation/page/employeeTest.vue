<template>
    <div class="employ">
        <el-row>
            <el-col :span="6" class="borderR">
                <div class="hd_box">
                    <div class="hd_name">成员（{{employAmount}}）</div>
                    <div class="paddingT10">
                        <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="employName">
                        </el-input>
                    </div>
                </div>
                <div class="ct_box ct_boxFirst">
                    <el-tree class="filter-tree" :data="data" :filter-node-method="filterNode" ref="tree"
                        @node-click="handleNodeClick">
                    </el-tree>
                </div>
            </el-col>
            <el-col :span="6" class="borderR">
                <div class="hd_box">
                    <div class="hd_name">{{talkName}}</div>
                </div>
                <div class="hd_tabs">
                    <el-tabs v-model="activeName">
                        <el-tab-pane label="内部联系人" name="0">
                            <div class="ct_box">
                                <!-- 应该是类似聊天记录的  后期弄成组件 看 -->
                                暂无联系人
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="外部联系人" name="1">
                            <div class="ct_box">
                                <!-- ！ -->
                                暂无联系人
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="群聊" name="2">
                            <div class="ct_box">
                                <!-- ！ -->
                                暂无联系人
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </el-col>
            <el-col :span="12">
                <div class="hd_box">
                    <div class="hd_name hd_nameRi">下载会话</div>
                </div>
                <div class=" hd_tabthree">
                    <el-tabs v-model="activeNameThree">
                        <el-tab-pane label="全部" name="0">
                            <div class="ct_box">
                                <div class="hds_time">
                                    <el-date-picker v-model="takeTime" type="date" placeholder="选择日期">
                                    </el-date-picker>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="图片及视频" name="1">
                            <div class="ct_box">
                                <div class="hds_time">
                                    <el-date-picker v-model="takeTime" type="date" placeholder="选择日期">
                                    </el-date-picker>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="文件" name="3">
                            <div class="ct_box">
                                <div class="hds_time">
                                    <el-date-picker v-model="takeTime" type="date" placeholder="选择日期">
                                    </el-date-picker>
                                </div>
                                <el-table :data="fileData" stripe style="width: 100%" :header-cell-style="{background:'#fff'}">
                                    <el-table-column prop="date" label="类型">
                                    </el-table-column>
                                    <el-table-column prop="name" label="名称">
                                    </el-table-column>
                                    <el-table-column prop="address" label="大小">
                                    </el-table-column>
                                    <el-table-column prop="address" label="来源">
                                    </el-table-column>
                                    <el-table-column prop="address" label="操作">
                                    </el-table-column>
                                </el-table>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="链接" name="4">
                            <div class="ct_box">
                                <div class="hds_time">
                                    <el-date-picker v-model="takeTime" type="date" placeholder="选择日期">
                                    </el-date-picker>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="语音通话" name="5">
                            <div class="ct_box">
                                <div class="hds_time">
                                    <el-date-picker v-model="takeTime" type="date" placeholder="选择日期">
                                    </el-date-picker>                                
                                </div>
                                 <el-table :data="fileData" stripe style="width: 100%" :header-cell-style="{background:'#fff'}">
                                        <el-table-column prop="date" label="发起人">
                                        </el-table-column>
                                        <el-table-column prop="name" label="通话时间">
                                        </el-table-column>
                                        <el-table-column prop="address" label="时长">
                                        </el-table-column>
                                        <el-table-column prop="address" label="操作">
                                        </el-table-column>
                                    </el-table>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </el-col>
        </el-row>

    </div>
</template>
<script>
    export default {
        data() {
            return {
                employAmount: 1,
                employName: '',
                talkName: '',
                data: [{
                    id: 1,
                    label: '一级 1',
                    children: [{
                        id: 4,
                        label: '二级 1-1',
                        children: [{
                            id: 9,
                            label: '三级 1-1-1'
                        }, {
                            id: 10,
                            label: '三级 1-1-2'
                        }]
                    }]
                }, {
                    id: 3,
                    label: '一级 3',
                    children: [{
                        id: 7,
                        label: '二级 3-1'
                    }, {
                        id: 8,
                        label: '二级 3-2'
                    }]
                }],
                activeName: "1",
                activeNameThree: '1',
                takeTime: '',
                fileData: []
            };
        },
        watch: {
            employName(val) {
                this.$refs.tree.filter(val);
            }
        },
        methods: {
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            handleNodeClick(data) {
                if (!data.children) {
                    console.log(data.label)
                    this.talkName = data.label
                }
            }
        }

    }
</script>
<style lang="scss" scoped>
    /deep/ #tab-0 {
        text-indent: 15px;
    }

    .borderR {
        border-right: 2px solid #ccc;
    }

    .paddingT10 {
        padding-top: 10px
    }

    .employ {
        background: #f6f6f9;
        min-height: 800px;

        .hd_tabs {
            background: #fff;
        }

        .hd_tabthree {
            /deep/ .el-tabs__header {
                margin: 0;
            }
        }

        .hd_box {
            padding: 15px;
        }

        .hd_name {
            font-size: 18px;
            min-height: 20px;
        }

        .hd_nameRi {
            color: #199ed8;
            text-align: right;
            font-size: 16px;
            cursor: pointer;
        }

        .ct_boxFirst {
            height: 720px;
        }

        .ct_box {
            background: white;
            min-height: 710px;
            padding: 10px;
            overflow-y: scroll;
            color: #999;
            text-align: center;

            ::-webkit-scrollbar {
                display: none;
            }

            .hds_time {
                float: left;
                padding: 10px 0;
            }
        }
    }
</style>