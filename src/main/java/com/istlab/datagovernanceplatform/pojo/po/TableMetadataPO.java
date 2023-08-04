package com.istlab.datagovernanceplatform.pojo.po;

import com.istlab.datagovernanceplatform.pojo.domain.ColumnMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TableMetadata")
public class TableMetadataPO {

//    tableName：表名
//    columns：包含 ColumnMetadata 对象的列表，用于表示表中的列信息
//    primaryKey：主键列的名称
//    foreignKeys：外键列的名称列表
//    indexes：索引列的名称列表
//    defaultSorting：默认排序的列名
//    tableComment：表的注释

    @Id
    private String id;
    private String tableName;
    private List<ColumnMetadata> columns;
    private String primaryKey;
    private List<String> foreignKeys;
    private List<String> indexes;
    private String tableComment;
    private String dataSourceId;
    private String topicArea;
}
