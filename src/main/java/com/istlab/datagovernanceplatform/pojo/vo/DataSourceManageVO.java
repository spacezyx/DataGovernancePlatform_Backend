package com.istlab.datagovernanceplatform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceManageVO {
    String id;
    String name;
    String comment;
    String host;
    Integer port;
    String user;
    String database;
    String dataType;
    String databaseType;
}
