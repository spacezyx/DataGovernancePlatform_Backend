package com.istlab.datagovernanceplatform.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "DataSourceInfo")
public class DataSourceInfoPO {
    @Id
    String id;
    String name;
    String comment;
    String host;
    Integer port;
    String user;
    String password;
    String database;
    String dataType;
    String databaseType;
    Boolean extractFlag;
    Timestamp lastExtractTime;
    Boolean fuseFlag;
    Timestamp lastFuseTime;

//    public enum DataTypeEumn {
//        STRUCTURED, SEMI_STRUCTURED, UNSTRUCTURED
//    }
//
//    public enum DatabaseTypeEumn {
//        PostgreSQL, SQLServer, MySQL, MongoDB, NOT_DB
//    }

    public DataSourceInfoPO(String name, String comment, String host, Integer port, String user, String password, String database,
                            String dataType, String databaseType) {
        this.name = name;
        this.comment = comment;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.dataType = dataType;
        this.databaseType = databaseType;
        this.extractFlag = false;
        this.lastExtractTime = null;
        this.fuseFlag = false;
        this.lastFuseTime = null;
    }

}
