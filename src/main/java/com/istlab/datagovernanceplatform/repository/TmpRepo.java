package com.istlab.datagovernanceplatform.repository;

import com.istlab.datagovernanceplatform.pojo.po.TmpPO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TmpRepo extends MongoRepository<TmpPO, String> {
    TmpPO findTmpPOById(String id);
}
