// 회원가입 중 생년월일
$(document).ready(function () {

    // 년도, 월, 일 옵션 설정
    function options(select, start, end) {
        for (let i = start; i <= end; i++) {
            select.append($('<option>', {
                value: i,
                text: i
            }));
        }
    }

    options($('select[name="year"]'), 1900, 2025); // Change the range as needed
    options($('select[name="month"]'), 1, 12);
    options($('select[name="day"]'), 1, 31);
});