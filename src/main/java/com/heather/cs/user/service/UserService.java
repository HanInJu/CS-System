package com.heather.cs.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.heather.cs.code.mapper.CodeMapper;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	private final CodeMapper codeMapper;

	@Transactional
	public void registerUser(User user) {
		if (userMapper.selectExistsUserId(user.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicated Id");
		}

		user.setRole("COUNSELOR");
		user.setStatus("AVAILABLE");
		user.setUseYn("Y");
		user.setCreatorId(user.getId());
		user.setModifierId(user.getId());

		userMapper.insertUser(user);
		userMapper.insertUserHistory(user.getId());
	}

	public String signIn(String id, String password) {
		// id를 통해서 유저정보를 가져와서 비밀번호를 비교해줄 수도 있지.
		// 이런 식으로 하면 둘 중 하나가 잘못됐다고밖에 알 수 없는데, 정확히 뭐가 잘못됐는지 알려면 위 방법대로 바꿔야 한다.
		// 한 유저가 몇 번 시도하면 막히는 기능 구현할 수 없음. 위 방법으로 변경 요함
		User user = new User();
		user.setId(id);
		user.setPassword(password); // 이렇게 할 거면 setter 보다 builder 패턴을 씀 요즘 setter 지양하는 경향이 있음
		if (!userMapper.selectExistsUserHasThisInformation(user)) { // 매퍼 메소드가 너무 길다. 로그인을 의미하기만 하면 되지
			// 매퍼메소드에서 3개 정도까지는 그냥 넣음(!)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id or Password is not correct");
			// 정확히 정보를 줘야 한다.
		}
		return id; // 이럴 필요 없음. 받은 걸 굳이 또다시 던질 이유가?
	}

	// 매니저도 상태 온오프가 있을 수 있으니까 role까지 검사할 필요가 없다.
	/* Code/Reformat Code : 포맷을 잘 지켜야 하고, 이것 때문에 바뀌는 일도 있어 */
	@Transactional
	public void changeTheCounselorStatus(String userId, String state) {
		// 컨트롤러에서 로그인 여부를 체크했어야 함.(쿠키로)
		// 유효성검삭 3개 정도 되는데, 너무 많음 -> 따로 함수로 빼는 편 (유효성검사하다가 너무 길어지니까)

		if (!userMapper.selectExistsUserId(userId)) {
			// 조건에 !가 있는데 잘 안보인다는 의견이 있음... isNot으로 메서드 빼는 사람도 있음 (내 취향은 어떤건지?)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exist : userId = " + userId);
		}

		String userRole = userMapper.selectUserRole(userId);
		if (!userRole.equals("COUNSELOR")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"The user is not a counselor : userId = " + userId);
		}

		String GROUP_CODE_OF_COUNSELOR = "COUNSELOR_STATUS"; // 상수를 지역변수로 선언한 게 이상함.
		String groupCode = codeMapper.selectGroupCode(state); // 그룹코드는 안바뀌는 상수니까 Enum으로 관리할 수 있다.
		// 보통은 레디스에 빼놓는데 지금은 안하니까 Enum으로 관리(바뀔 때마다 변경해야 한다는 단점이 있지만 지금은...)
		// EH 캐시? selectGroupCode를 부르는 걸 캐시로 잡을 수도 있음
		// 아니면 Enum 정도로 처리해도 큰 문제는 없을 것 같음
		if (!groupCode.equals(GROUP_CODE_OF_COUNSELOR)) { // 보통 코드를 체크하지 그룹코드(상위)를 체크하지는 않는다.
			// 만약 이걸 하려면 컨트롤러 단에서 체크해줬어어ㅑ 함. (체크하는 방법 여러 개 있음 더 간단한 방법 있을수도)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"The state value that a counselor cannot have : state = " + state);
		}

		User user = userMapper.selectUser(userId);
		String status = user.getStatus();
		if (status.equals(state)) {
			// Conflict 보다 BadRequest 가 더 적절.
			throw new ResponseStatusException(HttpStatus.CONFLICT,
				"The counselor's status is already " + state + " state : " + state);
		}

		user.setStatus(state);
		userMapper.updateStatus(user); // 매개변수 두 개 넘기는 정도인데 문제 없을 걸(4개부터 객체로 가면 좋음)
		userMapper.insertUserHistory(userId);

	}

}
