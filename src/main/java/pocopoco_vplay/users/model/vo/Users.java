package pocopoco_vplay.users.model.vo;

import lombok.*;

import java.util.Date;

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
    private Date userBrith;
    private String userGender;
    private String status;
    private String isAdmin;
    private Date joinDate;
    private String userComment;
    private String userCreatorRank;
    private String userPlan;
    private String userProfile;
}
