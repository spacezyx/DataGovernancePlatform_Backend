package com.istlab.datagovernanceplatform.repository;

import com.istlab.datagovernanceplatform.pojo.po.TableMetadataPO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableMetadataRepo extends MongoRepository<TableMetadataPO, String> {
    void deleteAllByDataSourceId(String id);
    List<TableMetadataPO> findAllByDataSourceId(String id);
    TableMetadataPO findTableMetadataPOByTableName(String name);
    boolean existsByTopicArea(String id);
}
