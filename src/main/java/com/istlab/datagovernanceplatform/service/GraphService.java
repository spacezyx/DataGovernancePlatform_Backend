package com.istlab.datagovernanceplatform.service;

import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;

public interface GraphService {
    GraphJsonDataPO getPostgresGraphByDatasourceId(String id);
}
