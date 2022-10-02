$(function() {

	if ($('#missionIconId').val()) {
		let iconSrc =  $('input[name="modalIconId"][value=' + $('#missionIconId').val() + ']').siblings('input[name="modalIconSrc"]').val()
		$('#iconSrc').attr('src', iconSrc)
	}

	$('.category').click(function() {
		let category = $(this).siblings('input').val()
		$('.icon-row').css('display', 'none')
		$('#' + category).css('display', 'flex')
	})

	$('.icon-img').click(function() {
		let missionIconId = $(this).siblings('input[name="modalIconId"]').val()
		let iconSrc = $(this).siblings('input[name="modalIconSrc"]').val()

		$('#missionIconId').val(missionIconId)
		$('#iconSrc').attr('src', iconSrc)

		$('.icon-group').click()
	})

})
