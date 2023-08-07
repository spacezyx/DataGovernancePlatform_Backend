package com.istlab.datagovernanceplatform.service;

import com.istlab.datagovernanceplatform.pojo.domain.SelectList;
import com.istlab.datagovernanceplatform.pojo.po.TableMetadataPO;
import com.istlab.datagovernanceplatform.pojo.dto.DataSourceInfoDTO;
import com.istlab.datagovernanceplatform.pojo.vo.DataSourceManageVO;
import com.istlab.datagovernanceplatform.pojo.vo.DatasourceListVO;
import com.istlab.datagovernanceplatform.utils.Result;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface DataSourceService {
    Result<String> insertDataSource(DataSourceInfoDTO dto) throws UnsupportedEncodingException;
    Result<String> verifyPostGreSQLConnection(DataSourceInfoDTO dto) throws UnsupportedEncodingException;
    List<DataSourceManageVO> getDataSourceManagementList();
    Result<String> deleteDataSourcePO(String id);
    List<DatasourceListVO> getDatasourceList();
    TableMetadataPO getTableMetadataById(String id);
    Result<String> extractMetadata(String id, String topicArea) throws Exception;
    List<SelectList> getSelectDatasourceList();
}
