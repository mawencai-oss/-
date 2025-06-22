<template>
  <div class="menu-container">
    <div class="header">
      <h2>菜单管理</h2>
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">添加菜单</el-button>
      </div>
    </div>
    
    <div class="search-bar">
      <el-form :model="queryParams" inline>
        <el-form-item label="菜单名称">
          <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="菜单状态" clearable>
            <el-option label="正常" value="1" />
            <el-option label="停用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      border
      style="width: 100%"
    >
      <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" />
      <el-table-column prop="icon" label="图标" align="center" width="100">
        <template #default="scope">
          <el-icon v-if="scope.row.icon">
            <component :is="scope.row.icon" />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="80" align="center" />
      <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true" />
      <el-table-column prop="path" label="路由地址" :show-overflow-tooltip="true" />
      <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" />
      <el-table-column prop="menuType" label="类型" width="80" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.menuType === 'M'">目录</el-tag>
          <el-tag type="success" v-else-if="scope.row.menuType === 'C'">菜单</el-tag>
          <el-tag type="info" v-else-if="scope.row.menuType === 'F'">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="可见" width="80" align="center">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.visible === 1">显示</el-tag>
          <el-tag type="info" v-else>隐藏</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.status === 1">正常</el-tag>
          <el-tag type="danger" v-else>停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="handleAdd(scope.row)" v-if="scope.row.menuType !== 'F'">新增</el-button>
          <el-button link type="primary" @click="handleEdit(scope.row)">修改</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 菜单表单对话框 -->
    <el-dialog
      :title="dialog.title"
      v-model="dialog.visible"
      width="600px"
      append-to-body
    >
      <el-form
        ref="menuFormRef"
        :model="menuForm"
        :rules="menuRules"
        label-width="100px"
      >
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="menuForm.parentId"
            :data="menuOptions"
            :props="{ label: 'menuName', value: 'id', children: 'children' }"
            placeholder="选择上级菜单"
            check-strictly
            clearable
          />
        </el-form-item>
        <el-form-item label="菜单类型" required>
          <el-radio-group v-model="menuForm.menuType">
            <el-radio label="M">目录</el-radio>
            <el-radio label="C">菜单</el-radio>
            <el-radio label="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="menuForm.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="menuForm.orderNum" :min="0" :max="999" controls-position="right" />
        </el-form-item>
        <el-form-item label="图标" v-if="menuForm.menuType !== 'F'">
          <el-input v-model="menuForm.icon" placeholder="请输入图标" />
        </el-form-item>
        <el-form-item label="路由地址" prop="path" v-if="menuForm.menuType !== 'F'">
          <el-input v-model="menuForm.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="menuForm.menuType === 'C'">
          <el-input v-model="menuForm.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="权限标识" prop="perms" v-if="menuForm.menuType !== 'M'">
          <el-input v-model="menuForm.perms" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="是否显示" v-if="menuForm.menuType !== 'F'">
          <el-radio-group v-model="menuForm.visible">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单状态">
          <el-radio-group v-model="menuForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelForm">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus';
import { getMenuList, getMenuTree, getMenuDetail, addMenu, updateMenu, deleteMenu } from '@/api/menu';

// 查询参数
const queryParams = reactive({
  menuName: '',
  status: ''
});

// 菜单列表
const menuList = ref<any[]>([]);

// 菜单树选项
const menuOptions = ref<any[]>([]);

// 加载状态
const loading = ref(false);

// 对话框状态
const dialog = reactive({
  visible: false,
  title: '',
  type: 'add' // 'add' 或 'edit'
});

// 菜单表单
const menuForm = reactive({
  id: undefined,
  parentId: 0,
  menuName: '',
  orderNum: 0,
  path: '',
  component: '',
  menuType: 'M',
  perms: '',
  icon: '',
  visible: 1,
  status: 1
});

// 校验规则
const menuRules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  orderNum: [{ required: true, message: '请输入显示顺序', trigger: 'blur' }],
  path: [{ required: true, message: '请输入路由地址', trigger: 'blur' }]
};

const menuFormRef = ref<FormInstance>();

// 加载菜单列表
const getList = async () => {
  loading.value = true;
  try {
    const res = await getMenuList(queryParams);
    menuList.value = res;
  } catch (err) {
    console.error('获取菜单列表失败:', err);
  } finally {
    loading.value = false;
  }
};

// 加载菜单树选项
const loadMenuOptions = async () => {
  try {
    // 构建选项，添加顶级菜单选项
    const topMenu = { id: 0, menuName: '主目录', children: [] };
    const menus = await getMenuTree();
    menuOptions.value = [topMenu].concat(menus);
  } catch (err) {
    console.error('获取菜单树失败:', err);
  }
};

// 查询按钮
const handleQuery = () => {
  getList();
};

// 重置查询
const resetQuery = () => {
  queryParams.menuName = '';
  queryParams.status = '';
  handleQuery();
};

// 添加菜单
const handleAdd = (row?: any) => {
  dialog.title = '添加菜单';
  dialog.type = 'add';
  
  // 重置表单
  Object.assign(menuForm, {
    id: undefined,
    parentId: row ? row.id : 0,
    menuName: '',
    orderNum: 0,
    path: '',
    component: '',
    menuType: 'M',
    perms: '',
    icon: '',
    visible: 1,
    status: 1
  });
  
  dialog.visible = true;
};

// 修改菜单
const handleEdit = async (row: any) => {
  dialog.title = '修改菜单';
  dialog.type = 'edit';
  
  try {
    const menu = await getMenuDetail(row.id);
    Object.assign(menuForm, menu);
    dialog.visible = true;
  } catch (err) {
    console.error('获取菜单详情失败:', err);
    ElMessage.error('获取菜单详情失败');
  }
};

// 提交表单
const submitForm = async () => {
  menuFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        if (dialog.type === 'add') {
          await addMenu(menuForm);
          ElMessage.success('添加成功');
        } else {
          await updateMenu(menuForm.id, menuForm);
          ElMessage.success('修改成功');
        }
        
        dialog.visible = false;
        getList();
        loadMenuOptions(); // 更新菜单选项
      } catch (err) {
        console.error('操作失败:', err);
        ElMessage.error('操作失败');
      }
    }
  });
};

// 取消表单
const cancelForm = () => {
  dialog.visible = false;
};

// 删除菜单
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除菜单 ${row.menuName} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id);
      ElMessage.success('删除成功');
      getList();
      loadMenuOptions(); // 更新菜单选项
    } catch (err) {
      console.error('删除失败:', err);
      ElMessage.error('删除失败');
    }
  }).catch(() => {});
};

// 监听菜单类型变化
watch(() => menuForm.menuType, (val) => {
  if (val === 'F') {
    menuForm.visible = 0;
  }
});

onMounted(() => {
  getList();
  loadMenuOptions();
});
</script>

<style scoped>
.menu-container {
  @apply bg-white p-5 rounded-md;
}

.header {
  @apply flex justify-between items-center mb-5;
}

.header h2 {
  @apply text-xl font-bold m-0;
}

.search-bar {
  @apply mb-5;
}

.dialog-footer {
  @apply flex justify-end;
}
</style> 