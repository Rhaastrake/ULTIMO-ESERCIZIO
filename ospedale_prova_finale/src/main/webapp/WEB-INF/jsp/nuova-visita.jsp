<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Ospedale — Nuova Visita</title>
    <style>
        body    { font-family: Arial, sans-serif; margin: 30px; background: #f5f5f5; }
        h1      { color: #1a3c5c; }
        .nav a  { margin-right: 15px; color: #1565c0; text-decoration: none; font-weight: bold; }
        .card   { background: white; padding: 25px; border-radius: 6px;
                  max-width: 520px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
        label   { display: block; margin-top: 15px; font-weight: bold; color: #333; }
        select, textarea {
                  width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc;
                  border-radius: 4px; font-size: 14px; box-sizing: border-box; }
        textarea { height: 80px; resize: vertical; }
        button  { margin-top: 20px; padding: 10px 20px; background: #1565c0;
                  color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 15px; }
        button:hover { background: #1a3c5c; }
        .errore { background: #ffcdd2; border: 1px solid #c62828;
                  padding: 10px; margin-bottom: 15px; border-radius: 4px; color: #b71c1c; }
        .info   { color: #666; font-size: 13px; margin-top: 5px; }
    </style>
</head>
<body>

<h1>🏥 Ospedale — Nuova Visita</h1>
<div class="nav" style="margin-bottom:20px;">
    <a href="${pageContext.request.contextPath}/medici">Torna ai medici</a>
    <a href="${pageContext.request.contextPath}/lista-visite">Lista visite</a>
</div>

<div class="card">
    <h2 style="color:#1a3c5c; margin-top:0;">Registra Visita</h2>

    <c:if test="${not empty param.errore}">
        <div class="errore">
            <c:choose>
                <c:when test="${param.errore == 'campi_mancanti'}">Compila tutti i campi del form.</c:when>
                <c:when test="${param.errore == 'non_trovato'}">Medico o paziente non trovato nel database.</c:when>
                <c:when test="${param.errore == 'formato_non_valido'}">Valore non valido inserito.</c:when>
                <c:otherwise>Errore durante la registrazione della visita.</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/nuova-visita" method="post">

        <label for="medicoId">Medico:</label>
        <select name="medicoId" id="medicoId" required>
            <option value="">-- Seleziona un medico --</option>
            <c:forEach var="m" items="${medici}">
                <option value="${m.id}">Dr. ${m.nome} — ${m.specializzazione}</option>
            </c:forEach>
        </select>

        <label for="pazienteId">Paziente:</label>
        <select name="pazienteId" id="pazienteId" required>
            <option value="">-- Seleziona un paziente --</option>
            <c:forEach var="p" items="${pazienti}">
                <option value="${p.id}">${p.nome} (${p.email})</option>
            </c:forEach>
        </select>

        <label for="descrizione">Descrizione:</label>
        <textarea name="descrizione" id="descrizione" required
                  placeholder="Es. Visita di controllo, dolore al petto..."></textarea>
        <p class="info">La data della visita verrà impostata automaticamente a oggi.</p>

        <button type="submit">Registra Visita</button>
    </form>
</div>

</body>
</html>
