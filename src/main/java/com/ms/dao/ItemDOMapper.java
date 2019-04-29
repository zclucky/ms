package com.ms.dao;

import com.ms.dataobject.ItemDO;

import java.util.List;

public interface ItemDOMapper {

    List<ItemDO> listItem();
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);
}