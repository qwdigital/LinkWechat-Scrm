<script>
import * as api from "@/api/organization";

export default {
  name: "SelectUser",
  components: {},
  props: {
    // 添加标签显隐
    visible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: "选择成员",
    },
  },
  data() {
    return {
      treeData: [],
      userList: [],
      defaultProps: {
        label: "name",
        children: "children",
        disabled(data, node) {
          return !data.userId;
        },
        isLeaf(data, node) {
          return !!data.userId;
        },
      },
    };
  },
  watch: {},
  computed: {
    Pvisible: {
      get() {
        // this.getTree();
        return this.visible;
      },
      set(val) {
        this.$emit("update:visible", val);
      },
    },
  },
  created() {},
  mounted() {},
  methods: {
    loadNode(node, resolve) {
      if (node.level === 0) {
        this.userList = [];
        api.getTree().then(({ data }) => {
          let _data = this.handleTree(data);
          resolve(_data);
          // api.getList({ department: _data[0].id }).then(({ rows, total }) => {
          //   _data && rows.unshift(..._data);
          //   resolve(rows);
          // });
        });
      } else {
        api.getList({ department: node.data.id }).then(({ rows, total }) => {
          node.data.children && rows.push(...node.data.children);
          resolve(rows);
        });
      }
    },
    handleCheckChange(data, checked, indeterminate) {
      checked
        ? this.userList.push(data)
        : this.userList.splice(this.userList.indexOf(data), 1);
      // console.log(data, checked, indeterminate);
    },
    submit() {
      this.Pvisible = false;
      this.$emit("success", this.userList.splice(0));
    },
  },
};
</script>

<template>
  <el-dialog :title="title" :visible.sync="Pvisible">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="12" :xs="24">
        <div class="head-container">
          <!-- <div>部门架构</div> -->
          <!-- :filter-node-method="filterNode" -->
          <el-tree
            v-if="Pvisible"
            ref="tree"
            lazy
            show-checkbox
            check-on-click-node
            :expand-on-click-node="true"
            :load="loadNode"
            :props="defaultProps"
            @check-change="handleCheckChange"
          ></el-tree>
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="12" :xs="24" class="user-list">
        <el-row :gutter="10">选择人员列表</el-row>
        <el-row v-for="(item, index) in userList" :key="index">
          <i class="el-icon-user-solid"></i>
          {{item.name}}
        </el-row>
      </el-col>
    </el-row>
    <div slot="footer">
      <el-button @click="Pvisible = false">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
.user-list {
  .el-row {
    line-height: 26px;
  }
}
</style>
