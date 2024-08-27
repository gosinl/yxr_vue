<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="ip地址" prop="ip">
        <el-input
          v-model="queryParams.ip"
          placeholder="请输入ip地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="加密签名" prop="msgSignature">
        <el-input
          v-model="queryParams.msgSignature"
          placeholder="请输入加密签名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间戳" prop="timestamp">
        <el-input
          v-model="queryParams.timestamp"
          placeholder="请输入时间戳"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:shanxipicc:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:shanxipicc:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:shanxipicc:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:shanxipicc:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shanxipiccList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="ip地址" align="center" prop="ip" />
      <el-table-column label="加密签名" align="center" prop="msgSignature" />
      <el-table-column label="时间戳" align="center" prop="timestamp" />
      <el-table-column label="状态" align="center" prop="status" >
      <template slot-scope="scope">
        <!-- 是否当未找到匹配的数据时，显示原值value   :show-value="false"-->
        <dict-tag :options="dict.type.api_medical_status" :value="scope.row.status" :show-value="false"/>
      </template>
      </el-table-column>
      <el-table-column label="接口类别" align="center" prop="interfaceType" >
      <template slot-scope="scope">
        <!-- 是否当未找到匹配的数据时，显示原值value   :show-value="false"-->
        <dict-tag :options="dict.type.medical_interface_type" :value="scope.row.interfaceType" :show-value="false"/>
      </template>
      </el-table-column>
      <el-table-column label="请求数据" align="center" prop="requestBody">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.requestBody"
            type="textarea"
            :autosize="{minRows: 2, maxRows: 5}"
            :style="{width: '100%'}"
            :readonly = true >
          </el-input>
        </template>
      </el-table-column>
      <el-table-column label="返回数据" align="center" prop="responseBody" >
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.responseBody"
            type="textarea"
            :autosize="{minRows: 2, maxRows: 5}"
            :style="{width: '100%'}"
            :readonly = true>
          </el-input>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:shanxipicc:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:shanxipicc:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改陕西PICC请求接口对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="ip地址" prop="ip">
          <el-input v-model="form.ip" placeholder="请输入ip地址" />
        </el-form-item>
        <el-form-item label="加密签名" prop="msgSignature">
          <el-input v-model="form.msgSignature" placeholder="请输入加密签名" />
        </el-form-item>
        <el-form-item label="时间戳" prop="timestamp">
          <el-input v-model="form.timestamp" placeholder="请输入时间戳" />
        </el-form-item>
        <el-form-item label="请求数据" prop="requestBody">
          <el-input v-model="form.requestBody" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="返回数据" prop="responseBody">
          <el-input v-model="form.responseBody" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listShanxipicc, getShanxipicc, delShanxipicc, addShanxipicc, updateShanxipicc } from "@/api/core/shanxipicc";

export default {
  name: "Shanxipicc",
  dicts: ['api_medical_status', 'medical_interface_type','sys_yes_no'],
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 陕西PICC请求接口表格数据
      shanxipiccList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ip: null,
        msgSignature: null,
        timestamp: null,
        status: null,
        interfaceType: null,
        requestBody: null,
        responseBody: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
          requestBody: [{
          required: true,
          message: '请输入多行文本',
          trigger: 'blur'
        }],
          responseBody: [{
          required: true,
          message: '请输入多行文本',
          trigger: 'blur'
        }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询陕西PICC请求接口列表 */
    getList() {
      this.loading = true;
      listShanxipicc(this.queryParams).then(response => {
        this.shanxipiccList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        ip: null,
        msgSignature: null,
        timestamp: null,
        status: null,
        interfaceType: null,
        requestBody: null,
        responseBody: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加陕西PICC请求接口";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getShanxipicc(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改陕西PICC请求接口";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateShanxipicc(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addShanxipicc(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除陕西PICC请求接口编号为"' + ids + '"的数据项？').then(function() {
        return delShanxipicc(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/shanxipicc/export', {
        ...this.queryParams
      }, `shanxipicc_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
