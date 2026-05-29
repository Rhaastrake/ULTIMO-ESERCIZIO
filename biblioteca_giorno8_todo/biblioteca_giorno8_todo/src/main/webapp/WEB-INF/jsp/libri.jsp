<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca — Lista Libri</title>
    <style>
        body       { font-family: Arial, sans-serif; margin: 30px; background: #f5f5f5; }
        h1         { color: #1F4E79; }
        .nav       { margin-bottom: 20px; }
        .nav a     { margin-right: 15px; color: #2E75B6; text-decoration: none; font-weight: bold; }
        .nav a:hover { text-decoration: underline; }
        table      { border-collapse: collapse; width: 100%; background: white; }
        th         { background: #1F4E79; color: white; padding: 10px; text-align: left; }
        td         { padding: 8px 10px; border-bottom: 1px solid #ddd; }
        tr:hover   { background: #EBF3FB; }
        .badge-si  { background: #E2EFDA; color: #375623; padding: 3px 8px; border-radius: 10px; }
        .badge-no  { background: #FCE4D6; color: #C00000; padding: 3px 8px; border-radius: 10px; }
        .messaggio { background: #E2EFDA; border: 1px solid #375623; padding: 10px; margin-bottom: 15px; border-radius: 4px; }
        .filtro    { color: #555; margin-bottom: 10px; font-style: italic; }
        form.cerca { margin-bottom: 20px; }
        input[type=text] { padding: 6px; width: 200px; border: 1px solid #ccc; border-radius: 4px; }
        button     { padding: 6px 12px; background: #2E75B6; color: white; border: none; border-radius: 4px; cursor: pointer; }
    </style>
</head>
<body>

<h1>📚 Sistema Biblioteca</h1>

<!-- Navigazione -->
<div class="nav">
    <a href="${pageContext.request.contextPath}/libri">Tutti i libri</a>
    <a href="${pageContext.request.contextPath}/libri?solo=disponibili">Solo disponibili</a>
    <a href="${pageContext.request.contextPath}/prestito">Nuovo prestito</a>
</div>

<%--
    JSTL — tag <c:if>:
    Mostra il messaggio di successo solo se il parametro "messaggio" e presente.
    ${param.messaggio} accede ai parametri dell'URL (?messaggio=prestito_creato)
--%>
<c:if test="${param.messaggio == 'prestito_creato'}">
    <div class="messaggio">✓ Prestito creato con successo!</div>
</c:if>

<!-- Form di ricerca per autore -->
<form class="cerca" action="${pageContext.request.contextPath}/libri" method="get">
    <input type="text" name="autore" placeholder="Cerca per autore..." value="${param.autore}">
    <button type="submit">Cerca</button>
    <a href="${pageContext.request.contextPath}/libri" style="margin-left:10px;">Reset</a>
</form>

<!-- Filtro attivo e totale -->
<p class="filtro">${filtro} — ${totale} libro/i trovati</p>

<%--
    JSTL — tag <c:choose> + <c:when> + <c:otherwise>:
    Equivalente a if/else in Java.
    Se la lista e vuota mostra un messaggio, altrimenti la tabella.
--%>
<c:choose>
    <c:when test="${empty libri}">
        <p>Nessun libro trovato.</p>
    </c:when>
    <c:otherwise>
        <table>
            <tr>
                <th>ID</th>
                <th>Titolo</th>
                <th>Autore</th>
                <th>Anno</th>
                <th>Disponibile</th>
            </tr>
            <%--
                JSTL — tag <c:forEach>:
                Itera sulla lista "libri" messa nel request scope dalla Servlet.
                var="libro" e il nome della variabile per ogni elemento.
                ${libro.titolo} accede al getter getTitolo() tramite EL (Expression Language).
            --%>
            <c:forEach var="libro" items="${libri}">
                <tr>
                    <td>${libro.id}</td>
                    <td>${libro.titolo}</td>
                    <td>${libro.autore}</td>
                    <td>${libro.anno}</td>
                    <td>
                        <%-- c:choose per badge colorato in base a disponibilita --%>
                        <c:choose>
                            <c:when test="${libro.disponibile}">
                                <span class="badge-si">Disponibile</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge-no">In prestito</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

</body>
</html>
