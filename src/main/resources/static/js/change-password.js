document.addEventListener("DOMContentLoaded", function () {
    const changePasswordForm = document.getElementById("change-password-form");
    const errorMessage = document.getElementById("error-message");
    const successMessage = document.getElementById("success-message");

    changePasswordForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        const originalPassword = document.getElementById("originalPassword").value;
        const changePassword = document.getElementById("newPassword").value; // "newPassword"에서 "changePassword"로 변경

        // 에러 및 성공 메시지 초기화
        errorMessage.textContent = "";
        successMessage.textContent = "";

        try {
            const token = localStorage.getItem("access_token"); // 토큰 가져오기
            if (!token) {
                alert("로그인이 필요합니다.");
                window.location.href = "/login.html";
                return;
            }

            const response = await fetch("/users/change-password", {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token, // Bearer 토큰 포함
                },
                body: JSON.stringify({ originalPassword, changePassword }) // "newPassword" 대신 "changePassword"로 요청
            });

            const responseData = await response.json();

            if (response.ok) { // 상태 코드가 200이면 성공 메시지
                successMessage.textContent = "비밀번호가 성공적으로 변경되었습니다. 2초 후 로그인페이지 이동합니다.";
                setTimeout(() => {
                    localStorage.removeItem("access_token");
                    window.location.href = "/login.html";
                }, 2000);
            } else if (response.status === 400) {
                // 서버에서 받은 오류 메시지 출력
                errorMessage.textContent = responseData.msg || "비밀번호 변경에 실패했습니다.";
            }
        } catch (error) {
            console.error("Error:", error);
            errorMessage.textContent = "서버와의 통신에 문제가 발생했습니다.";
        }
    });
});

// 뒤로가기 버튼 기능
function goBack() {
    window.history.back();
}
