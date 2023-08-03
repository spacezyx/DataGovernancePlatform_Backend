package com.istlab.datagovernanceplatform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasourceListVO {
    String id;
    String name;
    String comment;
    String host;
    Integer port;
    String user;
    String database;
    String dataType;
    String databaseType;

    String address;
    Boolean extractFlag;
    Timestamp lastExtractTime;
    Boolean fuseFlag;
    Timestamp lastFuseTime;
}
