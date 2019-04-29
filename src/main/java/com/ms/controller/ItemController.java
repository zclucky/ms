package com.ms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ms.controller.viewobject.ItemVO;
import com.ms.error.BusinessException;
import com.ms.response.CommomRetrunType;
import com.ms.service.ItemService;
import com.ms.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class ItemController extends BaseController {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Autowired
    ItemService itemService;


    // 创建商品
    @RequestMapping(value = "create", method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommomRetrunType createItem(@RequestParam(name = "title")String title,
                                   @RequestParam(name = "description")String description,
                                   @RequestParam(name = "price") BigDecimal price,
                                   @RequestParam(name = "stock") Integer stock,
                                   @RequestParam(name = "imgUrl") String imgUrl
                         ) throws BusinessException {
        //封装service层来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemReturn =itemService.createItem(itemModel);
        ItemVO itemVO = convertItemVOFromItemModel(itemReturn);
        return CommomRetrunType.create(itemVO);
    }

    // 浏览商品
    @RequestMapping(value = "get", method = {RequestMethod.GET})
    @ResponseBody
    private CommomRetrunType getItem( @RequestParam(name = "id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertItemVOFromItemModel(itemModel);
        return CommomRetrunType.create(itemVO);
    }

    // 浏览商品列表
    @RequestMapping(value = "listItem", method = {RequestMethod.GET})
    @ResponseBody
    private CommomRetrunType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
             return convertItemVOFromItemModel(itemModel);
        }).collect(Collectors.toList());
        return CommomRetrunType.create(itemVOList);
    }


    private ItemVO convertItemVOFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }


}
