package com.istlab.datagovernanceplatform.repository;


import com.istlab.datagovernanceplatform.pojo.po.DataSourceInfoPO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceInfoRepo extends MongoRepository<DataSourceInfoPO, String> {
    boolean existsByName(String name);
    boolean existsByHostAndPortAndDatabase(String host, Integer port, String database);
    DataSourceInfoPO getDataSourceInfoPOByHostAndPortAndDatabase(String host, Integer port, String database);
    boolean existsByFuseFlag(Boolean fuseFlag);
}
