package com.istlab.datagovernanceplatform.controller;

import com.istlab.datagovernanceplatform.pojo.domain.SelectList;
import com.istlab.datagovernanceplatform.pojo.dto.TextRangeDTO;
import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;
import com.istlab.datagovernanceplatform.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class MetadataController {
    @Autowired
    private GraphService graphService;

    @GetMapping(value = "/graph/getMetadataGraph/{id}")
    public GraphJsonDataPO getMetadataGraph(@PathVariable String id){
        return graphService.getPostgresGraphByDatasourceId(id);
    }

    @PostMapping(value = "/graph/getNodeTextRange")
    public List<SelectList> getNodeTextRange(@RequestBody TextRangeDTO textRangeDTO) throws SQLException {
        return graphService.getNodeTextRange(textRangeDTO);
    }

}
