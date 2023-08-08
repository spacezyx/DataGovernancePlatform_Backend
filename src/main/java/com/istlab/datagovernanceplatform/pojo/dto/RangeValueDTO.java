package com.istlab.datagovernanceplatform.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class RangeValueDTO {
    String id;
    String table;
    String column;
    List<String> attr;
}
