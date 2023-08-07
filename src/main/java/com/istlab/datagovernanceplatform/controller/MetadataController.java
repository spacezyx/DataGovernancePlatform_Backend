package com.istlab.datagovernanceplatform.controller;

import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;
import com.istlab.datagovernanceplatform.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetadataController {
    @Autowired
    private GraphService graphService;

    @GetMapping(value = "/graph/getMetadataGraph/{id}")
    public GraphJsonDataPO getMetadataGraph(@PathVariable String id){
        return graphService.getPostgresGraphByDatasourceId(id);
    }

}
