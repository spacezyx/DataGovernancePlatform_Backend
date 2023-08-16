package com.istlab.datagovernanceplatform.service.impl;

import com.istlab.datagovernanceplatform.pojo.domain.GraphLine;
import com.istlab.datagovernanceplatform.pojo.domain.GraphNode;
import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;
import com.istlab.datagovernanceplatform.pojo.po.TmpPO;
import com.istlab.datagovernanceplatform.repository.TmpRepo;
import com.istlab.datagovernanceplatform.service.TmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TmpServiceImpl implements TmpService {
    @Autowired
    private TmpRepo tmpRepo;

    @Override
    public void tmp() {
        List<TmpPO> tmpPOList = tmpRepo.findAll();
        GraphJsonDataPO graphJsonDataPO = new GraphJsonDataPO();
        List<GraphNode> nodes = new ArrayList<>();
        List<GraphLine> lines = new ArrayList<>();
        String rootId = "a";
        graphJsonDataPO.setRootId(rootId);
        GraphNode node = new GraphNode();
        node.setColor("red");
        node.setText("制造域");
        node.setId(rootId);
        nodes.add(node);
        for(TmpPO tmpPO : tmpPOList) {
            GraphJsonDataPO t = tmpPO.getGraphJsonData();
            List<GraphNode> m = t.getNodes();
            List<GraphLine> n = t.getLines();
            nodes.addAll(m);
            lines.addAll(n);
            GraphLine tableLine = new GraphLine();
            tableLine.setText("包含数据库");
            tableLine.setFrom(rootId);
            tableLine.setTo(tmpPO.getGraphJsonData().getRootId());
            tableLine.setColor("#D2C0A5");
            tableLine.setFontColor("#D2C0A5");
            lines.add(tableLine);
        }
        graphJsonDataPO.setNodes(nodes);
        graphJsonDataPO.setLines(lines);
        TmpPO tmpPO = new TmpPO();
        tmpPO.setData("tmp");
        tmpPO.setGraphJsonData(graphJsonDataPO);
        tmpRepo.save(tmpPO);
    }
}
