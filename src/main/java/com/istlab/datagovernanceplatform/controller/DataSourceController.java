package com.istlab.datagovernanceplatform.controller;

import com.istlab.datagovernanceplatform.pojo.dto.DataSourceInfoDTO;
import com.istlab.datagovernanceplatform.pojo.vo.DataSourceManageVO;
import com.istlab.datagovernanceplatform.pojo.vo.DatasourceListVO;
import com.istlab.datagovernanceplatform.service.DataSourceService;
import com.istlab.datagovernanceplatform.utils.Result;
import com.istlab.datagovernanceplatform.utils.ResultUtil;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@RestController
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @PostMapping(value = "/datasource/verify")
    public Result<String> createDatabase(@RequestBody DataSourceInfoDTO dto) throws UnsupportedEncodingException {
        if(Objects.equals(dto.getDatabaseType(), "PostgreSQL")) {
            return dataSourceService.verifyPostGreSQLConnection(dto);
        }
        else {
            return ResultUtil.failure("Datasource information error", -1);
        }
    }

    @PostMapping(value = "/datasource/insert")
    public Result<String> insertDataSource(@RequestBody DataSourceInfoDTO dto) throws UnsupportedEncodingException {
        if(Objects.equals(dto.getDatabaseType(), "PostgreSQL")) {
            return dataSourceService.insertDataSource(dto);
        }
        else {
            return ResultUtil.failure("Datasource information error", -1);
        }
    }

    @GetMapping(value = "/datasource/getManageVO")
    public List<DataSourceManageVO> getManageVO(){
       return dataSourceService.getDataSourceManagementList();
    }

    @DeleteMapping(value = "/datasource/delete/{id}")
    public Result<String> delete(@PathVariable String id){
        return dataSourceService.deleteDataSourcePO(id);
    }

    @GetMapping(value = "/datasource/getDatasourceList")
    public List<DatasourceListVO> getDatasourceList(){
        return dataSourceService.getDatasourceList();
    }

    @PostMapping(value = "/datasource/extractMetadata/{id}")
    public Result<String> extractMetadata(@PathVariable String id) throws Exception {
        return dataSourceService.extractMetadata(id);
    }


}
