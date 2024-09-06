document.getElementById("login-form").addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("error-message");

    // 기존의 에러 메시지 초기화
    errorMessage.textContent = "";
    document.querySelectorAll('.error-msg').forEach(el => el.remove());

    try {
        const response = await fetch("/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        if (response.status === 200) {
            // 토큰 처리
            const accessToken = response.headers.get("Access_token");
            if (accessToken) {
                const token = accessToken.startsWith('Bearer') ? accessToken : `Bearer ${accessToken}`;
                localStorage.setItem("access_token", token);
                window.location.href = "/post/newsfeed.html";
            } else {
                errorMessage.textContent = "토큰을 가져오는 데 실패했습니다.";
            }
        } else if (response.status === 400) {
            const errorData = await response.json();

            // 'body' 필드가 비어 있지 않은 경우에만 유효성 검사 처리
            if (errorData.body && Object.keys(errorData.body).length > 0) {
                if (errorData.body.email) {
                    displayErrorMessage("email", errorData.body.email);
                }
                if (errorData.body.password) {
                    displayErrorMessage("password", errorData.body.password);
                }
            }
            // 'body'가 비어 있으면 'msg' 필드에서 오류 메시지를 표시
            else if (errorData.msg) {
                errorMessage.textContent = errorData.msg;
            }
        }
    } catch (error) {
        errorMessage.textContent = "서버와의 통신에 문제가 발생했습니다.";
    }
});

function displayErrorMessage(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorElement = document.createElement('p');
    errorElement.classList.add('text-red-500', 'text-sm', 'mt-2', 'error-msg');
    errorElement.textContent = message;
    field.insertAdjacentElement('afterend', errorElement);
}

// 회원가입 페이지로 이동
function redirectToRegister() {
    window.location.href = "register.html";
}
