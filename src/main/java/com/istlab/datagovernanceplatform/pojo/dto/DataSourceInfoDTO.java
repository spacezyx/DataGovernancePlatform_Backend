package com.istlab.datagovernanceplatform.pojo.dto;

import com.istlab.datagovernanceplatform.pojo.po.DataSourceInfoPO;
import lombok.Data;

@Data
public class DataSourceInfoDTO {
    String name;
    String comment;
    String host;
    Integer port;
    String user;
    String password;
    String database;
    String dataType;
    String databaseType;
}
