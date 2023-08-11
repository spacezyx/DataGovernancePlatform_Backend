package com.istlab.datagovernanceplatform.controller;

import com.istlab.datagovernanceplatform.pojo.dto.TopicAreaDTO;
import com.istlab.datagovernanceplatform.pojo.po.TopicAreaPO;
import com.istlab.datagovernanceplatform.service.TopicAreaService;
import com.istlab.datagovernanceplatform.utils.Result;
import com.istlab.datagovernanceplatform.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TopicAreaController {
    @Autowired
    private TopicAreaService topicAreaService;

    @GetMapping(value = "/topic/getAll")
    public List<TopicAreaPO> getAll(){
        return topicAreaService.getAllTopicAreas();
    }

    @DeleteMapping(value = "/topic/{id}")
    public Result<String> delete(@PathVariable String id){
        topicAreaService.deleteTopicArea(id);
        return ResultUtil.success("成功删除主题域");
    }

    @GetMapping(value = "/topic/{id}")
    public TopicAreaPO get(@PathVariable String id){
        return topicAreaService.getTopicAreaById(id);
    }

    @PostMapping(value = "/topic/create")
    public Result<String> create(@RequestBody TopicAreaDTO topicArea){
        topicAreaService.createTopicArea(topicArea);
        return ResultUtil.success("成功创建主题域");
    }

    @PostMapping(value = "/topic/edit")
    public Result<String> edit(@RequestBody TopicAreaPO topicArea){
        topicAreaService.updateTopicArea(topicArea.getId(), topicArea);
        return ResultUtil.success("成功更新主题域");
    }
}
