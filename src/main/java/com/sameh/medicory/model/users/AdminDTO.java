package com.sameh.medicory.model.users;

import com.sameh.medicory.entity.enums.Gender;
import com.sameh.medicory.entity.enums.MaritalStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AdminDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private MaritalStatus maritalStatus;
    private Gender gender;

    private UserDTO user;

}
