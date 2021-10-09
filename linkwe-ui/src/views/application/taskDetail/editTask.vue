<script>
import * as api from '@/api/customer/group'

export default {
    name: 'editTask',
    data () {
        return {
            query: {
                name: "", // "客户名称",
                beginTime: "", // "开始时间",
                endTime: "" // "结束时间"
            },
        }
    },
    created () {
    },
    mounted () {
    },
    methods: {

    }
}
</script>

<template>
    <div class="task-edit-container">
        <div class="edit-model">
            <h4 class="title">关键数据</h4>
            <el-form
                :inline="true"
                :model="query"
                label-width="100px"
                class="top-search"
                size="small"
            >
                <el-form-item
                    label="任务名"
                    prop="name"
                >
                    <el-input
                        v-model="query.name"
                        placeholder="请输入"
                    ></el-input>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.task-edit-container {
    background-color: #efeff4;
    height: 100%;
    .edit-model {
        background: #fff;
        margin-bottom: 20px;
        padding: 20px;
    }
    .title {
        text-align: left;
        color: #303133;
    }
}
</style>
