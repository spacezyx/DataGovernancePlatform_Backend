package com.istlab.datagovernanceplatform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarityCheckVO {
    String name;
    String referedName;
    Double similarity;
    Boolean FuseFlag;
    Boolean NewFlag;
}
