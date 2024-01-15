// 회원가입 중 생년월일
//$(document).ready(function () {
//
//    // 년도, 월, 일 옵션 설정
//    function options(select, start, end) {
//        for (let i = start; i <= end; i++) {
//            select.append($('<option>', {
//                value: i,
//                text: i
//            }));
//        }
//    }
//
//    options($('select[name="year"]'), 1900, 2025); // Change the range as needed
//    options($('select[name="month"]'), 1, 12);
//    options($('select[name="day"]'), 1, 31);
//});


// 회원가입 중 생년월일 미선택시 오류 발생
//$(document).ready(function () {
//    $('form').submit(function (event) {
//
//        // 선택되지 않은 경우 에러 추가
//        if ($('#year').val() === '' || $('#month').val() === '' || $('#day').val() === '') {
//            $('#birth_error').text('생년월일을 입력하세요.');
//            event.preventDefault();
//        } else {
//            $('#birth_error').text('');
//        }
//    });
//});