document.addEventListener("DOMContentLoaded", function () {
    const createPostForm = document.getElementById("create-post-form");
    const errorMessage = document.getElementById("error-message");

    // 에러 메시지를 표시할 요소 추가
    const titleError = document.createElement('p');
    titleError.id = 'title-error';
    titleError.classList.add('text-red-500', 'text-sm', 'mb-2');
    document.getElementById("title").insertAdjacentElement('afterend', titleError);

    const contentError = document.createElement('p');
    contentError.id = 'content-error';
    contentError.classList.add('text-red-500', 'text-sm', 'mb-2');
    document.getElementById("content").insertAdjacentElement('afterend', contentError);

    createPostForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        const title = document.getElementById("title").value;
        const content = document.getElementById("content").value;

        // 에러 메시지 초기화
        errorMessage.textContent = "";
        titleError.textContent = "";
        contentError.textContent = "";

        try {
            const token = localStorage.getItem("access_token"); // 토큰 가져오기
            if (!token) {
                alert("로그인이 필요합니다.");
                window.location.href = "/login.html";
                return;
            }

            const response = await fetch("/boards", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token, // Bearer 토큰 포함
                },
                body: JSON.stringify({ title, content })
            });

            if (response.status === 200) {
                window.location.href = "/post/newsfeed.html"; // 게시글 작성 성공 후 이동
            } else if (response.status === 400) {
                const errorData = await response.json();

                // 필드별 유효성 검사 오류 메시지 처리
                if (errorData.body.title) {
                    titleError.textContent = errorData.body.title;
                }
                if (errorData.body.content) {
                    contentError.textContent = errorData.body.content;
                }

            }
        } catch (error) {
            errorMessage.textContent = "서버와의 통신에 문제가 발생했습니다.";
        }
    });
});

// 뒤로가기 버튼 기능
function goBack() {
    window.history.back();
}
