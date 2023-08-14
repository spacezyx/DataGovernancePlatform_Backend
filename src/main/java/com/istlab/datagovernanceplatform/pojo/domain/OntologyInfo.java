package com.istlab.datagovernanceplatform.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OntologyInfo {
    String id;
    String name;
    String topicAreaId;
    List<InstanceInfo> tables;
}
