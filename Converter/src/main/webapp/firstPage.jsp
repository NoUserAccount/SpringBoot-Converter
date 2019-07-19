<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.converter.FormModel"%>
<%@ page import="com.converter.ErrorModel"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
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
	font-size: 18px;
}
</style>

</head>

<body>
	<form class="obrazac" id="form" action="/converterSubmited" method="get" enctype="multipart/form-data">
		<h1>Kalkulator valuta</h1>
		<h4>Odaberite datum tečaja:</h4>
		<input type="date" id="datum" name="datum">
		<script>
			document.getElementById('datum').value = new Date().toISOString()
					.substring(0, 10);
		</script>
		<br>
		<h4>Odaberite polaznu valutu:</h4>
		<select id="polazna" name="polaznaValuta">
			<option value="default"></option>
		</select><br> <br> <input type="button" id="zamijeni" name="zamijeni"
			value="Zamijeni valute">
		<h4>Odaberite odredišnu valutu:</h4>
		<select id="odredisna" name="odredisnaValuta">
			<option value="default">Hrvatska kuna</option>
		</select>
		<h4>Unesite iznos za konverziju:</h4>
		<input type="number" value="1" min="0" step="0.01" id="unos"
			name="iznos"> 
			<input type="submit"
			id="button" value="Preračunaj" name="preracunaj"
			onClick=<%
			FormModel fm = new FormModel();
			String datum = request.getParameter("datum");
			String iznos = request.getParameter("iznos");
			String key = "EUR";

			fm.setDatum(datum);
			fm.setIznos(iznos);
			fm.setValutaO("odredisnaValuta");
			fm.setValutaP("polaznaValuta");%>
			>
		<br> <br> 
		
		<label id="status"></label>
		<%
			ErrorModel em = new ErrorModel();
			request.setAttribute("status", em.getErrorMessage());
		%>
	</form>
</body>
</html>