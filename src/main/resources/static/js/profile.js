document.addEventListener("DOMContentLoaded", function () {
    const profileName = document.getElementById("profile-name");
    const profileEmail = document.getElementById("profile-email");

    // 프로필 데이터를 서버에서 불러오기
    const fetchProfile = async () => {
        try {
            const token = localStorage.getItem("access_token"); // 토큰 가져오기
            if (!token) {
                alert("로그인이 필요합니다.");
                window.location.href = "/login.html";
                return;
            }

            const response = await fetch("/users/profile", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token, // Bearer 토큰 포함
                },
            });

            if (response.status === 200) {
                const data = await response.json();

                // 서버에서 받은 이름과 이메일 값을 화면에 표시
                profileName.textContent = data.body.name || 'NAME';
                profileEmail.textContent = data.body.email || 'EMAIL@EXAMPLE.COM';
            } else {
                console.error("프로필 정보를 불러오는데 실패했습니다.");
            }
        } catch (error) {
            console.error("서버와의 통신에 문제가 발생했습니다.");
        }
    };

    fetchProfile();

    // 뒤로가기 버튼 기능
    window.goBack = function () {
        window.history.back();
    };

    // 비밀번호 변경 페이지로 이동
    window.changePassword = function () {
        window.location.href = '/edit/change-password.html';
    };
});

// 뒤로가기 버튼 기능
function goBack() {
    window.history.back();
}
