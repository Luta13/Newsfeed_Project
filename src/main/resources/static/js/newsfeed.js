document.addEventListener("DOMContentLoaded", function () {
    const newsfeedList = document.getElementById('newsfeed-list');
    const paginationContainer = document.getElementById('pagination');
    const searchButton = document.getElementById('searchButton');
    const prevPageButton = document.getElementById('prevPage');
    const nextPageButton = document.getElementById('nextPage');
    const periodChoice = document.getElementById('periodChoice');
    const sortOrderSelect = document.getElementById('sortOrder');
    const datePickers = document.getElementById('datePickers');
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    // 모달 요소들
    const profileModal = document.getElementById('profileModal');
    const openProfileModalButton = document.getElementById('openProfileModal');
    const closeProfileModalButton = document.getElementById('closeProfileModal');
    const logoutButton = document.getElementById('logoutButton');
    const logoutModal = document.getElementById('logoutModal');
    const confirmLogout = document.getElementById('confirmLogout');
    const cancelLogout = document.getElementById('cancelLogout');
    const editProfileButton = document.getElementById('editProfile');

    const friendsModal = document.getElementById('friendsModal');
    const openFriendsModalButton = document.getElementById('openFriendsModal');
    const closeFriendsModalButton = document.getElementById('closeFriendsModal');
    const manageFriendsButton = document.getElementById('manageFriends');
    const friendRequestsButton = document.getElementById('friendRequests');
    const friendRequestsmanagementButton = document.getElementById('friendRequestsmanagement');

    let currentPage = 1;
    let totalPages = 1;
    let searchQuery = '';
    let sortOrder = 'LIKECNT';
    let startDate = '';
    let endDate = '';

    // 기간 선택 시 달력 표시 여부
    periodChoice.addEventListener('change', function () {
        if (this.value === 'CUSTOM') {
            datePickers.classList.remove('hidden');
        } else {
            datePickers.classList.add('hidden');
            startDate = '';  // 전체 기간이면 날짜 초기화
            endDate = '';
        }
    });

    // 게시물 불러오기
    const fetchPosts = async () => {
        const token = localStorage.getItem("access_token");
        if (!token) {
            alert("로그인이 필요합니다.");
            window.location.href = "/login.html";
            return;
        }

        const url = new URL("http://localhost:9090/newsfeed");
        url.searchParams.append('page', currentPage);
        url.searchParams.append('sort', sortOrder);

        if (periodChoice.value === 'CUSTOM') {
            if (startDate && endDate) {
                url.searchParams.append('startDt', startDate);
                url.searchParams.append('endDt', endDate);
            }
        }

        try {
            const response = await fetch(url.toString(), {
                method: 'GET',
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 200) {
                const data = await response.json();
                renderPosts(data.body.content);
                totalPages = data.body.totalPages;
                renderPagination();
            } else if (response.status === 401) {
                alert("인증 실패: 다시 로그인해주세요.");
                localStorage.removeItem("access_token");
                window.location.href = "/login.html";
            } else {
                alert("뉴스피드를 불러오지 못했습니다.");
            }
        } catch (err) {
            console.error("뉴스피드를 불러오는 데 실패했습니다.", err);
        }
    };

    // 게시물 렌더링
    const renderPosts = (posts) => {
        newsfeedList.innerHTML = posts.map(post => `
            <li class="mb-4">
                <div class="cursor-pointer" onclick="window.location.href='/view/postView.html?id=${post.boardId}'">
                    <p class="text-xl font-semibold text-gray-900">${post.title}</p>
                    <p class="text-sm text-gray-500">작성자: ${post.userName} | 좋아요: ${post.likeCnt} | 댓글: ${post.commentCnt}</p>
                    <p class="text-sm text-gray-500">작성일: ${post.createdAt} | 수정일: ${post.modifiedAt}</p>
                </div>
            </li>
        `).join('');
    };

    // 페이지네이션 렌더링
    const renderPagination = () => {
        paginationContainer.innerHTML = '';

        // 버튼 숨김 처리
        prevPageButton.classList.add('hidden');
        nextPageButton.classList.add('hidden');

        // 총 페이지가 1이면 버튼 숨김 처리
        if (totalPages <= 1) {
            return;
        }

        // 이전 버튼 표시 여부 (현재 페이지가 1보다 클 때만)
        if (currentPage > 1) {
            prevPageButton.classList.remove('hidden');
        }

        // 다음 버튼 표시 여부 (현재 페이지가 마지막 페이지보다 작을 때만)
        if (currentPage < totalPages) {
            nextPageButton.classList.remove('hidden');
        }

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.className = `px-3 py-1 ${currentPage === i ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-600'} rounded-md mx-1`;
            pageButton.addEventListener('click', () => {
                currentPage = i;
                fetchPosts();
            });
            paginationContainer.appendChild(pageButton);
        }
    };

    // 이전 페이지 버튼 클릭 시
    prevPageButton.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            fetchPosts();
        }
    });

    // 다음 페이지 버튼 클릭 시
    nextPageButton.addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            fetchPosts();
        }
    });

    // 검색 버튼 클릭 시
    searchButton.addEventListener('click', (e) => {
        e.preventDefault();
        searchQuery = document.getElementById('searchQuery').value;
        startDate = startDateInput.value;
        endDate = endDateInput.value;
        currentPage = 1;
        fetchPosts();
    });

    // 모달 열기 및 닫기
    openProfileModalButton.addEventListener('click', () => {
        profileModal.classList.remove('hidden');
    });

    closeProfileModalButton.addEventListener('click', () => {
        profileModal.classList.add('hidden');
    });

    logoutButton.addEventListener('click', () => {
        profileModal.classList.add('hidden');
        logoutModal.classList.remove('hidden');
    });

    confirmLogout.addEventListener('click', () => {
        localStorage.removeItem('access_token'); // 로그아웃하면 로컬 스토리지에서 토큰 삭제해주는 코드 !!
        window.location.href = "/login.html";
    });

    cancelLogout.addEventListener('click', () => {
        logoutModal.classList.add('hidden');
    });

    // 친구 관리 모달 열기
    openFriendsModalButton.addEventListener('click', () => {
        friendsModal.classList.remove('hidden');
    });

    closeFriendsModalButton.addEventListener('click', () => {
        friendsModal.classList.add('hidden');
    });

    // 친구 목록 관리로 이동
    manageFriendsButton.addEventListener('click', () => {
        friendsModal.classList.add('hidden');
        window.location.href = "/friends/manage.html";
    });

    // 친구 요청 확인으로 이동
    friendRequestsButton.addEventListener('click', () => {
        friendsModal.classList.add('hidden');
        window.location.href = "/friends/requests.html";
    });

    // 친구 요청 관리로 이동
    friendRequestsmanagementButton.addEventListener('click', () => {
        friendsModal.classList.add('hidden');
        window.location.href = "/friends/requests.html";
    });

    // 프로필 수정으로 이동
    editProfileButton.addEventListener('click', () => {
        profileModal.classList.add('hidden');
        window.location.href = "/edit/Profile.html";
    });

    fetchPosts();
});

// 게시글 작성 페이지로 이동
function redirectToCreatePost() {
    window.location.href = "/post/createPost.html";
}
