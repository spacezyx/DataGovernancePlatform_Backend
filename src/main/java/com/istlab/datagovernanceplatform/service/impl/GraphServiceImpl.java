package com.istlab.datagovernanceplatform.service.impl;

import com.istlab.datagovernanceplatform.pojo.domain.ColumnMetadata;
import com.istlab.datagovernanceplatform.pojo.domain.GraphLine;
import com.istlab.datagovernanceplatform.pojo.domain.GraphNode;
import com.istlab.datagovernanceplatform.pojo.po.DataSourceInfoPO;
import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;
import com.istlab.datagovernanceplatform.pojo.po.TableMetadataPO;
import com.istlab.datagovernanceplatform.repository.DataSourceInfoRepo;
import com.istlab.datagovernanceplatform.repository.TableMetadataRepo;
import com.istlab.datagovernanceplatform.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GraphServiceImpl implements GraphService {

    @Autowired
    private DataSourceInfoRepo dataSourceInfoRepo;

    @Autowired
    private TableMetadataRepo tableMetadataRepo;

    @Override
    public GraphJsonDataPO getPostgresGraphByDatasourceId(String id) {
        GraphJsonDataPO graphJsonDataPO = new GraphJsonDataPO();
        List<GraphNode> nodes = new ArrayList<>();
        List<GraphLine> lines = new ArrayList<>();

        // 数据源节点
        DataSourceInfoPO dataSourceInfoPO = dataSourceInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("DataSource不存在"));
        GraphNode rootNode = new GraphNode();
        String datasourceId = dataSourceInfoPO.getId();
        rootNode.setId(datasourceId);
        rootNode.setText(dataSourceInfoPO.getName());
        rootNode.setColor("#F68A08");

        graphJsonDataPO.setRootId(datasourceId);
        nodes.add(rootNode);

        // 表
        List<TableMetadataPO> tableMetadataPOS = tableMetadataRepo.findAllByDataSourceId(id);
        for(TableMetadataPO tableMetadataPO : tableMetadataPOS) {
            // 表节点
            GraphNode tableNode = new GraphNode();
            String tableId = tableMetadataPO.getId();
            tableNode.setId(tableId);
            tableNode.setText(tableMetadataPO.getTableName());
            tableNode.setColor("#B1BB96");
            // 数据源节点和表节点的连线
            GraphLine tableLine = new GraphLine();
            tableLine.setText("包含数据表");
            tableLine.setFrom(datasourceId);
            tableLine.setTo(tableId);
            nodes.add(tableNode);
            lines.add(tableLine);
            // 字段节点
            List<ColumnMetadata> columnMetadataList = tableMetadataPO.getColumns();
            for(int index = 0; index < columnMetadataList.size(); index++) {
                ColumnMetadata columnMetadata = columnMetadataList.get(index);
                GraphNode columnNode = new GraphNode();
                String columnId = tableId + columnMetadata.getColumnName();
                columnNode.setId(columnId);
                columnNode.setText(columnMetadata.getColumnName());
                columnNode.setColor("#65A3B7");
                GraphLine columnLine = new GraphLine();
                columnLine.setText("包含属性");
                columnLine.setFrom(tableId);
                columnLine.setTo(columnId);
                nodes.add(columnNode);
                lines.add(columnLine);
                // 外键关联
                if(columnMetadata.isForeignKey()) {
                    GraphLine fkLine = new GraphLine();
                    TableMetadataPO po = tableMetadataRepo.findTableMetadataPOByTableName(columnMetadata.getReferencedTableName());
                    String fkEndId = po.getId() + columnMetadata.getReferencedColumnName();
                    fkLine.setText("外键约束");
                    fkLine.setFrom(columnId);
                    fkLine.setTo(fkEndId);
                    lines.add(fkLine);
                }
            }
        }
        graphJsonDataPO.setLines(lines);
        graphJsonDataPO.setNodes(nodes);
        return graphJsonDataPO;
    }


}
