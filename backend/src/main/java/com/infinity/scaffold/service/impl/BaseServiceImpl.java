package com.infinity.scaffold.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.infinity.scaffold.service.IBaseService;

/**
 * 基础Service实现类，继承MyBatis-Plus的ServiceImpl
 *
 * @param <M> Mapper类型
 * @param <T> 实体类型
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

} 