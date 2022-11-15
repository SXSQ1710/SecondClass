package com.SecondClass.entity.R_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传递有效信息给校团委的实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_ShiAppInfo {
    Long aid;

    Long uid;

    @JsonProperty(value = "shi_app_description")
    String shiAppDescription;

    @JsonProperty(value = "shi_app_attachment")
    String shiAppAttachment;
}
