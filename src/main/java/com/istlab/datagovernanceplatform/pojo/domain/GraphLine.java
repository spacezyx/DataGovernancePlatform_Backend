package com.istlab.datagovernanceplatform.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.json.JsonObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphLine {
    String from;
    String to;
    String text;
    String color;
    String fontColor;
}
