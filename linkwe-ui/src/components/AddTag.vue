<script>
import * as api from '@/api/customer/tag'

export default {
  name: 'AddTag',
  components: {},
  props: {
    // 添加标签显隐
    visible: {
      type: Boolean,
      default: false
    },
    // 表单参数
    form: {
      type: Object,
      default: () => ({
        gourpName: '',
        weTags: []
      })
    }
  },
  data() {
    return {
      // 添加标签输入框
      newInput: '',
      // 表单验证规则
      rules: Object.freeze({
        gourpName: [{ required: true, message: '必填项', trigger: 'blur' }],
        weTags: [{ required: true, message: '必填项', trigger: 'blur' }]
      }),
      // 添加便签按钮显隐
      visibleAdd: false
    }
  },
  watch: {},
  computed: {
    Pvisible: {
      get() {
        if (this.visible) {
          this.$nextTick(() => {
            this.$refs['form'].clearValidate()
          })
        }
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  created() {},
  mounted() {},
  methods: {
    closeTag(tag, index) {
      if (tag.id) {
        tag.status = 1
      } else {
        this.form.weTags.splice(index, 1)
      }
    },

    showInput() {
      this.visibleAdd = true
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    newInputConfirm() {
      let name = this.newInput
      if (name) {
        Array.isArray(this.form.weTags) || (this.form.weTags = [])
        let isExist = this.form.weTags.some((e) => {
          return e.name === name
        })
        if (isExist) {
          this.msgError('标签名已存在，不可重复添加')
          return
        }
        this.form.weTags.push({ name })
      }
      this.visibleAdd = false
      this.newInput = ''
    },
    submit() {
      this.$refs['form'].validate((valid) => {
        this.newInput = ''
        let form = JSON.parse(JSON.stringify(this.form))
        if (!form.weTags.length) {
          return
        }
        api[form.groupId ? 'update' : 'add'](form).then(() => {
          this.msgSuccess('操作成功')
          this.Pvisible = false
          this.$emit('success')
        })
      })
    }
  }
}
</script>

<template>
  <el-dialog
    :title="(form.groupId ? '修改' : '添加') + '标签/组'"
    :visible.sync="Pvisible"
    width="500px"
    append-to-body
    :close-on-click-modal="false"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="标签组名称" prop="gourpName">
        <el-input
          v-model.trim="form.gourpName"
          maxlength="15"
          show-word-limit
          placeholder="请输入标签组名称，该名称不支持再次修改"
          :disabled="!!form.groupId"
        />
      </el-form-item>
      <el-form-item label="标签" prop="weTags">
        <template v-for="(item, index) in form.weTags">
          <el-tag
            v-if="item.status !== 1"
            type="primary"
            closable
            size="medium"
            :key="index"
            @close="closeTag(item, index)"
          >
            {{ item.name }}
          </el-tag>
        </template>
        <el-input
          class="input-new-tag"
          v-if="visibleAdd"
          v-model.trim="newInput"
          ref="saveTagInput"
          size="mini"
          maxlength="10"
          show-word-limit
          @keyup.enter.native="newInputConfirm"
          @blur="newInputConfirm"
        ></el-input>
        <el-button v-else type="primary" plain class="button-new-tag" size="mini" @click="showInput"
          >+ 添加标签</el-button
        >
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submit" v-preventReClick="1000">确 定</el-button>
      <el-button @click="Pvisible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
.input-new-tag {
  width: auto;
  margin-left: 10px;
  vertical-align: bottom;
}
.button-new-tag {
  margin-left: 10px;
}
</style>
