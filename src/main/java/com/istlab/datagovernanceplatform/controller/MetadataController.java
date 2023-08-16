package com.istlab.datagovernanceplatform.controller;

import com.istlab.datagovernanceplatform.pojo.domain.SelectList;
import com.istlab.datagovernanceplatform.pojo.dto.FuseTestDTO;
import com.istlab.datagovernanceplatform.pojo.dto.RangeValueDTO;
import com.istlab.datagovernanceplatform.pojo.dto.TextRangeDTO;
import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;
import com.istlab.datagovernanceplatform.pojo.po.TmpPO;
import com.istlab.datagovernanceplatform.pojo.vo.SimilarityCheckVO;
import com.istlab.datagovernanceplatform.repository.TmpRepo;
import com.istlab.datagovernanceplatform.service.GraphService;
import com.istlab.datagovernanceplatform.service.TmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class MetadataController {
    @Autowired
    private GraphService graphService;

    @Autowired
    private TmpService tmpService;

    @Autowired
    private TmpRepo tmpRepo;

    @GetMapping(value = "/graph/getMetadataGraph/{id}")
    public GraphJsonDataPO getMetadataGraph(@PathVariable String id){
//         return tmpRepo.findTmpPOById("64dc88235e1ad152a5e9a1b1").getGraphJsonData();
          return graphService.getPostgresGraphByDatasourceId(id);
    }

    @PostMapping(value = "/graph/getNodeTextRange")
    public List<SelectList> getNodeTextRange(@RequestBody TextRangeDTO textRangeDTO) throws SQLException {
        return graphService.getNodeTextRange(textRangeDTO);
    }

    @PostMapping(value = "/graph/getRangeValue")
    public List<Map<String, Object>> getRangeValue(@RequestBody RangeValueDTO rangeValueDTO) {
        return graphService.getRangeValue(rangeValueDTO);
    }

    @PostMapping(value = "/fuse/fuseTest")
    public List<SimilarityCheckVO> fuseTest(@RequestBody FuseTestDTO fuseTestDTO) {
        String id = fuseTestDTO.getId();
        String topicId = fuseTestDTO.getTopicAreaId();
        tmpService.tmp();
        return graphService.metadataFuse(id, topicId);
    }
}
