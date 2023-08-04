package com.istlab.datagovernanceplatform.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMetadata {

//    columnName：列名
//    dataType：数据类型
//    length：数据长度
//    isPrimaryKey：是否为主键
//    isForeignKey：是否为外键
//    referencedTableName：外键关联的表名
//    referencedColumnName：外键关联的列名
//    isIndexed：是否为索引列
//    isNotNull：是否为非空列
//    defaultValue：默认值
//    checkConstraint：检查约束条件

    private String columnName;
    private String dataType;
    private int length;
    private boolean isPrimaryKey;
    private boolean isForeignKey;
    private boolean isIndexed;
    private boolean isNotNull;

    // 这个东西怎么抽取
    private String defaultValue;
    private String checkConstraint;
    private String referencedTableName; // 外键关联的表名
    private String referencedColumnName; // 外键关联的列名
}
