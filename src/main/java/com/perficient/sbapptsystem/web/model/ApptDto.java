package com.perficient.sbapptsystem.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // Choose appropriate data type (map?)
    private String metadata;

    private String userId;

}
