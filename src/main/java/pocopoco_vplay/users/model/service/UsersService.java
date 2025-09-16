package pocopoco_vplay.users.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.mapper.UsersMapper;
import pocopoco_vplay.users.model.vo.Message;
import pocopoco_vplay.users.model.vo.Users;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

	private final UsersMapper mapper;

	public int checkId(String id) {
		return mapper.checkId(id);
	}

	public Users signIn(Users user) {
		return mapper.signIn(user);
	}

	public int insertUser(Users user) {
		return mapper.insertUser(user);
	}

	public ArrayList<Content> selectMyProjects(int id) {
		return mapper.selectMyProject(id);
	}

	public ArrayList<Content> selectMyRealProjects(int userNo) {
//		System.out.println(mapper.selectMyRealProjects(userNo));
		return mapper.selectMyRealProjects(userNo);
	}

	public int selectIdPhone(String userName, String userPhone) {
		return mapper.selectuserIdPhone(userName, userPhone);
	}

	public int findfollow(Users user) {
		return mapper.findfollow(user);
	}

	public int updateInfo(Users user) {
		return mapper.updateInfo(user);
	}

	public String findId(Users users) {
		return mapper.findId(users);
	}

	public int changePw(Users user) {
		return mapper.changePw(user);
	}

	public int findPw(Users users) {
		return mapper.findPw(users);
	}

	public int encodePwd(Users users) {
		return mapper.tempPwd(users);
	}

	public String findName(Users users) {
		return mapper.findName(users);
	}

	public Users existUsers(String kakaoId) {
		return mapper.existUsers(kakaoId);
	}

	public ArrayList<Users> selectTopUser() {
		return mapper.selectTopUser();
	}

	public int updateUserPlan(HashMap<String, Object> map) {
		return mapper.updateUserPlan(map);
	}

	public Object getPaymentDate(Users loginUser) {
		return mapper.getPaymentDate(loginUser);

	}

	public int deleteUserPlan(Users loginUser) {
		return mapper.deleteUserPlan(loginUser);
	}

	public int updateAlertShown(int userNo) {
		return mapper.updateAlertShown(userNo);

	}

	public int resetAlertShown(int userNo) {
		int result = mapper.resetAlertShown(userNo);
		System.out.println("서비스에서 의 값 : " + result);
		return result;

	}

	public int updateProfile(HashMap<String, String> map) {
		return mapper.updateProfile(map);
	}

	public String selectProfile(HashMap<String, String> map) {
		return mapper.selectProfile(map);
	}

	public Users existGoogleUsers(String googleId) {
		return mapper.existGoogleUsers(googleId);
	}

	public ArrayList<Message> selectMyMessage(String userNo) {
		return mapper.selectMyMessage(userNo);
	}

	public int updateMessageStatus(String messageNo) {
		return mapper.updateMessageStatus(messageNo);
	}

	public int existReceiver(String receiverName) {
		return mapper.existReceiver(receiverName);
	}

	public int insertMessage(Message msg) {
		return mapper.insertMessage(msg);
	}

	public int getReceiverNo(String receiverName) {
		return mapper.getReceiverNo(receiverName);
	}

	public int getUnreadMessageCount(int userNo) {
		return mapper.getUnreadMessageCount(userNo);
	}

	public ArrayList<Content> selectMyDownloads(int userNo) {
		return mapper.selectMyDownloads(userNo);
	}

    public ArrayList<Users> selectSubscribeList(int userNo) {
		return mapper.selectSubscribeList(userNo);
    }

	public Users getInfoUser(int createrNo) {
		return mapper.getInfoUser(createrNo);
	}

	public boolean isSubscribed(int createrNo, int userNo) {
		return mapper.isSubscribed(createrNo, userNo) == 1;
	}

	public int updateSubscribe(Map<String, Object> map) throws UsersException {
		try {
			Integer isCancelInt = (Integer) map.get("isCancel");
			boolean isCancel = (isCancelInt != null) && (isCancelInt > 0);
			int result = 0;

			if (!isCancel) { // 구독하려는 경우
				// 이미 구독 중인지 확인
				if (mapper.getSubscriptionCount(map) > 0) {
					// 이미 구독 중이면 아무것도 하지 않음 (중복 구독 방지)
					return 0;
				}
			}

			// 구독하지 않은 상태라면 insert, 구독 중이라면 delete
			result = mapper.updateSubscribe(map);
			return result;

		} catch (Exception e) {
			throw new UsersException("구독 처리 실패: " + e.getMessage());
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = new Users();
		user.setUserId(username);

		Users foundUser = mapper.signIn(user);

		if (foundUser == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
		}

		return User.builder()
				.username(foundUser.getUserId())
				.password(foundUser.getUserPw())
				.roles("Y".equals(foundUser.getIsAdmin()) ? "ADMIN" : "USER")
				.build();
	}

	public int checkPhone(String userPhone) {
		return mapper.checkPhone(userPhone);
	}

	public int checkEmail(String userEmail) {
		return mapper.checkEmail(userEmail);
	}
}