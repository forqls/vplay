document.addEventListener("DOMContentLoaded", function() {
	function updateUnreadMessageCount() {
		let messageBadge = document.getElementById("unreadMessageCount");

		if (!messageBadge) {
			console.error("🚨 `unreadMessageCount` 요소를 찾을 수 없음!");
			return;
		}

		fetch("/users/unreadMessages")
			.then(response => response.json())
			.then(count => {
				console.log("🚀 새 메시지 개수:", count);

				if (count > 0) {
					messageBadge.innerText = count;
					messageBadge.classList.remove("hidden");
				} else {
					messageBadge.classList.add("hidden");
				}
			})
			.catch(error => console.error("🚨 쪽지 개수 조회 오류:", error));
	}
	updateUnreadMessageCount();
	setInterval(updateUnreadMessageCount, 30000);
});