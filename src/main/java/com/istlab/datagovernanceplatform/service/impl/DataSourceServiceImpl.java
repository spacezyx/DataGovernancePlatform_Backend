package com.istlab.datagovernanceplatform.service.impl;

import com.istlab.datagovernanceplatform.pojo.dto.DataSourceInfoDTO;
import com.istlab.datagovernanceplatform.pojo.po.DataSourceInfoPO;
import com.istlab.datagovernanceplatform.pojo.vo.DataSourceManageVO;
import com.istlab.datagovernanceplatform.repository.DataSourceInfoRepo;
import com.istlab.datagovernanceplatform.service.DataSourceService;
import com.istlab.datagovernanceplatform.utils.Result;
import com.istlab.datagovernanceplatform.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceInfoRepo dataSourceInfoRepo;

    @Override
    public Result<String> verifyPostGreSQLConnection(DataSourceInfoDTO dto) throws UnsupportedEncodingException {
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
}
