package pocopoco_vplay.users.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.vo.Message;
import pocopoco_vplay.users.model.vo.Users;

@Mapper
public interface UsersMapper {

	int checkId(String id);

	Users signIn(Users user);

	int insertUser(Users user);

	ArrayList<Content> selectMyProject(int id);

	ArrayList<Content> selectMyRealProjects(int userNo);

	int selectuserIdPhone(@Param("userName") String userName, @Param("userPhone") String userPhone);

	int findfollow(Users user);

	String findId(Users users);

	int updateInfo(Users user);

	int changePw(Users user);

	int findPw(Users Users);

	int tempPwd(Users Users);

	String findName(Users users);

	Users existUsers(String kakaoId);

	ArrayList<Users> selectTopUser();

	int updateUserPlan(HashMap<String, Object> map);

	Object getPaymentDate(Users loginUser);

	int deleteUserPlan(Users loginUser);

	int updateAlertShown(int userNo);

	int resetAlertShown(int userNo);

	int updateProfile(HashMap<String, String> map);

	String selectProfile(HashMap<String, String> map);

	Users existGoogleUsers(String googleId);

	ArrayList<Message> selectMyMessage(String userNo);

	int updateMessageStatus(String messageNo);

	int existReceiver(String receiverName);

	int insertMessage(Message msg);

	int getReceiverNo(String receiverName);

	int getUnreadMessageCount(int userNo);

	ArrayList<Content> selectMyDownloads(int userNo);


    ArrayList<Users> selectSubscribeList(int userNo);

	Users getInfoUser(int createrNo);

	int isSubscribed(@Param("createrNo") int createrNo, @Param("userNo") int userNo);

	int updateSubscribe(HashMap<String, Object> map);

    int checkPhone(String userPhone);

	int checkEmail(String userEmail);

	int getSubscriptionCount(Map<String, Object> map);
}
