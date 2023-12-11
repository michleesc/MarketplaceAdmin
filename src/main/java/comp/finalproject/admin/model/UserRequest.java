package comp.finalproject.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String name;
    private String email;
    private String password;
    @ApiModelProperty(notes = "User roles (ROLE_USER or ROLE_ADMIN)", allowableValues = "ROLE_USER, ROLE_ADMIN")
    private String roles;
}
