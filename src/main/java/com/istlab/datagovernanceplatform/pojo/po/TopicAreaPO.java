package com.istlab.datagovernanceplatform.pojo.po;

import com.istlab.datagovernanceplatform.pojo.domain.OntologyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TopicArea")
public class TopicAreaPO {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean FuseFlag;
    private List<OntologyInfo> ontologyInfoList;
}
