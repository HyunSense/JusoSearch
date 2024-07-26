window.onload = function () {

    // 아이디 유효성 및 중복 체크
    $('#id').on("focusout", function(){
        let memberId = $('#id').val();
        console.log("id = " + memberId);

        if (memberId.length > 0) {
            if (!idPattern($(this).val())) {
                $('#idValidation').text('4자 이상 12자 이하, 영문자, 숫자 하나 이상을 포함해야 합니다.').css('color', 'red');
            } else {
                $('#idValidation').text('');
                $.ajax({
                    url: "/checkId",
                    method: "post",
                    data: memberId,
                    contentType : "application/json; charset=UTF-8;",
                    dataType: "json",
                    success: function(data) {
                        console.log("ajax 요청 성공");
                        console.log("data = " + data);

                        if (data > 0) {
                            $('#idValidation').text("아이디가 존재합니다. 다른 아이디를 입력해주세요.").css('color', 'red');
                            $("#id").focus();
                        } else {
                            $('#idValidation').text("사용 가능한 아이디입니다.").css('color', 'green');
                        }
                    },
                    error: function(data) {
                    }
                });
            }
        } else {
            $('#idValidation').text('');
        }
    });

    // 비밀번호 정규식 패턴 검사
    $('#password').on("focusout",function (){

        if($(this).val().length !== 0) {
            if(passwordPattern($(this).val())) {
                $('#pwValidation').text('비밀번호 조건에 만족합니다.').css('color', 'green');
            } else {
                $('#pwValidation').text('8글자 이상, 영문, 숫자, 특수문자(@$!%*#?&)를 사용하세요').css('color', 'red');
            }
        } else {
            $('#pwValidation').text('');
        }
    });

    // 이메일 정규식 패턴 검사
    $('#email').on("focusout", function(){
        if ($(this).val().length !== 0) {
            if (emailPattern($(this).val())) {
                $('#emailValidation').text('유효한 이메일 주소입니다.').css('color', 'green');
            } else {
                $('#emailValidation').text('유효하지 않은 이메일 주소입니다.').css('color', 'red');
            }
        } else {
            $('#emailValidation').text('');
        }
    })
}


// 비밀번호 유효성 검사
function pwCheck(){
    let password = $('#password').val();
    let repeatPassword = $('#repeatPassword').val();

    if(password.length !== 0 && repeatPassword.length !== 0) {
        if (password == repeatPassword) {
            $('#pwConfirm').text('비밀번호가 일치합니다.').css('color', 'green')
        } else {
            $('#pwConfirm').text('비밀번호 불일치합니다.').css('color', 'red')
        }
    } else {
        $('#pwConfirm').text('');
    }
}


// test() 메서드 : 주어진 문자열이 정규 표현식에 일치하는지 여부를 확인하는 데 사용
// 일치하면 true 반환
function passwordPattern (str) {
    return /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]+$/.test(str);
}

function idPattern(str) {
    return /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{4,12}$/.test(str);
}

function emailPattern(str) {
    return /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-za-z0-9\-]+/.test(str);
}