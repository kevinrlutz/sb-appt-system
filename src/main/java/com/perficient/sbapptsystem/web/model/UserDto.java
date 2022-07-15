package com.perficient.sbapptsystem.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    static final long serialVersionUID = 165543461805275707L;

    private String id;

    private String firstName;
    private String lastName;

    private String gender;
    private Integer age;

    private String email;
    private String phoneNumber;

    private List<ApptDto> appointmentList;
}
