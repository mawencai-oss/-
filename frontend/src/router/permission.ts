import { RouteRecordRaw } from 'vue-router';
import router from './index';
import Layout from '@/views/layout/index.vue';

// 组件映射表，用于动态引入组件
const layoutComponent = () => import('@/views/layout/index.vue');

// 动态引入组件
const loadComponent = (component: string) => {
  if (component === 'Layout') {
    return layoutComponent;
  }
  
  // 处理组件路径
  return () => import(`@/views/${component}.vue`);
};

/**
 * 生成动态路由
 * @param menus 菜单数据
 * @returns 路由配置
 */
export const generateRoutes = (menus: any[]): RouteRecordRaw[] => {
  const routes: RouteRecordRaw[] = [];

  const generateRoute = (menu: any): RouteRecordRaw => {
    const route: RouteRecordRaw = {
      path: menu.path,
      name: menu.path.replace('/', '') || 'index',
      meta: {
        title: menu.menuName,
        icon: menu.icon,
        hidden: menu.visible === 0
      }
    };

    // 判断组件类型
    if (menu.component === 'Layout') {
      route.component = layoutComponent;
    } else if (menu.component) {
      route.component = loadComponent(menu.component);
    }

    // 处理子路由
    if (menu.children && menu.children.length > 0) {
      route.children = menu.children
        .filter((child: any) => child.menuType !== 'F' && child.visible !== 0) // 过滤掉按钮和隐藏菜单
        .map((child: any) => generateRoute(child));
    }

    return route;
  };

  // 过滤顶级菜单（不包括按钮和隐藏菜单）
  const topMenus = menus.filter(menu => menu.menuType !== 'F' && menu.visible !== 0);
  
  // 生成路由配置
  topMenus.forEach(menu => {
    routes.push(generateRoute(menu));
  });

  return routes;
};

/**
 * 添加动态路由到路由器
 * @param routes 路由配置
 */
export const addRoutes = (routes: RouteRecordRaw[]): void => {
  routes.forEach(route => {
    router.addRoute(route);
  });
};

/**
 * 重置路由
 */
export const resetRoutes = (): void => {
  // 获取所有路由
  const routes = router.getRoutes();
  // 删除动态添加的路由
  routes.forEach(route => {
    if (route.name && route.name !== 'Login' && route.name !== 'NotFound') {
      router.removeRoute(route.name);
    }
  });
}; 