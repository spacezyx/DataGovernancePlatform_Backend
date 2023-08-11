package com.istlab.datagovernanceplatform.service;

import com.istlab.datagovernanceplatform.pojo.dto.TopicAreaDTO;
import com.istlab.datagovernanceplatform.pojo.po.TopicAreaPO;

import java.util.List;

public interface TopicAreaService {
    List<TopicAreaPO> getAllTopicAreas();
    TopicAreaPO getTopicAreaById(String id);
    TopicAreaPO createTopicArea(TopicAreaDTO topicArea);
    void updateTopicArea(String id, TopicAreaPO topicArea);
    void deleteTopicArea(String id);
}
