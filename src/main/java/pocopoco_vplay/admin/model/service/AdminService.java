package pocopoco_vplay.admin.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.admin.model.mapper.AdminMapper;
import pocopoco_vplay.admin.model.vo.PageInfo;
import pocopoco_vplay.users.model.vo.Users;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final AdminMapper mapper;

	public int getUsersCount() {
		return mapper.getUsersCount();
	}

	public ArrayList<Users> selectAllUser(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1)* pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectAllUser(rowBounds);
	}


}
