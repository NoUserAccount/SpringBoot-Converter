<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Currency Converter</title>
<style>
.obrazac {
	margin: auto;
	width: 500px;
	height: 500px;
	background-color: grey;
	border: black 2px solid;
	text-align: center;
}

h1 {
	color: white;
}

#odredisna, #polazna {
	width: 200px;
}

#status {
	height: 30px;
	width: 100%;
	color: white;
}
</style>
</head>

<body>
	<form class="obrazac">
		<h1>Kalkulator valuta</h1>
		<h4>Odaberite datum tečaja:</h4>
		<input type="date" id="datum">
		<script>
			document.getElementById('datum').value = new Date().toISOString()
					.substring(0, 10);
		</script>
		<br>
		<h4>Odaberite polaznu valutu:</h4>
		<select id="polazna">
			<option value="default"></option>
		</select>
		<h4>Odaberite odredišnu valutu:</h4>
		<select id="odredisna">
			<option value="default">Hrvatska kuna</option>
		</select>
		<h4>Unesite iznos za konverziju:</h4>
		<input type="number" value="1" min="0" step=".01" id="unos"> <input
			type="submit" id="button" value="Preračunaj"> <br> <br>
		<label id="status"></label>
	</form>
</body>
</html>

