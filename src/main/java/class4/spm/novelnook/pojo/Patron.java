package class4.spm.novelnook.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patron {

    int userid;
    String password;
    String firstname;
    String lastname;
    String telephone;
    String email;
    String avatar;
}
