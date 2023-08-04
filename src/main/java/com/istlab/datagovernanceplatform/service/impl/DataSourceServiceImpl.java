package com.istlab.datagovernanceplatform.service.impl;

import com.istlab.datagovernanceplatform.pojo.domain.ColumnMetadata;
import com.istlab.datagovernanceplatform.pojo.dto.DataSourceInfoDTO;
import com.istlab.datagovernanceplatform.pojo.po.DataSourceInfoPO;
import com.istlab.datagovernanceplatform.pojo.po.TableMetadataPO;
import com.istlab.datagovernanceplatform.pojo.vo.DataSourceManageVO;
import com.istlab.datagovernanceplatform.pojo.vo.DatasourceListVO;
import com.istlab.datagovernanceplatform.repository.DataSourceInfoRepo;
import com.istlab.datagovernanceplatform.repository.TableMetadataRepo;
import com.istlab.datagovernanceplatform.service.DataSourceService;
import com.istlab.datagovernanceplatform.utils.Result;
import com.istlab.datagovernanceplatform.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceInfoRepo dataSourceInfoRepo;

    @Autowired
    private TableMetadataRepo tableMetadataRepo;

    // 返回postgresql的连接
    private Connection postGreSQLConnection(String host, Integer port, String database, String User, String Password) {
        Connection connection;
        String URL = "jdbc:postgresql://" + host + ":" + port + "/";
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL + database, User, Password);
            if (connection != null) {
                return connection;
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            // TODO: 输出会乱码
            return null;
        }
        return null;
    }

    // 验证是否存在postgresql数据库
    @Override
    public Result<String> verifyPostGreSQLConnection(DataSourceInfoDTO dto) {
        Connection connection;
        String host = dto.getHost();
        Integer port = dto.getPort();
        String database = dto.getDatabase();
        String URL = "jdbc:postgresql://" + host + ":" + port + "/";
        String User = dto.getUser();
        String Password = dto.getPassword();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL + database, User, Password);

            if (connection != null) {
                return ResultUtil.success();
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            // TODO: 输出会乱码
            return ResultUtil.failure(e.toString(), -1);
        }
        return ResultUtil.success();
    }


    // 保存数据源信息
    @Override
    public Result<String> insertDataSource(DataSourceInfoDTO dto) throws UnsupportedEncodingException {
        Result<String> result = verifyPostGreSQLConnection(dto);
        if(dataSourceInfoRepo.existsByName(dto.getName())) {
            return ResultUtil.failure("新增数据源： " + dto.getName() + "失败, 已存在同名数据源，请修改数据源名称", -1);
        }
        if(dataSourceInfoRepo.existsByHostAndPortAndDatabase(dto.getHost(), dto.getPort(), dto.getDatabase())) {
            DataSourceInfoPO po = dataSourceInfoRepo.getDataSourceInfoPOByHostAndPortAndDatabase(dto.getHost(), dto.getPort(), dto.getDatabase());
            return ResultUtil.failure("新增数据源： " + dto.getName() + "失败, postgresql://"
                    + dto.getHost() + ":" + dto.getPort() + "/" + dto.getDatabase()  +"已存在，数据源名称:  " + po.getName(), -1);
        }
        if (result.getCode() == 0) {
            DataSourceInfoPO dataSourceInfoPO = new DataSourceInfoPO(dto.getName(), dto.getComment(), dto.getHost(), dto.getPort(), dto.getUser(),
                    dto.getPassword(), dto.getDatabase(), dto.getDataType(), dto.getDatabaseType());
            dataSourceInfoRepo.save(dataSourceInfoPO);
            return ResultUtil.success("新增数据源： " + dto.getName() + " 成功");
        }
        else {
            return ResultUtil.failure("新增数据源失败, 请验证数据源配置信息是否正确", -1);
        }
    }

    @Override
    public List<DataSourceManageVO> getDataSourceManagementList(){
        List<DataSourceManageVO> res = new ArrayList<>();
        List<DataSourceInfoPO> pos = dataSourceInfoRepo.findAll();
        for(DataSourceInfoPO tmp : pos) {
            DataSourceManageVO vo = new DataSourceManageVO(tmp.getId(), tmp.getName(), tmp.getComment(), tmp.getHost(),
                    tmp.getPort(), tmp.getUser(), tmp.getDatabase(), tmp.getDataType(), tmp.getDatabaseType());
            res.add(vo);
        }
        return res;
    }

    @Override
    public Result<String> deleteDataSourcePO(String id) {
        if(dataSourceInfoRepo.existsById(id)) {
            dataSourceInfoRepo.deleteById(id);
            return ResultUtil.success();
        }
        else {
            return ResultUtil.failure("不存在此数据源，删除失败", -1);
        }
    }

    @Override
    public List<DatasourceListVO> getDatasourceList() {
        List<DatasourceListVO> res = new ArrayList<>();
        List<DataSourceInfoPO> pos = dataSourceInfoRepo.findAll();
        for(DataSourceInfoPO tmp : pos) {
            String address = tmp.getHost() + ":" + tmp.getPort() + "/" + tmp.getDatabase();
            DatasourceListVO vo = new DatasourceListVO(tmp.getId(), tmp.getName(), tmp.getComment(),tmp.getHost(),
                    tmp.getPort(), tmp.getUser(), tmp.getDatabase(),
                    tmp.getDataType(), tmp.getDatabaseType(), address, tmp.getExtractFlag(), tmp.getLastExtractTime(),
                    tmp.getFuseFlag(), tmp.getLastFuseTime());
            res.add(vo);
        }
        return res;
    }

    // 获取某张表的元数据
    @Override
    public TableMetadataPO getTableMetadataById(String id) {
        return tableMetadataRepo.findById(id).orElseThrow(() -> new RuntimeException("TableMetadata不存在"));
    }

    // postgresql元数据抽取
    @Override
    public Result<String> extractMetadata(String id) throws Exception {
        DataSourceInfoPO dataSourceInfoPO = dataSourceInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("DataSourceInfoPO不存在"));

        Connection connection =  postGreSQLConnection(dataSourceInfoPO.getHost(), dataSourceInfoPO.getPort(), dataSourceInfoPO.getDatabase(),
                dataSourceInfoPO.getUser(), dataSourceInfoPO.getPassword());
        List<String> tableNames = getTableNames(connection);

        for(String tableName : tableNames) {
            try {
                TableMetadataPO tableMetadataPO = getTableMetadata(connection, tableName);
                tableMetadataRepo.save(tableMetadataPO);
            } catch (Exception e) {
                e.printStackTrace();
                connection.close();
                return ResultUtil.success("数据源: " + dataSourceInfoPO.getName() + " 元数据抽取发生未知错误" );
            }
        }

        connection.close();
        //修改上次抽取时间和抽取Flag
        dataSourceInfoPO.setExtractFlag(true);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return ResultUtil.success("数据源: " + dataSourceInfoPO.getName() + " 元数据抽取成功,成功导入" + tableNames.size() + "个表" );
    }

    // postgresql获取所有表名
    private List<String> getTableNames(Connection connection) {
        List<String> res = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();

            String query = "SELECT table_name FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema = 'public'";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                res.add(tableName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    // postgresql获取表结构
    private static TableMetadataPO getTableMetadata(Connection connection, String tableName) throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        TableMetadataPO tableMetadata = new TableMetadataPO();
        tableMetadata.setTableName(tableName);

        ResultSet columnsResult = metaData.getColumns(null, null, tableName, null);
        List<ColumnMetadata> columns = new ArrayList<>();
        while (columnsResult.next()) {
            ColumnMetadata column = new ColumnMetadata();
//            private String columnName;
//            private String dataType;
//            private int length;
//            private boolean isPrimaryKey;
//            private boolean isForeignKey;
//            private boolean isIndexed;
//            private boolean isNotNull;
//            private String defaultValue;
//            private String checkConstraint;
//            private String referencedTableName; // 外键关联的表名
//            private String referencedColumnName; // 外键关联的列名
            log.info(String.valueOf(columnsResult));
            column.setColumnName(columnsResult.getString("COLUMN_NAME"));
            column.setDataType(columnsResult.getString("TYPE_NAME"));
            column.setLength(columnsResult.getInt("COLUMN_SIZE"));
            columns.add(column);
        }
        tableMetadata.setColumns(columns);

        ResultSet primaryKeysResult = metaData.getPrimaryKeys(null, null, tableName);
        if (primaryKeysResult.next()) {
            tableMetadata.setPrimaryKey(primaryKeysResult.getString("COLUMN_NAME"));
        }

        ResultSet foreignKeysResult = metaData.getImportedKeys(null, null, tableName);
        List<String> foreignKeys = new ArrayList<>();
        while (foreignKeysResult.next()) {
            foreignKeys.add(foreignKeysResult.getString("FKCOLUMN_NAME"));
        }
        tableMetadata.setForeignKeys(foreignKeys);

        ResultSet indexesResult = metaData.getIndexInfo(null, null, tableName, false, false);
        List<String> indexes = new ArrayList<>();
        while (indexesResult.next()) {
            indexes.add(indexesResult.getString("COLUMN_NAME"));
        }
        tableMetadata.setIndexes(indexes);

        return tableMetadata;
    }
}
