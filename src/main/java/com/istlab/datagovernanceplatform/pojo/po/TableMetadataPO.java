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
    @Id
    private String id;
    private String tableName;
    private List<ColumnMetadata> columns;
    private List<String> primaryKey;
    private List<String> foreignKeys;
    private List<String> indexes;
    private String tableComment;
    private String dataSourceId;
    private TopicAreaPO topicArea;
}
