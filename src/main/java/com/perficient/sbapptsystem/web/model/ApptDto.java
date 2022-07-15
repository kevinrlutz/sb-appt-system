package com.perficient.sbapptsystem.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Created by RA on 06-30-2022.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApptDto {

    // Note: UUID is not accepted by MongoDB. Use String instead.
    private String id;

    private String apptName;

    private ApptTypeEnum apptType;

    private String description;

    private LocalTime startTime;

    private LocalTime endTime;

    // Choose appropriate data type (map?)
    private String metadata;

}
