package com.istlab.datagovernanceplatform.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TopicArea")
public class TopicAreaPO {
    @Id
    private String id;
    private String name;
    private String description;
}
