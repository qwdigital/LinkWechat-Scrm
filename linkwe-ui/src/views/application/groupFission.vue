<script>
import * as api from "@/api/task";

export default {
    name: 'Group',
    data () {
        return {
            query: {
                pageNum: 1,
                pageSize: 10,
                taskName: "",
                startTime: "",
                overTime: "",
                fissionType:2
            },
            dateRange: [],
            tableData: [],
            total:0
        }
    },
    created () {
        this.getList()
    },
    methods: {
        getList (data) {
            let params = Object.assign({},this.query,data)
            api.getList(params)
                .then(({ rows, total }) => {
                    this.tableData = rows;
                    this.total = +total;
                })
        },
        resetForm () {

        },
        toDetail(row){
            this.$router.push({
                path: `/application/fissionDetail?id=${row.id}`,
            })
        },
        newAdd () {
            this.$router.push({
                path: '/application/addFission',
            })
        },
        toEdit(row){
            this.$router.push({
                path: `/application/addFission?id=${row.id}`,
            })
        }
    },
}
</script>

<template>
    <div class="app-container">
        <el-form
            ref="queryForm"
            :inline="true"
            :model="query"
            label-width="100px"
            class="top-search"
            size="small"
        >
            <el-form-item
                label="裂变名"
                prop="taskName"
            >
                <el-input
                    v-model="query.taskName"
                    placeholder="请输入"
                ></el-input>
            </el-form-item>
            <el-form-item label="添加日期">
                <el-date-picker
                    v-model="dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                ></el-date-picker>
            </el-form-item>
            <el-form-item label=" ">
                <el-button
                    v-hasPermi="['customerManage:customer:query']"
                    type="primary"
                    @click="getList({pageNum:1})"
                >查询</el-button>
                <el-button
                    v-hasPermi="['customerManage:customer:query']"
                    type="primary"
                    @click="newAdd()"
                >新增任务</el-button>

            </el-form-item>
        </el-form>
        <el-table
            :data="tableData"
            border
            style="width: 100%"
        >
            <el-table-column
                prop="taskName"
                label="群裂变名称"
            >
            </el-table-column>
            <el-table-column
                prop="fissNum"
                label="裂变客户数量"
            >
            </el-table-column>
            <el-table-column
                prop="fissStatus"
                label="活动状态"
            >
                <template slot-scope="scope">
                    <p>{{ scope.row.fissStatus===1?'进行中':'已结束' }}</p>
                </template>
            </el-table-column>
            <el-table-column
                prop="startTime"
                label="活动时间"
            >
                <template slot-scope="scope">
                    <p>{{ scope.row.startTime}}-{{scope.row.overTime}}</p>
                </template>
            </el-table-column>
            <el-table-column
                prop="operation"
                label="操作"
            >
                <template slot-scope="scope">
                    <el-button
                        @click="toDetail(scope.row)"
                        v-hasPermi="['enterpriseWechat:view']"
                        size="mini"
                        type="text"
                        icon="el-icon-view"
                    >查看</el-button>
                    <el-button
                        v-if="scope.row.fissStatus!=2"
                         @click="toEdit(scope.row)"
                        v-hasPermi="['enterpriseWechat:edit']"
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                    >编辑</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="query.pageNum"
            :limit.sync="query.pageSize"
            @pagination="getList()"
        />
    </div>
</template>

<style lang="scss" scoped>
.mid-action {
    display: flex;
    justify-content: space-between;
    margin: 10px 0;
    align-items: center;
    .total {
        background-color: rgba(65, 133, 244, 0.1);
        border: 1px solid rgba(65, 133, 244, 0.2);
        border-radius: 3px;
        font-size: 14px;
        min-height: 32px;
        line-height: 32px;
        padding: 0 12px;
        color: #606266;
    }
    .num {
        color: #00f;
    }
}
</style>
