document.getElementById("register-form").addEventListener("submit", async function (e) {
    e.preventDefault();  // 기본 폼 제출 동작 방지

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // 에러 메시지 요소
    const nameError = document.getElementById("name-error");
    const emailError = document.getElementById("email-error");
    const passwordError = document.getElementById("password-error");

    // 에러 메시지 초기화
    nameError.textContent = "";
    emailError.textContent = "";
    passwordError.textContent = "";

    try {
        const response = await fetch("http://localhost:9090/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: name,
                email: email,
                password: password
            })
        });

        if (response.status === 200) {
            // 성공 시 확인 메시지를 띄우고 로그인 화면으로 리다이렉트
            alert("회원가입이 완료되었습니다.");
            window.location.href = "/login.html";  // 성공 시 로그인 페이지로 리다이렉트
        } else if (response.status === 400) {
            const data = await response.json();
            console.log(data);  // 응답 데이터 확인

            // 서버로부터 받은 오류 메시지를 각 필드에 표시
            if (data.body.name) {
                nameError.textContent = data.body.name;
            }
            if (data.body.email) {
                emailError.textContent = data.body.email;
            }
            if (data.body.password) {
                passwordError.textContent = data.body.password;
            }
           // 이메일 중복 오류 메시지
        } else if (response.status === 409) {
            const data = await response.json();
            alert(data.msg || "이미 등록된 이메일입니다.");
        } else {
            alert("회원가입에 실패했습니다. 다시 시도하세요.");
        }
    } catch (error) {
        alert("서버와의 통신에 문제가 발생했습니다.");
    }
});

// 뒤로가기 버튼 기능
function goBack() {
    window.history.back();
}
