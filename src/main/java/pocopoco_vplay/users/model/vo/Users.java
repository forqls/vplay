package pocopoco_vplay.users.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users {
	private int userNo;
    private String userId;
    private String userPw;
    private String userNickname;
    private String userName;
    private String userPhone;
    private String userEmail;
    private Date userBirth;
    private String userGender;
    private String status;
    private String isAdmin;
    private Date joinDate;
    private String userComment;
    private String userCreatorRank;
    private String userPlan;
    private String userProfile;
}
