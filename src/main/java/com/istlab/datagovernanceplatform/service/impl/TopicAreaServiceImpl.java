package com.istlab.datagovernanceplatform.service.impl;

import com.istlab.datagovernanceplatform.pojo.dto.TopicAreaDTO;
import com.istlab.datagovernanceplatform.pojo.po.TopicAreaPO;
import com.istlab.datagovernanceplatform.repository.TopicAreaRepo;
import com.istlab.datagovernanceplatform.service.TopicAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopicAreaServiceImpl implements TopicAreaService {
    @Autowired
    private TopicAreaRepo topicAreaRepo;

    @Override
    public List<TopicAreaPO> getAllTopicAreas() {
        return topicAreaRepo.findAll();
    }

    @Override
    public TopicAreaPO getTopicAreaById(String id) {
        return topicAreaRepo.findById(id).orElseThrow(() -> new RuntimeException("TopicAreaPO不存在"));
    }

    @Override
    public TopicAreaPO createTopicArea(TopicAreaDTO topicArea) {
        TopicAreaPO topicAreaPO = new TopicAreaPO();
        topicAreaPO.setName(topicArea.getName());
        topicAreaPO.setDescription(topicArea.getDescription());
        return topicAreaRepo.save(topicAreaPO);
    }

    @Override
    public void updateTopicArea(String id, TopicAreaPO topicArea) {
        if (topicAreaRepo.existsById(id)) {
            topicArea.setId(id);
            topicAreaRepo.save(topicArea);
        }
    }

    @Override
    public void deleteTopicArea(String id) {
        topicAreaRepo.deleteById(id);
    }

}
