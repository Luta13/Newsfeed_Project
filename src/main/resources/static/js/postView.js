document.addEventListener("DOMContentLoaded", function () {
    const postId = new URLSearchParams(window.location.search).get("id");

    // 토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem("access_token");
    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = "/login.html";
        return;
    }

    // 게시물 정보를 가져오는 함수
    const fetchPostDetails = async () => {
        try {
            const response = await fetch(`http://localhost:9090/boards/${postId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token,
                },
            });
            if (response.ok) {
                const data = await response.json();
                const post = data.body;

                // 게시물 정보를 화면에 출력
                document.getElementById("post-title").textContent = post.title;
                document.getElementById("post-author").textContent = `작성자: ${post.name}`;
                document.getElementById("post-date").textContent = `작성일: ${post.createDt}`;
                document.getElementById("post-content").textContent = post.content;

                // 게시물 수정, 삭제 버튼 이벤트 리스너넣으시면됩니다.

                // 댓글 수정, 삭제 버튼 이벤트 리스너 넣으시면됩니다.

            } else {
                alert("게시물 조회에 실패했습니다.");
            }
        } catch (error) {
            console.error("게시물 데이터를 가져오는 도중 오류가 발생했습니다.", error);
        }
    };

    // 댓글 정보를 가져오는 함수
    const fetchComments = async () => {
        try {
            const response = await fetch(`http://localhost:9090/comments/board/${postId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token,
                },
            });
            if (response.ok) {
                const data = await response.json();
                const comments = data.body;

                // 댓글 목록을 화면에 출력
                const commentsList = document.getElementById("comments-list");
                commentsList.innerHTML = comments.map(comment => `
                    <li class="p-4 bg-gray-100 rounded-lg shadow">
                        <p class="text-sm text-gray-700 font-semibold">${comment.memberName}</p>
                        <p class="text-sm text-gray-500">${comment.createDt}</p>
                        <p class="mt-2 text-gray-700">${comment.commentContent}</p>

                        <!-- 댓글 수정 및 삭제 버튼 -->
                        <div class="flex justify-end space-x-2 mt-2">
                            <button class="bg-blue-500 text-white py-1 px-2 rounded-lg hover:bg-blue-600">수정</button>
                            <button class="bg-red-500 text-white py-1 px-2 rounded-lg hover:bg-red-600">삭제</button>
                        </div>
                    </li>
                `).join('');
            } else {
                alert("댓글 조회에 실패했습니다.");
            }
        } catch (error) {
            console.error("댓글 데이터를 가져오는 도중 오류가 발생했습니다.", error);
        }
    };

    // 게시물 및 댓글 정보를 가져와 화면에 표시
    fetchPostDetails();
    fetchComments();
});

// 뒤로가기 버튼 기능
function goBack() {
    window.history.back();
}