package com.istlab.datagovernanceplatform.repository;

import com.istlab.datagovernanceplatform.pojo.po.TopicAreaPO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicAreaRepo  extends MongoRepository<TopicAreaPO, String> {

}
