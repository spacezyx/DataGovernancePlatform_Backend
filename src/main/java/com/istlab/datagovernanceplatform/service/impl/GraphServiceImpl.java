package com.istlab.datagovernanceplatform.service.impl;

import com.istlab.datagovernanceplatform.pojo.domain.*;
import com.istlab.datagovernanceplatform.pojo.dto.RangeValueDTO;
import com.istlab.datagovernanceplatform.pojo.dto.TextRangeDTO;
import com.istlab.datagovernanceplatform.pojo.po.DataSourceInfoPO;
import com.istlab.datagovernanceplatform.pojo.po.GraphJsonDataPO;
import com.istlab.datagovernanceplatform.pojo.po.TableMetadataPO;
import com.istlab.datagovernanceplatform.pojo.po.TopicAreaPO;
import com.istlab.datagovernanceplatform.pojo.vo.SimilarityCheckVO;
import com.istlab.datagovernanceplatform.repository.DataSourceInfoRepo;
import com.istlab.datagovernanceplatform.repository.TableMetadataRepo;
import com.istlab.datagovernanceplatform.repository.TopicAreaRepo;
import com.istlab.datagovernanceplatform.service.GraphService;
import com.istlab.datagovernanceplatform.utils.Result;
import com.istlab.datagovernanceplatform.utils.ResultUtil;
import com.istlab.datagovernanceplatform.utils.SimHashUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GraphServiceImpl implements GraphService {

    @Autowired
    private DataSourceInfoRepo dataSourceInfoRepo;

    @Autowired
    private TableMetadataRepo tableMetadataRepo;

    @Autowired
    private TopicAreaRepo topicAreaRepo;

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
            tableLine.setColor("#D2C0A5");
            tableLine.setFontColor("#D2C0A5");
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
                columnLine.setColor("#D2C0A5");
                columnLine.setFontColor("#D2C0A5");
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
                    fkLine.setFontColor("#D2C0A5");
                    fkLine.setColor("#D2C0A5");
                    lines.add(fkLine);
                }
            }
        }
        graphJsonDataPO.setLines(lines);
        graphJsonDataPO.setNodes(nodes);
        return graphJsonDataPO;
    }

    @Override
    public List<SelectList> getNodeTextRange(TextRangeDTO textRangeDTO) {
        List<SelectList> rangeList = new ArrayList<>();
        String id = textRangeDTO.getId();
        Statement statement = null;
        ResultSet resultSet = null;
        String table = textRangeDTO.getTable();
        String column = textRangeDTO.getColumn();
        DataSourceInfoPO dataSourceInfoPO = dataSourceInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("DataSourceInfoPO不存在"));

        Connection connection =  DataSourceServiceImpl.postGreSQLConnection(dataSourceInfoPO.getHost(), dataSourceInfoPO.getPort(), dataSourceInfoPO.getDatabase(),
                dataSourceInfoPO.getUser(), dataSourceInfoPO.getPassword());
        try {
            statement = connection.createStatement();

            String query = "SELECT " + column + " FROM " + table + " LIMIT 25";
            log.info(query);
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String res = resultSet.getString(column);
                SelectList selectList = new SelectList(res, res);
                rangeList.add(selectList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rangeList;
    }

    @Override
    public List<Map<String, Object>> getRangeValue(RangeValueDTO rangeValueDTO) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String id = rangeValueDTO.getId();
        Statement statement = null;
        ResultSet resultSet = null;
        String table = rangeValueDTO.getTable();
        String column = rangeValueDTO.getColumn();
        List<String> attr = rangeValueDTO.getAttr();
        DataSourceInfoPO dataSourceInfoPO = dataSourceInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("DataSourceInfoPO不存在"));
        Connection connection =  DataSourceServiceImpl.postGreSQLConnection(dataSourceInfoPO.getHost(), dataSourceInfoPO.getPort(), dataSourceInfoPO.getDatabase(),
                dataSourceInfoPO.getUser(), dataSourceInfoPO.getPassword());
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM " + table + " WHERE " + column + " IN (" +
                    String.join(",", attr.stream().map(d -> "'" + d + "'").toArray(String[]::new)) + ");";
            log.info(query);
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    // 元数据融合尝试
    // 这个函数只进行相似度计算，生成一个推荐的融合方案，真正的融合在comfirmFuse里
    @Override
    public List<SimilarityCheckVO> metadataFuse(String id, String topicAreaId) {
        // 获取数据源信息
        DataSourceInfoPO dataSourceInfoPO = dataSourceInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("DataSource不存在"));
        // 获取数据源下的所有表结构信息
        List<TableMetadataPO> tableMetadataPOS = tableMetadataRepo.findAllByDataSourceId(id);
        // 判断是否存在已融合的数据源
        TopicAreaPO topicAreaPO = topicAreaRepo.findById(topicAreaId).orElseThrow(() -> new RuntimeException("主题域不存在"));
        Boolean firstFuse = topicAreaPO.getFuseFlag();

       // 如果是第一次融合，不需要进行实体相似度计算
        List<SimilarityCheckVO> checkVOS = new ArrayList<>();
        if(!firstFuse) {
            for(TableMetadataPO tableMetadataPO : tableMetadataPOS) {
                SimilarityCheckVO similarityCheckVO = new SimilarityCheckVO();
                similarityCheckVO.setName(tableMetadataPO.getTableName());
                similarityCheckVO.setNewFlag(true);
                similarityCheckVO.setFuseFlag(false);
                checkVOS.add(similarityCheckVO);
            }
        } else {
            // TODO: 换好一点的相似度算法
            // 计算并生成给用户确认的返回列表
            String tmpPrefer = "";
            Double tmpSimilar = 0.0;
            for(TableMetadataPO tableMetadataPO : tableMetadataPOS) {
                SimHashUtil mySimHash_1 = new SimHashUtil(tableMetadataPO.getTableName(), 64);
                SimilarityCheckVO similarityCheckVO = new SimilarityCheckVO();
                for(OntologyInfo ontologyInfo : topicAreaPO.getOntologyInfoList()) {
                    SimHashUtil mySimHash_2 = new SimHashUtil(ontologyInfo.getName(), 64);
                    Double similar = mySimHash_1.getSimilar(mySimHash_2);
                    if(similar > 0.5 && similar > tmpSimilar){
                        tmpPrefer = ontologyInfo.getName();
                        tmpSimilar = similar;
                    }
                }
                if(Objects.equals(tmpPrefer, "")) {
                    similarityCheckVO.setName(tableMetadataPO.getTableName());
                    similarityCheckVO.setNewFlag(true);
                    similarityCheckVO.setFuseFlag(false);
                } else {
                    similarityCheckVO.setName(tableMetadataPO.getTableName());
                    similarityCheckVO.setNewFlag(false);
                    similarityCheckVO.setReferedName(tmpPrefer);
                    similarityCheckVO.setSimilarity(tmpSimilar);
                    similarityCheckVO.setFuseFlag(false);
                }
                checkVOS.add(similarityCheckVO);
            }
        }
        return checkVOS;
    }

    // 完成元数据融合
    @Override
    public Result<String> comfirmFuse(String id, String topicAreaId, List<SimilarityCheckVO> checkVOS) {
        // 根据用户选择进行融合
        // 获取数据源信息
        DataSourceInfoPO dataSourceInfoPO = dataSourceInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("DataSource不存在"));
        // 获取数据源下的所有表结构信息
        List<TableMetadataPO> tableMetadataPOS = tableMetadataRepo.findAllByDataSourceId(id);
        // 判断是否存在已融合的数据源
        Boolean firstFuse = tableMetadataRepo.existsByTopicArea(topicAreaId);
        // 获取主题域信息
        TopicAreaPO topicAreaPO = topicAreaRepo.findById(topicAreaId).orElseThrow(() -> new RuntimeException("TopicArea不存在"));
        // 将数据源下的所有表结构信息插入到主题域下
        for(TableMetadataPO tableMetadataPO : tableMetadataPOS) {
            tableMetadataPO.setTopicArea(topicAreaPO);
            tableMetadataRepo.save(tableMetadataPO);
        }
        // 将数据源的融合标志设置为true
        dataSourceInfoPO.setFuseFlag(true);
        dataSourceInfoRepo.save(dataSourceInfoPO);
        // 将主题域的融合标志设置为true
        topicAreaPO.setFuseFlag(true);
        topicAreaRepo.save(topicAreaPO);
        for(SimilarityCheckVO checkVO : checkVOS) {
            // 如果是新属性，直接插入
            if(checkVO.getNewFlag()) {
                // TODO: 写到这里了
            }
        }

        return ResultUtil.success("融合成功");
    }


}
