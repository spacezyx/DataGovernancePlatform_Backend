package com.istlab.datagovernanceplatform.service;

import com.istlab.datagovernanceplatform.pojo.domain.SelectList;
import com.istlab.datagovernanceplatform.pojo.dto.RangeValueDTO;
import com.istlab.datagovernanceplatform.pojo.dto.TextRangeDTO;
import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface GraphService {
    GraphJsonDataPO getPostgresGraphByDatasourceId(String id);
    List<SelectList> getNodeTextRange(TextRangeDTO textRangeDTO) throws SQLException;
    List<Map<String, Object>> getRangeValue(RangeValueDTO rangeValueDTO);
}
