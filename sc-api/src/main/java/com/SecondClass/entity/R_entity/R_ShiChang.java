package com.SecondClass.entity.R_entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R_ShiChang {
    Long uid;

    String shichangName;

    Integer totalNum;

    Date acquireTime;


}
