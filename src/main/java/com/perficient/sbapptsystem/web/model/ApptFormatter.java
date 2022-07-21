package com.perficient.sbapptsystem.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by RA on 06-30-2022.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApptFormatter {

    private String id;

    private String apptName;

    private ApptTypeEnum apptType;

    private String description;

    private String startTime;

    private String endTime;

    private String metadata;

    private String userId;

}
