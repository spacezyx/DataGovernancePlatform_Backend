package com.istlab.datagovernanceplatform.repository;

import com.istlab.datagovernanceplatform.pojo.po.TableMetadataPO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMetadataRepo extends MongoRepository<TableMetadataPO, String> {

}
