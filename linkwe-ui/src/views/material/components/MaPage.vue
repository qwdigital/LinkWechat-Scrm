<script>
import { getTree, getTreeDetail, updateTree, removeTree } from '@/api/material';
export default {
  name: 'MaPage',
  components: {},
  props: {},
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    getTree() {
      getTree().then(({ data }) => {
        this.treeData = this.handleTree(data);
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
  },
};
</script>

<template>
  <div class="page">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入素材"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
          <el-popover placement="top" width="160" v-model="visible">
            <div>添加一级分类</div>
            <el-select v-model="model" size="small" placeholder>
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="visible = false"
                >取消</el-button
              >
              <el-button type="primary" size="mini" @click="visible = false"
                >确定</el-button
              >
            </div>
            <el-button
              slot="reference"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              >添加分类</el-button
            >
          </el-popover>
        </div>
        <div class="head-container">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <el-col :span="20" :xs="24">
        <el-checkbox
          :indeterminate="isIndeterminate"
          v-model="checkAll"
          @change="handleCheckAllChange"
          >全选</el-checkbox
        >
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >添加图片</el-button
        >
        <el-popover placement="top" width="160" v-model="visible">
          <div>选择分组</div>
          <el-select v-model="model" size="small" placeholder>
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="visible = false"
              >取消</el-button
            >
            <el-button type="primary" size="mini" @click="visible = false"
              >确定</el-button
            >
          </div>
          <el-button slot="reference">移动分组</el-button>
        </el-popover>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >删除</el-button
        >
        <slot></slot>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped></style>
