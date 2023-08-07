package com.istlab.datagovernanceplatform.pojo.po;

import com.istlab.datagovernanceplatform.pojo.domain.GraphLine;
import com.istlab.datagovernanceplatform.pojo.domain.GraphNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphJsonDataPO {
    String rootId;
    List<GraphNode> nodes;
    List<GraphLine> lines;
}
