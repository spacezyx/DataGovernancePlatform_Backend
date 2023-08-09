package com.istlab.datagovernanceplatform.service.impl;

import com.istlab.datagovernanceplatform.pojo.domain.ColumnMetadata;
import com.istlab.datagovernanceplatform.pojo.domain.SelectList;
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
import java.util.*;

@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceInfoRepo dataSourceInfoRepo;

    @Autowired
    private TableMetadataRepo tableMetadataRepo;

    // 返回postgresql的连接
    public static Connection postGreSQLConnection(String host, Integer port, String database, String User, String Password) {
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

    @Override
    public List<SelectList> getSelectDatasourceList() {
        List<SelectList> selectLists = new ArrayList<>();
        List<DataSourceInfoPO> dataSourceInfoPOS = dataSourceInfoRepo.findAll();
        for(DataSourceInfoPO dataSourceInfoPO : dataSourceInfoPOS) {
            SelectList selectList = new SelectList(dataSourceInfoPO.getId(), dataSourceInfoPO.getName());
            selectLists.add(selectList);
        }
        return selectLists;
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
        // 每次抽取都重新更新这个datasource的元数据信息
        DataSourceInfoPO dataSourceInfoPO = dataSourceInfoRepo.findById(id).orElseThrow(() -> new RuntimeException("DataSourceInfoPO不存在"));

        Connection connection =  postGreSQLConnection(dataSourceInfoPO.getHost(), dataSourceInfoPO.getPort(), dataSourceInfoPO.getDatabase(),
                dataSourceInfoPO.getUser(), dataSourceInfoPO.getPassword());
        List<String> tableNames = getTableNames(connection);
        List<TableMetadataPO> tableMetadataPOS = new ArrayList<>();

        for(String tableName : tableNames) {
            try {
                TableMetadataPO tableMetadataPO = getTableMetadata(connection, tableName);
                tableMetadataPO.setDataSourceId(id);
//                tableMetadataPO.setTopicArea(topicArea);
                tableMetadataPOS.add(tableMetadataPO);
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
        dataSourceInfoPO.setLastExtractTime(timestamp);
        dataSourceInfoRepo.save(dataSourceInfoPO);
        tableMetadataRepo.deleteAllByDataSourceId(id);
        tableMetadataRepo.saveAll(tableMetadataPOS);
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
        String tableComment = getTableComment(connection, tableName);
        tableMetadata.setTableComment(tableComment);

        ResultSet columnsResult = metaData.getColumns(null, null, tableName, null);
        Map<String, ColumnMetadata> columnMetadataMap = new HashMap<>();
        while (columnsResult.next()) {
            String colName = columnsResult.getString("COLUMN_NAME");
            ColumnMetadata column = new ColumnMetadata();
            column.setColumnName(colName);
            column.setDataType(columnsResult.getString("TYPE_NAME"));
            column.setLength(columnsResult.getInt("COLUMN_SIZE"));
            column.setNullable(columnsResult.getBoolean("is_nullable"));
            columnMetadataMap.put(colName, column);
        }

        ResultSet primaryKeysResult = metaData.getPrimaryKeys(null, null, tableName);
        List<String> primaryKeys = new ArrayList<>();
        while (primaryKeysResult.next()) {
            String priKey = primaryKeysResult.getString("COLUMN_NAME");
            primaryKeys.add(priKey);
            columnMetadataMap.get(priKey).setPrimaryKey(true);
        }

        tableMetadata.setPrimaryKey(primaryKeys);
        ResultSet foreignKeysResult = metaData.getImportedKeys(null, null, tableName);
        List<String> foreignKeys = new ArrayList<>();
        while (foreignKeysResult.next()) {
            String colName = foreignKeysResult.getString("FKCOLUMN_NAME");
            foreignKeys.add(colName);
            columnMetadataMap.get(colName).setForeignKey(true);
            columnMetadataMap.get(colName).setReferencedTableName(foreignKeysResult.getString("pktable_name"));
            columnMetadataMap.get(colName).setReferencedColumnName(foreignKeysResult.getString("pkcolumn_name"));
        }
        tableMetadata.setForeignKeys(foreignKeys);

        ResultSet indexesResult = metaData.getIndexInfo(null, null, tableName, false, false);
        List<String> indexes = new ArrayList<>();
        while (indexesResult.next()) {
            String colName = indexesResult.getString("COLUMN_NAME");
            indexes.add(colName);
            columnMetadataMap.get(colName).setIndexed(true);
        }
        List<ColumnMetadata> columns = new ArrayList<>(columnMetadataMap.values());
        tableMetadata.setColumns(columns);
        tableMetadata.setIndexes(indexes);

        return tableMetadata;
    }

    private static String getTableComment(Connection connection, String tableName) throws SQLException {
        String tableComment = null;
        String query = "SELECT description FROM pg_description WHERE objoid=(SELECT oid FROM pg_class WHERE relname=?)";
        try (java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                tableComment = resultSet.getString("description");
            }
        }
        return tableComment;
    }
}
