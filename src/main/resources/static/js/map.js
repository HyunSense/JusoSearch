window.onload = function (){

    // 사용자가 Map에 처음 들어가면 주소 이력 1페이지를 항상 불러옴
    findUserAddress(1);

    // 지도 초기화
    initializeMap();

    //search 버튼 클릭시 검색 초기화
    $("#search-btn").on("click", function () {
        document.getElementById("juso").value = "";
        document.getElementById("address-body").innerText = "";
        document.getElementById("pagination").innetText = "";
        document.getElementById("totalCount").innerText = "";
    });

    // 주소 검색 버튼 클릭 시 실행되는 함수
    $("#search-juso").on("click", function(){

        // 기본값 : 1 페이지
        let currentPage=1;
        searchAddress(currentPage);
    })

    // 테이블의 행을 클릭할 때 실행되는 함수
    $(document).on("click", "#exampleModal tr", function () {

        // 클릭된 행에서 첫 번째 열의 텍스트를 가져옴 (도로명 주소)
        var roadAddress = $(this).find("td:nth-child(1)").text();
        console.log("주소 = " + roadAddress);

        // 도로명 주소를 좌표 값으로 변환(Naver MAP API)
        naver.maps.Service.geocode({
            query: roadAddress
        }, function(status, response) {
            if (status !== naver.maps.Service.Status.OK) {
                return alert('Something wrong!');
            }

            var result = response.v2, // 검색 결과의 컨테이너
                items = result.addresses; // 검색 결과의 배열

            // 리턴 받은 좌표 값을 변수에 저장
            let x = parseFloat(items[0].x);
            let y = parseFloat(items[0].y);

            // 해당 좌표로 지도에 마커를 추가하는 코드
            var map = new naver.maps.Map('map', {
                center: new naver.maps.LatLng(y, x), // 지도를 열 좌표
                zoom: 18
            });

            // 마커 생성
            var marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(y, x),
                map: map
            });

            // 모달 창 닫기
            $('#exampleModal').modal('hide');

            // 주소 저장 함수
            saveUserAddress(roadAddress);
        });
    });


    // 검색 이력 테이블에서 특정 검색 이력 삭제
    // 문제점 : Ajax로 추가된 버튼은 이벤트 발동이 되지 않음
    // -> 해결 : 이벤트 위임
    $("#search-list").on("click", ".deleteBtn",function (){
        console.log("특정 주소 삭제 버튼 클릭");

        // 삭제할 시간 가져오기
        let srchTime = $(this).parent().parent().children().eq(0).text();

        console.log("TagName = ", $(this).parent().parent().prop("tagName"));
        console.log("삭제할 일시 = ", srchTime);

        // 특정 검색 이력 삭제
        $.ajax({
            url: "/delete",
            method: "POST",
            // contentType: "application/json",
            data: {srchTime: srchTime},
            context: this,
            success: function() {
                console.log("특정 검색 이력 삭제 성공");
                $(this).parent().parent().remove();
            },
            error: function (){
                console.log("특정 검색 이력 삭제 실패");
            }
        })
    })

    // 전체 검색 기록 삭제
    $(".historyTable").on("click", ".deleteAllBtn", function (){
        console.log("전체 주소 삭제 버튼 클릭");

        $.ajax({
            url: "/deleteAll",
            method: "POST",
            // contentType: "application/json",
            success: function() {
                console.log("특정 검색 이력 삭제 성공");
                $("#search-list").empty(); // 수정부분
            },
            error: function (){
                console.log("특정 검색 이력 삭제 실패");
            }
        })
    })

    // 검색 이력에서 테이블 행 클릭 시 실행되는 함수
    $(document).on("click", "#search-list tr", function () {

        // 클릭한 행에서 검색일시 다음의 주소행을 가져와야함 (두번째열)
        var roadAddress = $(this).find("td:nth-child(2)").text();
        console.log("주소 = " + roadAddress);

        // 도로명 주소를 좌표 값으로 변환(API)
        naver.maps.Service.geocode({
            query: roadAddress
        }, function(status, response) {
            if (status !== naver.maps.Service.Status.OK) {
                return alert('Something wrong!');
            }

            var result = response.v2, // 검색 결과의 컨테이너
                items = result.addresses; // 검색 결과의 배열

            // 리턴 받은 좌표 값을 변수에 저장
            let x = parseFloat(items[0].x);
            let y = parseFloat(items[0].y);

            // 해당 좌표로 지도에 마커를 추가하는 코드
            var map = new naver.maps.Map('map', {
                center: new naver.maps.LatLng(y, x), // 지도를 열 좌표
                zoom: 18
            });

            // 마커 생성
            var marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(y, x),
                map: map
            });
        });
    });
} // ------- window.onload 끝 -------


// 지도 초기화 함수
function initializeMap(){

    // 지도 영역 출력
    var mapOptions = {
        center: new naver.maps.LatLng(37.3595704, 127.105399),
        zoom: 10
    };

    var map = new naver.maps.Map('map', mapOptions);

    // 기본 위치 마커 표시
    var marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(37.3595704, 127.105399),
        map: map
    });
}

// 주소 저장 Ajax
function saveUserAddress(roadAddress) {

    $.ajax({
        url: "/address",
        method: "POST",
        contentType: "application/json",
        data: roadAddress,
        success: function(response) {
            console.log("response = ", response);
            console.log("주소 저장 성공");
            findUserAddress(1); //주소이력 조회 함수
        },
        error: function (){
            console.log("주소 저장 실패");
        }
    });
}

// 주소 이력 조회 Ajax
function findUserAddress(currentPage) {

    $.ajax({
        url: "/address/" + currentPage,
        method: "GET",
        success: function(response) {
            console.log("주소 조회 성공");
            renderSearchHistory(response, currentPage);
        },
        error: function (){
            console.log("주소 조회 실패");
        }
    });
}

// 주소 이력 반복문 함수
// 주소 이력 조회 후 넘어온 결과값(response)를 이용
// 테이블 반복문을 생성
function renderSearchHistory(response, currentPage) {

    console.log("response = ", JSON.stringify(response));
    console.log("currentPage = ", JSON.stringify(currentPage));

    let searchList = document.getElementById("search-list");
    searchList.innerHTML = "";

    for (let i = 0; i < response.historyList.length; i++) {
        let searchHistory = response.historyList[i];
        console.log("searchHistory = ", searchHistory);

        // 테이블에 행 추가
        let newRow = document.createElement("tr");

        // 1) 검색일시 추가
        let tdSrchTime = document.createElement("td");
        tdSrchTime.textContent = searchHistory.srchTime;
        newRow.appendChild(tdSrchTime);

        // 2) 검색된 주소 추가
        let tdSrchedAddr = document.createElement("td");
        tdSrchedAddr.textContent = searchHistory.srchedAddr;
        newRow.appendChild(tdSrchedAddr);

        // 3) 삭제 버튼 추가
        let tdDelete = document.createElement("td");
        let tdDeleteBtn = document.createElement("button");
        tdDeleteBtn.setAttribute("type", "button");
        tdDeleteBtn.setAttribute("class", "btn btn-outline-secondary deleteBtn");
        tdDeleteBtn.textContent = "삭제";
        tdDelete.appendChild(tdDeleteBtn);
        newRow.appendChild(tdDelete);

        searchList.appendChild(newRow);
    }
    renderingPagination(response.totalPages, currentPage, "srchHisPagination");
}


// 주소 검색 함수
// 페이징 : 파라미터 currentPage 추가
function searchAddress(currentPage){

    console.log("search-btn click");
    console.log("searchAddress - currentPage = " + currentPage);

    var keyword = document.getElementById("juso").value;
    console.log("keyword = " + keyword);
    var data = {keyword : keyword, currentPage: currentPage};

    var apiUrl = "https://naveropenapi.apigw.ntruss.com/map-place/v1/search?query=" + encodeURIComponent(data);
    console.log("data = " + data.keyword);

    // ajax 요청
    $.ajax({
        url: "/juso",
        type: "post",
        data: JSON.stringify(data), // data 객체를 Json 형태로 바꿔주는 함수
        contentType: "application/json; charset-utf-8", // ajax에서 보내는 타입
        dataType: "json",   // 요청 컨트롤러로부터 받는 타입
        success: function(jsonStr){
            processJusoResponse(jsonStr);
        },
        error: function(){
            alert("에러 발생!");
        }
    });

}

// 주소 검색 결과를 처리하는 함수
function processJusoResponse(jsonStr){

    var common = jsonStr.results.common;
    var totalCount = common.totalCount;
    var jusoArray = jsonStr.results.juso;
    var countPerPage = common.countPerPage;
    var currentPage = common.currentPage;

    console.log("processJusoResponse - currentPage = " + currentPage);

    let totalPages = Math.ceil(totalCount / countPerPage);

    // 페이지를 누를때 table 초기화 필요
    let addressBody = document.getElementById("address-body");
    addressBody.innerHTML = "";
    let totalCountId = document.getElementById("totalCount");
    totalCountId.textContent = '도로명 주소 검색 결과' + '(' + totalCount + '건)';

    if(jusoArray != null) {
        for (let i = 0; i < jusoArray.length; i++) {
            let juso = jusoArray[i];

            // 테이블에 행 추가
            let newRow = document.createElement("tr");

            // 행에 데이터 추가
            // 1) 도로명 주소 추가
            let tdRoadAddr = document.createElement("td");
            tdRoadAddr.textContent = juso.roadAddr;
            newRow.appendChild(tdRoadAddr);

            // 2) 우편번호 추가
            let tdZipCode = document.createElement("td");
            tdZipCode.textContent = juso.zipNo;
            newRow.appendChild(tdZipCode);

            addressBody.appendChild(newRow);
        }
    }
    // 페이징 처리
    // 모달창 주소 검색에 대한 페이징이므로 id:"pagination"로 설정
    renderingPagination(totalPages, currentPage, "pagination");
}

// 페이지네이션 렌더링 함수
function renderingPagination(totalPages, currentPage, id) {

    let pageLimit = 5;
    let startPage = Math.floor((currentPage - 1) / pageLimit) * pageLimit + 1;
    let endPage = startPage + pageLimit - 1;

    console.log("currentPage = " + currentPage, ", startPage = " + startPage + ", endPage = " + endPage +
        ", pageLimit = " + pageLimit + ", totalPages = " + totalPages);

    if (totalPages < endPage) {
        endPage = totalPages;
    }
    console.log("endPage =", endPage);
    //이전 페이지
    let prevPage = startPage - 1;
    //다음 페이지
    let nextPage = endPage + 1;
    console.log("prevPage = " + prevPage + ", nextPage = " + nextPage);

    // 렌더링 함수를 두 곳에서 사용하기 위해 설정
    // `.pagination#${id}` : id 부분을 변수로 사용 가능
    // 주의 ) 백틱 사용하여 문자열 내에 변수 넣기
    // id가 pagination일 경우, 모달창 주소검색에 대한 페이징 처리가 된다.
    let selector = `.pagination#${id}`;
    let pagination = document.querySelector(selector);
    pagination.innerHTML = "";

    let a = '';

    if (currentPage > pageLimit) {
        a += '<li class="page-item"><a class="page-link" href="#" ' +
            'onclick="' + (id === "pagination" ? 'searchAddress(' + prevPage + ')' : 'findUserAddress(' + prevPage + ')') + '">' + "이전" + '</a></li>';
    }

    for (let i = startPage; i <= endPage; i++) {
        if (i == currentPage) {
            a += '<li class="page-item active"><a class="page-link" href="#" ' +
                'onclick="' + (id === "pagination" ? 'searchAddress(' + i + ')' : 'findUserAddress(' + i + ')') + '">' + i + '</a></li>';
        } else {
            a += '<li class="page-item"><a class="page-link" href="#" ' +
                'onclick="' + (id === "pagination" ? 'searchAddress(' + i + ')' : 'findUserAddress(' + i + ')') + '">' + i + '</a></li>';
        }
    }

    if (nextPage <= totalPages) {
        a += '<li class="page-item"><a class="page-link" href="#" ' +
            'onclick="' + (id === "pagination" ? 'searchAddress(' + nextPage + ')' : 'findUserAddress(' + nextPage + ')') + '">' + "다음" + '</a></li>';
    }

    pagination.innerHTML = a;
}