package com.istlab.datagovernanceplatform.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMetadata {
    private String columnName;
    private String dataType;
    private int length;
    private boolean isPrimaryKey;
    private boolean isForeignKey;
    private boolean isIndexed;
    private boolean isNotNull;
    private String defaultValue;
    private String checkConstraint;
    private String referencedTableName; // 外键关联的表名
    private String referencedColumnName; // 外键关联的列名
}
