package com.istlab.datagovernanceplatform.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstanceInfo {
    private String name;
    private String comments;
    private String position;
    Map<String, List<AttrInfo>> attrs;
    List<String> tags;
}
